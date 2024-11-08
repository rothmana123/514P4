abstract class GuessingGame extends Game{
    public abstract StringBuilder randomPhrase();
    public abstract StringBuilder generateHiddenPhrase(StringBuilder phrase);
    //public abstract void processGuess(String guess);
    public abstract String getGuess(StringBuilder previousGuesses);
}
