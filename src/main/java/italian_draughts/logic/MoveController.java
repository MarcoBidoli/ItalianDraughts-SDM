package italian_draughts.logic;

import italian_draughts.domain.Board;
import italian_draughts.domain.InvalidMoveException;
import italian_draughts.domain.Move;
import italian_draughts.domain.Piece;

import java.util.List;

public class MoveController {

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

