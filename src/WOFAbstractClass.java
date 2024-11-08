import java.util.ArrayList;
import java.util.List;

abstract class WOFAbstractClass extends GuessingGame {
    //doesnt need to implement WOFI
    public abstract List readPhrases();

    public abstract StringBuilder randomPhrase();
    public abstract StringBuilder generateHiddenPhrase(StringBuilder phrase);
    public abstract void processGuess(String guess);

    public abstract String getGuess(StringBuilder previousGuesses);

}

