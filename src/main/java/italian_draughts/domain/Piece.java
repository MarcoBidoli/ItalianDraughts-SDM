package italian_draughts.domain;

public class Piece {
    private GameColor color;
    private boolean king;

    public Piece(GameColor color){
        this.color = color;
        this.king = false;
    }
    public GameColor getColor() {
        return color;
    }
    public void setColor(GameColor color) {
        this.color = color;
    }
    public boolean isKing() {
        return king;
    }
    public void setKing(boolean king) {
        this.king = king;
    }
}
