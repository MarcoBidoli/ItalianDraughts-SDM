package italian_draughts.domain;

/**
 * Represents a piece on the board.
 */
public class Piece {
    private final GameColor color;
    private boolean king;

    /**
     * Creates a new Piece.
     * @param color The color of the piece.
     */
    public Piece(GameColor color){
        this.color = color;
        this.king = false;
    }

    /**
     * Gets the color of the piece.
     * @return The color of the piece.
     */
    public GameColor getColor() {
        return color;
    }

    /**
     * Checks if the piece is a king.
     * @return True if the piece is a king, false otherwise.
     */
    public boolean isKing() {
        return king;
    }

    /**
     * Sets the piece as a king or not.
     * @param king True to set the piece as a king, false otherwise.
     */
    public void setKing(boolean king) {
        this.king = king;
    }

    // Needed for print
    /**
     * Gets the symbol of the piece.
     * @return The symbol of the piece.
     */
    public char getSymbol() {
        if (color == GameColor.WHITE) return king ? 'W' : 'w';
        return king ? 'B' : 'b';
    }
}
