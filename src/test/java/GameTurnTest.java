import italian_draughts.domain.Board;
import italian_draughts.domain.GameColor;
import italian_draughts.domain.GameStatus;
import italian_draughts.domain.InvalidMoveException;
import italian_draughts.domain.Move;
import italian_draughts.logic.Game;

import java.util.List;

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

    @Test
    void processTurnWithoutDrawSwitchesTurn() throws InvalidMoveException {
        Game game = new Game();
        Board board = game.getBoard();

        // setup minimo: due pedine che possono muovere senza cattura
        board.placePiece(GameColor.WHITE, 5, 1);
        board.placePiece(GameColor.BLACK, 2, 2);

        game.processTurn(List.of(new Move(5, 1, 4, 2)));

        assertEquals(GameColor.BLACK, game.getCurrentPlayer());
        assertEquals(GameStatus.ONGOING, game.getStatus());
    }

}
