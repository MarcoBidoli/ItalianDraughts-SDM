public class Board {
    Cell[][] board = new Cell[8][8];

    public Board() {
        initCells();
    }

    public void initCells() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i + j) % 2 == 0) {
                    board[i][j].setColor(Color.BLACK);
                } else {
                    board[i][j].setColor(Color.WHITE);
                }
            }
        }
    }

    public Color getCellColor(int x, int y) {
        return board[x][y].getColor();
    }
}
