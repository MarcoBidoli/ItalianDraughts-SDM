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
    public static final int MAX_QUIET_MOVES = 40;

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

    public void processTurn(List<Move> moves) throws InvalidMoveException {
        if (status != GameStatus.ONGOING) return;
        if (moves == null || moves.isEmpty()) {
            throw new InvalidMoveException("Turn must contain at least one move");
        }

        // 1) Applica le mosse e ottieni info cattura (single source of truth)
        boolean captureOccurred = movePieces(new ArrayList<>(moves), this.gameBoard);

        // 2) Check draw (repetition + move-count)
        checkDraw(captureOccurred);
        if (status != GameStatus.ONGOING) {
            return; // in caso di DRAW, non cambia turno
        }

        // 3) Turno successivo
        nextTurn();

        // 4) Check win (pezzi finiti o nessuna mossa legale per il nuovo currentPlayer)
        checkWin();
    }

    private boolean hasKing(GameColor color) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece piece = gameBoard.getPieceAt(r,c);
                if (piece != null &&
                        piece.getColor() == color &&
                        piece.isKing()) {
                    return true;
                }
            }
        }
        return false;
    }

    private void updateMoveCountRule(boolean captureOccurred) {
        // Se c'è stata una cattura, la sequenza "senza catture" si interrompe: reset totale
        if (captureOccurred) {
            quietMovesNoCapture = 0;
            return;
        }

        // La Move-Count Rule si applica solo se entrambi hanno almeno un king
        boolean bothHaveKings = hasKing(GameColor.WHITE) && hasKing(GameColor.BLACK);
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
                board.getCell((currentMove.fromRow + currentMove.toRow) / 2,
                              (currentMove.toCol + currentMove.fromCol) / 2).empty();
                visits.clear();
            }
        }
        boardEncoder(board);
        return captureOccurred;
    }

    public void boardEncoder(Board board) {
        List<SquareEncoder> encoding = new ArrayList<>();
        int counter = 0;
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if((i + j) % 2 == 0){
                    char value;
                    counter++;
                    if (board.getPieceAt(i,j) != null) {
                        if (board.colorOfPiece(i,j) == GameColor.BLACK) {
                            value = board.isKingAt(i,j) ? 'B' : 'b';
                        } else {
                            value = board.isKingAt(i,j) ? 'W' : 'w';
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

    private void checkWin() {
        if (status != GameStatus.ONGOING) return;

        // Caso 1: il giocatore di turno non ha più pezzi
        if (!hasAnyPiece(currentPlayer)) {
            status = (currentPlayer == GameColor.WHITE) ? GameStatus.BLACK_WINS : GameStatus.WHITE_WINS;
            currentLegalMoves = new ArrayList<>();
            return;
        }

        // Caso 2: il giocatore di turno non ha mosse legali
        calculateLegalMoves();
        if (currentLegalMoves.isEmpty()) {
            status = (currentPlayer == GameColor.WHITE) ? GameStatus.BLACK_WINS : GameStatus.WHITE_WINS;
        }
    }

    public List<List<Move>> getCurrentLegalMoves() {
        return currentLegalMoves;
    }

    public List<List<Move>> getMovesFor(int row, int col) {
        try {
            LegalMoves legalMoves = new LegalMoves(this.gameBoard, this.currentPlayer);
            return legalMoves.getSinglePieceLegalMoves(row, col);
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

    public void setCurrentTurn(GameColor player) {
        this.currentPlayer = player;
    }
}
