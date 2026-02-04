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
        dBP.updateInfo(game);

        assertTrue(dBP.getStatusText().contains("WHITE"));
        String countTxt = dBP.getCountText();
        assertTrue(countTxt.contains("WHITES: 2"));
        assertTrue(countTxt.contains("BLACKS: 1"));
    }

    @Test
    public void changeTurnTest() throws InvalidMoveException {
        Game game = new Game();
        Board board = game.getBoard();

        DashboardPanel dBP = new DashboardPanel(game);
        dBP.updateInfo(game);

        board.placePiece(GameColor.WHITE, 7, 1);
        board.placePiece(GameColor.WHITE, 7, 3);
        board.placePiece(GameColor.BLACK, 0, 0);
        List<Move> move = new ArrayList<>();
        move.add(new Move(7, 1, 6, 2));
        game.applyTurn(move);

        dBP.updateInfo(game);

        assertTrue(dBP.getStatusText().contains("BLACK"));
    }
}
