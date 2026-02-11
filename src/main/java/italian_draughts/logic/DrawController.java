package italian_draughts.logic;

import italian_draughts.domain.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DrawController {

    public static final int MAX_QUIET_MOVES = 40;

    private int quietMovesNoCapture = 0;
    private final Map<List<SquareEncoder>, Integer> visits = new HashMap<>();

    // Ritorna true se scatta patta
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

    public boolean checkRepetition() {
        for (int count : visits.values()) {
            if (count >= 3) {
                return true;
            }
        }
        return false;
    }

    public Map<List<SquareEncoder>, Integer> getVisits() {
        return visits;
    }

    public void clearVisits() {
        visits.clear();
    }
}
