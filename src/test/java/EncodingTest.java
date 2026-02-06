import italian_draughts.domain.Board;
import italian_draughts.domain.GameColor;
import italian_draughts.domain.InvalidMoveException;
import italian_draughts.domain.Move;
import italian_draughts.logic.Game;
import italian_draughts.domain.SquareEncoder;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class EncodingTest {
    Board board = new Board();
    @Test
    public void testEncode() throws InvalidMoveException {
        board.initCells();
        board.placePiece(GameColor.WHITE, 4, 4);
        board.placePiece(GameColor.BLACK, 0, 0);
        Game game = new Game();
        game.boardEncoder(board);
        Map<List<SquareEncoder>, Integer> visits = game.getVisits();
        assertEquals(1, visits.size());
        for (Integer count : visits.values()) {
            assertEquals(1, count);
        }
        game.boardEncoder(board);
        for (Integer count : visits.values()) {
            assertEquals(2, count);
        }
        game.boardEncoder(board);
        for (Integer count : visits.values()) {
            assertEquals(3, count);
        }
        assertTrue(game.checkRepetition());
        List<SquareEncoder> enc = game.getVisits().keySet().iterator().next();
        SquareEncoder tile1 = new SquareEncoder('b',1);
        SquareEncoder tile2 = new SquareEncoder('w',19);
        List<SquareEncoder> testList = new ArrayList<>();
        testList.add(tile1);
        testList.add(tile2);
        assertEquals(testList, enc);
    }
    @Test
    public void removalTest() throws InvalidMoveException {
        board.initCells();
        board.placePiece(GameColor.WHITE, 3, 7);
        board.placePiece(GameColor.BLACK, 0, 0);
        board.placePiece(GameColor.WHITE, 4, 4);
        board.placePiece(GameColor.BLACK, 3, 3);
        Game game = new Game();
        game.boardEncoder(board);
        List<SquareEncoder> enc = game.getVisits().keySet().iterator().next();
        SquareEncoder tile1 = new SquareEncoder('b',1);
        SquareEncoder tile2 = new SquareEncoder('b',14);
        SquareEncoder tile3 = new SquareEncoder('w',16);
        SquareEncoder tile4 = new SquareEncoder('w',19);
        List<SquareEncoder> testList = new ArrayList<>();
        testList.add(tile1);
        testList.add(tile2);
        testList.add(tile3);
        testList.add(tile4);
        assertEquals(testList, enc);
        List<Move> moves = new ArrayList<>();
        moves.add(new Move(4, 4, 2, 2));
        game.movePieces(moves, board);
        game.boardEncoder(board);
        SquareEncoder tile6 = new SquareEncoder('w',10);
        List<SquareEncoder> testList2 = new ArrayList<>();
        testList2.add(tile1);
        testList2.add(tile6);
        testList2.add(tile3);

        List<SquareEncoder> enc2 = game.getVisits().keySet().iterator().next();
        assertEquals(testList2, enc2);
    }
}
