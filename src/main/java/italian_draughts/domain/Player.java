package italian_draughts.domain;

import italian_draughts.logic.Game;

import java.util.Optional;

public interface Player {
    Optional<Move> makeMove(Game game);

    GameColor getColor();
}
