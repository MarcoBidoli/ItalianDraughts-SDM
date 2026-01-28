import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MovingLegalMovesTest {

    // All tests regarding WHITE standard moves
    @Test
    void whitePieceMovesFrontwards() throws InvalidMoveException {
        // Empty board with one White piece at (2, 2)
        Board board = new Board();
        board.placePiece(Color.WHITE, 2, 2);

        Action action = new Action(board, Color.WHITE); // WHITE turn
        List<List<Move>> result = action.moving();

        // Result should contain one sub-list (for one piece) with two moves: (1,1) and (1,3)
        assertEquals(1, result.size());

        List<Move> pieceMoves = result.get(0);
        assertEquals(2, pieceMoves.size()); // 2 moves for this piece
        assertTrue(pieceMoves.stream().anyMatch(m -> m.toRow == 1 && m.toCol == 1)); // (2,2) -> (1,1)
        assertTrue(pieceMoves.stream().anyMatch(m -> m.toRow == 1 && m.toCol == 3)); // (2,2) -> (1,3)
    }

    @Test
    void whitePieceMovesFrontwardFromRightEdgeOfBoard() throws InvalidMoveException {
        // Empty board with one White piece
        Board board = new Board();
        board.placePiece(Color.WHITE, 5, 7);

        Action action = new Action(board, Color.WHITE); // WHITE turn
        List<List<Move>> result = action.moving();

        // Result should contain one sub-list (for one piece) with one move
        assertEquals(1, result.size());

        List<Move> pieceMoves = result.get(0);
        assertEquals(1, pieceMoves.size()); // 1 move for this piece
        assertTrue(pieceMoves.stream().anyMatch(m -> m.toRow == 4 && m.toCol == 6)); // (5,7) -> (4,6)
    }

    @Test
    void whitePieceMovesFrontwardFromLeftEdgeOfBoard() throws InvalidMoveException {
        // Empty board with one White piece
        Board board = new Board();
        board.placePiece(Color.WHITE, 4, 0);

        Action action = new Action(board, Color.WHITE); // WHITE turn
        List<List<Move>> result = action.moving();

        // Result should contain one sub-list (for one piece) with one move
        assertEquals(1, result.size());

        List<Move> pieceMoves = result.get(0);
        assertEquals(1, pieceMoves.size()); // 1 move for this piece
        assertTrue(pieceMoves.stream().anyMatch(m -> m.toRow == 3 && m.toCol == 1)); // (4,0) -> (3,1)
    }

    /* TODO: insert tests for multiple sub-lists with legal moves here */

    // All tests regarding BLACK standard moves
    @Test
    void blackPieceMovesFrontwards() throws InvalidMoveException {
        // Empty board with one Black piece
        Board board = new Board();
        board.placePiece(Color.BLACK, 2, 4);

        Action action = new Action(board, Color.BLACK); // BLACK turn
        List<List<Move>> result = action.moving();

        // Result should contain one sub-list (for one piece) with two moves: (1,1) and (1,3)
        assertEquals(1, result.size());

        List<Move> pieceMoves = result.get(0);
        assertEquals(2, pieceMoves.size()); // 2 moves for this piece
        assertTrue(pieceMoves.stream().anyMatch(m -> m.toRow == 3 && m.toCol == 3)); // (2,4) -> (4,3)
        assertTrue(pieceMoves.stream().anyMatch(m -> m.toRow == 3 && m.toCol == 5)); // (2,4) -> (4,5)
    }

    @Test
    void blackPieceMovesFrontwardFromRightEdgeOfBoard() throws InvalidMoveException {
        // Empty board with one Black piece
        Board board = new Board();
        board.placePiece(Color.BLACK, 3, 7);

        Action action = new Action(board, Color.BLACK); // BLACK turn
        List<List<Move>> result = action.moving();

        // Result should contain one sub-list (for one piece) with one move
        assertEquals(1, result.size());

        List<Move> pieceMoves = result.get(0);
        assertEquals(1, pieceMoves.size()); // 1 move for this piece
        assertTrue(pieceMoves.stream().anyMatch(m -> m.toRow == 4 && m.toCol == 6)); // (3,7) -> (4,6)
    }

    @Test
    void blackPieceMovesFrontwardFromLeftEdgeOfBoard() throws InvalidMoveException {
        // Empty board with one White piece
        Board board = new Board();
        board.placePiece(Color.WHITE, 2, 0);

        Action action = new Action(board, Color.WHITE); // WHITE turn
        List<List<Move>> result = action.moving();

        // Result should contain one sub-list (for one piece) with one move
        assertEquals(1, result.size());

        List<Move> pieceMoves = result.get(0);
        assertEquals(1, pieceMoves.size()); // 1 move for this piece
        assertTrue(pieceMoves.stream().anyMatch(m -> m.toRow == 3 && m.toCol == 1)); // (2,0) -> (3,1)
    }

    /* TODO: insert tests for multiple sub-lists with legal moves here */
}
