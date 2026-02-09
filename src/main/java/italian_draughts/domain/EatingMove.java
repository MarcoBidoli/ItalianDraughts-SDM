package italian_draughts.domain;

public class EatingMove extends Move{
    private final boolean kingEaten;

    public EatingMove(int fromRow, int fromCol, int toRow, int toCol, PieceType pieceType) throws IllegalArgumentException {
        super(fromRow, fromCol, toRow, toCol);
        if(pieceType.equals(PieceType.KING))
            this.kingEaten = true;
        else
            this.kingEaten = false;
    }

    public boolean kingEaten(){
        return kingEaten;
    }
}
