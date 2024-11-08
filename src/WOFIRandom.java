import java.util.*;

/**
 * The WOFIRandom class implements the WOFInterface for a random guessing
 * strategy in a Wheel of Fortune-style game. This class generates random
 * guesses and maintains a unique player ID for each instance.
 *
 * <p>This class keeps track of previously guessed letters to avoid
 * duplicate guesses and resets the player ID when required.</p>
 */
public class WOFIRandom implements WOFInterface {
    /** The unique player ID for this instance. */
    private int playerId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WOFIRandom that)) return false;
        return playerId == that.playerId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(playerId);
    }

    @Override
    public String toString() {
        return "WOFIRandom{" +
                "playerId=" + playerId +
                '}';
    }

    /**
     * Constructor for WOFIRandom. It generates a new unique player ID by
     * calling AllGameRecord and assigns it to this instance.
     */
    public WOFIRandom(){
        AllGameRecord.generateNewPlayerId();
        this.playerId = AllGameRecord.getPlayerId();
    }

    /**
     * Generates a random letter that has not been guessed before. Displays
     * the list of previous guesses and the hidden phrase, then selects a new
     * letter randomly.
     *
     * @param previousGuesses a StringBuilder containing letters that have already been guessed
     * @return a randomly generated letter as a String that has not been guessed before
     */
    @Override
    public String getGuess(StringBuilder previousGuesses) {
        while (true) {
            System.out.println("You have already guessed these letters: " + previousGuesses);
            System.out.println(WOFAI.getHiddenPhrase());
            System.out.println("Guess a Letter");

            Random random = new Random();
            char randomLowercaseLetter = (char) (random.nextInt(26) + 97); // Generates a random lowercase letter
            String letter = Character.toString(randomLowercaseLetter);

            if (previousGuesses.indexOf(letter) != -1) {
                System.out.println("You already guessed that letter");
                System.out.println();
                continue;
            }
            previousGuesses.append(letter);
            return letter;
        }
    }

    /**
     * Retrieves the player ID for this instance.
     *
     * @return the unique player ID
     */
    @Override
    public int playerId() {
        return this.playerId;
    }

    /**
     * Resets the player ID by generating a new unique ID.
     */
    @Override
    public void reset() {
        playerId = AllGameRecord.generateNewPlayerId();
    }
}




