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
        board.placePiece(Color.WHITE, 4, 4);
        board.placePiece(Color.BLACK, 0, 0);
        Game game = new Game();
        game.boardEncoder(board);
        Map<List<TileEnc>, Integer> visits = game.getVisits();
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
        List<TileEnc> enc = game.getVisits().keySet().iterator().next();
        TileEnc tile1 = new TileEnc(2,1);
        TileEnc tile2 = new TileEnc(1,19);
        List<TileEnc> testList = new ArrayList<>();
        testList.add(tile1);
        testList.add(tile2);
        assertEquals(testList, enc);
    }
    @Test
    public void removalTest() throws InvalidMoveException {
        board.initCells();
        board.placePiece(Color.WHITE, 3, 7);
        board.placePiece(Color.BLACK, 0, 0);
        board.placePiece(Color.WHITE, 4, 4);
        board.placePiece(Color.BLACK, 3, 3);
        Game game = new Game();
        game.boardEncoder(board);
        List<TileEnc> enc = game.getVisits().keySet().iterator().next();
        TileEnc tile1 = new TileEnc(2,1);
        TileEnc tile2 = new TileEnc(2,14);
        TileEnc tile3 = new TileEnc(1,16);
        TileEnc tile4 = new TileEnc(1,19);
        List<TileEnc> testList = new ArrayList<>();
        testList.add(tile1);
        testList.add(tile2);
        testList.add(tile3);
        testList.add(tile4);
        assertEquals(testList, enc);
        List<Move> moves = new ArrayList<>();
        moves.add(new Move(4, 4, 2, 2));
        game.movePieces(moves, board);
        game.boardEncoder(board);
        TileEnc tile6 = new TileEnc(1,10);
        List<TileEnc> testList2 = new ArrayList<>();
        testList2.add(tile1);
        testList2.add(tile6);
        testList2.add(tile3);

        List<TileEnc> enc2 = game.getVisits().keySet().iterator().next();
        assertEquals(testList2, enc2);
    }
}
