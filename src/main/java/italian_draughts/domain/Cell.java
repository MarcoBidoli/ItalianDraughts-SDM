package italian_draughts.domain;

/**
 * Represents a cell on the board.
 */
public class Cell {

    private Piece piece;
    private final GameColor cellColor;

    /**
     * Creates a new Cell.
     * @param cellColor The color of the cell.
     */
    public Cell(GameColor cellColor) {
        piece = null;
        this.cellColor = cellColor;
    }

    /**
     * Checks if the cell is empty.
     * @return True if the cell is empty, false otherwise.
     */
    public boolean isEmpty() {
        return this.piece == null;
    }

    /**
     * Gets the color of the cell.
     * @return The color of the cell.
     */
    public GameColor getColor() {
        return this.cellColor;
    }

    /**
     * Puts a piece on the cell.
     * @param p The piece to put on the cell.
     * @throws InvalidMoveException If the cell is not black or already occupied.
     */
    public void putPieceOn(Piece p) throws InvalidMoveException {
        if (this.getColor() != GameColor.BLACK) {
            throw new InvalidMoveException("Pieces can only be placed on black cells");
        }
        if (this.piece != null) {
            throw new InvalidMoveException("domain.Cell already occupied");
        }
        this.piece = p;
    }

    /**
     * Empties the cell.
     */
    public void empty() {
        this.piece = null;
    }

    /**
     * Gets the piece on the cell.
     * @return The piece on the cell.
     */
    public Piece getPiece() {
        return this.piece;
    }

    /**
     * Gets the symbol of the piece on the cell.
     * @return The symbol of the piece on the cell.
     */
    public char getSymbol() {
        return isEmpty() ? '-' : piece.getSymbol();
    }
}
