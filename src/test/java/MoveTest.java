import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MoveTest {

    @Test
    void throwExceptionNegativeCoordinates() {
        assertThrows(IllegalArgumentException.class, () -> new Move(-1, 0, 1, 1));
        assertThrows(IllegalArgumentException.class, () -> new Move(0, -1, 1, 1));
        assertThrows(IllegalArgumentException.class, () -> new Move(0, 0, -1, 1));
        assertThrows(IllegalArgumentException.class, () -> new Move(0, 0, 1, -1));
    }

    @Test
    void throwExceptionCoordinatesOutOfBoard() {
        assertThrows(IllegalArgumentException.class, () -> new Move(0, 9, 1, 1));
        assertThrows(IllegalArgumentException.class, () -> new Move(8, 1, 1, 1));
        assertThrows(IllegalArgumentException.class, () -> new Move(0, 0, 12, 1));
        assertThrows(IllegalArgumentException.class, () -> new Move(0, 0, 1, 9));
    }

    @Test
    void validCoordinates() {
        Move move = new Move(0, 1, 2, 3);
        assertEquals(0, move.fromRow);
        assertEquals(1, move.fromCol);
        assertEquals(2, move.toRow);
        assertEquals(3, move.toCol);
    }
}
