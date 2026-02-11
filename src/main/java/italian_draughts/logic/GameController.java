package italian_draughts.logic;

import italian_draughts.domain.GameColor;
import italian_draughts.domain.GameStatus;
import italian_draughts.domain.Move;

import java.util.List;

/**
 * Controller of the game.
 * It is responsible for handling the user's actions.
 */
public class GameController {
    private final Game game;

    /**
     * Constructor of the GameController.
     * @param game The game to be controlled.
     */
    public GameController(Game game) {
        this.game = game;
    }

    /**
     * Handles the action performed by the user.
     * @param row The row of the selected cell.
     * @param col The column of the selected cell.
     */
    public void actionPerformed(int row, int col) {
        if (game.getStatus() != GameStatus.ONGOING) {
            return;
        }
        game.handleSelection(row, col);
    }

    /**
     * Handles the resignation of a player.
     * @param loser The player who resigned.
     */
    public void resign(GameColor loser) {
        game.resignHandling(loser);
    }

    /**
     * Handles the draw agreement.
     */
    public void draw() {
        game.agreedDrawHandling();
    }

    /**
     * Returns the current legal moves of the game.
     * @return The current legal moves of the game.
     */
    public List<List<Move>> getGameCurrentLegalMoves() {
        return game.getCurrentLegalMoves();
    }
}
