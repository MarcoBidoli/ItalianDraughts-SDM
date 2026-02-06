import italian_draughts.domain.GameColor;
import italian_draughts.logic.Game;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameTurnTest {

    @Test
    void gameStartsWithWhiteTurn() {
        Game game = new Game();
        assertEquals(GameColor.WHITE, game.getCurrentPlayer());
    }

    @Test
    void nextTurnSwitchesPlayer() {
        Game game = new Game();

        game.nextTurn();
        assertEquals(GameColor.BLACK, game.getCurrentPlayer());

        game.nextTurn();
        assertEquals(GameColor.WHITE, game.getCurrentPlayer());
    }

    @Test
    void oppositeReturnsOtherColor() {
        assertEquals(GameColor.WHITE, Game.opposite(GameColor.BLACK));
        assertEquals(GameColor.BLACK, Game.opposite(GameColor.WHITE));
    }

    @Test
    void oppositeThrowsOnNull() {
        assertThrows(IllegalArgumentException.class, () -> Game.opposite(null));
    }
}
