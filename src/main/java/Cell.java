public class Cell {

    private Piece piece;
    GameColor cellColor;

    public Cell(GameColor cellColor) {
        piece = null;
        this.cellColor = cellColor;
    }

    public boolean isEmpty() {
        return this.piece == null;
    }

    public GameColor getColor() {
        return this.cellColor;
    }

    public void setColor(GameColor color) {
        this.cellColor = color;
    }

    public void putPieceOn(Piece p) throws InvalidMoveException {
        if (this.getColor() != GameColor.BLACK) {
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
