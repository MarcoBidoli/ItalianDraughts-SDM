import org.junit.jupiter.api.Test;

public class EatingLegalMovesTest {
    @Test
    public void singleEat() throws InvalidMoveException {
        Board board = new Board();
        board.placePiece(Color.WHITE, 5, 2);
        board.placePiece(Color.BLACK, 4, 3);

        Action action = new Action(board, Color.WHITE);

    }
}
