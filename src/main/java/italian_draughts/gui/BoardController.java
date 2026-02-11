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

    private void checkGameOver() {
        GameStatus status = game.getStatus();
        if (status != GameStatus.ONGOING) {
            String msg = switch (status) {
                case GameStatus.WHITE_WINS -> "WHITE WINS!";
                case GameStatus.BLACK_WINS -> "BLACK WINS!";
                case GameStatus.DRAW -> "DRAW!";
                default -> "";
            };
            //JOptionPane.showMessageDialog(this, msg, "Game Over", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }

    public List<List<Move>> getGameCurrentLegalMoves() {
        return game.getCurrentLegalMoves();
    }
}
