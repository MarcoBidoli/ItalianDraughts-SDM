public class Piece {
    private Color color;
    private boolean king;

    public Piece(Color color){
        this.color = color;
        this.king = false;
    }
    public Color getColor() {
        return color;
    }
    public void setColor(Color color) {
        this.color = color;
    }
    public boolean isKing() {
        return king;
    }
    public void setKing(boolean king) {
        this.king = king;
    }
}
