package italian_draughts.logic;

import italian_draughts.domain.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for the win.
 * It is responsible for checking if a player has won the game.
 */
public class WinController {

    private List<List<Move>> currentLegalMoves = new ArrayList<>();

    /**
     * Checks if a player has won the game.
     * @param board The board of the game.
     * @param currentPlayer The current player.
     * @return The status of the game after checking for a win.
     */
    public GameStatus checkWin(Board board, Player currentPlayer) {
        // Caso 1: il giocatore di turno non ha più pezzi
        if (!hasAnyPiece(board, currentPlayer)) {
            currentLegalMoves = new ArrayList<>();
            return (currentPlayer.color() == GameColor.WHITE)
                    ? GameStatus.BLACK_WINS
                    : GameStatus.WHITE_WINS;
        }

        // Caso 2: il giocatore di turno non ha mosse legali
        calculateLegalMoves(board, currentPlayer);
        if (currentLegalMoves.isEmpty()) {
            return (currentPlayer.color() == GameColor.WHITE)
                    ? GameStatus.BLACK_WINS
                    : GameStatus.WHITE_WINS;
        }

        return GameStatus.ONGOING;
    }

    /**
     * Calculates the legal moves for the current player.
     * @param board The board of the game.
     * @param currentPlayer The current player.
     */
    public void calculateLegalMoves(Board board, Player currentPlayer) {
        LegalMoves lm = new LegalMoves(board, currentPlayer.color());
        currentLegalMoves = lm.getLegalMoves();
    }

    @SuppressWarnings("ChainedMethodCall")
    private boolean hasAnyPiece(Board board, Player player) {
        GameColor playerColor = player.color();
        return Board.ALL_SQUARES.stream()
                .anyMatch(square -> {
                    Piece p = board.getPieceAt(square.row(), square.col());
                    return p != null && p.getColor() == playerColor;
                });
    }

    /**
     * Returns the current legal moves.
     * @return The current legal moves.
     */
    public List<List<Move>> getCurrentLegalMoves() {
        return currentLegalMoves;
    }
}
