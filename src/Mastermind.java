import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * The Mastermind class represents a command-line version of the Mastermind game.
 * In this game, the computer generates a secret color code, and the player attempts to guess
 * the exact colors and positions in the code within a limited number of attempts.
 *
 * <p>The class includes methods to generate the secret code, process guesses, provide feedback on
 * exact and partial matches, and record game results.</p>
 */
public class Mastermind extends GuessingGame {
    private static final char[] COLORS = {'R', 'G', 'B', 'Y', 'O', 'P'};
    private static final int CODE_LENGTH = 4;
    public static int maxAttempts;
    private static boolean allowDuplicates = false;

    public StringBuilder secretCode;
    private StringBuilder previousGuesses;
    private static Scanner scanner = new Scanner(System.in);
    private AllGameRecord allGameRecords;
    private int playerId;

    /**
     * Constructs a new Mastermind game instance, initializing a new player ID,
     * generating a secret code, and setting the maximum attempts.
     *
     * @param allGameRecords the AllGameRecord instance that stores game records
     */
    public Mastermind(AllGameRecord allGameRecords) {
        this.allGameRecords = allGameRecords;
        this.playerId = AllGameRecord.generateNewPlayerId();
        this.secretCode = randomPhrase();
        this.maxAttempts = 10;
        this.previousGuesses = new StringBuilder("");
    }

    /**
     * Retrieves the player's ID.
     *
     * @return the player's ID as an integer
     */
    private int playerId() {
        return this.playerId;
    }

    /**
     * Prompts the player to decide whether to play another game.
     *
     * @return true if the player wants to play another game, false otherwise
     */
    @Override
    public boolean playNext() {
        System.out.print("Would you like to play another game? (y/n): ");
        String response = scanner.nextLine().trim().toLowerCase();
        return response.equals("y");
    }

    /**
     * Runs a single game of Mastermind, allowing the player to make guesses until they win,
     * exhaust their attempts, or choose to quit. Records the game outcome based on win or loss.
     */
    @Override
    public void play() {
        String guess;
        int exactMatches;
        int partialMatches;
        System.out.println(secretCode);  // For testing purposes; remove in final version
        while (true) {
            guess = getGuess(previousGuesses);
            int[] result = processGuess(guess);
            exactMatches = result[0];
            partialMatches = result[1];
            if (checkWin(exactMatches, partialMatches)) {
                System.out.println("You won!");
                recordGame(true);
                break;
            } else if (maxAttempts == 0) {
                System.out.println("You lost!");
                recordGame(false);
                break;
            }
        }
    }

    /**
     * Checks if the player has won by guessing the exact positions and colors in the code.
     *
     * @param exactMatches the number of exact matches in the guess
     * @param partialMatches the number of partial matches in the guess
     * @return true if the player guessed the code correctly, false otherwise
     */
    public boolean checkWin(int exactMatches, int partialMatches) {
        if (exactMatches == CODE_LENGTH) {
            return true;
        } else {
            System.out.println("Feedback: " + exactMatches + " exact, " + partialMatches + " partial.");
            return false;
        }
    }

    /**
     * Records the outcome of the game, saving the score based on the player's performance.
     *
     * @param won true if the player won, false if they lost
     */
    private void recordGame(boolean won) {
        int score = calculateScore(won);
        GameRecord gameRecord = new GameRecord(score, playerId);
        allGameRecords.add(gameRecord);
        System.out.println("Game recorded with score: " + score);
    }

    /**
     * Calculates the score based on the player's remaining attempts if they won.
     *
     * @param won true if the player won, false otherwise
     * @return the calculated score as an integer
     */
    private int calculateScore(boolean won) {
        return won ? maxAttempts * 10 : 0;
    }

