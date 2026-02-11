import italian_draughts.domain.*;
import italian_draughts.gui.BoardController;
import italian_draughts.gui.BoardPanel;
import italian_draughts.gui.DashboardPanel;
import italian_draughts.gui.PaletteColors;
import italian_draughts.logic.Game;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BoardPanelTest {
    Player w = new HumanPlayer(GameColor.WHITE);
    Player b = new HumanPlayer(GameColor.BLACK);

    @Test
    public void showCorrectBoard() {
        PaletteColors colors = new PaletteColors();
        Game game = new Game(w, b);
        BoardController controller = new BoardController(game);
        Board board = game.getBoard();
        board.setGame();
        BoardPanel panel = new BoardPanel(controller, game);
        panel.setSize(panel.getPreferredSize());

        BufferedImage img = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = img.createGraphics();

        panel.paintComponent(g2);
        g2.dispose();

        int marginPixel = img.getRGB(10, 10);
        Color expectedMargin = colors.getWOOD_MARGIN();
        assertEquals(expectedMargin.getRGB(), marginPixel);

        //noinspection MagicNumber
        int  boardPixel = img.getRGB(30 + 5, 5);
        Color expectedBoard = colors.getWOOD_DARK();
        assertEquals(expectedBoard.getRGB(), boardPixel);
    }
}
