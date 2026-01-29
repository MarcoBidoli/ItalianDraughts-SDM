import java.util.List;

public class Game {
    private final Board gameBoard;
    private Color currentPlayer;

    public Game() {
        this.gameBoard = new Board();      // crea una nuoca scacchiera vuota
        this.gameBoard.setGame();          // imposta la configurazione iniziale
        this.currentPlayer = Color.WHITE;  // scelta: parte il bianco
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

    public void movePieces(List<Move> moves, Board board) {
        // TODO: implementazione verrà fatta dopo
    }

}
