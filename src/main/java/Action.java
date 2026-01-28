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
        List<List<Move>> eatings = new ArrayList<>();
        for(int i=0; i<8; i++) {
            for (int j = 2; j < 6; j++) { //pieces in columns 2-5 have the "standard" eating
                List<Move> eatingsXY = new ArrayList<>();
                if (gameBoard.getCell(i, j).getPiece().getColor().equals(player)) {
                    if(){
                        break;
                    }else {
                        eatingsXY.add(standardEatingL(i, j));
                        eatingsXY.add(standardEatingR(i, j));
                    }
                }
                eatings.add(eatingsXY);
            }
        }
    }

    private Move standardEatingL(int i, int j) {
        if (!gameBoard.getCell((i+1)*direction, j-1).isEmpty()) {
            if(gameBoard.getCell((i+2)*direction, j-2).isEmpty()) {
                return new Move(i, j, (i+2)*direction, j-2);
            }
        }
        return null;
    }
    private Move standardEatingR(int i, int j) {
        if (!gameBoard.getCell((i+1)*direction, j+1).isEmpty()) {
            if(gameBoard.getCell((i+2)*direction, j+-2).isEmpty()) {
                return new Move(i, j, (i+2)*direction, j+2);
            }
        }
        return null;
    }

    private List<List<Move>> moving() {

    }

    private List<List<Move>> kingEating() {

    }

}
