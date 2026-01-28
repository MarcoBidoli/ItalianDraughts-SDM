import java.util.ArrayList;
import java.util.List;

record Coords(int i, int j) {}

public class Action {
    private Board gameBoard;
    private Color player;
    private int direction;

    public Action(Board gameBoard, Color player) {
        this.gameBoard = gameBoard;
        this.player = player;
        this.direction = this.player.equals(Color.BLACK) ? 1 : -1;
    }

    public List<List<Move>> possibleMoves() {
        List<List<Move>> moves = new ArrayList<>();
        moves.addAll(kingEating());
        if(!moves.isEmpty())
            moves.addAll(eating());
        if(!moves.isEmpty())
            moves.addAll(moving());
        return moves;
    }

    private List<List<Move>> eating () {
        return new ArrayList<>();
    }

    // TODO @MarcoBidoli
    protected List<List<Move>> moving() {
        List<List<Move>> listOfAllMoves = new ArrayList<>();

        // Putting all player pieces coordinates in a list
        List<Coords> myPiecesCoords = new ArrayList<>();
        for(int i=0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                if (gameBoard.getCell(i, j).getPiece() != null)
                    if (gameBoard.getCell(i, j).getPiece().getColor().equals(player))
                        myPiecesCoords.add(new Coords(i, j));

        // For each save coordinate, check what legal moves are associated with that piece
        // If player == BLACK front moves are increasing i
        for(Coords coord : myPiecesCoords) {
            List<Move> movesForThisPiece = new ArrayList<>();
            Coords destCellCoord;

            // for all pieces, checking the frontwards moves
            // i+direction j+1
            destCellCoord = new Coords(coord.i()+direction, coord.j()+1);
            addCoordToLegalMoves(coord, destCellCoord, movesForThisPiece);
            // i+direction j-1
            destCellCoord = new Coords(coord.i()+direction, coord.j()-1);
            addCoordToLegalMoves(coord, destCellCoord, movesForThisPiece);

            // if king, checking the backwards moves
            if(gameBoard.getCell(coord.i(), coord.j()).getPiece().isKing()) {
                // i-direction j+1
                destCellCoord = new Coords(coord.i()-direction, coord.j()+1);
                addCoordToLegalMoves(coord, destCellCoord, movesForThisPiece);
                // i-direction j-1
                destCellCoord = new Coords(coord.i()-direction, coord.j()-1);
                addCoordToLegalMoves(coord, destCellCoord, movesForThisPiece);
            }
            listOfAllMoves.add(movesForThisPiece);
        }
        return listOfAllMoves;
    }

    private void addCoordToLegalMoves(Coords coord, Coords destCellCoord, List<Move> movesForThisPiece) {
        // Avoid out of board moves
        if ( !gameBoard.isOnBoard(destCellCoord.i(), destCellCoord.j()) ) return;

        // The standard way
        // check destination is empty
        if(gameBoard.getCell(destCellCoord.i(), destCellCoord.j()).isEmpty())
            movesForThisPiece.add(new Move(coord.i(), coord.j(), destCellCoord.i(), destCellCoord.j()));
    }

    private List<List<Move>> kingEating() {
        return new ArrayList<>();
    }

}
