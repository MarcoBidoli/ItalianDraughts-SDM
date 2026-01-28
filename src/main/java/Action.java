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
        List<List<Move>> allEatings = new ArrayList<>();
        for(int i=0; i<8; i++) {
            for (int j = 0; j < 8; j++) {
                Cell c = gameBoard.getCell(i, j);
                if (!c.isEmpty() && c.getPiece().getColor().equals(player)) {
                    findEatings(i, j, new ArrayList<>(), allEatings);
                }
            }
        }
        return allEatings;
    }

    private void findEatings(int x, int y, List<Move> eatings, List<List<Move>> allEatings) throws InvalidMoveException {
        boolean hasEat = false;
        int[] dirs = new int[]{1, -1}; //possible directions (L/R)

        for(int i : dirs) { //do it for every direction
            //eating "positions" - opponent and "over opponent"
            int xOpp = x + direction;
            int yOpp = y + i;
            int finX = x + 2*direction;
            int finY = y + 2*i;

            if(canEat(x, y, xOpp, yOpp, finX, finY)){ //check if an eating is doable
                hasEat = true;
                eatings.add(new Move(x, y, finX, finY));
                //simulation of the new status of the board (with the opponent's piece eaten and the player's piece moved)
                Piece eaten = gameBoard.getCell(xOpp, yOpp).getPiece();
                gameBoard.getCell(xOpp, yOpp).empty();
                Piece moved = gameBoard.getCell(x, y).getPiece();
                gameBoard.getCell(x, y).empty();
                gameBoard.getCell(finX, finY).putPieceOn(moved);

                //recursive call
                findEatings(finX, finY, eatings, allEatings);

                //restore the board
                gameBoard.getCell(finX, finY).empty();
                gameBoard.getCell(x, y).putPieceOn(moved);
                eatings.removeLast();
                gameBoard.getCell(xOpp, yOpp).putPieceOn(eaten);
            }
        }

        //add the possible eating to the total possible eats list
        if(!hasEat && !eatings.isEmpty())
            allEatings.add(new ArrayList<>(eatings));
    }

    private boolean canEat(int x, int y, int xOpp, int yOpp, int finX, int finY) {
        //compute whether the eating id doable or not

        if(!gameBoard.isOnBoard(finX, finY))
            return false;

        if(gameBoard.getCell(xOpp, yOpp).isEmpty() || gameBoard.getCell(xOpp, yOpp).getPiece().getColor() == player)
            return false;

        if(!gameBoard.getCell(finX, finY).isEmpty())
            return false;

        if(!gameBoard.getCell(x,y).getPiece().isKing() && gameBoard.getCell(xOpp,yOpp).getPiece().isKing())
            return false;

        return true;
    }

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
