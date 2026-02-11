package italian_draughts.gui;

import italian_draughts.domain.*;
import italian_draughts.logic.Game;

import java.util.List;

public class BoardController {
    private final Game game;

    public BoardController(Game game) {
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

    private void checkGameOver() {
        GameStatus status = game.getStatus();
        if (status != GameStatus.ONGOING) {

            System.exit(0);
        }
    }

    public List<List<Move>> getGameCurrentLegalMoves() {
        return game.getCurrentLegalMoves();
    }
}
