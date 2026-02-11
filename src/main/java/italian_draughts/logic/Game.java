package italian_draughts.logic;

import italian_draughts.domain.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the game logic.
 * This class is responsible for managing the game state, players, and turns.
 */
public class Game {

    public static final int MAX_QUIET_MOVES = DrawController.MAX_QUIET_MOVES;

    private final Board gameBoard;

    private final Player blackPlayer;
    private final Player whitePlayer;

    private Player currentPlayer;
    private GameStatus status;

    private Square selectedSquare = null;
    private List<List<Move>> selectedPieceMoves = new ArrayList<>();

    private final DrawController drawController;
    private final WinController winController;
    private final MoveController moveController;

    private final List<GameObserver> observers = new ArrayList<>();

    /**
     * Constructs a new Game instance.
     *
     * @param whitePlayer The white player.
     * @param blackPlayer The black player.
     */
    public Game(Player whitePlayer, Player blackPlayer) {
        this.gameBoard = new Board();
        this.blackPlayer = blackPlayer;
        this.whitePlayer = whitePlayer;
        this.currentPlayer = whitePlayer; // parte il bianco
        this.status = GameStatus.ONGOING;

        this.drawController = new DrawController();
        this.winController = new WinController();
        this.moveController = new MoveController();
    }

    /**
     * Gets the current player.
     *
     * @return The current player.
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Gets the game board.
     *
     * @return The game board.
     */
    public Board getBoard() {
        return gameBoard;
    }

    /**
     * Switches to the next turn.
     */
    public void nextTurn() {
        currentPlayer = getOpponent(currentPlayer);
    }

    /**
     * Gets the opponent of a player.
     *
     * @param player The player.
     * @return The opponent of the player.
     */
    public Player getOpponent(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }

        return (player.color() == GameColor.BLACK) ? this.whitePlayer : this.blackPlayer;
    }

    // SELECTION

    /**
     * Handles the selection of a square on the board.
     *
     * @param row The row of the selected square.
     * @param col The column of the selected square.
     */
    public void handleSelection(int row, int col) {
        for (List<Move> move : selectedPieceMoves) {
            Move last = move.getLast();
            if (last.toRow() == row && last.toCol() == col) {
                try {
                    processTurn(new ArrayList<>(move));
                    return;
                } catch (InvalidMoveException e) {
                    return;
                }
            }
        }

        updateSelectedPiece(row, col);
        updateSelectedPieceMoves(row, col);
        notifyObservers();
    }

    private void updateSelectedPiece(int row, int col) {
        List<List<Move>> moves = getMovesFor(row, col);

        if (!moves.isEmpty()) {
            this.selectedSquare = new Square(row, col);
        } else {
            this.selectedSquare = null;
        }
    }

    private void updateSelectedPieceMoves(int row, int col) {
        List<List<Move>> moves = getMovesFor(row, col);

        if (!moves.isEmpty()) {
            this.selectedPieceMoves = moves;
        } else {
            this.selectedPieceMoves = new ArrayList<>();
        }
    }

    /**
     * Gets the legal moves for a piece at the given position.
     *
     * @param row The row of the piece.
     * @param col The column of the piece.
     * @return A list of legal moves.
     */
    public List<List<Move>> getMovesFor(int row, int col) {
        LegalMoves legalMoves = new LegalMoves(gameBoard, currentPlayer.color());
        return legalMoves.getSinglePieceLegalMoves(row, col);
    }

    // TURN ORCHESTRATION

    private void processTurn(List<Move> moves) throws InvalidMoveException {
        if (status != GameStatus.ONGOING) return;
        if (moves == null || moves.isEmpty()) {
            throw new InvalidMoveException("Turn must contain at least one move");
        }

        boolean captureOccurred = moveController.movePieces(moves, this.gameBoard);

        // clear selection
        this.selectedSquare = null;
        this.selectedPieceMoves = new ArrayList<>();

        // aggiornamento trackers
        if (captureOccurred) {
            drawController.clearVisits();
        }
        drawController.boardEncoder(gameBoard);

        // draw
        if (drawController.checkDraw(captureOccurred, gameBoard, whitePlayer, blackPlayer)) {
            status = GameStatus.DRAW;
            notifyObservers();
            return;
        }

        // win
        nextTurn();
        GameStatus winStatus = winController.checkWin(gameBoard, currentPlayer);
        if (winStatus != GameStatus.ONGOING) {
            status = winStatus;
        }

        notifyObservers();
    }


    // STATUS / LEGAL MOVES

    /**
     * Gets the current status of the game.
     *
     * @return The game status.
     */
    public GameStatus getStatus() {
        return status;
    }

    /**
     * Calculates the legal moves for the current player.
     */
    public void calculateLegalMoves() {
        winController.calculateLegalMoves(gameBoard, currentPlayer);
    }

    /**
     * Gets the legal moves for the current player.
     *
     * @return A list of legal moves.
     */
    public List<List<Move>> getCurrentLegalMoves() {
        return winController.getCurrentLegalMoves();
    }

    // ========== DRAW / RESIGN ==========

    /**
     * Handles an agreed draw.
     */
    public void agreedDrawHandling() {
        status = GameStatus.DRAW;
        notifyObservers();
    }

    /**
     * Handles a resignation.
     *
     * @param loser The color of the player who resigned.
     */
    public void resignHandling(GameColor loser) {
        status = (loser == GameColor.WHITE) ? GameStatus.BLACK_WINS : GameStatus.WHITE_WINS;
        notifyObservers();
    }

    /**
     * Sets the current turn to the given player.
     *
     * @param player The player to set as the current turn.
     */
    public void setCurrentTurn(Player player) {
        currentPlayer = player;
    }

    // OBSERVERS

    /**
     * Adds a game observer.
     *
     * @param gameObserver The game observer to add.
     */
    public void addObserver(GameObserver gameObserver) {
        observers.add(gameObserver);
    }

    private void notifyObservers() {
        for (GameObserver obs : observers) {
            obs.modelChanged();
        }
    }

    // GETTERS SELECTION

    /**
     * Gets the selected square.
     *
     * @return The selected square.
     */
    public Square getSelectedSquare() {
        return selectedSquare;
    }

    /**
     * Gets the legal moves for the selected piece.
     *
     * @return A list of legal moves.
     */
    public List<List<Move>> getSelectedPieceMoves() {
        return selectedPieceMoves;
    }
}
