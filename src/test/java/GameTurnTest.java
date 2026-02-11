import italian_draughts.domain.*;
import italian_draughts.logic.Game;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameTurnTest {
    Player w = new HumanPlayer(GameColor.WHITE);
    Player b = new HumanPlayer(GameColor.BLACK);

    @Test
    void gameStartsWithWhiteTurn() {
        Game game = new Game(w, b);
        assertEquals(w, game.getCurrentPlayer());
    }

    @Test
    void nextTurnSwitchesPlayer() {
        Game game = new Game(w, b);

        game.nextTurn();
        assertEquals(b, game.getCurrentPlayer());

        game.nextTurn();
        assertEquals(w, game.getCurrentPlayer());
    }

    @Test
    void getOpponentReturnsCorrectPlayer() {
        Game game = new Game(w, b);

        assertSame(w, game.getOpponent(b));
        assertSame(b, game.getOpponent(w));
    }

    @Test
    void oppositeThrowsOnNull() {
        Game g = new Game(w, b);
        assertThrows(IllegalArgumentException.class, () -> g.getOpponent(null));
    }

    @Test
    void processTurnWithoutDrawSwitchesTurn() throws InvalidMoveException {
        Game game = new Game(w, b);
        Board board = game.getBoard();

        // setup minimo: due pedine che possono muovere senza cattura
        board.placePiece(GameColor.WHITE, 5, 1);
        board.placePiece(GameColor.BLACK, 2, 2);

        handleMove(game, List.of(new Move(5, 1, 4, 2)));

        assertEquals(b, game.getCurrentPlayer());
        assertEquals(GameStatus.ONGOING, game.getStatus());
    }

    private static void handleMove(Game game, List<Move> move) {
        game.handleSelection(move.getFirst().fromRow(), move.getFirst().fromCol());
        game.handleSelection(move.getFirst().toRow(), move.getFirst().toCol());
    }
}
