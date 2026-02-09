package italian_draughts.domain;

public class Piece {
    private final GameColor color;
    private boolean king;

    public Piece(GameColor color){
        this.color = color;
        this.king = false;
    }
    public GameColor getColor() {
        return color;
    }
    public boolean isKing() {
        return king;
    }
    public void setKing(boolean king) {
        this.king = king;
    }

    // Needed for print
    public char getSymbol() {
        if (color == GameColor.WHITE) return king ? 'W' : 'w';
        return king ? 'B' : 'b';
    }
}
