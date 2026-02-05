import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

public class BoardPanelTest {

    @Test
    public void showCorrectBoard() {
        Game game = new Game();
        game.getBoard().setGame();
        BoardPanel panel = new BoardPanel(game, new DashboardPanel(game));
        panel.setSize(panel.getPreferredSize());

        BufferedImage img = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = img.createGraphics();

        panel.paintComponent(g2);
        g2.dispose();

        int marginPixel = img.getRGB(10, 10);
        Color expectedMargin = new Color(115, 74, 33);
        assertEquals(expectedMargin.getRGB(), marginPixel);

        int  boardPixel = img.getRGB(30 + 5, 5);
        Color expectedBoard = new Color (153, 102, 51);
        assertEquals(expectedBoard.getRGB(), boardPixel);
    }

    @Test
    void selectionLogicTest() throws InvalidMoveException {
        Game game = new Game();
        Board board = game.getBoard();
        board.placePiece(GameColor.WHITE, 5, 1);
        game.calculateLegalMoves();

        BoardPanel panel = new BoardPanel(game, new DashboardPanel(game));

        panel.handleLogic(5, 1);

        assertNotNull(panel.getSelectedCoords());
        assertEquals(5, panel.getSelectedCoords().i());
        assertEquals(1, panel.getSelectedCoords().j());

        assertFalse(panel.getFilteredMoves().isEmpty());
    }
}
