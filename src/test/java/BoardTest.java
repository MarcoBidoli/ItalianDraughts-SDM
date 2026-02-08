import italian_draughts.domain.Board;
import italian_draughts.domain.GameColor;
import italian_draughts.domain.InvalidMoveException;
import italian_draughts.domain.Piece;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    @Test
    public void checkSEBlack() {
        Board board = new Board();
        assertEquals(GameColor.BLACK, board.getCellColor(7,7));
    }

    @Test
    public void checkNWBlack() {
        Board board = new Board();
        assertEquals(GameColor.BLACK, board.getCellColor(0,0));
    }

    @Test
    public void emptyTest() {
        Board board = new Board();
        assertTrue(board.isEmpty());
    }

    @Test
    public void countPieces() {
        Board board = new Board();
        board.setGame();
        int c = board.countColorPieces(GameColor.BLACK) + board.countColorPieces(GameColor.WHITE);
        assertEquals(24, c);
    }

    @Test
    public void countPiecesColor() {
        Board board = new Board();
        board.setGame();
        int w = board.countColorPieces(GameColor.WHITE), b = board.countColorPieces(GameColor.BLACK);
        assertEquals(12, w);
        assertEquals(12, b);
    }

    @Test
    public void printBoard() { //Questo metodo non testa niente, scegliere se tenere
        Board board = new Board();
        board.setGame();
        board.printBoard();
    }
    @Test
    public void getBoardRepresentationTest() throws InvalidMoveException {
        Board board = new Board();
        board.initCells();

        board.placePiece(GameColor.WHITE, 7, 1);
        board.placePiece(GameColor.BLACK, 0, 2);
        board.placeKing(GameColor.WHITE, 6, 4);
        String repr = board.getBoardRepresentation();

        assertNotNull(repr);
        assertTrue(repr.contains("w"));
        assertTrue(repr.contains("b"));
        assertTrue(repr.contains("W"));
        assertTrue(repr.contains("-") || repr.contains("."));
    }
    @Test
    public void checkEmptyCell() throws InvalidMoveException {
        Board board = new Board();
        board.initCells();
        board.placePiece(GameColor.WHITE, 7, 1);
        board.emptyCell(7,1);
        assertTrue(board.isEmptyCell(7,1));
    }
    @Test
    public void checkColorPiece() throws InvalidMoveException {
        Board board = new Board();
        board.initCells();
        board.placePiece(GameColor.WHITE, 7, 1);
        assertEquals(GameColor.WHITE, board.getColorOfPieceWithCoordinates(7,1));
    }
    @Test
    public void checkPiece() throws InvalidMoveException {
        Board board = new Board();
        board.initCells();
        board.placePiece(GameColor.WHITE, 7, 1);
        Piece testPiece = new Piece(GameColor.WHITE);
        assertEquals(testPiece.getColor(), board.getColorOfPieceWithCoordinates(7,1));
        assertEquals(testPiece.isKing(), board.isPieceWithCoordinatesKing(7,1));
    }
}
