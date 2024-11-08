import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * The WOFUserGame class represents a user-interactive version of a Wheel of Fortune-style game.
 * This class allows the user to guess letters to reveal a hidden phrase, while tracking their
 * score, guesses, and game results.
 *
 * <p>The class provides game mechanics such as loading phrases, generating hidden phrases,
 * processing guesses, and storing game records for each playthrough.</p>
 */
public class WOFUserGame extends WOFAbstractClass {
    private StringBuilder phrase;
    private StringBuilder hiddenPhrase;
    private int wrongAnswers;
    private static Scanner scanner = new Scanner(System.in);
    private StringBuilder previousGuesses;
    private List<String> phrases;
    private AllGameRecord allGameRecords;
    private int playerId;

    /**
     * Constructor for WOFUserGame that initializes the game with a new player ID,
     * loads phrases, and sets up the initial game state.
     *
     * @param allGameRecords the AllGameRecord instance that stores game records
     */
    public WOFUserGame(AllGameRecord allGameRecords) {
        this.playerId = playerId(); // Retrieves player ID
        this.allGameRecords = allGameRecords;
        this.phrases = readPhrases();
        this.phrase = randomPhrase();
        this.hiddenPhrase = generateHiddenPhrase(phrase);
        this.wrongAnswers = 5;
        this.previousGuesses = new StringBuilder("");
    }

    /**
     * Retrieves the current player ID from AllGameRecord.
     *
     * @return the player ID as an integer
     */
    public int playerId() {
        return AllGameRecord.getPlayerId();
    }

    /**
     * Resets the game state to allow the player to start a new game.
     * Initializes a new phrase, resets wrong answers, and clears previous guesses.
     */
    public void reset() {
        this.phrase = randomPhrase();
        this.hiddenPhrase = generateHiddenPhrase(phrase);
        this.wrongAnswers = 5;
        this.previousGuesses.setLength(0); // Clears previous guesses
    }

    /**
     * Runs a single game round, allowing the user to make guesses until they win or lose.
     * Records the game result based on whether the user successfully guesses the phrase.
     */
    @Override
    public void play() {
        while (true) {
            System.out.println(hiddenPhrase.toString());
            String guess = this.getGuess(previousGuesses);
            System.out.println("Player guessed " + guess);
            processGuess(guess);

            if (checkWin()) {
                System.out.println("You won!");
                recordGame(true);
                break;
            } else if (wrongAnswers <= 0) {
                System.out.println("You lost!");
                recordGame(false);
                break;
            }
        }
    }

    /**
     * Records the game result in AllGameRecord with a calculated score.
     *
     * @param won true if the player won, false if the player lost
     */
    private void recordGame(boolean won) {
        int score = calculateScore(won);
        GameRecord gameRecord = new GameRecord(score, playerId);
        allGameRecords.add(gameRecord);
        System.out.println("Game recorded with score: " + score);
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
                System.out.print("Continue as the same player? (y/n): ");
                String response = scanner.nextLine().trim().toLowerCase();
                if (response.equals("n")) {
                    this.playerId = AllGameRecord.generateNewPlayerId();
                    System.out.println("New player ID assigned: " + this.playerId);
                }
                reset();
            } else {
                break;
            }
        }
        return allGameRecords;
    }

    /**
     * Asks the user if they would like to play another game.
     *
     * @return true if the user wants to play another game, false otherwise
     */
    @Override
    public boolean playNext() {
        System.out.print("Would you like to play another game? (y/n): ");
        String response = scanner.nextLine().trim().toLowerCase();
        return response.equals("y");
    }

    /**
     * Selects a random phrase from the list of available phrases and removes it from the list.
     *
     * @return the randomly selected phrase as a StringBuilder
     */
    @Override
    public StringBuilder randomPhrase() {
        if (phrases.isEmpty()) {
            System.out.println("No more phrases available!");
            return null;
        }

        Random random = new Random();
        int index = random.nextInt(phrases.size());
        String selectedPhrase = phrases.remove(index); // Select and remove the phrase
        return new StringBuilder(selectedPhrase);
    }

    /**
     * Reads phrases from an external file ("phrases.txt") to use in the game.
     *
     * @return a List of phrases as strings
     */
    @Override
    public List<String> readPhrases() {
        List<String> phraseList = new ArrayList<>();
        try {
            phraseList = Files.readAllLines(Paths.get("phrases.txt"));
        } catch (IOException e) {
            System.out.println(e);
        }
        return phraseList;
    }

    /**
     * Generates a hidden version of the phrase, replacing each character with an asterisk, except for spaces.
     *
     * @param phrase the original phrase to conceal
     * @return the concealed phrase as a StringBuilder
     */
    @Override
    public StringBuilder generateHiddenPhrase(StringBuilder phrase) {
        StringBuilder hiddenPhrase = new StringBuilder();
        for (int i = 0; i < phrase.length(); i++) {
            hiddenPhrase.append(phrase.charAt(i) == ' ' ? ' ' : "*");
        }
        return hiddenPhrase;
    }

    /**
     * Processes the player's guess, updating the hidden phrase if the guessed letter is present
     * or decrementing wrong answers if not.
     *
     * @param guess the guessed letter as a string
     */
    public void processGuess(String guess) {
        String checkLetter = guess.substring(0, 1);

        if (phrase.indexOf(checkLetter) != -1) {
            for (int i = 0; i < phrase.length(); i++) {
                if (checkLetter.charAt(0) == phrase.charAt(i)) {
                    hiddenPhrase.replace(i, i + 1, checkLetter);
                }
            }
        } else {
            wrongAnswers--;
            System.out.println("Nope. Wrong Answers left: " + wrongAnswers);
        }
    }

    /**
     * Retrieves a letter guess from the player, ensuring it is a valid, single, unused letter.
     *
     * @param previousGuesses a StringBuilder containing letters that have already been guessed
     * @return the guessed letter as a string
     */
    @Override
    public String getGuess(StringBuilder previousGuesses) {
        while (true) {
            System.out.println("Guess a Letter");
            String guessString = scanner.nextLine().toLowerCase();

            if (guessString.length() != 1 || !Character.isLetter(guessString.charAt(0)) || previousGuesses.indexOf(guessString) != -1) {
                System.out.println("Invalid guess. Please enter a single unused letter.");
                continue;
            }

            previousGuesses.append(guessString);
            return guessString;
        }
    }

    /**
     * Calculates the score based on the game outcome and remaining wrong answers.
     *
     * @param won true if the player won, false if the player lost
     * @return the calculated score as an integer
     */
    private int calculateScore(boolean won) {
        return won ? wrongAnswers * 10 : 0;
    }

    /**
     * Checks whether the player has successfully guessed the phrase.
     *
     * @return true if the player has won, false otherwise
     */
    public Boolean checkWin() {
        return phrase.toString().equals(hiddenPhrase.toString());
    }

    /**
     * Main method to run the WOFUserGame. Initializes the game and plays all rounds.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        AllGameRecord allGames = new AllGameRecord();
        WOFUserGame game = new WOFUserGame(allGames);
        game.playAll();

        System.out.println("All games played:");
        for (GameRecord record : allGames.listOfGameRecords) {
            System.out.println("Player ID: " + record.playerId + ", Score: " + record.score);
        }
    }
}
