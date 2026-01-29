import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PieceTest {
    @Test
    void isKing() {
        Piece piece = new Piece(Color.BLACK);
        assertFalse(piece.isKing());
    }
    @Test
    void isBlack() {
        Piece piece = new Piece(Color.BLACK);
        assertEquals(Color.BLACK, piece.getColor());
    }
    @Test
    void isWhite() {
        Piece piece = new Piece(Color.WHITE);
        assertEquals(Color.WHITE, piece.getColor());
    }
    @Test
    void becomesKing() {
        Piece piece = new Piece(Color.BLACK);
        piece.setKing(true);
        assertTrue(piece.isKing());
    }
}
