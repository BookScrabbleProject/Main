package Model;

import Model.gameClasses.Player;
import Model.gameClasses.Tile;

import java.util.*;

public class GuestModel extends PlayerModel implements Observer {
    // start to write the guest model-Benda
    // commit to jira
    ClientCommunication clientCommunication;
    int numOfTileInBag;
    Character[][] currentBoard;
    HashMap<Integer, Integer> scoreMap;
    HashMap<Integer, Integer> numberOfTilesMap;
    HashMap<Integer, String> playersNameMap;


    GuestModel(String ip, int port, String name) { // build a model for a player that logged in, with the port and ip address of the host server.
        myPlayer=new Player(name);
        scoreMap = new HashMap<>();
        numberOfTilesMap = new HashMap<>();
        playersNameMap = new HashMap<>();
        numOfTileInBag = 0;
        currentBoard = new Character[15][15];
        for (int i = 0; i < 15; i++) {
            Arrays.fill(currentBoard[i], '_');
        }
        clientCommunication = new ClientCommunication(ip, port);
    }

    @Override
    public void tryPlaceWord(String word, int col, int row, boolean isVertical) { //sends to the host the request of the player to try and place a new word on the board
        String vertical = "0";
        if (isVertical)
            vertical = "1";

        String methodName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        clientCommunication.send(this.myPlayer.getId(), methodName, word, String.valueOf(col), String.valueOf(row), vertical);
    }


    @Override
    public void challenge(String word) { // sends to the host the request of the player to challenge the word
        String methodName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        clientCommunication.send(this.myPlayer.getId(), methodName, word);
    }

    @Override
    public void takeTileFromBag() { // takes a tile from the bag and updates the tiles number in the bag
        String methodName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        clientCommunication.send(this.myPlayer.getId(), methodName);
    }

    @Override
    public void setBoardStatus(Character[][] board) {// need to implement
        currentBoard = board;
    }

    @Override
    public Character[][] getBoardStatus() { // need to implement
        return currentBoard;
    }

    @Override
    public int getNumberOfTilesInBag() {
        return numOfTileInBag;

    } // returns the number of tiles currently in the bag

    @Override
    public HashMap<Integer, Integer> getPlayersScores() { // map from player id to the player's scores
        return scoreMap;
    }

    @Override
    public HashMap<Integer, Integer> getPlayersNumberOfTiles() { //map from players id to the number of tiles that the player has in his hand
        return numberOfTilesMap;
    }

    public Character[][] stringToBoard(String s) {
        Character[][] updatedBoard = new Character[15][15];
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                updatedBoard[i][j] = s.charAt((15 * i) + j);
            }
        }
        return updatedBoard;
    }

    @Override
    public List<Character> getMyHand() {
        return myPlayer.getTiles();
    }

    @Override
    public void update(Observable o, Object arg) { // are the notifyObservers messages ok?
        String argString = (String) arg;
        String[] splitedArgString = argString.split(":");
        int id = Integer.parseInt(splitedArgString[0]);
        String methodName = splitedArgString[1];
        String[] arguments = splitedArgString[2].split(",");
        switch (methodName) {
            case "tryPlaceWord", "challenge", "startGame" -> {// notify with id,success/fail,list of the words-args
                setChanged();
                notifyObservers(arg);
            }
            case "boardUpdated" -> {
                currentBoard = stringToBoard(arguments[0]);
                setChanged();
                notifyObservers("boardUpdated");
            }
            case "scoreUpdated" -> {
                scoreMap.put(id, Integer.parseInt(arguments[0]));
                setChanged();
                notifyObservers("scoreUpdated");
            }
            case "numOfTilesUpdated" -> {
                numberOfTilesMap.put(id, Integer.parseInt(arguments[0]));
                setChanged();
                notifyObservers("numOfTilesUpdated");
            }
            case "setHand" -> { //
                List<Character> newHand = new ArrayList<>();
                for (int i = 0; i < arguments[0].length(); i++)
                    newHand.add((Character) arguments[0].charAt(i));
                this.myPlayer.setHand(newHand);
                setChanged();
                notifyObservers("setHand");
            }
            case "newPlayerTurn" -> {
                setCurrentPlayerIndex(Integer.parseInt(arguments[0]); setChanged();
                notifyObservers("newPlayerTurn");
            } case "setId" -> {
                myPlayer.setId(Integer.parseInt(arguments[0]));
                setChanged();
                notifyObservers("setId");
            }
            case "playersListUpdated" -> {
                String[] newPlayer = arguments[0].split("-");
                for (String player : arguments) {
                    int playerId = Integer.parseInt(player.split("-")[0]);
                    String playerName = player.split("-")[1];
                    scoreMap.put(playerId, 0);
                    numberOfTilesMap.put(playerId, 0);// either 0 or 7 depends if we create the hand before the game started.
                    playersNameMap.put(playerId, playerName);
                }
                setChanged();
                notifyObservers("playersListUpdated:" + newPlayer[1]);
            }
        }
    }
}
