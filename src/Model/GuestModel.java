package Model;

import Model.gameClasses.Player;

import java.util.*;

/**
 * Guest model class
 */
public class GuestModel extends PlayerModel implements Observer {

    ClientCommunication clientCommunication;
    int numOfTileInBag;
    Character[][] currentBoard;
    HashMap<Integer, Integer> scoreMap;
    HashMap<Integer, Integer> numberOfTilesMap;

    HashMap<Integer, String> playersNameMap;

    /**
     * build a model for the guest and connect to the host server using clientCommunication
     * @param ip= ip of the host server
     * @param port= port of the host server
     * @param name= name of the player
     */
    public GuestModel(String ip, int port, String name) {
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
        clientCommunication.addObserver(this);
    }

    /**
     * will ask from the server to put a word on the board
     * @param word- string of the word that the user is trying to put on the board
     * @param col- index of the starting column of the word
     * @param row- index of the starting row of the word
     * @param isVertical- a boolean indicating if the word is vertical(true) or horizontal(false)
     */

    @Override
    public void tryPlaceWord(String word, int col, int row, boolean isVertical) {
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

    /**
     * will ask from the server to challenge the word
     * @param word- the word that will be sent to the server
     */
    @Override
    public void challenge(String word) {
        String methodName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        clientCommunication.send(this.myPlayer.getId(), methodName, word);
    }

    /**
     * takeTileFromBag function that will ask from the server to take a tile from the bag
     */
    @Override
    public void takeTileFromBag() {
        String methodName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        clientCommunication.send(this.myPlayer.getId(), methodName);
    }

    /**
     * will update the board status
     * @param board- represent the updated state of the board
     */

    public void setBoardStatus(Character[][] board) {// need to implement
        currentBoard = board;
    }

    /**
     *
     * @return - the most updated state of the board
     */
    @Override
    public Character[][] getBoardStatus() { // need to implement
        return currentBoard;
    }

    /**
     *
     * @return - the current number of tiles in the bag
     */
    @Override
    public int getNumberOfTilesInBag() {
        return numOfTileInBag;

    }

    /**
     *
     * @return - a map from a player's id to the player's score
     */
    @Override
    public HashMap<Integer, Integer> getPlayersScores() { // map from player id to the player's scores
        return scoreMap;
    }

    public ClientCommunication getClientCommunication() {
        return clientCommunication;
    }

    public HashMap<Integer, String> getPlayersNameMap() {
        return playersNameMap;
    }

    /**
     *
     * @return - a map from a player's id to the player's score
     */
    @Override
    public HashMap<Integer, Integer> getPlayersNumberOfTiles() { //map from players id to the number of tiles that the player has in his hand
        return numberOfTilesMap;
    }

    /**
     *
     * @param s - a string that represents the current state of the board
     * @return - a matrix of characters that represent the "s" string
     */
    public Character[][] stringToCharacterMatrix(String s) {
        Character[][] updatedBoard = new Character[15][15];
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                updatedBoard[i][j] = s.charAt((15 * i) + j);
            }
        }
        return updatedBoard;
    }

    /**
     *
     * @return - the hand of the player
     */
    @Override
    public List<Character> getMyHand() {
        return myPlayer.getTiles();
    }

    /**
     * receive updates from the observable object
     * @Details cases: tryPlaceWord, challenge, startGame, boardUpdated, scoreUpdated, numOfTilesUpdated, setHand, newPlayerTurn, setId, playersListUpdated
     * @param o     the observable object
     * @param arg   an argument passed to the {@code notifyObservers}
     *                 method.
     */
    @Override
    public void update(Observable o, Object arg) {
        String argString = (String) arg;
        String[] splitedArgString = argString.split(":");
        int id = Integer.parseInt(splitedArgString[0]);
        String methodName = splitedArgString[1];
        String[] arguments = splitedArgString[2].split(",");
        switch (methodName) {
            case "tryPlaceWord", "challenge" :
                setChanged();
                notifyObservers(methodName+":"+splitedArgString[2]);

            case "boardUpdated" :
                setBoardStatus(stringToCharacterMatrix(arguments[0]));
                setChanged();
                notifyObservers("boardUpdated");

            case "scoreUpdated" :
                scoreMap.put(id, Integer.parseInt(arguments[0]));
                setChanged();
                notifyObservers("scoreUpdated");

            case "tilesUpdated" :
                numberOfTilesMap.put(id, Integer.parseInt(arguments[0]));
                setChanged();
                notifyObservers("tilesUpdated");

            case "setHand" :
                List<Character> newHand = new ArrayList<>();
                for (int i = 0; i < arguments[0].length(); i++)
                    newHand.add((Character) arguments[0].charAt(i));
                this.myPlayer.setHand(newHand);
                setChanged();
                notifyObservers("setHand");

            case "newPlayerTurn" :
                setCurrentPlayerId(Integer.parseInt(arguments[0]));
                setChanged();
                notifyObservers("newPlayerTurn");
             case "setId" :
                myPlayer.setId(Integer.parseInt(arguments[0]));
                setChanged();
                notifyObservers(methodName+":"+arguments[0]);

            case "playersListUpdated" :
                for (String player : arguments) {
                    int playerId = Integer.parseInt(player.split("-")[0]);
                    String playerName = player.split("-")[1];
                    scoreMap.put(playerId, 0);
                    numberOfTilesMap.put(playerId, 0);
                    playersNameMap.put(playerId, playerName);
                    }
                setChanged();
                notifyObservers(methodName+":"+splitedArgString[2]);

            case "tilesInBagUpdated" :
                numOfTileInBag=Integer.parseInt(arguments[0]);
                setChanged();
                notifyObservers("tilesInBagUpdated");
            case "tilesWithScores":
                setChanged();
                notifyObservers(methodName+":"+splitedArgString[2]);
            case "startGame":
                break;
        }
    }
}
