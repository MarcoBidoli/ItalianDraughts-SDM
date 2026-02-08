package italian_draughts.domain;

public class Move {
    public int fromRow;
    public int fromCol;
    public int toRow;
    public int toCol;

    public Move(int fromRow, int fromCol, int toRow, int toCol) throws IllegalArgumentException {
        if (Board.isNotOnBoard(fromRow, fromCol))
            throw new IllegalArgumentException("Start cell is not in board");
        else if (Board.isNotOnBoard(toRow, toCol))
            throw new IllegalArgumentException("Arrive cell is not on board");
        else {
            this.fromRow = fromRow;
            this.fromCol = fromCol;
            this.toRow = toRow;
            this.toCol = toCol;
        }
    }

    // Temporarily needed for display moves
    @Override
    public String toString() {
        return String.format("(%d,%d) -> (%d,%d)", fromRow, fromCol, toRow, toCol);
    }
}
