import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MovingLegalMovesTest {

    private List<Move> getMovesForPiece(List<List<Move>> allMoves, int testedPieceRow, int testedPieceCol) {
        return allMoves.stream()
                .filter(list -> !list.isEmpty() &&
                        list.getFirst().fromRow == testedPieceRow &&
                        list.getFirst().fromCol == testedPieceCol)
                .findFirst()// single list for each piece
                .orElse(List.of()); // Return empty list if no moves found
    }

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

        List<Move> pieceMoves = result.getFirst();
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

        List<Move> pieceMoves = result.getFirst();
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

        List<Move> pieceMoves = result.getFirst();
        assertEquals(1, pieceMoves.size()); // 1 move for this piece
        assertTrue(pieceMoves.stream().anyMatch(m -> m.toRow == 3 && m.toCol == 1)); // (4,0) -> (3,1)
    }

    @Test
    void whitePieceBlockedByFriend() throws InvalidMoveException {
        Board board = new Board();
        board.placePiece(Color.WHITE, 2, 4); // Block the left diagonal
        board.placePiece(Color.WHITE, 3, 3);

        Action action = new Action(board, Color.WHITE);
        List<List<Move>> result = action.moving();

        List<Move> allMoves = result.stream()
                .flatMap(List::stream)
                .toList();

        assertTrue(allMoves.stream().anyMatch(m -> m.fromRow == 3 && m.fromCol == 3 && m.toRow == 2 && m.toCol == 2)); // legal move exists
        assertTrue(allMoves.stream().noneMatch(m -> m.fromRow == 3 && m.fromCol == 3 && m.toRow == 2 && m.toCol == 4)); // the move on the occupied cell does not
    }

    @Test
    void multipleWhitePieces() throws InvalidMoveException {
        Board board = new Board();
        board.placePiece(Color.WHITE, 5, 3); // Expected moves (4,2) and (4,4)
        board.placePiece(Color.WHITE, 5, 7); // Expected move (4,6)

        Action action = new Action(board, Color.WHITE);
        List<List<Move>> result = action.moving();

        // moves for exactly 2 pieces
        assertEquals(2, result.size());

        // First piece
        List<Move> movesA = getMovesForPiece(result, 5, 3);
        assertEquals(2, movesA.size());
        assertTrue(movesA.stream().anyMatch(m -> m.fromRow == 5 && m.fromCol == 3 && m.toRow == 4 && m.toCol == 2));
        assertTrue(movesA.stream().anyMatch(m -> m.fromRow == 5 && m.fromCol == 3 && m.toRow == 4 && m.toCol == 4));

        // Second piece
        List<Move> movesB = getMovesForPiece(result, 5, 7);
        assertEquals(1, movesB.size());
        assertTrue(movesB.stream().anyMatch(m -> m.fromRow == 5 && m.fromCol == 7 && m.toRow == 4 && m.toCol == 6));
    }

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

        List<Move> pieceMoves = result.getFirst();
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

        List<Move> pieceMoves = result.getFirst();
        assertEquals(1, pieceMoves.size()); // 1 move for this piece
        assertTrue(pieceMoves.stream().anyMatch(m -> m.toRow == 4 && m.toCol == 6)); // (3,7) -> (4,6)
    }

    @Test
    void blackPieceMovesFrontwardFromLeftEdgeOfBoard() throws InvalidMoveException {
        // Empty board with one White piece
        Board board = new Board();
        board.placePiece(Color.BLACK, 2, 0);

        Action action = new Action(board, Color.BLACK); // WHITE turn
        List<List<Move>> result = action.moving();

        // Result should contain one sub-list (for one piece) with one move
        assertEquals(1, result.size());

        List<Move> pieceMoves = result.getFirst();
        assertEquals(1, pieceMoves.size()); // 1 move for this piece
        assertTrue(pieceMoves.stream().anyMatch(m -> m.toRow == 3 && m.toCol == 1)); // (2,0) -> (3,1)
    }

    @Test
    void blackPieceBlockedByFriend() throws InvalidMoveException {
        Board board = new Board();
        board.placePiece(Color.BLACK, 3, 3);
        board.placePiece(Color.BLACK, 4, 4); // Block the left diagonal

        Action action = new Action(board, Color.BLACK);
        List<List<Move>> result = action.moving();

        List<Move> allMoves = result.stream()
                .flatMap(List::stream)
                .toList();

        assertTrue(allMoves.stream().anyMatch(m -> m.fromRow == 3 && m.fromCol == 3 && m.toRow == 4 && m.toCol == 2)); // legal move exists
        assertTrue(allMoves.stream().noneMatch(m -> m.fromRow == 3 && m.fromCol == 3 && m.toRow == 4 && m.toCol == 4)); // the move on the occupied cell does not
    }

    @Test
    void multipleBlackPieces() throws InvalidMoveException {
        Board board = new Board();
        board.placePiece(Color.BLACK, 3, 3); // Expected moves (4,2) and (4,4)
        board.placePiece(Color.BLACK, 6, 0); // Expected move (7,1)

        Action action = new Action(board, Color.BLACK);
        List<List<Move>> result = action.moving();

        // moves for exactly 2 pieces
        assertEquals(2, result.size());

        // First piece
        List<Move> movesA = getMovesForPiece(result, 3, 3);
        assertEquals(2, movesA.size());
        assertTrue(movesA.stream().anyMatch(m -> m.fromRow == 3 && m.fromCol == 3 && m.toRow == 4 && m.toCol == 2));
        assertTrue(movesA.stream().anyMatch(m -> m.fromRow == 3 && m.fromCol == 3 && m.toRow == 4 && m.toCol == 4));

        // Second piece
        List<Move> movesB = getMovesForPiece(result, 6, 0);
        assertEquals(1, movesB.size());
        assertTrue(movesB.stream().anyMatch(m -> m.fromRow == 6 && m.fromCol == 0 && m.toRow == 7 && m.toCol == 1));
    }
}
