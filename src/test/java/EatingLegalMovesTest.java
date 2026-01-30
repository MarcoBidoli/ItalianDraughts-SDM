import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EatingLegalMovesTest {
    @Test
    public void singleEat() throws InvalidMoveException {
        Board board = new Board();
        board.placePiece(Color.WHITE, 5, 3);
        board.placePiece(Color.BLACK, 4, 2);

        LegalMoves legalMoves = new LegalMoves(board, Color.WHITE);

        List<List<Move>> result = legalMoves.eating();
        assertEquals(1, result.size());

        List<Move> pieceEatings = result.getFirst();
        assertEquals(1, pieceEatings.size());
        assertEquals(3, pieceEatings.get(0).toRow);
        assertEquals(1, pieceEatings.get(0).toCol);

        assertFalse(board.getCell(4, 2).isEmpty());
        assertNotNull(board.getCell(5,3).getPiece());
    }

    @Test
    public void doubleEat() throws InvalidMoveException {
        Board board = new Board();
        board.placePiece(Color.WHITE, 5, 3);
        board.placePiece(Color.BLACK, 4, 2);
        board.placePiece(Color.BLACK, 2, 2);

        LegalMoves legalMoves = new LegalMoves(board, Color.WHITE);

        List<List<Move>> result = legalMoves.eating();
        assertEquals(1, result.size());

        List<Move> pieceEatings = result.getFirst();
        assertEquals(2, pieceEatings.size());
        assertEquals(3, pieceEatings.get(0).toRow);
        assertEquals(1, pieceEatings.get(0).toCol);
        assertEquals(1, pieceEatings.get(1).toRow);
        assertEquals(3, pieceEatings.get(1).toCol);

        assertFalse(board.getCell(4, 2).isEmpty());
        assertFalse(board.getCell(2, 2).isEmpty());
        assertNotNull(board.getCell(5,3).getPiece());
        assertTrue(board.getCell(3, 1).isEmpty());
        assertTrue(board.getCell(1, 3).isEmpty());
    }

    @Test
    public void pieceEatingKing() throws InvalidMoveException {
        Board board = new Board();
        board.placePiece(Color.WHITE, 5, 3);
        board.placeKing(Color.BLACK, 4, 2);

        LegalMoves legalMoves = new LegalMoves(board, Color.WHITE);
        List<List<Move>> result = legalMoves.eating();
        assertEquals(0, result.size());
    }

    @Test
    public void kingEatsFirst() throws InvalidMoveException {
        Board board = new Board();
        board.placeKing(Color.WHITE, 4, 2);
        board.placePiece(Color.WHITE, 4, 4);
        board.placePiece(Color.BLACK, 3, 3);

        LegalMoves legalMoves = new LegalMoves(board, Color.WHITE);
        List<List<Move>> result = legalMoves.eating();
        assertEquals(2, result.size());

        List<Move> pieceEatings = result.getFirst();
        assertEquals(1, pieceEatings.size());
        assertEquals(2, pieceEatings.get(0).toRow, "Obtained a wrong piece eaten - rows");
        assertEquals(4, pieceEatings.get(0).toCol,  "Obtained a wrong piece eaten - cols");

        pieceEatings = result.get(1);
        assertEquals(1, pieceEatings.size());
        assertEquals(2, pieceEatings.get(0).toRow);
        assertEquals(2, pieceEatings.get(0).toCol);
    }

    @Test
    public void kingEatenFirst() throws InvalidMoveException {
        Board board = new Board();
        board.placeKing(Color.WHITE, 4, 2);
        board.placePiece(Color.BLACK, 3, 1);
        board.placePiece(Color.BLACK, 3, 3);

        LegalMoves legalMoves = new LegalMoves(board, Color.WHITE);
        List<List<Move>> result = legalMoves.eating();
        assertEquals(2, result.size());

        List<Move> pieceEatings = result.getFirst();
        assertEquals(1, pieceEatings.size());
        assertEquals(2, pieceEatings.get(0).toRow, "Obtained a wrong piece eaten - rows");
        assertEquals(0, pieceEatings.get(0).toCol,  "Obtained a wrong piece eaten - cols");

        pieceEatings = result.get(1);
        assertEquals(1, pieceEatings.size());
        assertEquals(2, pieceEatings.get(0).toRow);
        assertEquals(4, pieceEatings.get(0).toCol);
    }

    @Test
    public void eatTheMostPieces() throws InvalidMoveException {

    }

    @Test
    public void eatTheMostKings() throws InvalidMoveException {

    }
}
