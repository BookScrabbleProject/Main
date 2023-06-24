package Model;

import General.MethodsNames;
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
    private String hostServerIp;
    private int hostServerPort;

    /**
     * build a model for the guest and connect to the host server using clientCommunication
     *
     * @param ip=   ip of the host server
     * @param port= port of the host server
     * @param name= name of the player
     */
    public GuestModel(String ip, int port, String name) {
        myPlayer = new Player(name);
        scoreMap = new HashMap<>();
        numberOfTilesMap = new HashMap<>();
        playersNameMap = new HashMap<>();
        numOfTileInBag = 0;
        currentBoard = new Character[15][15];
        hostServerIp = ip;
        hostServerPort = port;
        for (int i = 0; i < 15; i++) {
            Arrays.fill(currentBoard[i], '_');
        }
        clientCommunication = new ClientCommunication();
        clientCommunication.addObserver(this);
//        clientCommunication = new ClientCommunication(ip, port);
//        clientCommunication.addObserver(this);
    }

    @Override
    public void startGame() {

    }

    public void connectToHostServer() {
        clientCommunication.connect(hostServerIp, hostServerPort, myPlayer.getName());
    }

    @Override
    public void closeConnection() {
        clientCommunication.close();
    }

    /**
     * will ask from the server to put a word on the board
     *
     * @param word-       string of the word that the user is trying to put on the board
     * @param col-        index of the starting column of the word
     * @param row-        index of the starting row of the word
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
     *
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
        clientCommunication.send(this.myPlayer.getId(), MethodsNames.TAKE_TILE_FROM_BAG);
    }

    /**
     * @return - the most updated state of the board
     */
    @Override
    public Character[][] getBoardStatus() { // need to implement
        return currentBoard;
    }

    /**
     * will update the board status
     *
     * @param board- represent the updated state of the board
     */

    public void setBoardStatus(Character[][] board) {// need to implement
        currentBoard = board;
    }

    /**
     * @return - the current number of tiles in the bag
     */
    @Override
    public int getNumberOfTilesInBag() {
        return numOfTileInBag;

    }

    /**
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
     * @return - a map from a player's id to the player's score
     */
    @Override
    public HashMap<Integer, Integer> getPlayersNumberOfTiles() { //map from players id to the number of tiles that the player has in his hand
        return numberOfTilesMap;
    }

    /**
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
     * @return - the hand of the player
     */
    @Override
    public List<Character> getMyHand() {
        return myPlayer.getTiles();
    }

    /**
     * receive updates from the observable object
     *
     * @param o   the observable object
     * @param arg an argument passed to the {@code notifyObservers}
     *            method.
     * @Details cases: tryPlaceWord, challenge, startGame, boardUpdated, scoreUpdated, tilesUpdated, setHand, newPlayerTurn, setId, playersListUpdated,tilesInBagUpdated,tilesWithScores
     */
    @Override
    public void update(Observable o, Object arg) {
        String argString = (String) arg;
        Queue<String> messagesQ = new LinkedList<>(Arrays.asList(argString.split("\n")));

        while (!messagesQ.isEmpty()) {
            argString = messagesQ.poll();
            String[] splitedArgString = argString.split(":");
            int id = Integer.parseInt(splitedArgString[0]);
            String methodName = splitedArgString[1];
            String[] arguments = null;
            try {
                arguments = splitedArgString[2].split(",");
            } catch (Exception e) {
//                System.out.println(">>>> GM update: try to split arguments from splitedArgString[2] but there are no arguments\nmsg: " + argString + " <<<");
            }


            switch (methodName) {
                case MethodsNames.TRY_PLACE_WORD, MethodsNames.CHALLENGE:
                    setChanged();
                    notifyObservers(methodName + ":" + splitedArgString[2]);
                    break;

                case MethodsNames.BOARD_UPDATED:
                    setBoardStatus(stringToCharacterMatrix(arguments[0]));
                    setChanged();
                    notifyObservers(MethodsNames.BOARD_UPDATED);
                    break;

                case MethodsNames.SCORE_UPDATED:
                    scoreMap.put(id, Integer.parseInt(arguments[0]));
                    setChanged();
                    notifyObservers(MethodsNames.SCORE_UPDATED);
                    break;

                case MethodsNames.NUM_OF_TILES_UPDATED:
                    numberOfTilesMap.put(id, Integer.parseInt(arguments[0]));
                    setChanged();
                    notifyObservers(MethodsNames.NUM_OF_TILES_UPDATED);
                    break;

                case MethodsNames.SET_HAND:
                    List<Character> newHand = new ArrayList<>();
                    for (int i = 0; i < arguments[0].length(); i++)
                        if (arguments[0].charAt(i) != '_')
                            newHand.add((Character) arguments[0].charAt(i));
                    this.myPlayer.setHand(newHand);
                    setChanged();
                    notifyObservers(MethodsNames.SET_HAND);
                    break;

                case MethodsNames.NEW_PLAYER_TURN:
                    setCurrentPlayerId(Integer.parseInt(arguments[0]));
                    setChanged();
                    notifyObservers(MethodsNames.NEW_PLAYER_TURN);
                    break;

                case MethodsNames.SET_ID:
                    myPlayer.setId(Integer.parseInt(arguments[0]));
                    setChanged();
                    notifyObservers(MethodsNames.SET_ID + ":" + arguments[0]);
                    break;

                case MethodsNames.PLAYERS_LIST_UPDATED:
                    for (String player : arguments) {
                        int playerId = Integer.parseInt(player.split("-")[0]);
                        String playerName = player.split("-")[1];
                        scoreMap.put(playerId, 0);
                        numberOfTilesMap.put(playerId, 0);
                        playersNameMap.put(playerId, playerName);
                    }
                    setChanged();
                    notifyObservers(MethodsNames.PLAYERS_LIST_UPDATED + ":" + splitedArgString[2]);
                    break;

                case MethodsNames.NUMBER_OF_TILES_IN_BAG_UPDATED:
                    numOfTileInBag = Integer.parseInt(arguments[0]);
                    setChanged();
                    notifyObservers(MethodsNames.NUMBER_OF_TILES_IN_BAG_UPDATED);
                    break;

                case MethodsNames.TILES_WITH_SCORES:
                    setChanged();
                    notifyObservers(MethodsNames.TILES_WITH_SCORES + ":" + splitedArgString[2]);
                    break;

                case MethodsNames.START_GAME:
                    setChanged();
                    notifyObservers(MethodsNames.START_GAME);
                    break;

                case MethodsNames.DISCONNECT_FROM_SERVER:
                    closeConnection();
                    setChanged();
                    notifyObservers(MethodsNames.DISCONNECT_FROM_SERVER);
                    break;

                case MethodsNames.CONNECT:
                    setChanged();
                    notifyObservers(MethodsNames.CONNECT + ":" + splitedArgString[2]);
                    break;

                case MethodsNames.END_GAME:
                    System.out.println(argString);
                    setChanged();
                    notifyObservers(MethodsNames.END_GAME + ":" + splitedArgString[2]);
                    break;
                case MethodsNames.CLOSE_CHALLENGE_ALERT:
                    setChanged();
                    notifyObservers(MethodsNames.CLOSE_CHALLENGE_ALERT);
                    break;
            }
        }
    }
}
