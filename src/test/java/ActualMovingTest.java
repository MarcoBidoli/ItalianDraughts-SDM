import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ActualMovingTest {
    Board board = new Board();
    @Test
    public void testMoving() throws InvalidMoveException {
        board.initCells();
        board.placePiece(Color.WHITE, 4, 4);
        Action action = new Action(board, Color.WHITE);
        List<Move> moves = new ArrayList<>();
        moves.add(new Move(4, 4, 3, 3));
        Game game = new Game();
        game.movePieces(moves, board);
        assertTrue(board.getCell(4,4).isEmpty());
        assertSame(Color.WHITE, board.getCell(3, 3).getPiece().getColor());
    }
    @Test
    public void testEating() throws InvalidMoveException {
        board.initCells();
        board.placePiece(Color.WHITE, 4, 4);
        board.placePiece(Color.BLACK, 3, 3);
        Action action = new Action(board, Color.WHITE);
        List<Move> moves = new ArrayList<>();
        moves.add(new Move(4, 4, 2, 2));
        Game game = new Game();
        game.movePieces(moves, board);
        assertTrue(board.getCell(4,4).isEmpty());
        assertTrue(board.getCell(3,3).isEmpty());
        assertSame(Color.WHITE, board.getCell(2, 2).getPiece().getColor());
    }
    @Test
    public void testDoubleEating() throws InvalidMoveException {
        board.initCells();
        board.placePiece(Color.WHITE, 6, 6);
        board.placePiece(Color.BLACK, 5, 5);
        board.placePiece(Color.BLACK, 3, 3);
        Action action = new Action(board, Color.WHITE);
        List<Move> moves = new ArrayList<>();
        moves.add(new Move(6, 6, 4, 4));
        moves.add(new Move(4, 4, 2, 2));
        Game game = new Game();
        game.movePieces(moves, board);
        assertTrue(board.getCell(6,6).isEmpty());
        assertTrue(board.getCell(5,5).isEmpty());
        assertTrue(board.getCell(4,4).isEmpty());
        assertTrue(board.getCell(3,3).isEmpty());
        assertSame(Color.WHITE, board.getCell(2, 2).getPiece().getColor());
    }
    @Test
    public void testTripleEating() throws InvalidMoveException {
        board.initCells();
        board.placePiece(Color.WHITE, 6, 6);
        board.placePiece(Color.BLACK, 5, 5);
        board.placePiece(Color.BLACK, 3, 3);
        board.placePiece(Color.BLACK, 1, 1);
        Action action = new Action(board, Color.WHITE);
        List<Move> moves = new ArrayList<>();
        moves.add(new Move(6, 6, 4, 4));
        moves.add(new Move(4, 4, 2, 2));
        moves.add(new Move(2, 2, 0, 0));
        Game game = new Game();
        game.movePieces(moves, board);
        assertTrue(board.getCell(6,6).isEmpty());
        assertTrue(board.getCell(5,5).isEmpty());
        assertTrue(board.getCell(4,4).isEmpty());
        assertTrue(board.getCell(3,3).isEmpty());
        assertTrue(board.getCell(2,2).isEmpty());
        assertTrue(board.getCell(1,1).isEmpty());
        assertSame(Color.WHITE, board.getCell(0, 0).getPiece().getColor());
    }
}
