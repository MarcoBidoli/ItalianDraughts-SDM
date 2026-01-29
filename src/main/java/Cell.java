/**
 * Represents a single square on the checkers board.
 * A cell can be either Black or White and may contain a {@link Piece}.
 */
public class Cell {

    private Piece piece;
    GameColor cellColor;

    /**
     * Constructs a new Cell with a specific color.
     * Initially, the cell is created without any piece.
     * * @param cellColor The {@link Color} assigned to this cell.
     */
    public Cell(GameColor cellColor) {
        piece = null;
        this.cellColor = cellColor;
    }

    /**
     * Checks if the cell is currently unoccupied.
     * * @return true if there is no piece on the cell, false otherwise.
     */
    public boolean isEmpty() {
        return this.piece == null;
    }

    /**
     * @return The color of this specific cell.
     */
    public GameColor getColor() {
        return this.cellColor;
    }

    /**
     * Updates the color of the cell.
     * * @param color The new color to be assigned.
     */
    public void setColor(GameColor color) {
        this.cellColor = color;
    }

    /**
     * Places a piece on the cell if valid.
     * This method enforces the rule that pieces can only reside on Black cells
     * and cannot overlap existing pieces.
     * * @param p The {@link Piece} to place on this cell.
     * @throws InvalidMoveException if the cell is White or already occupied.
     */
    public void putPieceOn(Piece p) throws InvalidMoveException {
        if (this.getColor() != GameColor.BLACK) {
            throw new InvalidMoveException("Pieces can only be placed on black cells");
        }
        if (this.piece != null) {
            throw new InvalidMoveException("Cell already occupied");
        }
        this.piece = p;
    }

    /**
     * Removes the piece from the cell.
     */
    public void empty() {
        this.piece = null;
    }

    /**
     * Retrieves the piece currently occupying the cell.
     * * @return The {@link Piece} object, or null if the cell is empty.
     */
    public Piece getPiece() {
        return this.piece;
    }
}
