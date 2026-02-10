package italian_draughts.gui;

import italian_draughts.domain.GameStatus;
import italian_draughts.domain.InvalidMoveException;
import italian_draughts.domain.Move;
import italian_draughts.domain.Square;
import italian_draughts.logic.Game;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class BoardController {
    private Game game;
    private List<List<Move>> filteredMoves = new ArrayList<>();
    private Square selectedSquares = null;

    public BoardController(Game game) {
        this.game = game;
    }

    public void actionPerformed(int row, int col) {
        for (List<Move> move : filteredMoves) {
            Move lastMove = move.getLast();
            if (lastMove.toRow == row && lastMove.toCol == col) {
                try {
                    game.processTurn(new ArrayList<>(move));
                    selectedSquares = null;
                    filteredMoves = new ArrayList<>();
                    checkGameOver();
                    return;
                } catch (InvalidMoveException e) {

                    return;
                }
            }
        }

        // Handle domain.Piece Selection
        List<List<Move>> pieceMoves = game.getMovesFor(row, col);

        if (!pieceMoves.isEmpty()) {
            selectedSquares = new Square(row, col);
            filteredMoves = pieceMoves;
        } else {
            selectedSquares = null;
            filteredMoves = new ArrayList<>();
        }
    }

    private void checkGameOver() {
        GameStatus status = game.getStatus();
        if (status != GameStatus.ONGOING) {
            String msg = switch (status) {
                case GameStatus.WHITE_WINS -> "WHITE WINS!";
                case GameStatus.BLACK_WINS -> "BLACK WINS!";
                case GameStatus.DRAW -> "DRAW!";
                default -> "";
            };
            //JOptionPane.showMessageDialog(this, msg, "Game Over", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }

    public List<List<Move>> getFilteredMoves() {
        return this.filteredMoves;
    }

    public Square getSelectedSquares() { return selectedSquares; }

}
