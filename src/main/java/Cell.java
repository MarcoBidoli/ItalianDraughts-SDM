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

    public void setColor(Color cellColor) {
        this.cellColor = cellColor;
    }
}
