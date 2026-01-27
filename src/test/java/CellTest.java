import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CellTest {
    @Test
    void cellIsEmpty() {
        Cell cell = new Cell(Color.BLACK);
        assertTrue(cell.isEmpty());
    }

    @Test
    void cellIsWhite() {
        Cell cell = new Cell(Color.WHITE);
        assertEquals(Color.WHITE, cell.getColor());
    }

    @Test
    void cellIsBlack() {
        Cell cell = new Cell(Color.BLACK);
        assertEquals(Color.BLACK, cell.getColor());
    }

    @Test
    void putWhitePieceOnCell() {
        Cell target = new Cell(Color.BLACK);
        assertTrue(target.putPieceOn(new Piece(Color.WHITE)));
    }

    @Test
    void putBlackPieceOnCell() {
        Cell target = new Cell(Color.BLACK);
        assertTrue(target.putPieceOn(new Piece(Color.BLACK)));
    }

    @Test
    void putPieceOnOccupiedCell() {
        Cell target = new Cell(Color.BLACK);
        target.putPieceOn(new Piece(Color.WHITE)); // First piece on cell target
        assertFalse(target.putPieceOn(new Piece(Color.BLACK))); // Putting another piece should return a false
    }

    @Test
    void putPieceOnWhiteCell() {
        Cell target = new Cell(Color.WHITE); // Pieces on white cells are not allowed
        assertFalse(target.putPieceOn(new Piece(Color.BLACK)));
    }

    @Test
    void emptyCell() {
        Cell target = new Cell(Color.BLACK);
        target.putPieceOn(new Piece(Color.WHITE));
        target.empty();// Piece on cell target
        assertEquals(null, target.getPiece());
    }
}
