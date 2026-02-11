package italian_draughts.domain;

/**
 * Represents an eating move.
 */
public class EatingMove extends Move{
    private final boolean kingEaten;

    /**
     * Creates a new EatingMove.
     * @param fromRow The starting row of the move.
     * @param fromCol The starting column of the move.
     * @param toRow The ending row of the move.
     * @param toCol The ending column of the move.
     * @param pieceType The type of the eaten piece.
     * @throws IllegalArgumentException If the move is invalid.
     */
    public EatingMove(int fromRow, int fromCol, int toRow, int toCol, PieceType pieceType) throws IllegalArgumentException {
        super(fromRow, fromCol, toRow, toCol);
        this.kingEaten = pieceType.equals(PieceType.KING);
    }

    /**
     * Checks if a king was eaten.
     * @return True if a king was eaten, false otherwise.
     */
    public boolean kingEaten(){
        return kingEaten;
    }
}
