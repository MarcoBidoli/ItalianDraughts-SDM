package italian_draughts.logic;

/**
 * Observer of the game.
 * It is notified when the model of the game changes.
 */
public interface GameObserver {
    /**
     * This method is called when the model of the game changes.
     */
    void modelChanged();
}
