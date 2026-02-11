package italian_draughts.domain;

public class Move {
    private final int fromRow;
    private final int fromCol;
    private final int toRow;
    private final int toCol;

    public Move(int fromRow, int fromCol, int toRow, int toCol) throws IllegalArgumentException {
        if (Board.positionIsOffBoard(fromRow, fromCol))
            throw new IllegalArgumentException("Start cell is not in board");
        else if (Board.positionIsOffBoard(toRow, toCol))
            throw new IllegalArgumentException("Arrive cell is not on board");
        else {
            this.fromRow = fromRow;
            this.fromCol = fromCol;
            this.toRow = toRow;
            this.toCol = toCol;
        }
    }

    // Temporarily needed for display moves
    @Override
    public String toString() {
        return String.format("(%d,%d) -> (%d,%d)", fromRow, fromCol, toRow, toCol);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Move move = (Move) obj;
        boolean matchingRows = fromRow == move.fromRow && toRow == move.toRow;
        boolean matchingCols = fromCol == move.fromCol && toCol == move.toCol;
        return matchingRows && matchingCols;
    }

    public int toRow() {
        return toRow;
    }

    public int toCol() {
        return toCol;
    }

    public int fromRow() {
        return fromRow;
    }

    public int fromCol() {
        return fromCol;
    }
}
