import italian_draughts.domain.*;
import italian_draughts.logic.LegalMoves;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EatingLegalMovesTest {
    @Test
    public void singleEat() throws InvalidMoveException {
        Board board = new Board();
        board.placePiece(GameColor.WHITE, 5, 3);
        board.placePiece(GameColor.BLACK, 4, 2);

        LegalMoves legalMoves = new LegalMoves(board, GameColor.WHITE);

        List<List<Move>> result = legalMoves.eating();
        assertEquals(1, result.size());

        List<Move> pieceEatings = result.getFirst();
        assertEquals(1, pieceEatings.size());
        assertEquals(3, pieceEatings.getFirst().toRow());
        assertEquals(1, pieceEatings.getFirst().toCol());

        Cell pieceEatenCell = board.getCell(4, 2);
        assertFalse(pieceEatenCell.isEmpty());
        Cell firstPieceCell = board.getCell(5,3);
        assertNotNull(firstPieceCell.getPiece());
    }

    @Test
    public void doubleEat() throws InvalidMoveException {
        Board board = new Board();
        String boardString = "------------------b---------------b--------w--------------------";
        board.stringToBoard(boardString);
        board.printBoard();
        LegalMoves legalMoves = new LegalMoves(board, GameColor.WHITE);

        List<List<Move>> result = legalMoves.eating();
        assertEquals(1, result.size());

        List<Move> pieceEatings = result.getFirst();
        assertEquals(2, pieceEatings.size());
        assertEquals(3, pieceEatings.get(0).toRow());
        assertEquals(1, pieceEatings.get(0).toCol());
        assertEquals(1, pieceEatings.get(1).toRow());
        assertEquals(3, pieceEatings.get(1).toCol());

        assertEquals(boardString, board.getBoardRepresentation());
    }

    @Test
    public void pieceEatingKing() throws InvalidMoveException {
        Board board = new Board();
        board.placePiece(GameColor.WHITE, 5, 3);
        board.placePiece(GameColor.BLACK, PieceType.KING, 4, 2);

        LegalMoves legalMoves = new LegalMoves(board, GameColor.WHITE);
        List<List<Move>> result = legalMoves.eating();
        assertEquals(0, result.size());
    }

    @Test
    public void kingEatsFirst() throws InvalidMoveException {
        Board board = new Board();
        board.placePiece(GameColor.WHITE, PieceType.KING, 4, 2);
        board.placePiece(GameColor.WHITE, 4, 4);
        board.placePiece(GameColor.BLACK, 3, 3);

        LegalMoves legalMoves = new LegalMoves(board, GameColor.WHITE);
        List<List<Move>> result = legalMoves.eating();
        assertEquals(1, result.size());

        List<Move> pieceEatings = result.getFirst();
        assertEquals(1, pieceEatings.size());
        assertEquals(2, pieceEatings.getFirst().toRow(), "Obtained a wrong piece eaten - rows");
        assertEquals(4, pieceEatings.getFirst().toCol(), "Obtained a wrong piece eaten - cols");
    }

    @Test
    public void kingEatenFirst() throws InvalidMoveException {
        Board board = new Board();
        board.placePiece(GameColor.WHITE, PieceType.KING, 4, 2);
        board.placePiece(GameColor.BLACK, 3, 1);
        board.placePiece(GameColor.BLACK, 3, 3);

        LegalMoves legalMoves = new LegalMoves(board, GameColor.WHITE);
        List<List<Move>> result = legalMoves.eating();
        assertEquals(2, result.size());

        List<Move> pieceEatings = result.getFirst();
        assertEquals(1, pieceEatings.size());
        assertEquals(2, pieceEatings.getFirst().toRow(), "Obtained a wrong piece eaten - rows");
        assertEquals(0, pieceEatings.getFirst().toCol(), "Obtained a wrong piece eaten - cols");

        pieceEatings = result.get(1);
        assertEquals(1, pieceEatings.size());
        assertEquals(2, pieceEatings.getFirst().toRow());
        assertEquals(4, pieceEatings.getFirst().toCol());
    }

    @Test
    public void eatTheMostPieces() throws InvalidMoveException {
        Board board = new Board();
        board.placePiece(GameColor.WHITE, PieceType.KING, 5, 3);
        board.placePiece(GameColor.BLACK, PieceType.KING, 4, 2);
        board.placePiece(GameColor.WHITE, 5, 5);
        board.placePiece(GameColor.BLACK, 4, 6);
        board.placePiece(GameColor.BLACK, 2, 6);

        LegalMoves legalMoves = new LegalMoves(board, GameColor.WHITE);
        List<List<Move>> result = legalMoves.eating();
        assertEquals(1, result.size(), "Found too much best eatings");

        List<Move> pieceEatings = result.getFirst();
        assertEquals(2, pieceEatings.size());
        assertEquals(3, pieceEatings.get(0).toRow(), "1st Obtained a wrong piece eaten - rows");
        assertEquals(7, pieceEatings.get(0).toCol(), "1st Obtained a wrong piece eaten - cols");
        assertEquals(1, pieceEatings.get(1).toRow(), "2nd Obtained a wrong piece eaten - rows");
        assertEquals(5, pieceEatings.get(1).toCol(), "2nd Obtained a wrong piece eaten - cols");
    }

    @Test
    public void eatTheMostKings() throws InvalidMoveException {
        Board board = new Board();
        board.placePiece(GameColor.WHITE, PieceType.KING, 5, 3);
        board.placePiece(GameColor.BLACK, PieceType.KING, 4, 2);
        board.placePiece(GameColor.BLACK, PieceType.KING, 4, 4);
        board.placePiece(GameColor.BLACK, PieceType.KING, 2, 4);

        LegalMoves legalMoves = new LegalMoves(board, GameColor.WHITE);
        List<List<Move>> result = legalMoves.eating();
        assertEquals(1, result.size(), "Found too much best eatings");

        List<Move> pieceEatings = result.getFirst();
        assertEquals(2, pieceEatings.size());
        assertEquals(3, pieceEatings.get(0).toRow(), "1st Obtained a wrong piece eaten - rows");
        assertEquals(5, pieceEatings.get(0).toCol(), "1st Obtained a wrong piece eaten - cols");
        assertEquals(1, pieceEatings.get(1).toRow(), "2nd Obtained a wrong piece eaten - rows");
        assertEquals(3, pieceEatings.get(1).toCol(), "2nd Obtained a wrong piece eaten - cols");
    }

    @Test
    public void multipleBestEatings() throws InvalidMoveException {
        Board board = new Board();
        String stringBoard = "----------------------------------B-B------W--------------------";
        board.stringToBoard(stringBoard);

        LegalMoves legalMoves = new LegalMoves(board, GameColor.WHITE);
        List<List<Move>> result = legalMoves.eating();
        assertEquals(2, result.size(), "Couldn't find all best eatings");
        for (int i = 0; i < result.size(); i++) {
            List<Move> move = result.get(i);
            assertEquals(1, move.size());
            Move first = move.getFirst();
            assertEquals(3, first.toRow(), "wrong row - " + i);
            assertTrue(first.toCol() == 5 || first.toCol() == 1, "wrong column - " + i);
        }

        board.emptyBoard();
        stringBoard = "--------------------B-B-----------B-B------W--------------------";
        board.stringToBoard(stringBoard);
        legalMoves = new LegalMoves(board, GameColor.WHITE);
        result = legalMoves.eating();
        assertEquals(2, result.size(), "Couldn't find all best eatings");

        for (int i = 0; i < result.size(); i++) {
            List<Move> move = result.get(i);
            assertEquals(2, move.size(), "Couldn't complete eating - " + i);
            Move first = move.getFirst();
            Move second = move.get(1);
            assertEquals(3, first.toRow());
            assertEquals(5, first.toCol());
            assertEquals(1, second.toRow());
            assertTrue(second.toCol() == 3 || second.toCol() == 7, "wrong column - " + i);
        }
    }
}
