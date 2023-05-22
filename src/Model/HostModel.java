package Model;

import Model.gameClasses.Board;
import Model.gameClasses.Player;
import Model.gameClasses.Tile;
import Model.gameClasses.Word;

import java.util.*;
import java.util.function.Function;


public class HostModel extends PlayerModel implements Observer {
    private static HostModel hostModel = null;
    HashMap<Integer, Player> connectedPlayers;
    HostServer hostServer;
    Board board;
    Tile[][] prevBoard;
    Tile.Bag bag;
    int requestedId;
    String password;
//    HashMap<String, Function<T, R> func >functionMap; // func to replace the if-else in update

    /**
     * method that return the host model itself
     *
     * @return hostmodel
     */

    public static HostModel getHost() {
        if (hostModel == null) {
            hostModel = new HostModel();
        }
        return hostModel;
    }

    /**
     * Default constructor method to the host model
     * create map from id to player
     * start the host server
     * build the board
     * create the bag
     * restart the password
     */
    private HostModel() {

        connectedPlayers = new HashMap<>();
        myPlayer.setId(0);
        connectedPlayers.put(myPlayer.getId(), myPlayer);
        hostServer = new HostServer();
        hostServer.hostServer.start();
        board = new Board();
        board.buildBoard();
        prevBoard = board.getTiles();
        bag = Tile.Bag.getBag();
        password = null;
        requestedId = -1;
    }

//    /**
//     * constructor method to the host model
//     * create map from id to player
//     * start the host server
//     * build the board
//     * create the bag
//     *
//     * @param pass that reset the password of the game
//     */
//    public HostModel(String pass) {
//        connectedPlayers = new HashMap<>();
//        connectedPlayers.put(myPlayer.getId(), myPlayer);
//        hostServer = new HostServer();
//        hostServer.hostServer.start();
//        board = new Board();
//        board.buildBoard();
//        prevBoard = board.getTiles();
//        bag = Tile.Bag.getBag();
//        password = pass;
//        requestedId = -1; // how to look at it with another func?
//    }

    /**
     * A method that try to place the word on the board
     * create tile[] from the string word, create Word.
     * notify to the binding objects by a format - requestedId + ":" + method + ":" + inputs
     * @param word       a string that represent the word that the player want to place on the board
     * @param col        represent the starting col of the word in the board
     * @param row        represent the starting row of the word in the board
     * @param isVertical represrmt if the word is vertical or not with boolean paramater
     */
    @Override
    public void tryPlaceWord(String word, int col, int row, boolean isVertical) {
        List<Tile> t = new ArrayList<>();
        for (char c : word.toCharArray()) {
            t.add(Tile.Bag.getBag().getTile(c));
        }
        Word w = new Word(t, row, col, isVertical); // todo - need to change string word to tile[] - how?
        int score = board.tryPlaceWord(w);
        if (score > 0) {

        }
        //if the score 0 - can't place the word
        else {

        }

        hasChanged();
        String toNotify = requestedId + ":" + "tryPlaceWord" + ":" + word + col + row + isVertical;
        notifyObservers(toNotify);
    }

    /**
     * A method which check if the word is valid or not
     * send the information to the handler with the meyhod sendtohandler()
     * notify to the binding objects by a format - requestedId + ":" + method + ":" + inputs
     * @param word a given word to check if it valid or not
     */
    @Override
    public void challenge(String word) {
        hostServer.sendtoHandler(); // todo need to add this func into yuval
        hasChanged();
        String toNotify = requestedId + ":" + "challenge" + ":" + word;
        notifyObservers(toNotify);
    }

    /**
     * A method which take on tile randomly from the bag and put it in the player list of tiles
     * notify to the binding objects by a format - requestedId + ":" + method + ":" + inputs
     */
    @Override
    public void takeTileFromBag() {
        //todo help func in feature
        if (requestedId == -1)
            requestedId = myPlayer.getId();
        Tile t = bag.getRand();
        myPlayer.addTile(t);
        hasChanged();
        String toNotify = requestedId + ":" + "takeTileFromBag" + ":" + t.getLetter() + "," + t.getScore(); // see other player hand or not? - send to the direct player or to all players
        notifyObservers(toNotify);
        requestedId = -1;
    }

