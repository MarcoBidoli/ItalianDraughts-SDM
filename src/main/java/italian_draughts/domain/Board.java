package italian_draughts.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * Represents the game board.
 */
public class Board {
    private final Cell[][] board;

    public static final List<Square> PLAYABLE_SQUARES;
    public static final List<Square> ALL_SQUARES;

    static {
        List<Square> valid_squares = new ArrayList<>();
        List<Square> all_squares = new ArrayList<>();
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if ((r + c) % 2 == 0) {
                    valid_squares.add(new Square(r, c));
                }
                all_squares.add(new Square(r, c));
            }
        }
        PLAYABLE_SQUARES = Collections.unmodifiableList(valid_squares);
        ALL_SQUARES = Collections.unmodifiableList(all_squares);
    }

    /**
     * Creates a new Board.
     */
    public Board() {
        board = new Cell[8][8];
        initCells();
    }

    /**
     * Iterates over a list of squares and applies an action to each square.
     * @param list The list of squares.
     * @param action The action to apply.
     */
    public void forEachSquare(List<Square> list, Consumer<Square> action) {
        for (Square sq : list) action.accept(sq);
    }

    // Shortcut to avoid repeating board[r][c] logic everywhere
    public void onCell(Square sq, Consumer<Cell> action) {
        action.accept(board[sq.row()][sq.col()]);
    }

    /**
     * Initializes the cells of the board.
     */
    public void initCells() {
        forEachSquare(ALL_SQUARES, square -> {
            GameColor squareColor = PLAYABLE_SQUARES.contains(square) ? GameColor.BLACK : GameColor.WHITE;
            board[square.row()][square.col()] = new Cell(squareColor);
        });
    }

    /**
     * Gets the color of the cell at the given position.
     * @param x The row of the cell.
     * @param y The column of the cell.
     * @return The color of the cell.
     */
    public GameColor getCellColor(int x, int y) {
        return board[x][y].getColor();
    }

    /**
     * Gets the cell at the given position.
     * @param x The row of the cell.
     * @param y The column of the cell.
     * @return The cell at the given position.
     */
    public Cell getCell(int x, int y) {
        return board[x][y];
    }

    /**
     * Empties all playable cells on the board.
     */
    public void emptyBoard() {
        forEachSquare(PLAYABLE_SQUARES, square -> onCell(square, Cell::empty));
    }

    // TODO: choose if replacing in tests with the other PlacePiece()
    /**
     * Places a piece of the given color at the given position.
     * @param color The color of the piece.
     * @param x The row of the position.
     * @param y The column of the position.
     * @throws InvalidMoveException If the move is invalid.
     */
    public void placePiece(GameColor color, int x, int y) throws InvalidMoveException {
        board[x][y].putPieceOn(new Piece(color));
    }

    /**
     * Places a piece at the given position.
     * @param piece The piece to place.
     * @param x The row of the position.
     * @param y The column of the position.
     * @throws InvalidMoveException If the move is invalid.
     */
    public void placePiece(Piece piece, int x, int y) throws InvalidMoveException {
        board[x][y].putPieceOn(piece);
    }

    /**
     * Places a piece of the given color and type at the given position.
     * @param pieceColor The color of the piece.
     * @param pieceType The type of the piece.
     * @param x The row of the position.
     * @param y The column of the position.
     * @throws InvalidMoveException If the move is invalid.
     */
    public void placePiece(GameColor pieceColor, PieceType pieceType, int x, int y) throws InvalidMoveException {
        Piece p = new Piece(pieceColor);
        if (pieceType.equals(PieceType.KING))
                p.setKing(true);
        board[x][y].putPieceOn(p);
    }

    /**
     * Sets up the initial game configuration on the board.
     */
    public void setGame() {
        forEachSquare(PLAYABLE_SQUARES, square -> {
            int row = square.row();
            int col = square.col();
            try {
                if (row < 3) placePiece(GameColor.BLACK, PieceType.MAN, row, col); // BLACK ON TOP
                else if (row > 4) placePiece(GameColor.WHITE, PieceType.MAN, row, col); // WHITE ON BOTTOM
            } catch (InvalidMoveException e) {
                System.err.println("Failed to initialize board at [" + row + "][" + col + "]: " + e.getMessage());
                throw new RuntimeException("Board initialization failed", e);
            }
        });
    }

    /**
     * Resets the game to its initial state.
     */
    public void resetGame() {
        emptyBoard();
        setGame();
    }

    // Added for action branch support
    /**
     * Checks if a position is off the board.
     * @param i The row of the position.
     * @param j The column of the position.
     * @return True if the position is off the board, false otherwise.
     */
    public static boolean positionIsOffBoard(int i, int j) {
        return !onBoard(i, j);
    }

    /**
     * Checks if a position is on the board.
     * @param i The row of the position.
     * @param j The column of the position.
     * @return True if the position is on the board, false otherwise.
     */
    public static boolean onBoard(int i, int j) {
        return i >= 0 && i < 8 && j >= 0 && j < 8;
    }

    /**
     * Prints the board to the console.
     */
    public void printBoard() {
        StringBuilder sb = new StringBuilder();
        sb.append("   0  1  2  3  4  5  6  7\n");

        forEachSquare(ALL_SQUARES, square -> {
            int row = square.row();
            int col = square.col();

            if (col == 0) sb.append(row).append(" ");
            onCell(square, cell -> sb.append(" ").append(cell.getSymbol()).append(" "));
            if (col == 7) sb.append("\n");
        });
        IO.println(sb.toString());
    }

    /**
     * Gets a string representation of the board.
     * @return A string representation of the board.
     */
    public String getBoardRepresentation() {
        StringBuilder sb = new StringBuilder();

        forEachSquare(ALL_SQUARES, square ->
                onCell(square, cell -> sb.append(cell.getSymbol()))
        );
        return sb.toString();
    }

    /**
     * Sets the board according to a string representation.
     * @param string The string representation of the board.
     */
    public void stringToBoard(String string) {
        string = string.replaceAll("\\s+", "");
        final int STRING_SIZE = 64;
        if(string.length() != STRING_SIZE)
            throw new IllegalArgumentException("Board string must be 64 characters, but was " + string.length());

        String finalString = string;
        forEachSquare(ALL_SQUARES, square -> {
            int row = square.row();
            int col = square.col();

            char symbol = finalString.charAt(row * 8 + col);

            try {
                convertSymbolToPiece(symbol, row, col);
            } catch (InvalidMoveException e) {
                throw new RuntimeException("Failed to convert string to board at (" + row + ", " + col + ")", e);
            }
        });
    }

    private void convertSymbolToPiece(char symbol, int row, int col) throws InvalidMoveException {
        switch (symbol) {
            case 'w' -> placePiece(GameColor.WHITE, PieceType.MAN, row, col);
            case 'W' -> placePiece(GameColor.WHITE, PieceType.KING, row, col);
            case 'b' -> placePiece(GameColor.BLACK, PieceType.MAN, row, col);
            case 'B' -> placePiece(GameColor.BLACK, PieceType.KING, row, col);
            case '-' -> emptyCell(row, col);
            default -> throw new IllegalArgumentException("Invalid char " + symbol);
        }
    }

    /**
     * Counts the number of pieces of a given color on the board.
     * @param color The color of the pieces to count.
     * @return The number of pieces of the given color.
     */
    public int countColorPieces(GameColor color) {
        int count = 0;
        for (Square square : PLAYABLE_SQUARES) {
            Cell cell = getCell(square.row(), square.col());
            if (!cell.isEmpty()) {
                Piece pieceOnCell = cell.getPiece();
                if (pieceOnCell.getColor() == color) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Gets the color of the piece at the given position.
     * @param i The row of the position.
     * @param j The column of the position.
     * @return The color of the piece.
     */
    public GameColor colorOfPiece(int i, int j) {
        Piece piece = board[i][j].getPiece();
        return piece.getColor();
    }

    /**
     * Gets the piece at the given position.
     * @param i The row of the position.
     * @param j The column of the position.
     * @return The piece at the given position.
     */
    public Piece getPieceAt(int i, int j) {
        return board[i][j].getPiece();
    }

    /**
     * Checks if the piece at the given position is a king.
     * @param i The row of the position.
     * @param j The column of the position.
     * @return True if the piece is a king, false otherwise.
     */
    public boolean isKingAt(int i, int j) {
        Piece piece = board[i][j].getPiece();
        return piece.isKing();
    }

    /**
     * Empties the cell at the given position.
     * @param i The row of the cell.
     * @param j The column of the cell.
     */
    public void emptyCell(int i, int j) {
        board[i][j].empty();
    }

    /**
     * Checks if the cell at the given position is empty.
     * @param i The row of the cell.
     * @param j The column of the cell.
     * @return True if the cell is empty, false otherwise.
     */
    public boolean isEmptyCell(int i, int j) {
        return board[i][j].isEmpty();
    }

    /**
     * Checks if the piece at the given position is owned by the given player color.
     * @param playerColor The color of the player.
     * @param row The row of the piece.
     * @param col The column of the piece.
     * @return True if the piece is owned by the player, false otherwise.
     */
    public boolean isPieceOwnedBy(GameColor playerColor, int row, int col) {
        Cell cell = getCell(row, col);
        if(cell.isEmpty()) return false;
        GameColor pieceOnCellColor = colorOfPiece(row, col);
        return pieceOnCellColor.equals(playerColor);
    }
}
