package italian_draughts.app;
import italian_draughts.domain.Board;
import italian_draughts.domain.GameColor;
import italian_draughts.domain.HumanPlayer;
import italian_draughts.domain.Player;
import italian_draughts.gui.BoardPanel;
import italian_draughts.gui.DashboardPanel;
import italian_draughts.logic.Game;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    void main() {

        Player w = new HumanPlayer(GameColor.WHITE);
        Player b = new HumanPlayer(GameColor.BLACK);
        Game game = new Game(w, b);
        Board gameBoard = game.getBoard();
        gameBoard.setGame();
        game.calculateLegalMoves();

        JFrame frame = new JFrame("Italian Draughts");

        // --- ICON LOADING LOGIC START ---
        try {
            // Use ImageIO to load the image synchronously to prevent the Mac CImage null crash
            BufferedImage icon = ImageIO.read(new File("italian-draughts-masked.png"));

            // Set the window icon (standard)
            frame.setIconImage(icon);

            // Set the Mac Dock icon (specific for macOS)
            if (Taskbar.isTaskbarSupported()) {
                Taskbar taskbar = Taskbar.getTaskbar();
                taskbar.setIconImage(icon);
            }
        } catch (IOException e) {
            System.err.println("Could not load icon: " + e.getMessage());
            // Fallback or ignore
        } catch (UnsupportedOperationException e) {
            // Taskbar not supported (e.g., on some Linux distros or older Java versions)
        }
        // --- ICON LOADING LOGIC END ---

        frame.setLayout(new BorderLayout());

        DashboardPanel dashboardPanel = new DashboardPanel(game);
        BoardPanel boardPanel = new BoardPanel(game, dashboardPanel);

        boardPanel.setDashboardPanel(dashboardPanel);

        frame.add(dashboardPanel, BorderLayout.NORTH);
        frame.add(boardPanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}