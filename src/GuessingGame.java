/**
 * The GuessingGame class is an abstract class that extends the Game class.
 * It provides a framework for games that involve generating a hidden phrase or code
 * and processing player guesses to reveal or match that phrase.
 *
 * <p>Subclasses should implement methods for generating a random phrase, creating a hidden
 * version of the phrase, and obtaining player guesses. This class allows flexibility for
 * different guessing games, such as Wheel of Fortune or Mastermind.</p>
 */
abstract class GuessingGame extends Game {

    /**
     * Generates a random phrase or code for the guessing game.
     * This phrase is the target the player tries to guess.
     *
     * @return a StringBuilder containing the randomly generated phrase
     */
    public abstract StringBuilder randomPhrase();

    /**
     * Creates a hidden or masked version of the provided phrase, typically by replacing
     * each character with a symbol (e.g., '*') to conceal it from the player.
     *
     * @param phrase the original phrase to be concealed
     * @return a StringBuilder containing the hidden version of the phrase
     */
    public abstract StringBuilder generateHiddenPhrase(StringBuilder phrase);

    /**
     * Prompts the player to enter a guess and processes the input, typically ensuring
     * the guess follows game rules (e.g., length and valid characters).
     *
     * @param previousGuesses a StringBuilder containing the player's previous guesses,
     *                        allowing subclasses to prevent duplicate guesses if needed
     * @return a String containing the player's guess
     */
    public abstract String getGuess(StringBuilder previousGuesses);
}

