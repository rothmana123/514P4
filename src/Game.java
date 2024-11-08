/**
 * The Game class is an abstract base class for games that involve multiple rounds or sessions.
 * It defines the core structure for gameplay, allowing subclasses to implement the methods
 * for playing individual rounds, handling multiple sessions, and determining if the player wants
 * to continue playing.
 *
 * <p>This class is intended to be extended by specific game implementations, such as guessing games,
 * providing a consistent framework for managing gameplay flow.</p>
 */
abstract class Game {

    /**
     * Plays all rounds or sessions of the game, allowing for multiple rounds or players.
     * This method should handle all game rounds and return a record of the entire game session.
     *
     * @return an AllGameRecord object containing the results of all rounds played in the session
     */
    public abstract AllGameRecord playAll();

    /**
     * Plays a single round of the game. Each subclass should implement this method to define
     * the core gameplay logic for one round or session.
     */
    public abstract void play();

    /**
     * Determines if the player wishes to continue playing after a round.
     * This method is intended to be implemented by subclasses to prompt the player
     * for their choice or check any relevant game conditions.
     *
     * @return true if the player wants to play another round, false otherwise
     */
    public abstract boolean playNext();
}

