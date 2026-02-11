package italian_draughts.domain;

/**
 * Represents the encoding of a square.
 * @param pieceEnc The encoding of the piece.
 * @param positionEnc The encoding of the position.
 */
public record SquareEncoder(char pieceEnc, int positionEnc) {}
