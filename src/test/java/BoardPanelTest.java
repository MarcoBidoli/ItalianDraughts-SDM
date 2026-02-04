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
        BoardPanel panel = new BoardPanel(game.getBoard(), game);
        panel.setSize(655, 680);

        BufferedImage img = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = img.createGraphics();

        panel.paintComponent(g2);
        g2.dispose();

        int pixelColor = img.getRGB(10, 10);
        Color expected = new Color(153, 102, 51);
        assertEquals(expected.getRGB(), pixelColor);
    }

    @Test
    void selectionLogicTest() throws InvalidMoveException {
        Game game = new Game();
        Board board = game.getBoard();
        board.placePiece(GameColor.WHITE, 5, 1);
        game.calculateLegalMoves();

        BoardPanel panel = new BoardPanel(board, game);

        panel.handleLogic(5, 1);

        assertNotNull(panel.getSelectedCoords());
        assertEquals(5, panel.getSelectedCoords().i());
        assertEquals(1, panel.getSelectedCoords().j());

        assertFalse(panel.getFilteredMoves().isEmpty());
    }
}
