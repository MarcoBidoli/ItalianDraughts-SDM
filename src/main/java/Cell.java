public class Cell {

    private Piece piece;
    Color cellColor;

    public Cell(Color cellColor) {
        piece = null;
        this.cellColor = cellColor;
    }

    public boolean isEmpty() {
        return this.piece == null;
    }

    public Color getColor() {
        return this.cellColor;
    }

    public void setColor(Color color) {
        this.cellColor = color;
    }

    public void putPieceOn(Piece p) throws InvalidMoveException {
        if (this.getColor() != Color.BLACK) {
            throw new InvalidMoveException("Pieces can only be placed on black cells");
        }
        if (this.piece != null) {
            throw new InvalidMoveException("Cell already occupied");
        }
        this.piece = p;
    }

    public void empty() {
        this.piece = null;
    }

    public Piece getPiece() {
        return this.piece;
    }
}
