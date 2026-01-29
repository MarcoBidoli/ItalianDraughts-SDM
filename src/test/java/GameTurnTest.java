import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameTurnTest {

    @Test
    void gameStartsWithWhiteTurn() {
        Game game = new Game();
        assertEquals(Color.WHITE, game.getCurrentPlayer());
    }

    @Test
    void nextTurnSwitchesPlayer() {
        Game game = new Game();

        game.nextTurn();
        assertEquals(Color.BLACK, game.getCurrentPlayer());

        game.nextTurn();
        assertEquals(Color.WHITE, game.getCurrentPlayer());
    }

    @Test
    void oppositeReturnsOtherColor() {
        assertEquals(Color.WHITE, Game.opposite(Color.BLACK));
        assertEquals(Color.BLACK, Game.opposite(Color.WHITE));
    }

    @Test
    void oppositeThrowsOnNull() {
        assertThrows(IllegalArgumentException.class, () -> Game.opposite(null));
    }
}
