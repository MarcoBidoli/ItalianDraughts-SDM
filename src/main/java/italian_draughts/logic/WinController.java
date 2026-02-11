package italian_draughts.logic;

import italian_draughts.domain.*;

import java.util.ArrayList;
import java.util.List;

public class WinController {

    private List<List<Move>> currentLegalMoves = new ArrayList<>();

    public GameStatus checkWin(Board board, Player currentPlayer) {
        // Caso 1: il giocatore di turno non ha più pezzi
        if (!hasAnyPiece(board, currentPlayer)) {
            currentLegalMoves = new ArrayList<>();
            return (currentPlayer.getColor() == GameColor.WHITE)
                    ? GameStatus.BLACK_WINS
                    : GameStatus.WHITE_WINS;
        }

        // Caso 2: il giocatore di turno non ha mosse legali
        calculateLegalMoves(board, currentPlayer);
        if (currentLegalMoves.isEmpty()) {
            return (currentPlayer.getColor() == GameColor.WHITE)
                    ? GameStatus.BLACK_WINS
                    : GameStatus.WHITE_WINS;
        }

        return GameStatus.ONGOING;
    }

    public void calculateLegalMoves(Board board, Player currentPlayer) {
        LegalMoves lm = new LegalMoves(board, currentPlayer.getColor());
        currentLegalMoves = lm.getLegalMoves();
    }

    private boolean hasAnyPiece(Board board, Player player) {
        GameColor playerColor = player.getColor();
        return Board.ALL_SQUARES.stream()
                .anyMatch(square -> {
                    Piece p = board.getPieceAt(square.row(), square.col());
                    return p != null && p.getColor() == playerColor;
                });
    }

    public List<List<Move>> getCurrentLegalMoves() {
        return currentLegalMoves;
    }
}
