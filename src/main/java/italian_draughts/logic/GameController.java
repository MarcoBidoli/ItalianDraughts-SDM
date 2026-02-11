package italian_draughts.logic;

import italian_draughts.domain.GameColor;
import italian_draughts.domain.GameStatus;
import italian_draughts.domain.Move;

import java.util.List;

public class GameController {
    private final Game game;

    public GameController(Game game) {
        this.game = game;
    }

    public void actionPerformed(int row, int col) {
        if (game.getStatus() != GameStatus.ONGOING) {
            return;
        }
        game.handleSelection(row, col);
    }

    public void resign(GameColor loser) {
        game.resignHandling(loser);
    }

    public void draw() {
        game.agreedDrawHandling();
    }

    public List<List<Move>> getGameCurrentLegalMoves() {
        return game.getCurrentLegalMoves();
    }
}
