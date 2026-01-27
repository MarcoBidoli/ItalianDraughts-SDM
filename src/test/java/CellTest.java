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
    void putWhitePieceOnCell() throws InvalidMoveException {
        Cell target = new Cell(Color.BLACK);
        target.putPieceOn(new Piece(Color.WHITE));
        assertNotNull(target.getPiece());
    }

    @Test
    void putBlackPieceOnCell() throws InvalidMoveException {
        Cell target = new Cell(Color.BLACK);
        target.putPieceOn(new Piece(Color.BLACK));
        assertNotNull(target.getPiece());
    }

    @Test
    void putPieceOnOccupiedCell() throws InvalidMoveException {
        Cell target = new Cell(Color.BLACK);

        target.putPieceOn(new Piece(Color.WHITE));  // Place the first piece normally

        assertThrows(InvalidMoveException.class, () -> {
            target.putPieceOn(new Piece(Color.BLACK));   // Putting another piece
        });
    }

    @Test
    void putPieceOnWhiteCell() {
        Cell target = new Cell(Color.WHITE);
        assertThrows(InvalidMoveException.class, () -> target.putPieceOn(new Piece(Color.BLACK)));
    }

    @Test
    void emptyCell() throws InvalidMoveException {
        Cell target = new Cell(Color.BLACK);
        target.putPieceOn(new Piece(Color.WHITE));
        target.empty();// Piece on cell target
        assertNull(target.getPiece());
    }

    @Test
    public void nonEmptyCell() {
        Cell cell = new Cell(Color.BLACK);
        assertNull(cell.getPiece());
    }
}
