package italian_draughts.logic;

import italian_draughts.domain.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {
    private final Board gameBoard;
    private GameColor currentPlayer;
    private GameStatus status;
    private int quietMovesNoCapture;
    private final Map<List<SquareEncoder>, Integer> visits;

    private List<List<Move>> currentLegalMoves;

    public Game() {
        this.gameBoard = new Board();      // crea una nuova scacchiera vuota
        this.currentPlayer = GameColor.WHITE;  // scelta: parte il bianco
        this.status = GameStatus.ONGOING;
        this.visits = new HashMap<>();
        this.currentLegalMoves = new ArrayList<>();
    }

    public GameColor getCurrentPlayer() {
        return currentPlayer;
    }

    public Board getBoard() {
        return gameBoard;
    }

    public void nextTurn() {
        currentPlayer = opposite(currentPlayer);
    }

    // opposite() = dato un colore, ritorna l'altro
    public static GameColor opposite(GameColor player) {
        if (player == null) {
            throw new IllegalArgumentException("player cannot be null");
        }
        return (player == GameColor.BLACK) ? GameColor.WHITE : GameColor.BLACK;
    }

    public void applyTurn(List<Move> moves) throws InvalidMoveException {
        if(status != GameStatus.ONGOING) return;

        // Salviamo chi sta giocando ORA (prima di eventuali cambi turno)
        GameColor playerWhoMoved = currentPlayer;

        // 1) Il turno contiene almeno una cattura?
        boolean captureOccurred = moves.stream()
                .anyMatch(m -> Math.abs(m.fromRow - m.toRow) == 2);

        // 2) Applica le mosse alla board (riutilizzo il codice di Federico)
        movePieces(new ArrayList<>(moves), this.gameBoard);

        //check move repetition
        if(checkRepetition()) {
            status = GameStatus.DRAW;
            return;
        }

        // 3) Update contatori draw (domain.Move-Count Rule)
        updateDrawCounters(playerWhoMoved, captureOccurred);
        updateStatusByPieces(this.gameBoard);

        // Se la regola draw ha dichiarato DRAW, non cambia turno
        if (status != GameStatus.ONGOING) {
            return;
        }

        // 4) Turno successivo
        nextTurn();
        calculateLegalMoves();
    }


    private boolean hasKing(GameColor color) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece piece = gameBoard.getCell(r, c).getPiece();
                if (piece != null &&
                        piece.getColor() == color &&
                        piece.isKing()) {
                    return true;
                }
            }
        }
        return false;
    }

    private void updateDrawCounters(GameColor playerWhoMoved, boolean captureOccurred) {
        // Se c'è stata una cattura, la sequenza "senza catture" si interrompe: reset totale
        if (captureOccurred) {
            quietMovesWhite = 0;
            quietMovesBlack = 0;
            return;
        }

        // La domain.Move-Count Rule si applica solo se entrambi hanno almeno un king
        boolean bothHaveKings = hasKing(GameColor.WHITE) && hasKing(GameColor.BLACK);
        if (!bothHaveKings) {
            return;
        }

        // Incremento il contatore del giocatore che ha appena mosso
        if (playerWhoMoved == GameColor.WHITE) {
            quietMovesWhite++;
        } else if (playerWhoMoved == GameColor.BLACK) {
            quietMovesBlack++;
        }

        // Trigger draw: 40 mosse consecutive per ciascun giocatore senza catture
        if (quietMovesWhite >= 40 && quietMovesBlack >= 40) {
            status = GameStatus.DRAW;
        }
    }

    public boolean movePieces(List<Move> move, Board board) throws InvalidMoveException {
        boolean captureOccured = false;

        while (!move.isEmpty()) {
            Move currentMove = move.removeFirst();
            Piece pieceToMove = board.getCell(currentMove.fromRow, currentMove.fromCol).getPiece();

            //promotion
            promotionCheck(currentMove, pieceToMove);

            //moving
            board.getCell(currentMove.toRow, currentMove.toCol).putPieceOn(pieceToMove);
            board.getCell(currentMove.fromRow, currentMove.fromCol).empty();

            boolean isCapture = Math.abs(currentMove.fromRow - currentMove.toRow) == 2;
            //if a capture, empty middle cell
            if (isCapture) {
                captureOccured = true;
                board.getCell((currentMove.fromRow + currentMove.toRow) / 2,
                              (currentMove.toCol + currentMove.fromCol) / 2).empty();
            visits.clear();
            }
        }
        boardEncoder(board);
        return captureOccured;
    }

    public void boardEncoder(Board board) {
        List<SquareEncoder> encoding = new ArrayList<>();
        int counter = 0;
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if((i + j) % 2 == 0){
                    char value;
                    counter++;
                    if (board.getCell(i,j).getPiece() != null) {
                        if (board.getCell(i, j).getPiece().getColor() == GameColor.BLACK) {
                            value = board.getCell(i, j).getPiece().isKing() ? 'B' : 'b';
                        } else {
                            value = board.getCell(i, j).getPiece().isKing() ? 'W' : 'w';
                        }
                        encoding.add(new SquareEncoder(value, counter));
                    }
                }
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

    private void updateStatusByPieces(Board board) {
        int white = 0;
        int black = 0;

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = board.getCell(r, c).getPiece();
                if (p != null) {
                    if (p.getColor() == GameColor.WHITE) white++;
                    else if (p.getColor() == GameColor.BLACK) black++;
                }
            }
        }

        if (white == 0) status = GameStatus.BLACK_WINS;
        else if (black == 0) status = GameStatus.WHITE_WINS;
        else status = GameStatus.ONGOING;
    }

    public void calculateLegalMoves() {
        try {
            LegalMoves lm = new LegalMoves(gameBoard, currentPlayer);
            this.currentLegalMoves = lm.getLegalMoves();
        } catch (InvalidMoveException e) {
            this.currentLegalMoves = new ArrayList<>();
        }
    }

    private boolean hasAnyPiece(GameColor color) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = gameBoard.getCell(r, c).getPiece();
                if (p != null && p.getColor() == color) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<List<Move>> getCurrentLegalMoves() {
        return currentLegalMoves;
    }

    public List<List<Move>> getMovesFor(int row, int col) {
        try {
            return new LegalMoves(this.gameBoard, this.currentPlayer).getSinglePieceLegalMoves(row, col);
        } catch (InvalidMoveException e) {
            throw new RuntimeException(e);
        }
    }

    public void agreedDrawHandling(){
        status = GameStatus.DRAW;
    }

    public void resignHandling(GameColor loser) {
        status = (loser == GameColor.WHITE) ? GameStatus.BLACK_WINS : GameStatus.WHITE_WINS;
    }
}
