package italian_draughts.app;

import italian_draughts.domain.InvalidMoveException;
import italian_draughts.gui.BoardPanel;
import italian_draughts.gui.DashboardPanel;
import italian_draughts.logic.Game;
import italian_draughts.domain.Board;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    // private final logic.Game game = new logic.Game();
    // private int turnCounter = 1;

    void main() {

        Game game = new Game();
        game.getBoard().setGame();
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


        /*
        List<List<domain.Move>> currentPlayerLegalMoves = new ArrayList<>();
        while(game.getStatus() == domain.GameStatus.ONGOING) {
            printGameStatus();
            game.getBoard().printBoard();
            currentPlayerLegalMoves = new logic.LegalMoves(game.getBoard(), game.getCurrentPlayer()).getLegalMoves();

            // display legal moves to the player where to choose from
            printMoves(currentPlayerLegalMoves);

            // input move from the list proposed
            String input = getValidInput(currentPlayerLegalMoves.size());
            if (input.equals("q"))
                break;

            // Applying the selected move
            List<domain.Move> selectedMove = currentPlayerLegalMoves.get(Integer.parseInt(input)-1);
            game.movePieces(selectedMove, game.getBoard());

            // Show updated board
            game.getBoard().printBoard();

            turnCounter++;

            // Switch player
            game.nextTurn();
        }

        if(game.getStatus() == domain.GameStatus.DRAW)
            IO.println("DRAW!");
        else
            IO.println(game.getStatus() == domain.GameStatus.BLACK_WINS ? "BLACK WINS!" : "WHITE WINS!");
    */
    }
}


    /*
    // Helper to display moves to the player
    private void printMoves(List<List<domain.Move>> allLegalMoves) {
        if (allLegalMoves == null || allLegalMoves.isEmpty()) {
            System.out.println("No moves to display.");
            return;
        }

        for (int i = 0; i < allLegalMoves.size(); i++) {
            // Flatten the inner list of moves into a single string
            String moveSequence = allLegalMoves.get(i).stream()
                    .map(domain.Move::toString)
                    .collect(Collectors.joining(" "));

            // Print the index and the full sequence on one line
            System.out.printf("%d. %s%n", i + 1, moveSequence);
        }
    }

    // Display basic game informations
    private void printGameStatus() {
        String player = game.getCurrentPlayer().toString();
        IO.println(player + "'s TURN");
        IO.println("Turn count: " + turnCounter);
        IO.println("logic.Game status: " + game.getStatus());
    }

    // Input validator, just for this simple input implementation
    public String getValidInput(int moveCount) {
        while (true) {
            String prompt = String.format("Choose move (1-%d) or 'q' to quit: ", moveCount);
            String input = IO.readln(prompt).trim().toLowerCase();

            if (input.equals("q"))
                return "q";

            try {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= moveCount)
                    return String.valueOf(choice);
                else
                    IO.println("Enter a number between 1 and " + moveCount);
            } catch (NumberFormatException e) {
                IO.println("Error: Invalid input. Enter a number or 'q'.");
            }
        }
    }
}
*/