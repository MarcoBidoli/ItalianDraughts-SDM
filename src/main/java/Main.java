import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    private final Game game = new Game();
    private int turnCounter = 1;

    void main() throws InvalidMoveException {

        List<List<Move>> currentPlayerLegalMoves = new ArrayList<>();


        while(game.getStatus() == GameStatus.ONGOING) {
            printGameStatus();
            game.getBoard().printBoard();
            currentPlayerLegalMoves = new LegalMoves(game.getBoard(), game.getCurrentPlayer()).getLegalMoves();

            // IO.println(currentPlayerLegalMoves.toString()); // TODO: debug only - remove before commit

            // display legal moves to the player where to choose from
            // TODO: If a single move is present, force it
            printMoves(currentPlayerLegalMoves);

            // input move from the list proposed
            String prompt = String.format("Choose move (1-%d): ", currentPlayerLegalMoves.size());
            String input = IO.readln(prompt);
            int choice = Integer.parseInt(input) - 1;
            List<Move> selectedMove = currentPlayerLegalMoves.get(choice);
            game.movePieces(selectedMove, game.getBoard());

            // Show updated board
            game.getBoard().printBoard();

            turnCounter++;

            // Switch player
            game.nextTurn();
        }
    }


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
}
