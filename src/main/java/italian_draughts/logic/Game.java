package italian_draughts.logic;

import italian_draughts.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Game {

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

    public Game(Player whitePlayer, Player blackPlayer) {
        this.gameBoard = new Board();
        this.blackPlayer = blackPlayer;
        this.whitePlayer = whitePlayer;
        this.currentPlayer = whitePlayer; // parte il bianco
        this.status = GameStatus.ONGOING;

        this.drawController = new DrawController();
        this.winController = new WinController();
        this.moveController = new MoveController(drawController);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Board getBoard() {
        return gameBoard;
    }

    public void nextTurn() {
        currentPlayer = getOpponent(currentPlayer);
    }

    public Player getOpponent(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }

        return (player.getColor() == GameColor.BLACK) ? this.whitePlayer : this.blackPlayer;
    }

    // ========== SELECTION  ==========

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

    public List<List<Move>> getMovesFor(int row, int col) {
        try {
            LegalMoves legalMoves = new LegalMoves(gameBoard, currentPlayer.getColor());
            return legalMoves.getSinglePieceLegalMoves(row, col);
        } catch (InvalidMoveException e) {
            throw new RuntimeException(e);
        }
    }

    // ========== TURN ORCHESTRATION ==========

    private void processTurn(List<Move> moves) throws InvalidMoveException {
        if (status != GameStatus.ONGOING) return;
        if (moves == null || moves.isEmpty()) {
            throw new InvalidMoveException("Turn must contain at least one move");
        }

        boolean captureOccurred = moveController.movePieces(moves, this.gameBoard);

        // clear selection
        this.selectedSquare = null;
        this.selectedPieceMoves = new ArrayList<>();

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

    // ========== STATUS / LEGAL MOVES ==========

    public GameStatus getStatus() {
        return status;
    }

    public void calculateLegalMoves() {
        winController.calculateLegalMoves(gameBoard, currentPlayer);
    }

    public List<List<Move>> getCurrentLegalMoves() {
        return winController.getCurrentLegalMoves();
    }

    // ========== DRAW / RESIGN ==========

    public void agreedDrawHandling() {
        status = GameStatus.DRAW;
        notifyObservers();
    }

    public void resignHandling(GameColor loser) {
        status = (loser == GameColor.WHITE) ? GameStatus.BLACK_WINS : GameStatus.WHITE_WINS;
        notifyObservers();
    }

    public void setCurrentTurn(Player player) {
        currentPlayer = player;
    }

    // ========== OBSERVERS ==========

    public void addObserver(GameObserver gameObserver) {
        observers.add(gameObserver);
    }

    private void notifyObservers() {
        for (GameObserver obs : observers) {
            obs.modelChanged();
        }
    }

    // ========== GETTERS SELECTION ==========

    public Square getSelectedSquare() {
        return selectedSquare;
    }

    public List<List<Move>> getSelectedPieceMoves() {
        return selectedPieceMoves;
    }

    // ========== DEBUG / SUPPORT METHODS ==========

    public Map<List<SquareEncoder>, Integer> getVisits() {
        return drawController.getVisits();
    }

    public boolean checkRepetition() {
        return drawController.checkRepetition();
    }

    public void boardEncoder(Board board) {
        drawController.boardEncoder(board);
    }

    public boolean movePieces(List<Move> move, Board board) throws InvalidMoveException {
        return moveController.movePieces(move, board);
    }
}
