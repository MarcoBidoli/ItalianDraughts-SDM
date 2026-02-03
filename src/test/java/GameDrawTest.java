import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameDrawTest {

    @Test
    void drawTriggeredAfter40QuietMovesEachWhenBothHaveKings() throws InvalidMoveException {
        Board board = new Board();
        board.initCells();

        // Mettiamo un solo pezzo bianco e uno nero e li rendiamo king
        board.placePiece(GameColor.WHITE, 4, 4);
        board.getCell(4, 4).getPiece().setKing(true);

        board.placePiece(GameColor.BLACK, 3, 3);
        board.getCell(3, 3).getPiece().setKing(true);

        Game game = new Game();

        Board gb = game.getBoard();
        gb.initCells();
        gb.placePiece(GameColor.WHITE, 4, 4);
        gb.getCell(4, 4).getPiece().setKing(true);
        gb.placePiece(GameColor.BLACK, 3, 3);
        gb.getCell(3, 3).getPiece().setKing(true);

        // Ora muoviamo i due king avanti/indietro senza catture.
        // White: (4,4) <-> (5,5)
        // Black: (3,3) <-> (2,2)
        for (int i = 0; i < 40; i++) {
            // turno White
            List<Move> w = new ArrayList<>();
            if (i % 2 == 0) w.add(new Move(4, 4, 5, 5));
            else w.add(new Move(5, 5, 4, 4));
            game.applyTurn(w);

            // turno Black
            List<Move> b = new ArrayList<>();
            if (i % 2 == 0) b.add(new Move(3, 3, 2, 2));
            else b.add(new Move(2, 2, 3, 3));
            game.applyTurn(b);
        }

        assertEquals(GameStatus.DRAW, game.getStatus());
    }
}
