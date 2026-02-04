import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    //private final Game game = new Game();
    private int turnCounter = 1;

    void main() throws InvalidMoveException {

        Game game = new Game();
        game.getBoard().setGame();
        game.calculateLegalMoves();

        JFrame frame = new JFrame("Italian Draughts");
        frame.setLayout(new BorderLayout());

        BoardPanel boardPanel = new BoardPanel(game.getBoard(), game);
        DashboardPanel dashboardPanel = new DashboardPanel(game);

        boardPanel.setDashboardPanel(dashboardPanel);

        frame.add(dashboardPanel, BorderLayout.NORTH);
        frame.add(boardPanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


        /*
        List<List<Move>> currentPlayerLegalMoves = new ArrayList<>();
        while(game.getStatus() == GameStatus.ONGOING) {
            printGameStatus();
            game.getBoard().printBoard();
            currentPlayerLegalMoves = new LegalMoves(game.getBoard(), game.getCurrentPlayer()).getLegalMoves();

            // display legal moves to the player where to choose from
            printMoves(currentPlayerLegalMoves);

            // input move from the list proposed
            String input = getValidInput(currentPlayerLegalMoves.size());
            if (input.equals("q"))
                break;

            // Applying the selected move
            List<Move> selectedMove = currentPlayerLegalMoves.get(Integer.parseInt(input)-1);
            game.movePieces(selectedMove, game.getBoard());

            // Show updated board
            game.getBoard().printBoard();

            turnCounter++;

            // Switch player
            game.nextTurn();
        }

        if(game.getStatus() == GameStatus.DRAW)
            IO.println("DRAW!");
        else
            IO.println(game.getStatus() == GameStatus.BLACK_WINS ? "BLACK WINS!" : "WHITE WINS!");
    */
    }
}


    /*
    // Helper to display moves to the player
    private void printMoves(List<List<Move>> allLegalMoves) {
        if (allLegalMoves == null || allLegalMoves.isEmpty()) {
            System.out.println("No moves to display.");
            return;
        }

        for (int i = 0; i < allLegalMoves.size(); i++) {
            // Flatten the inner list of moves into a single string
            String moveSequence = allLegalMoves.get(i).stream()
                    .map(Move::toString)
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
        IO.println("Game status: " + game.getStatus());
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