    /**
     * Method that refill player hand tiles after he placed tiles on the board
     * notify all the other players by the format - requestedId + ":" + method + ":" + inputs
     */
    public void refillPlayerHand() {
        int numOfTiles = connectedPlayers.get(requestedId).getTiles().size();
        if (requestedId == -1)
            requestedId = myPlayer.getId();
        if(numOfTiles<7)
        {
            for (int i = numOfTiles; i <= 7; i++) {
                Tile tile = bag.getRand();
                connectedPlayers.get(requestedId).addTile(tile);
            }
        }
        hasChanged();
        String toNotify = requestedId + ":" + "refillPlayerHand";
        notifyObservers(toNotify);
        requestedId = -1;
    }

    /**
     * A method which increase the turns in the game and
     * notify to the binding objects by a format - requestedId + ":" + method + ":" + inputs
     */
    @Override
    public void passTheTurn() {
        currentPlayerIndex++;
        currentPlayerIndex %= 4;
        hasChanged();
        String toNotify = requestedId + ":" + "passTheTurn";
        notifyObservers(toNotify);
    }

    // todo help function to notify observers and guest players

    /**
     * A method that set the prevboard to the new state of the board and notify to the other players that the board has changed
     *
     * @param board a Tile matrix that represent the board which we want to set
     *              notify to the binding objects by a format - requestedId + ":" +method + ":" + inputs
     */
    @Override
    public void setBoardStatus(Tile[][] board) {
        prevBoard = board;
        hasChanged();
        String toNotify = requestedId + ":" + "setBoardStatus" + ":" + board;
        notifyObservers(toNotify);
    }

    /**
     * A method that return the status of the board
     * @return the board status in a Tile matrix
     */
    @Override
    public Tile[][] getBoardStatus() {
        return board.getTiles();
    }

    /**
     * A method that return the numbers of tile in the bag
     * @return the number of the tile which are in the bag with the parameter totalTiles
     */
    @Override
    public int getNumberOfTilesInBag() {
        return bag.totalTiles;
    }

    /**
     * A method that create a map with all the players scores
     * @return a map from key: id of the players to value: player score
     */
    @Override
    public HashMap<Integer, Integer> getPlayersScores() {
        HashMap<Integer, Integer> playersScore = new HashMap<>();
        for (Integer idP : connectedPlayers.keySet())
            playersScore.put(idP, myPlayer.getScore());
        return playersScore;
    }

    /**
     * A method that create map with all the players number of tiles
     * @return a map from key: id of the players to value: player number of tiles
     */
    @Override
    public HashMap<Integer, Integer> getPlayersNumberOfTiles() {
        HashMap<Integer, Integer> playerNumOfTiles = new HashMap<>();
        for (Integer idP : connectedPlayers.keySet())
            playerNumOfTiles.put(idP, myPlayer.getTiles().size());
        return playerNumOfTiles;
    }

    /**
     * A method that take care of the reading by the format we have created and calls the method we need
     * format: requestedId+":"+method+":"+inputs
     * @param o   the observable object.
     * @param arg an argument passed to the <code>notifyObservers</code>
     *            method.
     */
    @Override
    public void update(Observable o, Object arg) {
        String request = (String) arg;
        String[] newrequest = request.split(":");
        requestedId = Integer.parseInt(newrequest[0]);
        String methodName = newrequest[1];
        String[] inputs = null;

        switch (methodName) {
            case "tryPlaceWord": {
                inputs = newrequest[2].split(",");
                String word = inputs[0];
                int col = Integer.parseInt(inputs[1]);
                int row = Integer.parseInt(inputs[2]);
                boolean isVertical = Boolean.parseBoolean(inputs[3]);
                tryPlaceWord(word, col, row, isVertical);
                break;
            }
            case "challenge": {
                String word = newrequest[2];
                challenge(word);
                break;
            }
            case "takeTileFromBag": {
                takeTileFromBag();
                break;
            }
            case "passTheTurn": {
                passTheTurn();
                break;
            }
            case "setBoardStatus": {
                Tile[][] board = newrequest[2];
                setBoardStatus(board);
                break;
            }
            case "getBoardStatus": {
                getBoardStatus();
                break;
            }
            case "getNumberOfTilesInBag": {
                getNumberOfTilesInBag();
                break;
            }
            case "getPlayersScores": {
                getPlayersScores();
                break;
            }
            case "getPlayersNumberOfTiles": {
                getPlayersNumberOfTiles();
                break;
            }
            case "refillPlayerHand": {
                refillPlayerHand();
                break;
            }
        }
    }
}
