import italian_draughts.domain.*;
import italian_draughts.logic.GameController;
import italian_draughts.gui.DashboardPanel;
import italian_draughts.logic.Game;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DashboardPanelTest {
    Player w = new HumanPlayer(GameColor.WHITE);
    Player b = new HumanPlayer(GameColor.BLACK);

    @Test
    public void updateStatusCountTest() throws InvalidMoveException {
        Game game = new Game(w, b);
        Board board = game.getBoard();

        board.placePiece(GameColor.WHITE, 7, 1);
        board.placePiece(GameColor.WHITE, 7, 3);
        board.placePiece(GameColor.BLACK, 0, 0);

        DashboardPanel dBP = new DashboardPanel(game, new GameController(game));
        game.addObserver(dBP);
        dBP.modelChanged();

        String statusText = dBP.getStatusText();
        assertTrue(statusText.contains("WHITE"));
        String countTxt = dBP.getCountText();
        assertTrue(countTxt.contains("WHITE 2"));
        assertTrue(countTxt.contains("BLACK 1"));
    }

    @Test
    public void changeTurnTest() throws InvalidMoveException {
        Game game = new Game(w, b);
        Board board = game.getBoard();

        DashboardPanel dBP = new DashboardPanel(game, new GameController(game));
        game.addObserver(dBP);

        String statusText = dBP.getStatusText();
        assertTrue(statusText.contains("WHITE"));

        board.placePiece(GameColor.WHITE, 7, 1);
        board.placePiece(GameColor.WHITE, 7, 3);
        board.placePiece(GameColor.BLACK, 0, 0);
        List<Move> move = new ArrayList<>();
        move.add(new Move(7, 1, 6, 2));

        handleMove(game, move);

        statusText = dBP.getStatusText();
        assertTrue(statusText.contains("BLACK"));
    }

    private static void handleMove(Game game, List<Move> move) {
        game.handleSelection(move.getFirst().fromRow(), move.getFirst().fromCol());
        game.handleSelection(move.getFirst().toRow(), move.getFirst().toCol());
    }
}
