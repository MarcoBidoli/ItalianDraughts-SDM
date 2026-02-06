import italian_draughts.domain.Cell;
import italian_draughts.domain.GameColor;
import italian_draughts.domain.InvalidMoveException;
import italian_draughts.domain.Piece;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CellTest {
    @Test
    void cellIsEmpty() {
        Cell cell = new Cell(GameColor.BLACK);
        assertTrue(cell.isEmpty());
    }

    @Test
    void cellIsWhite() {
        Cell cell = new Cell(GameColor.WHITE);
        assertEquals(GameColor.WHITE, cell.getColor());
    }

    @Test
    void cellIsBlack() {
        Cell cell = new Cell(GameColor.BLACK);
        assertEquals(GameColor.BLACK, cell.getColor());
    }

    @Test
    void putWhitePieceOnCell() throws InvalidMoveException {
        Cell target = new Cell(GameColor.BLACK);
        target.putPieceOn(new Piece(GameColor.WHITE));
        assertNotNull(target.getPiece());
    }

    @Test
    void putBlackPieceOnCell() throws InvalidMoveException {
        Cell target = new Cell(GameColor.BLACK);
        target.putPieceOn(new Piece(GameColor.BLACK));
        assertNotNull(target.getPiece());
    }

    @Test
    void putPieceOnOccupiedCell() throws InvalidMoveException {
        Cell target = new Cell(GameColor.BLACK);

        target.putPieceOn(new Piece(GameColor.WHITE));  // Place the first piece normally

        assertThrows(InvalidMoveException.class, () -> {
            target.putPieceOn(new Piece(GameColor.BLACK));   // Putting another piece
        });
    }

    @Test
    void putPieceOnWhiteCell() {
        Cell target = new Cell(GameColor.WHITE);
        assertThrows(InvalidMoveException.class, () -> target.putPieceOn(new Piece(GameColor.BLACK)));
    }

    @Test
    void emptyCell() throws InvalidMoveException {
        Cell target = new Cell(GameColor.BLACK);
        target.putPieceOn(new Piece(GameColor.WHITE));
        target.empty();// domain.Piece on cell target
        assertNull(target.getPiece());
    }

    @Test
    public void nonEmptyCell() {
        Cell cell = new Cell(GameColor.BLACK);
        assertNull(cell.getPiece());
    }
}
