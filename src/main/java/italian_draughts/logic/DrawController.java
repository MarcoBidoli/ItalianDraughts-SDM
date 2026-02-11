package italian_draughts.logic;

import italian_draughts.domain.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for the draw.
 * It is responsible for checking if the game is a draw.
 */
public class DrawController {

    public static final int MAX_QUIET_MOVES = 40;

    private int quietMovesNoCapture = 0;
    private final Map<List<SquareEncoder>, Integer> visits = new HashMap<>();

    /**
     * Checks if the game is a draw.
     * @param captureOccurred True if a capture occurred in the last move, false otherwise.
     * @param board The board of the game.
     * @param whitePlayer The white player.
     * @param blackPlayer The black player.
     * @return True if the game is a draw, false otherwise.
     */
    public boolean checkDraw(boolean captureOccurred, Board board, Player whitePlayer, Player blackPlayer) {
        // 1) repetition rule
        if (checkRepetition()) {
            return true;
        }

        // 2) move-count rule
        return updateMoveCountRule(captureOccurred, board, whitePlayer, blackPlayer);
    }

    @SuppressWarnings("ChainedMethodCall")
    private boolean hasKing(Board board, Player player) {
        GameColor playerColor = player.color();
        return Board.ALL_SQUARES.stream()
                .anyMatch(square -> {
                    Piece p = board.getPieceAt(square.row(), square.col());
                    return p != null && p.getColor() == playerColor && p.isKing();
                });
    }

    private boolean updateMoveCountRule(boolean captureOccurred, Board board, Player whitePlayer, Player blackPlayer) {
        if (captureOccurred) {
            quietMovesNoCapture = 0;
            return false;
        }

        boolean bothHaveKings = hasKing(board, whitePlayer) && hasKing(board, blackPlayer);
        if (!bothHaveKings) {
            quietMovesNoCapture = 0;
            return false;
        }

        quietMovesNoCapture++;

        return quietMovesNoCapture >= MAX_QUIET_MOVES;
    }

    /**
     * Encodes the board.
     * @param board The board of the game.
     */
    public void boardEncoder(Board board) {
        List<SquareEncoder> encoding = new ArrayList<>();
        int index = 0;

        for (Square square : Board.PLAYABLE_SQUARES) {
            index++;
            Cell cell = board.getCell(square.row(), square.col());

            if (!cell.isEmpty()) {
                encoding.add(new SquareEncoder(cell.getSymbol(), index));
            }
        }
        visits.merge(encoding, 1, Integer::sum);
    }

    /**
     * Checks if a repetition occurred.
     * @return True if a repetition occurred, false otherwise.
     */
    public boolean checkRepetition() {
        for (int count : visits.values()) {
            if (count >= 3) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the visits.
     * @return The visits.
     */
    public Map<List<SquareEncoder>, Integer> getVisits() {
        return visits;
    }

    /**
     * Clears the visits.
     */
    public void clearVisits() {
        visits.clear();
    }
}
