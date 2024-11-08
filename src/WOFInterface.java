/**
 * The WOFInterface defines the essential methods for a Wheel of Fortune-style player.
 * It includes methods for obtaining a player's next guess, retrieving the player's unique ID,
 * and resetting the player to start a new game.
 *
 * <p>Implementing classes must define these methods to support game mechanics such as
 * guessing letters, identifying players, and resetting the player's state.</p>
 */
public interface WOFInterface {

    /**
     * Retrieves the next guess from the player. This method should return a letter
     * that the player has selected for their guess, ensuring it is not a repeat of previous guesses.
     *
     * @param previousGuesses a StringBuilder containing letters that have already been guessed
     * @return the next guessed letter as a String
     */
    String getGuess(StringBuilder previousGuesses);

    /**
     * Retrieves the unique ID associated with this player.
     *
     * @return an integer representing the player's unique ID
     */
    int playerId();

    /**
     * Resets the player's state to start a new game. This method may involve
     * resetting the player ID or any other game-specific attributes.
     */
    void reset();
}


