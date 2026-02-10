package italian_draughts.app;

import italian_draughts.domain.*;
import italian_draughts.logic.Game;
import italian_draughts.logic.LegalMoves;

import java.util.List;


// TODO: update with current logic and rules and make it same level of GUI functionalities or remove entirely
public class CLI {
    private final Game game = new Game();
    private int turnCounter = 1;

    void main() throws InvalidMoveException {
        String playerInput = "";
        GameStatus status = game.getStatus();
        Board board = game.getBoard();
        board.setGame();

        while (status == GameStatus.ONGOING && !playerInput.equals("q")) {
            GameColor player = game.getCurrentPlayer();

            IO.println("\n\n" + player + "'s TURN | Turn: " + turnCounter);
            board.printBoard();

            LegalMoves lm = new LegalMoves(board, player);
            List<List<Move>> legalMovesForCurrentPlayer = lm.getLegalMoves();

            // display legal moves to the player where to choose from
            displayLegalMoves(legalMovesForCurrentPlayer);

            // input move from the list proposed
            playerInput = getValidInput(legalMovesForCurrentPlayer.size());

            // Applying the selected move
            List<Move> selectedMove = legalMovesForCurrentPlayer.get(Integer.parseInt(playerInput) - 1);
            game.movePieces(selectedMove, board);

            turnCounter++;
            game.nextTurn();
        }

        displayEndGameStatus();
    }

    private void displayEndGameStatus() {
        if (game.getStatus() == GameStatus.DRAW)
            IO.println("DRAW!");
        else
            IO.println(game.getStatus() == GameStatus.BLACK_WINS ? "BLACK WINS!" : "WHITE WINS!");
    }

    // Helper to display moves to the player
    private void displayLegalMoves(List<List<Move>> allLegalMoves) {
        if (allLegalMoves == null || allLegalMoves.isEmpty()) {
            IO.println("No moves to display.");
            return;
        }

        for (int i = 0; i < allLegalMoves.size(); i++) {
            List<Move> moves = allLegalMoves.get(i);
            String moveSequence = String.join(" ", moves.stream()
                    .map(Object::toString)
                    .toList());
            System.out.printf("%d. %s%n", i + 1, moveSequence);
        }
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
