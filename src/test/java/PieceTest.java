import italian_draughts.domain.GameColor;
import italian_draughts.domain.Piece;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PieceTest {
    @Test
    void isKing() {
        Piece piece = new Piece(GameColor.BLACK);
        assertFalse(piece.isKing());
    }
    @Test
    void isBlack() {
        Piece piece = new Piece(GameColor.BLACK);
        assertEquals(GameColor.BLACK, piece.getColor());
    }
    @Test
    void isWhite() {
        Piece piece = new Piece(GameColor.WHITE);
        assertEquals(GameColor.WHITE, piece.getColor());
    }
    @Test
    void becomesKing() {
        Piece piece = new Piece(GameColor.BLACK);
        piece.setKing(true);
        assertTrue(piece.isKing());
    }
}
