/**
 * Represents the 8x8 game board for Italian Checkers.
 * This class manages a matrix of {@link Cell} objects and handles the
 * spatial distribution of pieces.
 * * The coordinate system follows a standard 2D array structure where
 * [0][0] is the top-left corner and [7][7] is the bottom-right.
 */
public class Board {
    /** The 8x8 matrix representing the physical board. */
    private Cell[][] board;

    /**
     * Initializes a new Board with an empty 8x8 grid.
     * Automatically calls {@link #initCells()} to set up the checkerboard pattern.
     */
    public Board() {
        board = new Cell[8][8];
        initCells();
    }

    /**
     * Creates the checkerboard pattern by assigning colors to cells.
     */
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

    /**
     * Retrieves the color of a specific cell.
     * @param x The row index.
     * @param y The column index.
     * @return The {@link GameColor} of the requested cell.
     */
    public GameColor getCellColor(int x, int y) {
        return board[x][y].getColor();
    }

    /**
     * Accesses the {@link Cell} object at the specified coordinates.
     * @param x The row index.
     * @param y The column index.
     * @return The Cell object.
     */
    public Cell getCell(int x, int y) {
        return board[x][y];
    }

    /**
     * Clears all pieces from the board.
     */
    private void emptyBoard() {
        for(int i=0; i<8; i++) {
            for(int j=0; j<8; j++) {
                if(!board[i][j].isEmpty()) {
                    board[i][j].empty();
                }
            }
        }
    }

    /**
     * Checks if the board contains zero pieces.
     * @return true if all cells are empty, false otherwise.
     */
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

    /**
     * Places a new piece of the specified color at the given coordinates.
     * @param color The color of the piece to be created.
     * @param x The row index.
     * @param y The column index.
     * @throws InvalidMoveException if the target cell is White or occupied.
     */
    public void placePiece(GameColor color, int x, int y) throws InvalidMoveException {
        board[x][y].putPieceOn(new Piece(color));
    }

    public void placeKing(GameColor color, int x, int y) throws InvalidMoveException {
        Piece p = new Piece(color);
        p.setKing(true);
        board[x][y].putPieceOn(p);
    }

    /**
     * Sets the initial configuration for a standard Italian Checkers game.
     * Places Black pieces in the first 3 rows and White pieces in the last 3 rows
     * (only on Black cells).
     */
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

    /**
     * Resets the board to the standard starting position.
     */
    public void resetGame() {
        emptyBoard();
        setGame();
    }

    /**
     * Safety check to ensure coordinates fall within the 8x8 grid.
     * Essential for prevent ArrayIndexOutOfBoundsExceptions during move calculations.
     * @param i The row index.
     * @param j The column index.
     * @return true if coordinates are valid (0-7), false otherwise.
     */
    public boolean isOnBoard(int i, int j) {
        return i >= 0 && i < 8 && j >= 0 && j < 8;
    }

    /**
     * Prints the current board state to the console.
     */
    public void printBoard() {
        IO.println(this.getBoardRepresentation());
    }

    /**
     * Generates a string-based visual representation of the board.
     * Uses Unicode symbols (⛀, ⛂, ⛁, ⛃) for pieces and Kings.
     * @return A formatted string representing the board grid.
     */
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
                    char symbol = (p.getColor() == GameColor.WHITE) ? '⛀' : '⛂';
                    if(p.isKing()) symbol = (p.getColor() == GameColor.WHITE) ? '⛁' : '⛃';;
                    sb.append(" ").append(symbol).append(" ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
