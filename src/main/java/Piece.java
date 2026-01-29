/**
 * Represents a checker piece on the board.
 * A piece is defined by its color and its status (regular or King).
 * In Italian Checkers, a piece's status determines its movement range
 * and its ability to capture specific opponent pieces.
 */
public class Piece {
    private GameColor color;
    /** The color of the piece, representing the player who owns it. */
    private Color color;
    /** * Indicates whether the piece has been promoted to a King (Dama).
     * Regular pieces become Kings upon reaching the opponent's final row.
     */
    private boolean king;

    /**
     * Constructs a new regular piece of the specified color.
     * By default, newly created pieces are not Kings.
     * @param color The {@link Color} assigned to this piece.
     */
    public Piece(GameColor color) {
        this.color = color;
        this.king = false;
    }

    /**
     * @return The {@link Color} of this piece.
     */
    public GameColor getColor() {
        return color;
    }

    /**
     * Updates the color of the piece.
     * @param color The new color to assign.
     */
    public void setColor(GameColor color) {
        this.color = color;
    }

    /**
     * Checks if the piece has been promoted to a King.
     * @return true if the piece is a King (Dama), false if it is a regular piece.
     */
    public boolean isKing() {
        return king;
    }

    /**
     * Promotes or demotes the piece's King status.
     * This is typically called by the {@link Game} class when a piece reaches
     * the last row of the board.
     * @param king true to promote to King, false to revert.
     */
    public void setKing(boolean king) {
        this.king = king;
    }
}
