import italian_draughts.domain.Board;
import italian_draughts.domain.GameColor;
import italian_draughts.domain.InvalidMoveException;
import italian_draughts.domain.Move;
import italian_draughts.logic.LegalMoves;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MovingLegalMovesTest {

    private List<List<Move>> getMovesForPiece(List<List<Move>> allMoves, int pieceFromRow, int pieceFromCol) {
        return allMoves.stream()
                .filter(list -> !list.isEmpty() &&
                        list.getFirst().fromRow == pieceFromRow &&
                        list.getFirst().fromCol == pieceFromCol)
                .toList();
    }

    // All tests regarding WHITE standard moves
    @Test
    void whitePieceMovesFrontwards() throws InvalidMoveException {
        // Empty board with one White piece at (2, 2)
        Board board = new Board();
        board.placePiece(GameColor.WHITE, 2, 2);

        LegalMoves legalMoves = new LegalMoves(board, GameColor.WHITE); // WHITE turn

        // Fake implementation
//        List<List<domain.Move>> result = List.of(
//                List.of(new domain.Move(2, 2, 1, 1)),
//                List.of(new domain.Move(2, 2, 1, 3))
//        );
        List<List<Move>> result = legalMoves.moving();

        // expected 2 distinct List<domain.Move> sequences (paths)
        assertEquals(2, result.size());

        // Each sub-list should contain exactly 1 move for a standard move
        for (List<Move> path : result) {
            assertEquals(1, path.size());
        }

        // Both intended destinations are covered by the paths
        boolean foundLeft = result.stream()
                .anyMatch(path -> path.getFirst().toRow == 1 && path.getFirst().toCol == 1); // (2,2) -> (1,1)
        boolean foundRight = result.stream()
                .anyMatch(path -> path.getFirst().toRow == 1 && path.getFirst().toCol == 3); // (2,2) -> (1,3)

        assertTrue(foundLeft);
        assertTrue(foundRight);
    }

    @Test
    void whitePieceMovesFrontwardFromRightEdgeOfBoard() throws InvalidMoveException {
        // Empty board with one White piece
        Board board = new Board();
        board.placePiece(GameColor.WHITE, 5, 7);

        LegalMoves legalMoves = new LegalMoves(board, GameColor.WHITE); // WHITE turn
        List<List<Move>> result = legalMoves.moving();

        // Expected 1 sub-list with exactly 1 move
        assertEquals(1, result.size());
        assertEquals(1, result.getFirst().size());

        boolean foundLeft = result.stream()
                .anyMatch(path -> path.getFirst().toRow == 4 && path.getFirst().toCol == 6); // (5,7) -> (4,6)

        assertTrue(foundLeft);
    }

    @Test
    void whitePieceMovesFrontwardFromLeftEdgeOfBoard() throws InvalidMoveException {
        // Empty board with one White piece
        Board board = new Board();
        board.placePiece(GameColor.WHITE, 4, 0);

        LegalMoves legalMoves = new LegalMoves(board, GameColor.WHITE); // WHITE turn
        List<List<Move>> result = legalMoves.moving();

        // Expected 1 sub-list with exactly 1 move
        assertEquals(1, result.size());
        assertEquals(1, result.getFirst().size());

        boolean foundRight = result.stream()
                .anyMatch(path -> path.getFirst().toRow == 3 && path.getFirst().toCol == 1); // (4,0) -> (3,1)

        assertTrue(foundRight);
    }

    @Test
    void whitePieceBlockedByFriend() throws InvalidMoveException {
        Board board = new Board();
        board.placePiece(GameColor.WHITE, 2, 4);
        board.placePiece(GameColor.WHITE, 3, 3);

        LegalMoves legalMoves = new LegalMoves(board, GameColor.WHITE);
        List<List<Move>> result = legalMoves.moving();

        List<List<Move>> pathsForPiece = getMovesForPiece(result, 3, 3);

        // Only 1 path expected
        assertEquals(1, pathsForPiece.size());
        // Only 1 move for this path
        assertEquals(1, pathsForPiece.getFirst().size());

        Move m = pathsForPiece.getFirst().getFirst();
        // check the only available move
        assertEquals(3, m.fromRow);
        assertEquals(3, m.fromCol);
        assertEquals(2, m.toRow);
        assertEquals(2, m.toCol);
    }

    @Test
    void multipleWhitePieces() throws InvalidMoveException {
        Board board = new Board();
        board.placePiece(GameColor.WHITE, 5, 3); // Expected moves (4,2) and (4,4)
        board.placePiece(GameColor.WHITE, 5, 7); // Expected move (4,6)

        LegalMoves legalMoves = new LegalMoves(board, GameColor.WHITE);
        List<List<Move>> result = legalMoves.moving();

        // First piece
        List<List<Move>> movesA = getMovesForPiece(result, 5, 3);
        assertEquals(2, movesA.size());
        Move mL = new Move(5, 3, 4, 2);
        Move mR = new Move(5, 3, 4, 4);
        assertTrue(movesA.stream()
                .map(path -> path.getFirst())
                .anyMatch(m -> m.equals(mL)));
        assertTrue(movesA.stream()
                .map(path -> path.getFirst())
                .anyMatch(m -> m.equals(mR)));
        // Second piece
        List<List<Move>> movesB = getMovesForPiece(result, 5, 7);
        assertEquals(1, movesB.size());
        Move mT = new Move(5, 7, 4, 6);
        assertTrue(movesB.stream()
                .map(path -> path.getFirst())
                .anyMatch(m -> m.equals(mT)));
    }

    // ----------------------------------- All tests regarding BLACK standard moves ------------------------------------

    @Test
    void blackPieceMovesFrontwards() throws InvalidMoveException {
        // Empty board with one Black piece
        Board board = new Board();
        board.placePiece(GameColor.BLACK, 2, 4);
        board.printBoard();

        LegalMoves legalMoves = new LegalMoves(board, GameColor.BLACK); // BLACK turn
        List<List<Move>> result = legalMoves.moving();
        IO.println(result);

        // expected 2 distinct List<domain.Move> sequences (paths)
        assertEquals(2, result.size());

        // Each sub-list should contain exactly 1 move for a standard move
        for (List<Move> path : result) {
            assertEquals(1, path.size());
        }

        // Both intended destinations are covered by the paths
        boolean foundRight = result.stream()
                .anyMatch(path -> path.getFirst().toRow == 3 && path.getFirst().toCol == 3); // (2,4) -> (3,5)
        boolean foundLeft = result.stream()
                .anyMatch(path -> path.getFirst().toRow == 3 && path.getFirst().toCol == 5); // (2,4) -> (3,3)

        assertTrue(foundLeft);
        assertTrue(foundRight);
    }

    @Test
    void blackPieceMovesFrontwardFromRightEdgeOfBoard() throws InvalidMoveException {
        // Empty board with one Black piece
        Board board = new Board();
        board.placePiece(GameColor.BLACK, 3, 7);

        LegalMoves legalMoves = new LegalMoves(board, GameColor.BLACK); // BLACK turn
        List<List<Move>> result = legalMoves.moving();

        // Expected 1 sub-list with exactly 1 move
        assertEquals(1, result.size());
        assertEquals(1, result.getFirst().size());

        boolean foundLeft = result.stream()
                       .anyMatch(path -> path.getFirst().toRow == 4 && path.getFirst().toCol == 6); // (3,7) -> (4,6)
        assertTrue(foundLeft);
    }

    @Test
    void blackPieceMovesFrontwardFromLeftEdgeOfBoard() throws InvalidMoveException {
        // Empty board with one White piece
        Board board = new Board();
        board.placePiece(GameColor.BLACK, 2, 0);

        LegalMoves legalMoves = new LegalMoves(board, GameColor.BLACK); // WHITE turn
        List<List<Move>> result = legalMoves.moving();

        // Expected 1 sub-list with exactly 1 move
        assertEquals(1, result.size());
        assertEquals(1, result.getFirst().size());

        boolean foundRight = result.stream()
                .anyMatch(path -> path.getFirst().toRow == 3 && path.getFirst().toCol == 1); // (2,0) -> (3,1)

        assertTrue(foundRight);
    }

    @Test
    void blackPieceBlockedByFriend() throws InvalidMoveException {
        Board board = new Board();
        board.placePiece(GameColor.BLACK, 3, 3);
        board.placePiece(GameColor.BLACK, 4, 4); // Block the left diagonal

        LegalMoves legalMoves = new LegalMoves(board, GameColor.BLACK);
        List<List<Move>> result = legalMoves.moving();

        List<List<Move>> pathsForPiece = getMovesForPiece(result, 3, 3);

        // Only 1 path expected
        assertEquals(1, pathsForPiece.size());
        // Only 1 move for this path
        assertEquals(1, pathsForPiece.getFirst().size());

        Move m = pathsForPiece.getFirst().getFirst();
        // check the only available move
        assertEquals(3, m.fromRow);
        assertEquals(3, m.fromCol);
        assertEquals(4, m.toRow);
        assertEquals(2, m.toCol);
    }

    @Test
    void multipleBlackPieces() throws InvalidMoveException {
        Board board = new Board();
        board.placePiece(GameColor.BLACK, 3, 3); // Expected moves (4,2) and (4,4)
        board.placePiece(GameColor.BLACK, 6, 0); // Expected move (7,1)

        LegalMoves legalMoves = new LegalMoves(board, GameColor.BLACK);
        List<List<Move>> result = legalMoves.moving();

        // First piece
        List<List<Move>> movesA = getMovesForPiece(result, 3, 3);
        assertEquals(2, movesA.size());
        assertTrue(movesA.stream().anyMatch(path -> path.getFirst().toRow == 4 && path.getFirst().toCol == 2));
        assertTrue(movesA.stream().anyMatch(path -> path.getFirst().toRow == 4 && path.getFirst().toCol == 4));

        // Second piece
        List<List<Move>> movesB = getMovesForPiece(result, 6, 0);
        assertEquals(1, movesB.size());
        assertTrue(movesB.stream().anyMatch(path -> path.getFirst().toRow == 7 && path.getFirst().toCol == 1));
    }
}
