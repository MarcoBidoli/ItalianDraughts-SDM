public class Board {
    private Cell[][] board;


    /* TODO
    * - add methods to work with pieces from a Board obj
    *   e.g. instead of gameBoard[i][j].getCell().getPiece().getColor() -> gameBoard[i][j].getPlayerColor()
    *       or gameBoard[i][j].isPieceKing() etc.
    *
     */

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

    public void placePiece(Color color, int x, int y) throws InvalidMoveException {
        board[x][y].putPieceOn(new Piece(color));
    }

    public void setGame() {
        for(int i=0; i<3; i++) {
            for(int j=0; j<8; j++) {
                if (board[i][j].getColor() == Color.BLACK)
                    try {
                        placePiece(Color.BLACK, i, j);
                    } catch (InvalidMoveException e) {
                        // TODO: Handle error message
                    }
            }
        }

        for(int i=5; i<8; i++) {
            for(int j=0; j<8; j++) {
                if (board[i][j].getColor() == Color.BLACK)
                    try {
                        placePiece(Color.WHITE, i, j);
                    } catch (InvalidMoveException e) {
                        // TODO: Handle error message
                    }
            }
        }
    }

    public void resetGame() {
        emptyBoard();
        setGame();
    }

    // Added for action branch support
    public boolean isOnBoard(int i, int j) {
        return i >= 0 && i < 8 && j >= 0 && j < 8;
    }

    public void printBoard() {
        IO.println(this.getBoardRepresentation());
    }

    public String getBoardRepresentation() {
        StringBuilder sb = new StringBuilder();
        sb.append("   0  1  2  3  4  5  6  7\n");

        for(int i = 0; i < board.length; i++) {
            sb.append(i).append(" ");
            for(int j = 0; j < board[i].length; j++) {
                if (board[i][j].isEmpty()) {
                    sb.append(" . ");
                } else {
                    Piece p = board[i][j].getPiece();
                    char symbol = (p.getColor() == Color.WHITE) ? '⛀' : '⛂';
                    if(p.isKing()) symbol = (p.getColor() == Color.WHITE) ? '⛁' : '⛃';;
                    sb.append(" ").append(symbol).append(" ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
