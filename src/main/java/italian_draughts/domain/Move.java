package italian_draughts.domain;

public class Move {
    public int fromRow;
    public int fromCol;
    public int toRow;
    public int toCol;

    public Move(int fromRow, int fromCol, int toRow, int toCol) throws IllegalArgumentException {
        if (fromRow < 0 || fromCol < 0 || toRow < 0 || toCol < 0) {
            throw new IllegalArgumentException("Negative coordinates in domain.Move");
        } else if (fromRow > 7 || fromCol > 7 || toRow > 7 || toCol > 7) {
               throw new IllegalArgumentException("Coordinates out of board");
        } else {
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
