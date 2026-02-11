package italian_draughts.domain;

/**
 * Exception thrown when an invalid move is attempted.
 */
public class InvalidMoveException extends Exception {
    /**
     * Creates a new InvalidMoveException.
     * @param msg The message of the exception.
     */
    public InvalidMoveException(String msg) {
        super(msg);
    }
}
