import italian_draughts.domain.*;
import italian_draughts.logic.Game;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameStatusTest {
    Player w = new HumanPlayer(GameColor.WHITE);
    Player b = new HumanPlayer(GameColor.BLACK);

    @Test
    void newGameStartsOngoing() {
        Game game = new Game(w, b);
        assertEquals(GameStatus.ONGOING, game.getStatus());
    }

    @Test
    void whiteWinsWhenNoBlackPiecesRemain(){
        //TODO: implementing this test
    }

    // TODO: remove the move after Ale&Fede refactoring
    // Just prepare a board with no moves and check status
    @Test
    public void testWinConditionWhenNoMovesAvailable() throws InvalidMoveException {
        Game game = new Game(w, b);
        Board board = game.getBoard();
        // board where Black is one move away from being trapped
        // and White is about to make that move.
        board.placePiece(GameColor.WHITE, PieceType.KING, 1, 7);
        board.placePiece(GameColor.WHITE, PieceType.KING, 2, 6);
        board.placePiece(GameColor.BLACK, 0, 6);
        game.setCurrentTurn(new HumanPlayer(GameColor.WHITE));
        List<Move> whiteWinningMove = new ArrayList<>(List.of(new Move(2, 6,1, 5)));

        // Process the turn
        game.handleSelection(whiteWinningMove.getFirst().fromRow(), whiteWinningMove.getFirst().fromCol());
        game.handleSelection(whiteWinningMove.getFirst().toRow(), whiteWinningMove.getFirst().toCol());
        board.printBoard();

        // The turn should have switched to Black internally,
        // and checkWin() should have seen Black has no moves.
        assertEquals(GameStatus.WHITE_WINS, game.getStatus(),"White should win if Black has no legal moves left.");
    }

}
