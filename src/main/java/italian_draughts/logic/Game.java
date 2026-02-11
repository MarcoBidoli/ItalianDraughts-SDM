package italian_draughts.logic;

import italian_draughts.domain.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {
    private final Board gameBoard;

    private final Player blackPlayer;
    private final Player whitePlayer;

    private Player currentPlayer;
    private GameStatus status;
    private int quietMovesNoCapture;
    private final Map<List<SquareEncoder>, Integer> visits;
    public static final int MAX_QUIET_MOVES = 40;

    private List<GameObserver> observers = new ArrayList<>();
    private List<List<Move>> currentLegalMoves;private Square selectedSquare = null;
    private List<List<Move>> selectedPieceMoves = new ArrayList<>();

    public Game(Player whitePlayer, Player blackPlayer) {
        this.gameBoard = new Board(); // crea una nuova scacchiera vuota
        this.blackPlayer = blackPlayer;
        this.whitePlayer = whitePlayer;
        this.currentPlayer = whitePlayer; // scelta: parte il bianco
        this.status = GameStatus.ONGOING;
        this.visits = new HashMap<>();
        this.currentLegalMoves = new ArrayList<>();
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Board getBoard() {
        return gameBoard;
    }

    public void nextTurn() {
        currentPlayer = getOpponent(currentPlayer);
    }

    public Player getOpponent(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }

        return (player.getColor() == GameColor.BLACK) ? this.whitePlayer : this.blackPlayer;
    }

    public void niente(int row, int col) {
        for(List<Move> move : selectedPieceMoves) {
            Move last = move.getLast();
            if(last.toRow == row && last.toCol == col) {
                try {
                    processTurn(new ArrayList<>(move));
                    return;
                } catch (InvalidMoveException e) {
                    return;
                }
            }
        }

        updateSelection(row, col);
    }

    private void updateSelection(int row, int col) {
        List<List<Move>> moves = getMovesFor(row, col);

        if(!moves.isEmpty()) {
            this.selectedSquare = new Square(row, col);
            this.selectedPieceMoves = moves;
        } else {
            //when click on a piece with no possible moves, to remove green indicators
            this.selectedSquare = null;
            this.selectedPieceMoves = new ArrayList<>();
        }
        notifyObservers();
    }

    public void processTurn(List<Move> moves) throws InvalidMoveException {
        if (status != GameStatus.ONGOING) return;
        if (moves == null || moves.isEmpty()) {
            throw new InvalidMoveException("Turn must contain at least one move");
        }

        // 1) Applica le mosse e ottieni info cattura (single source of truth)
        boolean captureOccurred = movePieces(moves, this.gameBoard);

        this.selectedSquare = null;
        this.selectedPieceMoves = new ArrayList<>();

        // 2) Check draw (repetition + move-count)
        checkDraw(captureOccurred);
        if (status == GameStatus.ONGOING) {
            nextTurn();
            checkWin();
        }

        notifyObservers();
    }

    private boolean hasKing(Player player) {
        GameColor playerColor = player.getColor();
        //noinspection ChainedMethodCall
        return Board.ALL_SQUARES.stream()
                .anyMatch(square -> {
                    Piece p = gameBoard.getPieceAt(square.row(), square.col());
                    return p != null && p.getColor() == playerColor && p.isKing();
                });
    }

    private void updateMoveCountRule(boolean captureOccurred) {
        // Se c'è stata una cattura, la sequenza "senza catture" si interrompe: reset totale
        if (captureOccurred) {
            quietMovesNoCapture = 0;
            return;
        }

        // La Move-Count Rule si applica solo se entrambi hanno almeno un king
        boolean bothHaveKings = hasKing(whitePlayer) && hasKing(blackPlayer);
        if (!bothHaveKings) {
            quietMovesNoCapture = 0; // RIPETIZIONE VOLUTA, need check
            return;
        }

        // Incremento il contatore
        quietMovesNoCapture++;

        // Trigger draw: 40 mosse consecutive senza catture
        if (quietMovesNoCapture >= MAX_QUIET_MOVES) {
            status = GameStatus.DRAW;
        }
    }

    private void checkDraw(boolean captureOccurred) {
        if (status != GameStatus.ONGOING) return;

        // 1) Move repetition (3 volte stessa configurazione)
        if (checkRepetition()) {
            status = GameStatus.DRAW;
            return;
        }

        // 2) Move-count rule (40 mosse complessive senza cattura, se applicabile)
        updateMoveCountRule(captureOccurred);
    }

    public boolean movePieces(List<Move> move, Board board) throws InvalidMoveException {
        boolean captureOccurred = false;

        while (!move.isEmpty()) {
            Move currentMove = move.removeFirst();
            Piece pieceToMove = board.getPieceAt(currentMove.fromRow, currentMove.fromCol);

            //promotion
            promotionCheck(currentMove, pieceToMove);

            //moving
            board.placePiece(pieceToMove, currentMove.toRow, currentMove.toCol);
            board.emptyCell(currentMove.fromRow, currentMove.fromCol);

            boolean isCapture = Math.abs(currentMove.fromRow - currentMove.toRow) == 2;
            //if a capture, empty middle cell
            if (isCapture) {
                captureOccurred = true;
                board.emptyCell((currentMove.fromRow + currentMove.toRow) / 2,
                        (currentMove.toCol + currentMove.fromCol) / 2);
                visits.clear();
            }
        }
        boardEncoder(board);
        return captureOccurred;
    }

    public void boardEncoder(Board board) {
        List<SquareEncoder> encoding = new ArrayList<>();
        int index = 0;

        for (Square square : Board.PLAYABLE_SQUARES) {
            index++;
            Cell cell = board.getCell(square.row(), square.col());

            if (!cell.isEmpty()) {
                encoding.add(new SquareEncoder(cell.getSymbol(), index));
            }
        }
        visits.merge(encoding, 1, Integer::sum);
    }

    public boolean checkRepetition() {
        for (int count : visits.values()) {
            if (count >= 3) {
                return true;
            }
        }
        return false;
    }

    public Map<List<SquareEncoder>, Integer> getVisits() {
        return visits;
    }

    private static void promotionCheck(Move currentMove, Piece pieceToMove) {
        if((currentMove.toRow == 0 || currentMove.toRow == 7) && !pieceToMove.isKing()){
            pieceToMove.setKing(true);
        }
    }

    public GameStatus getStatus() {
        return status;
    }

    public void calculateLegalMoves() {
        LegalMoves lm = new LegalMoves(gameBoard, currentPlayer.getColor());
        this.currentLegalMoves = lm.getLegalMoves();
    }

    private boolean hasAnyPiece(Player player) {
        GameColor playerColor = player.getColor();
        //noinspection ChainedMethodCall
        return Board.ALL_SQUARES.stream()
                .anyMatch(square -> {
                    Piece p = gameBoard.getPieceAt(square.row(), square.col());
                    return p != null && p.getColor() == playerColor;
                });
    }

    private void checkWin() {
        if (status != GameStatus.ONGOING) return;

        // Caso 1: il giocatore di turno non ha più pezzi
        if (!hasAnyPiece(currentPlayer)) {
            status = (currentPlayer.getColor() == GameColor.WHITE) ? GameStatus.BLACK_WINS : GameStatus.WHITE_WINS;
            currentLegalMoves = new ArrayList<>();
            return;
        }

        // Caso 2: il giocatore di turno non ha mosse legali
        calculateLegalMoves();
        if (currentLegalMoves.isEmpty()) {
            status = (currentPlayer.getColor() == GameColor.WHITE) ? GameStatus.BLACK_WINS : GameStatus.WHITE_WINS;
        }
    }

    public List<List<Move>> getCurrentLegalMoves() {
        return currentLegalMoves;
    }

    public List<List<Move>> getMovesFor(int row, int col) {
        try {
            LegalMoves legalMoves = new LegalMoves(gameBoard, currentPlayer.getColor());
            return legalMoves.getSinglePieceLegalMoves(row, col);
        } catch (InvalidMoveException e) {
            throw new RuntimeException(e);
        }
    }

    public void agreedDrawHandling(){
        status = GameStatus.DRAW;
        notifyObservers();
    }

    public void resignHandling(GameColor loser) {
        status = (loser == GameColor.WHITE) ? GameStatus.BLACK_WINS : GameStatus.WHITE_WINS;
        notifyObservers();
    }

    public void setCurrentTurn(Player player) {
        currentPlayer = player;
    }


    public void addObserver(GameObserver gameObserver) {
        observers.add(gameObserver);
    }

    private void notifyObservers(){
        for(GameObserver obs : observers) {
            obs.modelChanged();
        }
    }


    public Square getSelectedSquare() {
        return selectedSquare;
    }

    public List<List<Move>> getSelectedPieceMoves() {
        return selectedPieceMoves;
    }
}
