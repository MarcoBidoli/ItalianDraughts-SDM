/**
 * Thrown to indicate that a requested move or piece placement violates
 * the board's spatial constraints.
 **/
public class InvalidMoveException extends Exception {
    /**
     * Constructs a new exception with a specific detail message.
     * @param msg The description of why the move was considered invalid.
     */
    public InvalidMoveException(String msg) {
        super(msg);
    }
}
