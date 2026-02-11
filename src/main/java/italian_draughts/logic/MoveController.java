package italian_draughts.logic;

import italian_draughts.domain.Board;
import italian_draughts.domain.InvalidMoveException;
import italian_draughts.domain.Move;
import italian_draughts.domain.Piece;

import java.util.List;

/**
 * Controller for the moves.
 * It is responsible for moving the pieces on the board.
 */
public class MoveController {

    /**
     * Moves the pieces on the board according to the given move.
     * @param move The move to be executed.
     * @param board The board on which the move is to be executed.
     * @return True if a capture occurred, false otherwise.
     * @throws InvalidMoveException If the move is invalid.
     */
    public boolean movePieces(List<Move> move, Board board) throws InvalidMoveException {
        boolean captureOccurred = false;

        while (!move.isEmpty()) {
            Move currentMove = move.removeFirst();
            Piece pieceToMove = board.getPieceAt(currentMove.fromRow(), currentMove.fromCol());

            //promotion
            promotionCheck(currentMove, pieceToMove);

            //moving
            board.placePiece(pieceToMove, currentMove.toRow(), currentMove.toCol());
            board.emptyCell(currentMove.fromRow(), currentMove.fromCol());

            boolean isCapture = Math.abs(currentMove.fromRow() - currentMove.toRow()) == 2;
            if (isCapture) {
                captureOccurred = true;
                board.emptyCell((currentMove.fromRow() + currentMove.toRow()) / 2,
                        (currentMove.toCol() + currentMove.fromCol()) / 2);
            }
        }

        return captureOccurred;
    }

    private static void promotionCheck(Move currentMove, Piece pieceToMove) {
        if ((currentMove.toRow() == 0 || currentMove.toRow() == 7) && !pieceToMove.isKing()) {
            pieceToMove.setKing(true);
        }
    }
}
