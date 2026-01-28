import java.util.ArrayList;
import java.util.List;

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
                List<Move> eatingsXY = new ArrayList<>();
                if (gameBoard.getCell(i, j).getPiece().getColor().equals(player)) {
                    findEatings(i, j, new ArrayList<>(), allEatings);
                }
            }
        }
        return allEatings;
    }

    private void findEatings(int x, int y, List<Move> eatings, List<List<Move>> allEatings) {
        boolean canEat = false;

        int[] dirs = new int[]{1, -1};
        for(int i : dirs) {
            int xOpp = x + direction;
            int yOpp = y + i;
            int finX = x + 2*direction;
            int finY = y + 2*i;
            if(canEat(x, y, xOpp, yOpp, finX, finY)){
                canEat = true;
                eatings.add(new Move(x, y, finX, finY));
                Piece eaten = gameBoard.getCell(xOpp, yOpp).getPiece();
                gameBoard.getCell(xOpp, yOpp).empty();
                Piece moved = gameBoard.getCell(x, y).getPiece();
                gameBoard.getCell(x, y).empty();
                gameBoard.getCell(finX, finY).putPieceOn(moved);
                findEatings(finX, finY, eatings, allEatings);

                gameBoard.getCell(finX, finY).empty();
                gameBoard.getCell(x, y).putPieceOn(moved);
                eatings.remove(eatings.size() - 1);
                gameBoard.getCell(xOpp, yOpp).putPieceOn(eaten);
            }
        }

        if(!canEat && !eatings.isEmpty())
            allEatings.add(new ArrayList<>(eatings));
    }

    private boolean canEat(int x, int y, int xOpp, int yOpp, int finX, int finY) {
        //TODO: substitute with isOnBoard Board method
        if(finX < 0 || finX >= 8 || finY < 0 || finY >= 8)
            return false;

        if(gameBoard.getCell(xOpp, yOpp).isEmpty() && gameBoard.getCell(xOpp, yOpp).getPiece().getColor() == player)
            return false;

        if(!gameBoard.getCell(finX, finY).isEmpty())
            return false;

        if(!gameBoard.getCell(x,y).getPiece().isKing() && gameBoard.getCell(xOpp,yOpp).getPiece().isKing())
            return false;

        return true;
    }

    private List<List<Move>> moving() {

    }

    private List<List<Move>> kingEating() {

    }

}
