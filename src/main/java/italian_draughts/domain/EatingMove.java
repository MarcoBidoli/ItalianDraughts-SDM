package italian_draughts.domain;

public class EatingMove extends Move{
    private final boolean kingEaten;

    public EatingMove(int fromRow, int fromCol, int toRow, int toCol, PieceType pieceType) throws IllegalArgumentException {
        super(fromRow, fromCol, toRow, toCol);
        this.kingEaten = pieceType.equals(PieceType.KING);
    }

    public boolean kingEaten(){
        return kingEaten;
    }
}
