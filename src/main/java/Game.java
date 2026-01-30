import java.util.List;

public class Game {
    private final Board gameBoard;
    private Color currentPlayer;
    private GameStatus status;

    public Game() {
        this.gameBoard = new Board();      // crea una nuoca scacchiera vuota
        this.gameBoard.setGame();          // imposta la configurazione iniziale
        this.currentPlayer = Color.WHITE;  // scelta: parte il bianco
        this.status = GameStatus.ONGOING;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    public Board getBoard() {
        return gameBoard;
    }

    public void nextTurn() {
        currentPlayer = opposite(currentPlayer);
    }

    // opposite() = dato un colore, ritorna l'altro
    public static Color opposite(Color player) {
        if (player == null) {
            throw new IllegalArgumentException("player cannot be null");
        }
        return (player == Color.BLACK) ? Color.WHITE : Color.BLACK;
    }

    public void movePieces(List<Move> move, Board board) throws InvalidMoveException {
        while (!move.isEmpty()) {
            Move currentMove = move.removeFirst();
            Piece pieceToMove = board.getCell(currentMove.fromRow, currentMove.fromCol).getPiece();
            promotionCheck(currentMove, pieceToMove);
            board.getCell(currentMove.toRow, currentMove.toCol).putPieceOn(pieceToMove);
            board.getCell(currentMove.fromRow, currentMove.fromCol).empty();
            if (Math.abs(currentMove.fromRow - currentMove.toRow) == 2) {
                board.getCell((currentMove.fromRow + currentMove.toRow) / 2, (currentMove.toCol + currentMove.fromCol) / 2).empty();
            }
        }
        updateStatusByPieces(board);
    }

    private static void promotionCheck(Move currentMove, Piece pieceToMove) {
        if(currentMove.toRow == 0 || currentMove.toRow == 7 && !pieceToMove.isKing()){
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
                    if (p.getColor() == Color.WHITE) white++;
                    else if (p.getColor() == Color.BLACK) black++;
                }
            }
        }

        if (white == 0) status = GameStatus.BLACK_WINS;
        else if (black == 0) status = GameStatus.WHITE_WINS;
        else status = GameStatus.ONGOING;
    }
}
