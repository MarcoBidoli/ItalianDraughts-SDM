package italian_draughts.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class Board {
    private final Cell[][] board;

    private static final List<Coords> PLAYABLE_SQUARES;
    private static final List<Coords> ALL_SQUARES;

    static {
        List<Coords> valid_squares = new ArrayList<>();
        List<Coords> all_squares = new ArrayList<>();
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if ((r + c) % 2 == 0) {
                    valid_squares.add(new Coords(r, c));
                }
                all_squares.add(new Coords(r, c));
            }
        }
        PLAYABLE_SQUARES = Collections.unmodifiableList(valid_squares);
        ALL_SQUARES = Collections.unmodifiableList(all_squares);
    }

    public Board() {
        board = new Cell[8][8];
        initCells();
    }

    private void forEachSquare(List<Coords> list, Consumer<Coords> action) {
        for (Coords sq : list) action.accept(sq);
    }

    // Shortcut to avoid repeating board[r][c] logic everywhere
    private void onCell(Coords sq, Consumer<Cell> action) {
        action.accept(board[sq.row()][sq.col()]);
    }

    public void initCells() {
        forEachSquare(ALL_SQUARES, square -> {
            GameColor squareColor = PLAYABLE_SQUARES.contains(square) ? GameColor.BLACK : GameColor.WHITE;
            board[square.row()][square.col()] = new Cell(squareColor);
        });
    }

    public GameColor getCellColor(int x, int y) {
        return board[x][y].getColor();
    }

    public Cell getCell(int x, int y) {
        return board[x][y];
    }

    public void emptyBoard() {
        forEachSquare(PLAYABLE_SQUARES, square -> onCell(square, Cell::empty));
    }

    // TODO: choose if replacing in tests with the other PlacePiece()
    public void placePiece(GameColor color, int x, int y) throws InvalidMoveException {
        board[x][y].putPieceOn(new Piece(color));
    }

    public void placePiece(Piece piece, int x, int y) throws InvalidMoveException {
        board[x][y].putPieceOn(piece);
    }

    public void placePiece(GameColor pieceColor, PieceType pieceType, int x, int y) throws InvalidMoveException {
        Piece p = new Piece(pieceColor);
        if (pieceType.equals(PieceType.KING))
                p.setKing(true);
        board[x][y].putPieceOn(p);
    }

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

    public void resetGame() {
        emptyBoard();
        setGame();
    }

    // Added for action branch support
    public static boolean positionIsOffBoard(int i, int j) {
        return !onBoard(i, j);
    }

    public static boolean onBoard(int i, int j) {
        return i >= 0 && i < 8 && j >= 0 && j < 8;
    }

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

    public String getBoardRepresentation() {
        StringBuilder sb = new StringBuilder();

        forEachSquare(ALL_SQUARES, square ->
                onCell(square, cell -> sb.append(cell.getSymbol()))
        );
        return sb.toString();
    }

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

    public int countColorPieces(GameColor color) {
        int count = 0;
        for (Coords square : PLAYABLE_SQUARES) {
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

    public GameColor colorOfPiece(int i, int j) {
        Piece piece = board[i][j].getPiece();
        return piece.getColor();
    }

    public Piece getPieceAt(int i, int j) {
        return board[i][j].getPiece();
    }

    public boolean isKingAt(int i, int j) {
        Piece piece = board[i][j].getPiece();
        return piece.isKing();
    }
    public void emptyCell(int i, int j) {
        board[i][j].empty();
    }

    public boolean isEmptyCell(int i, int j) {
        return board[i][j].isEmpty();
    }

    public boolean isPieceOwnedBy(GameColor playerColor, int row, int col) {
        Cell cell = getCell(row, col);
        if(cell.isEmpty()) return false;
        GameColor pieceOnCellColor = colorOfPiece(row, col);
        return pieceOnCellColor.equals(playerColor);
    }
}
