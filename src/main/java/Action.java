import java.util.ArrayList;
import java.util.List;

public class Action {
    private Board gameBoard;
    private Color player;

    public Action(Board gameBoard, Color player) {
        this.gameBoard = gameBoard;
        this.player = player;
    }

    public List<Move> possibleMoves() {
        List<Move> moves = new ArrayList<>();

        return moves;
    }

}
