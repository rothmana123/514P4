/**
 * The AllGameRecord class is responsible for managing a list of game records.
 * It provides methods to add game records, generate unique player IDs,
 * calculate average scores, and retrieve top scores.
 *
 * <p>This class serves as a central storage and utility for processing
 * information about multiple games and players.</p>
 */

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

public class AllGameRecord {
    public static ArrayList<GameRecord> listOfGameRecords;
    public static int playerId = 0;

    @Override
    public String toString() {
        return "AllGameRecord{}";
    }


    //constructor
    public AllGameRecord(){
        listOfGameRecords = new ArrayList<GameRecord>();
    }

    //return current playerId
    //wait, is this necessary?
    //also in GameRecord...
    public static int getPlayerId(){
        return playerId;
    }

    //generate unique player Id
    public static int generateNewPlayerId() {
        playerId++;
        return playerId;
    }

    //add game to list of records
    public void add(GameRecord game){
        listOfGameRecords.add(game);
    }

    //
    public static float average(ArrayList<GameRecord> games){
        int sum = 0;
        int length = games.size();
        for(GameRecord game : games){
            sum+=game.score;
        }
        return (float) sum /length;
    }

    public static float playerAverage(int playerId){
        ArrayList<GameRecord> gamesByPlayerID = new ArrayList<>();
        for(GameRecord game : listOfGameRecords){
            if(game.playerId == playerId){
                gamesByPlayerID.add(game);
            }
        }
        return average(gamesByPlayerID);
    }

    public static ArrayList<GameRecord> highGameList(ArrayList<GameRecord> arrayOfGames, int n){
        //Collections.sort(arrayOfGames);
        arrayOfGames.sort((game1, game2) -> Integer.compare(game2.score, game1.score));

        // Ensures we don't exceed the list size if n is greater than arrayOfGames size
        n = Math.min(n, arrayOfGames.size());
        return new ArrayList<>(arrayOfGames.subList(0, n));
        //return arrayOfGames;
    }

    public static ArrayList<GameRecord> highGameListPlayer(int playerId, ArrayList<GameRecord> arrayOfGames, int n){
        ArrayList<GameRecord> gamesByPlayerID = new ArrayList<>();
        for(GameRecord game : arrayOfGames){
            if(game.playerId == playerId){
                gamesByPlayerID.add(game);
            }
        }
        return highGameList(gamesByPlayerID, n);
    }



}
