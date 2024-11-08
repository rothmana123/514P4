import java.util.ArrayList;
import java.util.List;

/**
 * The WOFAbstractClass is an abstract class for a Wheel of Fortune-style guessing game.
 * It provides the basic structure and required methods for subclasses to implement
 * game-specific logic, such as generating phrases, processing guesses, and managing the
 * hidden phrase.
 *
 * <p>This class extends the GuessingGame superclass and defines abstract methods that
 * subclasses must implement to handle phrase reading, phrase generation, hidden phrase
 * creation, and guess processing.</p>
 */
abstract class WOFAbstractClass extends GuessingGame {

    /**
     * Reads and returns a list of phrases that will be used in the game.
     * This method is expected to be implemented by subclasses to provide a custom
     * source of phrases.
     *
     * @return a List of phrases to be used in the game
     */
    public abstract List<String> readPhrases();

    /**
     * Generates a random phrase from the list of available phrases and returns it
     * as a StringBuilder.
     *
     * @return a StringBuilder containing the randomly selected phrase
     */
    public abstract StringBuilder randomPhrase();

    /**
     * Generates a hidden version of the provided phrase, where each character is
     * replaced (e.g., with underscores) to conceal the actual phrase.
     *
     * @param phrase the original phrase to be hidden
     * @return a StringBuilder containing the hidden version of the phrase
     */
    public abstract StringBuilder generateHiddenPhrase(StringBuilder phrase);

    /**
     * Processes the player's guess, checks for matches in the hidden phrase,
     * and updates the game state accordingly.
     *
     * @param guess the player's guess input
     */
    public abstract void processGuess(String guess);

    /**
     * Retrieves the next guess from the player. This method could implement
     * custom logic to prompt for and validate player guesses based on previous
     * guesses or other game criteria.
     *
     * @param previousGuesses a StringBuilder containing previous guesses
     * @return the next guess as a String
     */
    public abstract String getGuess(StringBuilder previousGuesses);
}

