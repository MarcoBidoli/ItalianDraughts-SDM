/**
 * Represents a movement on the checkerboard from a starting cell to a destination cell.
 * This class acts as a data carrier for coordinates and ensures that any Move created
 * contains valid board indices (0-7).
 */
public class Move {
    /** The starting row index of the move. */
    protected int fromRow;
    /** The starting column index of the move. */
    protected int fromCol;
    /** The destination row index of the move. */
    protected int toRow;
    /** The destination column index of the move. */
    protected int toCol;

    /**
     * Constructs a new Move with coordinate validation.
     * * @param fromRow The starting row (0-7).
     * @param fromCol The starting column (0-7).
     * @param toRow   The destination row (0-7).
     * @param toCol   The destination column (0-7).
     * @throws IllegalArgumentException if any coordinate is negative or exceeds the board size (7).
     */
    public Move(int fromRow, int fromCol, int toRow, int toCol) throws IllegalArgumentException {
        if (fromRow < 0 || fromCol < 0 || toRow < 0 || toCol < 0) {
            throw new IllegalArgumentException("Negative coordinates in Move");
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
