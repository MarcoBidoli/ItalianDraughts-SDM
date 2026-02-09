import italian_draughts.domain.Board;
import italian_draughts.domain.GameColor;
import italian_draughts.domain.InvalidMoveException;
import italian_draughts.domain.Move;
import italian_draughts.gui.DashboardPanel;
import italian_draughts.logic.Game;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DashboardPanelTest {
    @Test
    public void updateStatusCountTest() throws InvalidMoveException {
        Game game = new Game();
        Board board = game.getBoard();

        board.placePiece(GameColor.WHITE, 7, 1);
        board.placePiece(GameColor.WHITE, 7, 3);
        board.placePiece(GameColor.BLACK, 0, 0);

        DashboardPanel dBP = new DashboardPanel(game);
        dBP.updateInfo();

        assertTrue(dBP.getStatusText().contains("WHITE"));
        String countTxt = dBP.getCountText();
        assertTrue(countTxt.contains("WHITE 2"));
        assertTrue(countTxt.contains("BLACK 1"));
    }

    @Test
    public void changeTurnTest() throws InvalidMoveException {
        Game game = new Game();
        Board board = game.getBoard();

        DashboardPanel dBP = new DashboardPanel(game);
        dBP.updateInfo();

        board.placePiece(GameColor.WHITE, 7, 1);
        board.placePiece(GameColor.WHITE, 7, 3);
        board.placePiece(GameColor.BLACK, 0, 0);
        List<Move> move = new ArrayList<>();
        move.add(new Move(7, 1, 6, 2));
        game.processTurn(move);

        dBP.updateInfo();

        assertTrue(dBP.getStatusText().contains("BLACK"));
    }
}
