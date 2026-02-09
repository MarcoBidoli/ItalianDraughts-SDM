import italian_draughts.domain.*;
import italian_draughts.logic.Game;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameDrawTest {

    @Test
    void drawTriggeredAfter40QuietMovesEachWhenBothHaveKings() throws InvalidMoveException {
        Game game = new Game();
        Board gb = game.getBoard();
        gb.initCells();

        // White king in basso a sinistra (celle nere, cioè pari)
        gb.placePiece(GameColor.WHITE, 6, 2);
        gb.getPieceAt(6,2).setKing(true);

        // Black king in alto a destra (celle nere, cioè pari)
        gb.placePiece(GameColor.BLACK, 0, 6);
        gb.getPieceAt(0,6).setKing(true);

        // Percorsi ciclici (4 posizioni) per evitare avanti-indietro ripetuto
        // White cycle: (6,2) -> (5,1) -> (4,2) -> (5,3) -> (6,2) ...
        int[][] whitePath = {
                {6, 2}, {5, 1}, {4, 2}, {5, 3}
        };

        // Black cycle: (0,6) -> (1,5) -> (2,6) -> (1,7) -> (0,6) ...
        int[][] blackPath = {
                {0, 6}, {1, 5}, {2, 6}, {1, 7}
        };

        int wIdx = 0;
        int bIdx = 0;

        for (int i = 0; i < 40; i++) {
            // turno White: sposta lungo il ciclo
            List<Move> w = new ArrayList<>();
            int[] wFrom = whitePath[wIdx];
            int[] wTo = whitePath[(wIdx + 1) % whitePath.length];
            w.add(new Move(wFrom[0], wFrom[1], wTo[0], wTo[1]));
            game.processTurn(w);
            wIdx = (wIdx + 1) % whitePath.length;

            // turno Black: sposta lungo il ciclo
            List<Move> b = new ArrayList<>();
            int[] bFrom = blackPath[bIdx];
            int[] bTo = blackPath[(bIdx + 1) % blackPath.length];
            b.add(new Move(bFrom[0], bFrom[1], bTo[0], bTo[1]));
            game.processTurn(b);
            bIdx = (bIdx + 1) % blackPath.length;
        }

        assertEquals(GameStatus.DRAW, game.getStatus());
    }
}
