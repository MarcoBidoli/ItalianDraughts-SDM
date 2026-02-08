package italian_draughts.domain;

public class Board {
    private final Cell[][] board;


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
                    board[i][j] = new Cell(GameColor.BLACK);
                } else {
                    board[i][j] = new Cell(GameColor.WHITE);
                }
            }
        }
    }

    public GameColor getCellColor(int x, int y) {
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

    public void placePiece(GameColor color, int x, int y) throws InvalidMoveException {
        board[x][y].putPieceOn(new Piece(color));
    }

    public void placeKing(GameColor color, int x, int y) throws InvalidMoveException {
        Piece p = new Piece(color);
        p.setKing(true);
        board[x][y].putPieceOn(p);
    }

    public void setGame() {
        for(int i=0; i<3; i++) {
            for(int j=0; j<8; j++) {
                if (board[i][j].getColor() == GameColor.BLACK)
                    try {
                        placePiece(GameColor.BLACK, i, j);
                    } catch (InvalidMoveException e) {
                        // TODO: Handle error message
                    }
            }
        }

        for(int i=5; i<8; i++) {
            for(int j=0; j<8; j++) {
                if (board[i][j].getColor() == GameColor.BLACK)
                    try {
                        placePiece(GameColor.WHITE, i, j);
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
    public boolean isNotOnBoard(int i, int j) {
        return !onBoard(i, j);
    }

    public static boolean onBoard(int i, int j) {
        return i >= 0 && i < 8 && j >= 0 && j < 8;
    }

    public void printBoard() {
        String repr = getBoardRepresentation();

        StringBuilder sb = new StringBuilder();
        sb.append("   0  1  2  3  4  5  6  7\n");

        int index = 0;
        for (int i = 0; i < 8; i++) {
            sb.append(i).append(" ");
            for (int j = 0; j < 8; j++) {
                char c = repr.charAt(index++);
                char symbol = switch (c) {
                    case 'w' -> '⛀';
                    case 'W' -> '⛁';
                    case 'b' -> '⛂';
                    case 'B' -> '⛃';
                    case '-' -> '.';
                    default -> throw new IllegalArgumentException("Invalid character detected: '" + c + "'");
                };
                sb.append(" ").append(symbol).append(" ");
            }
            sb.append("\n");
        }

        IO.println(sb.toString());
    }


    public String getBoardRepresentation() {
        StringBuilder sb = new StringBuilder();

        for (Cell[] cells : board) {
            for (Cell cell : cells) {
                if (cell.isEmpty()) {
                    sb.append('-');
                } else {
                    Piece p = cell.getPiece();
                    if (p.getColor() == GameColor.WHITE) {
                        sb.append(p.isKing() ? 'W' : 'w');
                    } else {
                        sb.append(p.isKing() ? 'B' : 'b');
                    }
                }
            }
        }
        return sb.toString();
    }
    public int countColorPieces(GameColor color) {
        int p=0;
        for(int i=0; i<8; i++) {
            for(int j=0; j<8; j++) {
                if(!getCell(i,j).isEmpty()) {
                    if(getColorOfPieceWithCoordinates(i, j) == color)
                        p++;
                }
            }
        }
        return p;
    }

    public GameColor getColorOfPieceWithCoordinates(int i, int j) {
        return getCell(i, j).getPiece().getColor();
    }

    public Piece getPieceWithCoordinates(int i, int j) {
        return getCell(i, j).getPiece();
    }

    public boolean isPieceWithCoordinatesKing(int i, int j) {
        return getCell(i, j).getPiece().isKing();
    }
    public void emptyCell(int i, int j) {
        board[i][j].empty();
    }

    public boolean isEmptyCell(int i, int j) {
        return board[i][j].isEmpty();
    }
//fare test di sti metodi
}
