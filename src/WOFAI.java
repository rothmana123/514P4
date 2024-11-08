import java.util.List;
import java.util.Scanner;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * The WOFAI class extends WOFAbstractClass to implement a Wheel of Fortune-style AI game.
 * This class manages various AI strategies for guessing, tracks the game state, and records scores.
 *
 * <p>The WOFAI class provides constructors to initialize different AI players or player lists,
 * allowing for customized gameplay configurations.</p>
 */
public class WOFAI extends WOFAbstractClass {
    private StringBuilder phrase;
    private static StringBuilder hiddenPhrase;
    private int wrongAnswers;
    private static Scanner scanner = new Scanner(System.in);
    private StringBuilder previousGuesses;
    private List<String> phrases;
    private AllGameRecord allGameRecords;
    private int playerId;
    private WOFInterface player;
    public static int index = 0;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WOFAI wofai)) return false;
        return playerId == wofai.playerId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(playerId);
    }

    @Override
    public String toString() {
        return "WOFAI{" +
                "phrase=" + phrase +
                ", wrongAnswers=" + wrongAnswers +
                ", previousGuesses=" + previousGuesses +
                ", phrases=" + phrases +
                ", allGameRecords=" + allGameRecords +
                ", playerId=" + playerId +
                ", player=" + player +
                ", WOFPlayers=" + WOFPlayers +
                '}';
    }

    private ArrayList<WOFInterface> WOFPlayers;

    /**
     * Default constructor that initializes a WOFAI game with a random AI player.
     *
     * @param allGameRecords the AllGameRecord instance that manages all game records
     */
    public WOFAI(AllGameRecord allGameRecords) {
        System.out.println("This is the default implementation with WOFI Random");
        this.player = new WOFIRandom();
        this.phrases = readPhrases();
        resetGame();
        this.playerId = player.playerId();
        this.allGameRecords = allGameRecords;
        System.out.println("The default playerId is " + playerId);
        playAll(player);
    }

    /**
     * Constructor that allows the user to choose a specific AI player.
     *
     * @param allGameRecords the AllGameRecord instance that manages all game records
     * @param player the chosen WOFInterface player
     */
    public WOFAI(AllGameRecord allGameRecords, WOFInterface player) {
        System.out.println("This implementation allows user to choose Concrete WOF Interface Implementation");
        this.player = player;
        this.phrases = readPhrases();
        resetGame();
        this.playerId = player.playerId();
        this.allGameRecords = allGameRecords;
        System.out.println("The default playerId is " + playerId);
        playAll(player);
    }

    /**
     * Constructor that allows the user to initialize a list of AI players.
     * Each AI player will play all available phrases, generating unique records.
     *
     * @param allGameRecords the AllGameRecord instance that manages all game records
     * @param WOFPlayers the list of AI players implementing WOFInterface
     */
    public WOFAI(AllGameRecord allGameRecords, ArrayList<WOFInterface> WOFPlayers) {
        System.out.println("This implementation runs a list of Concrete WOF Interface Implementations");
        this.WOFPlayers = WOFPlayers;
        this.phrases = readPhrases();
        resetGame();
        this.allGameRecords = allGameRecords;

        for (WOFInterface player : WOFPlayers) {
            this.playerId = player.playerId();
            playAll(player); // Play all games for the current player
            this.phrases = readPhrases(); // Reload phrases for the next player
            resetGame();
        }
    }

    /**
     * Resets the game state, initializing a new phrase, hidden phrase, and resetting wrong answers and previous guesses.
     */
    public void resetGame() {
        this.phrase = randomPhrase();
        this.hiddenPhrase = generateHiddenPhrase(phrase);
        this.wrongAnswers = 10;
        this.previousGuesses = new StringBuilder("");
    }

    /**
     * Plays the game with the specified AI player and resets the game for each new phrase.
     *
     * @param player the AI player to play the game
     * @return AllGameRecord containing all game records
     */
    public AllGameRecord playAll(WOFInterface player) {
        while (true) {
            System.out.println("Current playerId: " + playerId);
            play(player);
            if (playNext()) {
                resetGame();
            } else {
                break;
            }
        }
        return allGameRecords;
    }

    /**
     * Plays a single game for the provided AI player, handling guessing, win/loss conditions, and score recording.
     *
     * @param player the AI player to play the game
     */
    public void play(WOFInterface player) {
        WOFAI.index = 0;
        System.out.println(hiddenPhrase.toString());
        while (true) {
            String guess = player.getGuess(previousGuesses);
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
     * Checks whether the player has successfully guessed the phrase.
     *
     * @return true if the player has won, false otherwise
     */
    public Boolean checkWin() {
        return phrase.toString().equals(hiddenPhrase.toString());
    }

    /**
     * Records the game result, storing it in AllGameRecord with a calculated score based on game outcome.
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
     * Calculates the score for the game based on whether the player won.
     *
     * @param won true if the player won, false if the player lost
     * @return the calculated score as an integer
     */
    private int calculateScore(boolean won) {
        return won ? wrongAnswers * 10 : 0;
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
     * Selects a random phrase from the list of phrases and removes it from the list.
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
        String selectedPhrase = phrases.remove(index);
        return new StringBuilder(selectedPhrase);
    }

    /**
     * Generates a hidden version of the phrase, replacing each character with an asterisk, except for spaces.
     *
     * @param phrase the original phrase to conceal
     * @return the concealed phrase as a StringBuilder
     */
    public StringBuilder generateHiddenPhrase(StringBuilder phrase) {
        StringBuilder hiddenPhrase = new StringBuilder();
        for (int i = 0; i < phrase.length(); i++) {
            hiddenPhrase.append(phrase.charAt(i) == ' ' ? ' ' : "*");
        }
        return hiddenPhrase;
    }

    /**
     * Processes the player's guess, updating the hidden phrase if the guessed letter is present or decrementing wrong answers if not.
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
     * Gets a letter guess from the player, ensuring it hasn’t been guessed before and is a valid letter.
     *
     * @param previousGuesses a StringBuilder containing letters that have already been guessed
     * @return the guessed letter as a string
     */
    @Override
    public String getGuess(StringBuilder previousGuesses) {
        while (true) {
            System.out.println("You have already guessed these letters: " + previousGuesses);
            System.out.println(hiddenPhrase);
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
     * Retrieves the current hidden phrase.
     *
     * @return the hidden phrase as a StringBuilder
     */
    public static StringBuilder getHiddenPhrase() {
        return hiddenPhrase;
    }

 @Override
    public AllGameRecord playAll() {
        return null;
    }

    //not going to use bc I need to pass in the WOFI object
    @Override
    public void play() {
    }

    //Play Next for Default AI
    @Override
    public boolean playNext() {
        return phrases.size() > 0;
    }

    //Main method to launch the WOF Players

    public static void main(String[] args) {
        int gametype;
        Scanner gameTypeScanner = new Scanner(System.in);
        AllGameRecord allGames = new AllGameRecord();
        while (true) {
            System.out.println("Choose your AI Game:");
            System.out.println("0. for User WOF 1. for Default 2. to Choose 3. to auto-Play all 3 Players 4. Mastermind 5. Averages");
            gametype = gameTypeScanner.nextInt();
            if (gametype == 0){
                WOFUserGame game = new WOFUserGame(allGames);
                game.playAll();

                // Print all game records for verification
                System.out.println("All games played:");
                for(GameRecord record : allGames.listOfGameRecords) {
                    System.out.println("Player ID: " + record.playerId + ", Score: " + record.score);
                }
            }else if (gametype == 1) {
                //default player
                WOFAI game = new WOFAI(allGames);


                System.out.println("All games played:");
                for (GameRecord record : allGames.listOfGameRecords) {
                    System.out.println("Player ID: " + record.playerId + ", Score: " + record.score);
                }
            } else if (gametype == 2) {
                //user chooses player
                while (true) {
                    System.out.println("Choose a Player: 1. Random 2. Vowel First 3. Common Letters");
                    int choice = scanner.nextInt();
                    if (choice == 1) {
                        WOFIRandom random = new WOFIRandom();
                        WOFAI game1 = new WOFAI(allGames, random);
                        break;
                    } else if (choice == 2) {
                        WOFIVowelFirst vowel = new WOFIVowelFirst();
                        WOFAI game1 = new WOFAI(allGames, vowel);
                        break;
                    } else if (choice == 3) {
                        WOFICommon common = new WOFICommon();
                        WOFAI game1 = new WOFAI(allGames, common);
                        break;
                    } else {
                        System.out.println("Answer must be 1, 2 or 3");
                        System.out.println("Choose a Player: 1. Random 2. Vowel First 3. Common Letters 4. exit");
                    }
                }
                System.out.println("All games played:");
                for (GameRecord record : allGames.listOfGameRecords) {
                    System.out.println("Player ID: " + record.playerId + ", Score: " + record.score);
                }
            } else if (gametype == 3) {
                //Passing a list of player objects into constructor
                ArrayList<WOFInterface> wofIArray = new ArrayList<>();
                WOFIRandom random = new WOFIRandom();
                WOFICommon common = new WOFICommon();
                WOFIVowelFirst vowel = new WOFIVowelFirst();
                wofIArray.add(random);
                wofIArray.add(common);
                wofIArray.add(vowel);
                WOFAI game2 = new WOFAI(allGames, wofIArray);
                System.out.println("All games played:");
                for (GameRecord record : allGames.listOfGameRecords) {
                    System.out.println("Player ID: " + record.playerId + ", Score: " + record.score);
                }
                int totalPlayers = 3; // Assuming three players, each with three games

                System.out.println("High Scores for Each Player:");
                for (int player = 1; player <= totalPlayers; player++) {
                    System.out.println("Player " + player + " high scores: " + AllGameRecord.highGameListPlayer(player, allGames.listOfGameRecords, 3));
                }

// Display the average score across all nine games
                float overallAverage = AllGameRecord.average(allGames.listOfGameRecords);
                System.out.println("The average score across all nine games is: " + overallAverage);

// Display the average score for each player individually
                System.out.println("Average Scores for Each Player:");
                for (int player = 1; player <= totalPlayers; player++) {
                    float playerAverage = AllGameRecord.playerAverage(player);
                    System.out.println("The average score for Player " + player + " is: " + playerAverage);
                }
            } else if(gametype == 4) {
                Mastermind game = new Mastermind(allGames);
                //System.out.println(game.secretCode);

                game.playAll();

                // Print all game records for verification
                System.out.println("All games played:");
                for (GameRecord record : allGames.listOfGameRecords) {
                    System.out.println("Player ID: " + record.playerId + ", Score: " + record.score);
                }
            }
            else {
                System.out.println("The average of all scores is " + AllGameRecord.average(allGames.listOfGameRecords));
                int totalGames = AllGameRecord.getPlayerId();
                //int numberGames = totalGames/2;
                System.out.println("The 2 highest scores are " + AllGameRecord.highGameList(allGames.listOfGameRecords, 2));
                System.out.println("which player would you like to see the highest and average scores for 1-" + totalGames);
                int player = gameTypeScanner.nextInt();
                System.out.println("The average of all scores for Player " + player + " is " + AllGameRecord.playerAverage(player));
                System.out.println("The highest score for player " + player + " is " + AllGameRecord.highGameListPlayer(player, allGames.listOfGameRecords, 1));
            }
        }
    }
}


