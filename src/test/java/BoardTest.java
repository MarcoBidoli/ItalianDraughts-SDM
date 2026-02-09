import italian_draughts.domain.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    private final int nTotPieces = 24;
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
    public void countPieces() {
        Board board = new Board();
        board.setGame();
        int c = board.countColorPieces(GameColor.BLACK) + board.countColorPieces(GameColor.WHITE);
        assertEquals(nTotPieces, c);
    }

    @Test
    public void countPiecesColor() {
        Board board = new Board();
        board.setGame();
        int w = board.countColorPieces(GameColor.WHITE), b = board.countColorPieces(GameColor.BLACK);
        assertEquals(nTotPieces/2, w);
        assertEquals(nTotPieces/2, b);
    }

    @Test
    public void getBoardRepresentationTest() throws InvalidMoveException {
        Board board = new Board();
        board.initCells();

        board.placePiece(GameColor.WHITE, 7, 1);
        board.placePiece(GameColor.BLACK, 0, 2);
        board.placePiece(GameColor.WHITE, PieceType.KING,6, 4);
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
        assertEquals(GameColor.WHITE, board.colorOfPiece(7,1));
    }
    @Test
    public void checkPiece() throws InvalidMoveException {
        Board board = new Board();
        board.initCells();
        board.placePiece(GameColor.WHITE, 7, 1);
        Piece testPiece = new Piece(GameColor.WHITE);
        assertEquals(testPiece.getColor(), board.colorOfPiece(7,1));
        assertEquals(testPiece.isKing(), board.isPieceWithCoordinatesKing(7,1));
    }
    @Test
    public void checkStringToBoard() throws InvalidMoveException {
        Board board = new Board();
        board.initCells();
        String strTest = "--w-------------b-----------------------------------------------";
        board.stringToBoard(strTest);
        assertEquals(strTest, board.getBoardRepresentation());
    }
    @Test
    public void checkStringToBoardWrongLength(){
        Board board = new Board();
        board.initCells();
        String strTest = "--";
        assertThrows(IllegalArgumentException.class, () -> board.stringToBoard(strTest));
    }
    @Test
    public void checkStringToBoardWrongSymbol(){
        Board board = new Board();
        board.initCells();
        String strTest = "hello-----------------------------------------------------------";
        assertThrows(IllegalArgumentException.class, () -> board.stringToBoard(strTest));
    }
}
