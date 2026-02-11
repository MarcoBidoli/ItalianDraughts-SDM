import italian_draughts.domain.*;
import italian_draughts.logic.DrawController;
import italian_draughts.logic.MoveController;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EncodingTest {

    private final Board board;

    public EncodingTest() {
        board = new Board();
    }

    @Test
    public void testEncode() throws InvalidMoveException {
        board.resetGame();
        board.initCells();

        board.placePiece(GameColor.WHITE, 4, 4);
        board.placePiece(GameColor.BLACK, 0, 0);

        DrawController drawController = new DrawController();

        drawController.boardEncoder(board);
        Map<List<SquareEncoder>, Integer> visits = drawController.getVisits();

        assertEquals(1, visits.size());
        for (Integer count : visits.values()) {
            assertEquals(1, count);
        }

        drawController.boardEncoder(board);
        for (Integer count : visits.values()) {
            assertEquals(2, count);
        }

        drawController.boardEncoder(board);
        for (Integer count : visits.values()) {
            assertEquals(3, count);
        }

        assertTrue(drawController.checkRepetition());

        Map<List<SquareEncoder>, Integer> getVisits = drawController.getVisits();
        ArrayList<List<SquareEncoder>> allEnc = new ArrayList<>(getVisits.keySet());
        List<SquareEncoder> enc = allEnc.getFirst();

        SquareEncoder tile1 = new SquareEncoder('b', 1);
        //noinspection MagicNumber
        SquareEncoder tile2 = new SquareEncoder('w', 19);

        List<SquareEncoder> testList = new ArrayList<>();
        testList.add(tile1);
        testList.add(tile2);

        assertEquals(testList, enc);
    }

    @Test
    public void removalTest() throws InvalidMoveException {
        board.resetGame();
        board.initCells();

        board.placePiece(GameColor.WHITE, 3, 7);
        board.placePiece(GameColor.BLACK, 0, 0);
        board.placePiece(GameColor.WHITE, 4, 4);
        board.placePiece(GameColor.BLACK, 3, 3);

        DrawController drawController = new DrawController();
        MoveController moveController = new MoveController();

        drawController.boardEncoder(board);

        Map<List<SquareEncoder>, Integer> getVisits = drawController.getVisits();
        ArrayList<List<SquareEncoder>> allEnc = new ArrayList<>(getVisits.keySet());
        List<SquareEncoder> enc = allEnc.getFirst();

        SquareEncoder tile1 = new SquareEncoder('b', 1);
        //noinspection MagicNumber
        SquareEncoder tile2 = new SquareEncoder('b', 14);
        //noinspection MagicNumber
        SquareEncoder tile3 = new SquareEncoder('w', 16);
        //noinspection MagicNumber
        SquareEncoder tile4 = new SquareEncoder('w', 19);

        List<SquareEncoder> testList = new ArrayList<>();
        testList.add(tile1);
        testList.add(tile2);
        testList.add(tile3);
        testList.add(tile4);

        assertEquals(testList, enc);

        List<Move> moves = new ArrayList<>();
        moves.add(new Move(4, 4, 2, 2));

        boolean captureOccurred = moveController.movePieces(moves, board);

        // nel vostro modello: su cattura si azzerano le visite
        if (captureOccurred) {
            drawController.clearVisits();
        }
        drawController.boardEncoder(board);

        SquareEncoder tile6 = new SquareEncoder('w', 10);

        List<SquareEncoder> testList2 = new ArrayList<>();
        testList2.add(tile1);
        testList2.add(tile6);
        testList2.add(tile3);

        Map<List<SquareEncoder>, Integer> getVisits2 = drawController.getVisits();
        ArrayList<List<SquareEncoder>> allEnc2 = new ArrayList<>(getVisits2.keySet());
        List<SquareEncoder> enc2 = allEnc2.getFirst();

        assertEquals(testList2, enc2);
    }
}
