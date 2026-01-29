import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameStatusTest {

    @Test
    void newGameStartsOngoing() {
        Game game = new Game();
        assertEquals(GameStatus.ONGOING, game.getStatus());
    }

    @Test
    void whiteWinsWhenNoBlackPiecesRemain() throws InvalidMoveException {
        Board board = new Board();
        board.initCells();
        board.placePiece(Color.WHITE, 4, 4); // c'è solo bianco

        Game game = new Game();
        game.movePieces(new java.util.ArrayList<>(), board); // forza update status senza mosse

        assertEquals(GameStatus.WHITE_WINS, game.getStatus());
    }
}
