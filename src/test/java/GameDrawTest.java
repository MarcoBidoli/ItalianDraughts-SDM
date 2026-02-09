import italian_draughts.domain.*;
import italian_draughts.logic.Game;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameDrawTest {

    @Test
    void drawTriggeredAfter40QuietMovesWhenBothHaveKings() throws InvalidMoveException {
        Game game = new Game();
        Board gb = game.getBoard();
        gb.initCells();

        // White king
        gb.placePiece(GameColor.WHITE, 6, 2);
        gb.getPieceAt(6, 2).setKing(true);

        // Black king
        gb.placePiece(GameColor.BLACK, 0, 6);
        gb.getPieceAt(0, 6).setKing(true);

        // 4-pos cycles to avoid repeating the same position back-and-forth
        int[][] whitePath = {{6, 2}, {5, 1}, {4, 2}, {5, 3}};
        int[][] blackPath = {{0, 6}, {1, 5}, {2, 6}, {1, 7}};

        int wIdx = 0;
        int bIdx = 0;

        for (int i = 0; i < Game.MAX_QUIET_MOVES; i++) {
            int[] wFrom = whitePath[wIdx];
            int[] wTo = whitePath[(wIdx + 1) % whitePath.length];
            game.processTurn(List.of(new Move(wFrom[0], wFrom[1], wTo[0], wTo[1])));
            wIdx = (wIdx + 1) % whitePath.length;

            int[] bFrom = blackPath[bIdx];
            int[] bTo = blackPath[(bIdx + 1) % blackPath.length];
            game.processTurn(List.of(new Move(bFrom[0], bFrom[1], bTo[0], bTo[1])));
            bIdx = (bIdx + 1) % blackPath.length;
        }

        // Draw reached at the last move; turn must not switch after draw
        assertEquals(GameStatus.DRAW, game.getStatus());
    }
}
