import italian_draughts.domain.*;
import italian_draughts.logic.Game;
import org.junit.jupiter.api.Test;

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

        // 4-position cycles to avoid repeating the same position back-and-forth
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

        // Draw reached at the last move
        assertEquals(GameStatus.DRAW, game.getStatus());
    }

    @Test
    void drawTriggeredByRepetitionRule() throws InvalidMoveException {
        Game game = new Game();
        Board board = game.getBoard();

        board.emptyBoard();
        board.placePiece(GameColor.WHITE, PieceType.KING, 6, 2);
        board.placePiece(GameColor.BLACK, PieceType.KING, 1, 5);

        // Back-and-forth moves
        Move[] sequence = {
                new Move(6, 2, 5, 1), // W
                new Move(1, 5, 2, 6), // B
                new Move(5, 1, 6, 2), // W back
                new Move(2, 6, 1, 5)  // B back
        };

        // Repeat until DRAW is triggered by the repetition rule
        for (int i = 0; i < 3 && game.getStatus() == GameStatus.ONGOING; i++) {
            for (Move m : sequence) {
                game.processTurn(List.of(m));
                if (game.getStatus() != GameStatus.ONGOING) break;
            }
        }

        assertEquals(GameStatus.DRAW, game.getStatus());
    }

}
