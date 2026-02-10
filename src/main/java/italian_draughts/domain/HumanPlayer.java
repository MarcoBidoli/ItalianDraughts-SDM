package italian_draughts.domain;

import italian_draughts.logic.Game;

import java.util.Optional;

public class HumanPlayer implements Player {
    private final GameColor color;

    public HumanPlayer(GameColor color) {
        this.color = color;
    }

    @Override
    public Optional<Move> makeMove(Game game) {
        return Optional.empty();
    }

    @Override
    public GameColor getColor() {
        return color;
    }
}
