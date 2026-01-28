import java.util.ArrayList;
import java.util.List;

public class Action {
    private Board gameBoard;
    private Color player;

    public Action(Board gameBoard, Color player) {
        this.gameBoard = gameBoard;
        this.player = player;
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

    private List<List<Move>> moving() {

    }

    private List<List<Move>> kingEating() {

    }

}
