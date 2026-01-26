import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TestPiece {
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
        assertEquals(piece.isKing(), true);
    }
}
