import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CellTest {
    @Test
    void isEmpty() {
        Cell cell = new Cell(Color.BLACK);
        assertTrue(cell.isEmpty());
    }

    @Test
    void cellIsWhite() {
        Cell cell = new Cell(Color.WHITE);
        assertEquals(Color.WHITE, cell.getColor());
    }
}
