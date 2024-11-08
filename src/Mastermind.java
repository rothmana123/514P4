import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Mastermind extends GuessingGame{
    private static final char[] COLORS = {'R', 'G', 'B', 'Y', 'O', 'P'};
    private static final int CODE_LENGTH = 4;
    //private int codeLength;
    public static int maxAttempts;
    private static boolean allowDuplicates = false;

    //private static List<Character> secretCode;
    public StringBuilder secretCode;
    private StringBuilder previousGuesses;

    private static Scanner scanner = new Scanner(System.in);

    private AllGameRecord allGameRecords;
    private int playerId;

    public Mastermind(AllGameRecord allGameRecords){
        this.allGameRecords = allGameRecords;
        this.playerId = AllGameRecord.generateNewPlayerId();

        //create the phrase
        this.secretCode = randomPhrase();
        this.maxAttempts = 10;
        this.previousGuesses = new StringBuilder("");
    }

    private int playerId() {
        return this.playerId;
    }

    @Override
    public boolean playNext() {
        System.out.print("Would you like to play another game? (y/n): ");
        String response = scanner.nextLine().trim().toLowerCase();
        return response.equals("y");
    }

    @Override
    public void play() {
        String guess;
        int exactMatches = 0;
        int partialMatches = 0;
        System.out.println(secretCode);
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

    public boolean checkWin(int exactMatches, int partialMatches){
        if (exactMatches == CODE_LENGTH) {
            return true;
        } else {
            System.out.println("Feedback: " + exactMatches + " exact, " + partialMatches + " partial.");
            return false;
        }
    }

    private void recordGame(boolean won) {
        int score = calculateScore(won);
        GameRecord gameRecord = new GameRecord(score, playerId);
        System.out.println(gameRecord.score);
        allGameRecords.add(gameRecord);
        System.out.println("Game recorded with score: " + score);
    }

    private int calculateScore(boolean won) {
        return won ? maxAttempts * 10 : 0;
    }

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
                    if (guess.charAt(i) == secretCode.charAt(j)) {
                        partialMatches++;
//                        secretCodeFlags[j] = true;
                        break;
                    }
                }
            }
        return new int[]{exactMatches, partialMatches};
    }

    public void reset(){
        // Reset fields for a new game
        this.secretCode = randomPhrase();
        this.maxAttempts = 10;
        //this.previousGuesses.setLength(0); // Clear previous guesses
    }

    @Override
    public AllGameRecord playAll() {
        while (true) {
            play();
            if (playNext()) {
                System.out.print("Continue as same Player? (y/n): ");
                String response = scanner.nextLine().trim().toLowerCase();
                if(response.equals("n")){
                    this.playerId = AllGameRecord.generateNewPlayerId();
                    //System.out.println("checking to see if playerId changed " + this.playerId);
                }
                reset();
            } else {
                break;
            }
        }
        //should make a diagram of how all the methods modify allGameRecords to track data flow
        return allGameRecords;
    }

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

    //not relevant to this game
    @Override
    public StringBuilder generateHiddenPhrase(StringBuilder phrase) {
        return null;
    }

    public static void main(String[] args) {
        AllGameRecord allGameRecords = new AllGameRecord();
        Mastermind game = new Mastermind(allGameRecords);
        //System.out.println(game.secretCode);

        game.playAll();

        // Print all game records for verification
        System.out.println("All games played:");
        for(GameRecord record : allGameRecords.listOfGameRecords) {
            System.out.println("Player ID: " + record.playerId + ", Score: " + record.score);
        }
    }
}
