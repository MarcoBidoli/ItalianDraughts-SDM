package italian_draughts.domain;

/**
 * Represents the status of the game.
 */
public enum GameStatus {
    /**
     * The game is ongoing.
     */
    ONGOING,
    /**
     * White player wins.
     */
    WHITE_WINS,
    /**
     * Black player wins.
     */
    BLACK_WINS,
    /**
     * The game is a draw.
     */
    DRAW
}
