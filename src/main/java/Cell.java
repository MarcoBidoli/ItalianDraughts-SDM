public class Cell {

    private Piece piece;
    Color cellColor;

    public Cell(Color cellColor) {
        piece = null;
        this.cellColor = cellColor;
    }

    public boolean isEmpty() {
        return true;
    }

    public Color getColor() {
        return this.cellColor;
    }

    public void setColor(Color color) {
        this.cellColor = color;
    }

    public boolean putPieceOn(Piece p) {
        if(this.getColor() == Color.BLACK) {
            if(this.piece == null) {
                this.piece = p;
                return true;
            }
        }
        return false;
    }
}
