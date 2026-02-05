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
        Map<List<SquareEnc>, Integer> visits = game.getVisits();
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
        List<SquareEnc> enc = game.getVisits().keySet().iterator().next();
        SquareEnc tile1 = new SquareEnc('n',1);
        SquareEnc tile2 = new SquareEnc('w',19);
        List<SquareEnc> testList = new ArrayList<>();
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
        List<SquareEnc> enc = game.getVisits().keySet().iterator().next();
        SquareEnc tile1 = new SquareEnc('n',1);
        SquareEnc tile2 = new SquareEnc('n',14);
        SquareEnc tile3 = new SquareEnc('w',16);
        SquareEnc tile4 = new SquareEnc('w',19);
        List<SquareEnc> testList = new ArrayList<>();
        testList.add(tile1);
        testList.add(tile2);
        testList.add(tile3);
        testList.add(tile4);
        assertEquals(testList, enc);
        List<Move> moves = new ArrayList<>();
        moves.add(new Move(4, 4, 2, 2));
        game.movePieces(moves, board);
        game.boardEncoder(board);
        SquareEnc tile6 = new SquareEnc('w',10);
        List<SquareEnc> testList2 = new ArrayList<>();
        testList2.add(tile1);
        testList2.add(tile6);
        testList2.add(tile3);

        List<SquareEnc> enc2 = game.getVisits().keySet().iterator().next();
        assertEquals(testList2, enc2);
    }
}
