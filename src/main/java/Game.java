import java.util.List;

public class Game {
    private Board gameBoard;

    public Board movePieces(List<Move> move, Board board) throws InvalidMoveException {
        while(!move.isEmpty()) {
            Move currentMove = move.removeFirst();
            Piece pieceToMove = board.getCell(currentMove.fromRow, currentMove.fromCol).getPiece();
            board.getCell(currentMove.toRow, currentMove.toCol).putPieceOn(pieceToMove);
            board.getCell(currentMove.fromRow, currentMove.fromCol).empty();
            if(Math.abs(currentMove.fromRow - currentMove.toRow) == 2){
                board.getCell((currentMove.fromRow + currentMove.toRow)/2, (currentMove.toCol + currentMove.fromCol)/2).empty();
                movePieces(move, board);
            }
        }
        return board;
    }
}
