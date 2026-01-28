import java.util.ArrayList;
import java.util.List;

public record Coords(int i, int j) {}

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

    }

    // TODO @MarcoBidoli
    private List<List<Move>> moving() {
        List<List<Move>> listOfAllMoves = new ArrayList<>();

        List<Coords> myPiecesCoords = new ArrayList<>();
        for(int i=0; i < 8; i++) {
            for(int j=0; j < 8; j++) {
                if(gameBoard.getCell(i,j).getPiece().getColor().equals(player)) {
                    myPiecesCoords.add(new Coords(i,j));
                }
            }
        }

        // If player == BLACK front moves are increasing i
        for(Coords coord : myPiecesCoords) {
            List<Move> movesForThisPiece = new ArrayList<>();
            Cell destinationCell;
            Coords destCellCoord;
            Move myMove;

            // for all pieces, checking the frontwards moves

            // i+direction j+1
            destCellCoord = new Coords(coord.i()+direction, coord.j()+1);
            destinationCell = gameBoard.getCell(destCellCoord.i(), destCellCoord.j());
            if(destinationCell.isEmpty()) {
                 myMove = new Move(coord.i(), coord.j(), destCellCoord.i(), destCellCoord.j());
                 movesForThisPiece.add(myMove);
            }
            // i+direction j-1
            destCellCoord = new Coords(coord.i()+direction, coord.j()-1);
            destinationCell = gameBoard.getCell(destCellCoord.i(), destCellCoord.j());
            if(destinationCell.isEmpty()) {
                myMove = new Move(coord.i(), coord.j(), destCellCoord.i(), destCellCoord.j());
                movesForThisPiece.add(myMove);
            }

            // if king, checking the backwards moves
            if(gameBoard.getCell(coord.i(), coord.j()).getPiece().isKing()) {
                // i-direction j+1
                destCellCoord = new Coords(coord.i()-direction, coord.j()+1);
                destinationCell = gameBoard.getCell(destCellCoord.i(), destCellCoord.j());
                if(destinationCell.isEmpty()) {
                    myMove = new Move(coord.i(), coord.j(), destCellCoord.i(), destCellCoord.j());
                    movesForThisPiece.add(myMove);
                }

                // i-direction j-1
                destCellCoord = new Coords(coord.i()-direction, coord.j()-1);
                destinationCell = gameBoard.getCell(destCellCoord.i(), destCellCoord.j());
                if(destinationCell.isEmpty()) {
                    myMove = new Move(coord.i(), coord.j(), destCellCoord.i(), destCellCoord.j());
                    movesForThisPiece.add(myMove);
                }
            }

            listOfAllMoves.add(movesForThisPiece);
        }
        return listOfAllMoves;
    }

    private List<List<Move>> kingEating() {

    }

}
