public class Board {
    Cell[][] board;

    public Board() {
        board = new Cell[8][8];
        initCells();
    }

    public void initCells() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i + j) % 2 == 0) {
                    board[i][j] = new Cell(Color.BLACK);
                } else {
                    board[i][j] = new Cell(Color.WHITE);
                }
            }
        }
    }

    public Color getCellColor(int x, int y) {
        return board[x][y].getColor();
    }

    public Cell getCell(int x, int y) {
        return board[x][y];
    }

    private void emptyBoard() {
        for(int i=0; i<8; i++) {
            for(int j=0; j<8; j++) {
                if(!board[i][j].isEmpty()) {
                    board[i][j].empty();
                }
            }
        }
    }

    public boolean isEmpty() {
        for(int i=0; i<8; i++) {
            for(int j=0; j<8; j++) {
                if(!board[i][j].isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean placePiece(Color color, int x, int y) {
        return board[x][y].putPieceOn(new Piece(color));
    }

    public void setGame() {
        for(int i=0; i<3; i++) {
            for(int j=0; j<8; j++) {
                if (board[i][j].getColor() == Color.BLACK)
                    placePiece(Color.BLACK, i, j);
            }
        }

        for(int i=5; i<8; i++) {
            for(int j=0; j<8; j++) {
                if (board[i][j].getColor() == Color.BLACK)
                    placePiece(Color.WHITE, i, j);
            }
        }
    }

    public void resetGame() {
        emptyBoard();
        setGame();
    }
}