    /**
     * Generates a random secret code of specified length using available colors,
     * ensuring no duplicates if allowDuplicates is false.
     *
     * @return the randomly generated secret code as a StringBuilder
     */
    @Override
    public StringBuilder randomPhrase() {
        StringBuilder secretCode = new StringBuilder();
        Random random = new Random();

        while (secretCode.length() < CODE_LENGTH) {
            char color = COLORS[random.nextInt(COLORS.length)];
            if (allowDuplicates || secretCode.indexOf(String.valueOf(color)) == -1) {
                secretCode.append(color);
            }
        }
        return secretCode;
    }

    /**
     * Prompts the player to input a guess, ensuring the guess meets validation criteria.
     *
     * @param previousGuesses a StringBuilder containing letters that have already been guessed
     * @return the player's guess as a String
     */
    @Override
    public String getGuess(StringBuilder previousGuesses) {
        maxAttempts--;
        String guess;
        while (true) {
            System.out.println("Enter a guess of " + CODE_LENGTH + " colors (R, G, B, Y, O, P): ");
            guess = scanner.nextLine().toUpperCase();

            if (!isValidGuess(guess)) {
                System.out.println("Invalid guess, please enter a guess with " + CODE_LENGTH + " valid colors.");
                continue;
            }
            break;
        }
        return guess;
    }

    /**
     * Processes the player's guess, calculating the number of exact and partial matches.
     *
     * @param guess the player's guess
     * @return an array where the first element is the number of exact matches and the second element is the number of partial matches
     */
    private int[] processGuess(String guess) {
        int exactMatches = 0;
        int partialMatches = 0;

        // First pass: Find exact matches
        for (int i = 0; i < CODE_LENGTH; i++) {
            if (guess.charAt(i) == secretCode.charAt(i)) {
                exactMatches++;
            }
        }

        // Second pass: Find partial matches
        for (int i = 0; i < CODE_LENGTH; i++) {
            for (int j = 0; j < CODE_LENGTH; j++) {
                if (i != j && guess.charAt(i) == secretCode.charAt(j)) {
                    partialMatches++;
                    break;
                }
            }
        }
        return new int[]{exactMatches, partialMatches};
    }

    /**
     * Resets the game state to start a new game, including generating a new secret code
     * and resetting maxAttempts.
     */
    public void reset() {
        this.secretCode = randomPhrase();
        this.maxAttempts = 10;
    }

    /**
     * Plays multiple games in succession, allowing the user to reset as a new player if desired.
     *
     * @return the AllGameRecord containing all game records
     */
    @Override
    public AllGameRecord playAll() {
        while (true) {
            play();
            if (playNext()) {
                System.out.print("Continue as same Player? (y/n): ");
                String response = scanner.nextLine().trim().toLowerCase();
                if (response.equals("n")) {
                    this.playerId = AllGameRecord.generateNewPlayerId();
                }
                reset();
            } else {
                break;
            }
        }
        return allGameRecords;
    }

    /**
     * Validates the player's guess to ensure it matches the required code length and contains only allowed colors.
     *
     * @param guess the player's guess
     * @return true if the guess is valid, false otherwise
     */
    private static boolean isValidGuess(String guess) {
        if (guess.length() != CODE_LENGTH) return false;

        for (char c : guess.toCharArray()) {
            boolean isValidColor = false;
            for (char color : COLORS) {
                if (c == color) {
                    isValidColor = true;
                    break;
                }
            }
            if (!isValidColor) return false;
        }
        return true;
    }

    /**
     * This method is irrelevant to Mastermind and returns null. It is only implemented to fulfill the abstract class requirements.
     *
     * @param phrase the phrase to conceal (not used in Mastermind)
     * @return null since hidden phrases are not relevant in this game
     */
    @Override
    public StringBuilder generateHiddenPhrase(StringBuilder phrase) {
        return null;
    }

    /**
     * Main method to run the Mastermind game. Initializes the game, plays all rounds, and prints game records.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        AllGameRecord allGameRecords = new AllGameRecord();
        Mastermind game = new Mastermind(allGameRecords);
        game.playAll();

        System.out.println("All games played:");
        for (GameRecord record : allGameRecords.listOfGameRecords) {
            System.out.println("Player ID: " + record.playerId + ", Score: " + record.score);
        }
    }
}
