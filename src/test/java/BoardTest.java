import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BoardTest {

    @Test
    public void checkSEBlack() {
        Board board = new Board();
        assertEquals(GameColor.BLACK, board.getCellColor(7,7));
    }

    @Test
    public void checkNWBlack() {
        Board board = new Board();
        assertEquals(GameColor.BLACK, board.getCellColor(0,0));
    }

    @Test
    public void emptyTest() {
        Board board = new Board();
        assertEquals(true, board.isEmpty());
    }

    @Test
    public void countPieces() {
        Board board = new Board();
        board.setGame();
        int c = 0;
        for(int i=0; i<8; i++) {
            for(int j=0; j<8; j++) {
                if(!board.getCell(i,j).isEmpty())
                    c++;
            }
        }
        assertEquals(24, c);
    }

    @Test
    public void countPiecesColor() {
        Board board = new Board();
        board.setGame();
        int w = 0, b = 0;
        for(int i=0; i<8; i++) {
            for(int j=0; j<8; j++) {
                if(!board.getCell(i,j).isEmpty()) {
                    if(board.getCell(i,j).getPiece().getColor() == GameColor.WHITE)
                        w++;
                    if(board.getCell(i,j).getPiece().getColor() == GameColor.BLACK)
                        b++;
                }
            }
        }
        assertEquals(12, w);
        assertEquals(12, b);
    }

    @Test
    public void printBoard() {
        Board board = new Board();
        board.setGame();
        board.printBoard();
    }
}
