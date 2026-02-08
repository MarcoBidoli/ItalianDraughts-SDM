import italian_draughts.domain.Board;
import italian_draughts.domain.GameColor;
import italian_draughts.domain.GameStatus;
import italian_draughts.domain.Move;
import italian_draughts.domain.InvalidMoveException;
import italian_draughts.logic.Game;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameStatusTest {

    @Test
    void newGameStartsOngoing() {
        Game game = new Game();
        assertEquals(GameStatus.ONGOING, game.getStatus());
    }

    @Test
    void whiteWinsWhenNoBlackPiecesRemain() throws InvalidMoveException {
        Game game = new Game();
        Board board = game.getBoard();

        // Svuota la scacchiera del game (Board non espone emptyBoard pubblicamente)
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (!board.getCell(r, c).isEmpty()) {
                    board.getCell(r, c).empty();
                }
            }
        }

        // Posizione: bianco può catturare l'unico nero
        board.placePiece(GameColor.WHITE, 5, 1);
        board.placePiece(GameColor.BLACK, 4, 2);

        // Cattura: (5,1) -> (3,3)
        game.processTurn(List.of(new Move(5, 1, 3, 3)));

        assertEquals(GameStatus.WHITE_WINS, game.getStatus());
    }
}
