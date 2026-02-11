package italian_draughts.domain;

/**
 * Represents a human player.
 * @param color The color of the player.
 */
public record HumanPlayer(GameColor color) implements Player {
}
