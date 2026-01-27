import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BoardTest {

    @Test
    public void checkSEBlack() {
        Board board = new Board();
        assertEquals(Color.BLACK, board.getCellColor(7,7));
    }

    @Test
    public void checkNWBlack() {
        Board board = new Board();
        assertEquals(Color.BLACK, board.getCellColor(0,0));
    }

    @Test
    public void emptyTest() {
        Board board = new Board();
        assertEquals(true, board.isEmpty());
    }
}
