import italian_draughts.domain.*;
import italian_draughts.logic.Game;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ActualMovingTest {
    private final Board board = new Board();
    private final Game game = new Game();
    private final List<Move> moves = new ArrayList<>();
    @Test
    public void testMoving() throws InvalidMoveException {
        board.initCells();
        board.placePiece(GameColor.WHITE, PieceType.MAN, 4, 4);
        moves.add(new Move(4, 4, 3, 3));
        game.movePieces(moves, board);
        assertTrue(board.isEmptyCell(4,4));
        assertSame(GameColor.WHITE, board.colorOfPiece(3,3));
    }
    @Test
    public void testEating() throws InvalidMoveException {
        board.initCells();
        board.placePiece(GameColor.WHITE, PieceType.MAN, 4, 4);
        board.placePiece(GameColor.BLACK, PieceType.MAN, 3, 3);
        moves.add(new Move(4, 4, 2, 2));
        game.movePieces(moves, board);
        assertTrue(board.isEmptyCell(4,4));
        assertTrue(board.isEmptyCell(3,3));
        assertSame(GameColor.WHITE, board.colorOfPiece(2,2));
    }
    @Test
    public void testDoubleEating() throws InvalidMoveException {
        board.initCells();
        board.placePiece(GameColor.WHITE, PieceType.MAN, 6, 6);
        board.placePiece(GameColor.BLACK, PieceType.MAN, 5, 5);
        board.placePiece(GameColor.BLACK, PieceType.MAN, 3, 3);
        moves.add(new Move(6, 6, 4, 4));
        moves.add(new Move(4, 4, 2, 2));
        game.movePieces(moves, board);
        assertTrue(board.isEmptyCell(6,6));
        assertTrue(board.isEmptyCell(5,5));
        assertTrue(board.isEmptyCell(4,4));
        assertTrue(board.isEmptyCell(3,3));
        assertSame(GameColor.WHITE, board.colorOfPiece(2,2));
    }
    @Test
    public void testTripleEating() throws InvalidMoveException {
        board.initCells();
        board.placePiece(GameColor.WHITE, PieceType.MAN, 6, 6);
        board.placePiece(GameColor.BLACK, PieceType.MAN, 5, 5);
        board.placePiece(GameColor.BLACK, PieceType.MAN, 3, 3);
        board.placePiece(GameColor.BLACK, PieceType.MAN, 1, 1);
        moves.add(new Move(6, 6, 4, 4));
        moves.add(new Move(4, 4, 2, 2));
        moves.add(new Move(2, 2, 0, 0));
        game.movePieces(moves, board);
        assertTrue(board.isEmptyCell(6,6));
        assertTrue(board.isEmptyCell(5,5));
        assertTrue(board.isEmptyCell(4,4));
        assertTrue(board.isEmptyCell(3,3));
        assertTrue(board.isEmptyCell(2,2));
        assertTrue(board.isEmptyCell(1,1));
        assertSame(GameColor.WHITE, board.colorOfPiece(0,0));
    }
    @Test
    public void testPromotion() throws InvalidMoveException {
        board.initCells();
        board.placePiece(GameColor.WHITE, PieceType.MAN, 6, 6);
        moves.add(new Move(6, 6, 7, 7));
        game.movePieces(moves, board);
        assertTrue(board.isKingAt(7,7));
    }
}