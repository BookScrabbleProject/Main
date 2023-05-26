package Model;

import Model.gameClasses.*;

import java.net.Socket;
import java.util.*;


public class HostModel extends PlayerModel implements Observer {
    private static HostModel hostModel = null;
    HashMap<Integer, Player> connectedPlayers;
    HostServer hostServer;
    Board board;
    Tile[][] prevBoard;
    Tile.Bag bag;
    int requestedId;
    int nextId;
    String password;
    int lastWordScore;
    String wordFromPlayers;

    /**
     * method that return the host model itself
     * @return hostModel
     */
    public static HostModel getHost() {
        if (hostModel == null) {
            hostModel = new HostModel();
        }
        return hostModel;
    }

    /**
     * @return the host server
     */
    public HostServer getHostServer() {
        return hostServer;
    }
    /**
     * Default constructor method to the host model
     * create map from id to player
     * build the board
     * create the bag
     * restart the password
     */
    private HostModel() {
        nextId = 0;
        connectedPlayers = new HashMap<>();
        myPlayer.setId(generateId());
        connectedPlayers.put(myPlayer.getId(), myPlayer);
        board = new Board();
        board.buildBoard();
        prevBoard = board.getTiles();
        bag = Tile.Bag.getBag();
        password = null;
        requestedId = -1;
        lastWordScore = 0;
        wordFromPlayers = null;
    }

    /**
     * method that connect and start the connection with the server and open its own server.
     * @param gameServerIp this parameter is the ip of the server
     * @param gameServerPort this parameter is the port of the server
     * @param myPort this is my own port to the connection between the hostModel and the server
     */
    public void connectToBookScrabbleServer(int myPort,String gameServerIp,Integer gameServerPort){hostServer = new HostServer(myPort,new GuestModelHandler(),gameServerIp,gameServerPort);}

    /**
     * method that add player to the game and create player, add the player to the map and create a string builder of ids
     * Sends the information to the hostServer and notify with the format : requestedId + ":" + method + ":" + inputs
     * @param socket - socket parameter that send to the hostServer
     */
   public void addPlayer(Socket socket){
        Player p = new Player(generateId(),null,0,null);
        connectedPlayers.put(p.getId(),p);
        StringBuilder playersIdsAndNames = new StringBuilder();
        playersIdsAndNames.append(p.getId()).append("-").append(p.getName()).append(",");
        for (Integer id: connectedPlayers.keySet())
            if (id != p.getId())
            {
                playersIdsAndNames.append(id).append("-");
                playersIdsAndNames.append(connectedPlayers.get(id).getName());
                playersIdsAndNames.append(",");
            }
        playersIdsAndNames.deleteCharAt(playersIdsAndNames.length()-1);
        hostServer.addSocket(p.getId(),socket);
        hostServer.sendToSpecificPlayer(p.getId(),"setId",Integer.toString(p.getId()));
        hostServer.sendToAllPlayers(-1,"playersListUpdated",playersIdsAndNames.toString());
        setChanged();
        String toNotify = requestedId + ":" + "addPlayer";
        notifyObservers(toNotify);
   }

    /**
     * method that change the string to be without '_'
     * @param word string of the word we tried to put on board
     * @return new word without '_'
     */
   private String wordNoSpace(String word){
       StringBuilder stringBuilder = new StringBuilder();
       for (Character c: word.toCharArray())
           if(c != '_')
               stringBuilder.append(c);
       return stringBuilder.toString();
   }

    /**
     * method that change between board to string object
     * @param tilesBoard represent the board
     * @return the board in a string
     */
   private String boardToString(Tile[][] tilesBoard){
       StringBuilder stringBoard = new StringBuilder();
       for (Tile[] theTile : tilesBoard)
           for (Tile tile : theTile)
               stringBoard.append(tile.letter);
       return stringBoard.toString();
   }

    /**
     *
     * @param board represent the board in string
     * @return the board in a matrix of Characters
     */
   private Character[][] boardToCharMatrix(String board){
       Character[][] boardCharMatrix = new Character[15][15];
       for (int i = 0; i < 15; i++)
           for (int j = 0; j < 15; j++)
               boardCharMatrix[i][j] = board.charAt(15*i + j);
       return boardCharMatrix;
   }

    /**
     * method that convert list of characters into string
     * @param hand list of characters
     * @return string that represents player hand
     */

   private String handToString(List<Character> hand){
       StringBuilder stringBuilder = new StringBuilder();
       for (Character character: hand)
           stringBuilder.append(character);
       return stringBuilder.toString();
   }
    /**
     * @return new id of the player
     */
    int generateId() {
        return nextId++;
    }

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
    public void tryPlaceWord(String word, int col, int row, boolean isVertical) {//run removeTiles method
        List<Tile> t = new ArrayList<>();
        for (char c : word.toCharArray())
            t.add(Tile.Bag.getBag().getTile(c));
        Word w = new Word((Tile[]) t.toArray(), row, col, isVertical);
        hostServer.sendToBookScrabbleServer("Q",word);
        int score = board.tryPlaceWord(w);
        lastWordScore = score;
        wordFromPlayers = wordNoSpace(word);
        if(score > 0) {
            connectedPlayers.get(requestedId).addScore(lastWordScore);
            refillPlayerHand();
            hostServer.sendToAllPlayers(requestedId,"tryPlaceWord",String.valueOf(lastWordScore));
            hostServer.sendToAllPlayers(requestedId,"scoreUpdated", String.valueOf(connectedPlayers.get(requestedId).getScore()));
            hostServer.sendToAllPlayers(-1,"boardUpdated",boardToString(board.getTiles()));

        }
        setChanged();
        String toNotify = requestedId + ":" + "tryPlaceWord" + ":" + score;
        notifyObservers(toNotify);
    }

    /**
     * A method which check if the word is valid or not
     * send the information to the handler with the method sendtohandler()
     * notify to the binding objects by a format - requestedId + ":" + method + ":" + inputs
     * @param word a given word to check if it valid or not
     */
    @Override
    public void challenge(String word) {
        hostServer.sendToBookScrabbleServer("C",word);
        setChanged();
        String toNotify = requestedId + ":" + "challenge" + ":" + word;
        notifyObservers(toNotify);
    }

    /**
     * A method which take on tile randomly from the bag and put it in the player list of tiles
     * notify to the binding objects by a format - requestedId + ":" + method + ":" + inputs
     */
    @Override
    public void takeTileFromBag() {
        if (requestedId == -1)
            requestedId = myPlayer.getId();
        Tile t = bag.getRand();
        connectedPlayers.get(requestedId).addTiles(String.valueOf(t.letter));
        setChanged();
        String toNotify = requestedId + ":" + "takeTileFromBag" + ":" + t.getLetter() + "," + t.getScore();
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
            for (int i = numOfTiles; i <= 7; i++)
                connectedPlayers.get(requestedId).addTiles(String.valueOf(bag.getRand().letter));

        setChanged();
        String toNotify = requestedId + ":" + "refillPlayerHand";
        notifyObservers(toNotify);
        requestedId = -1;
    }

    /**
     * A method which increase the turns in the game
     * notify to the binding objects by a format - requestedId + ":" + method + ":" + inputs
     */
    public void passTheTurn() {
        currentPlayerId++;
        currentPlayerId %= connectedPlayers.size();
        prevBoard = board.getTiles();
        hostServer.sendToAllPlayers(-1,"newPlayerTurn", String.valueOf(currentPlayerId));
        setChanged();
        String toNotify = requestedId + ":" + "passTheTurn";
        notifyObservers(toNotify);
    }

    /**
     * A method that set the prevboard to the new state of the board and notify to the other players that the board has changed
     *              notify to the binding objects by a format - requestedId + ":" +method + ":" + inputs
     */
    public void setBoardStatus() {
        setChanged();
        String toNotify = requestedId + ":" + "setBoardStatus" + ":" + boardToString(prevBoard);
        notifyObservers(toNotify);
    }

    /**
     * A method that return the status of the board
     * @return the board status in a Tile matrix
     */
    @Override
    public Character[][] getBoardStatus() {
        return boardToCharMatrix(boardToString(board.getTiles()));
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

    @Override
    public List<Character> getMyHand() {
        return super.getMyTiles();
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
        String[] newRequest = request.split(":");
        requestedId = Integer.parseInt(newRequest[0]);
        String methodName = newRequest[1];
        String[] inputs = null;

        switch (methodName) {
            case "tryPlaceWord" -> {
                inputs = newRequest[2].split(",");
                String word = inputs[0];
                int col = Integer.parseInt(inputs[1]);
                int row = Integer.parseInt(inputs[2]);
                boolean isVertical = Boolean.parseBoolean(inputs[3]);
                if (inputs[4].equals("0")) {
                    hostServer.sendToSpecificPlayer(requestedId, "tryPlaceWord", "0");
                }
                else {
                    tryPlaceWord(word, col, row, isVertical);
                    for (Character c : wordFromPlayers.toCharArray())
                        connectedPlayers.get(requestedId).getTiles().remove(c);
                    hostServer.sendToAllPlayers(requestedId,"scoreUpdated", String.valueOf(connectedPlayers.get(requestedId).getScore()));
                    hostServer.sendToSpecificPlayer(requestedId, "handUpdated", handToString(connectedPlayers.get(requestedId).getTiles()));
                    hostServer.sendToAllPlayers(requestedId, "numOfCardsUpdate", String.valueOf(connectedPlayers.get(requestedId).getTiles().size()));
                    hostServer.sendToAllPlayers(requestedId,"tryPlaceWord","1");
                    break;
                }
            }
            case "challenge" -> {
                inputs = newRequest[2].split(",");
                String word = inputs[0];
                if (inputs[1].equals("0")) {
                    board.setTiles(prevBoard);
                    connectedPlayers.get(currentPlayerId).setScore(connectedPlayers.get(currentPlayerId).getScore() - lastWordScore);
                    connectedPlayers.get(currentPlayerId).addTiles(wordFromPlayers);
                    hostServer.sendToAllPlayers(0, "boardUpdated",boardToString(board.getTiles()));
                    hostServer.sendToSpecificPlayer(currentPlayerId,"setHand",handToString(connectedPlayers.get(currentPlayerId).getTiles()));
                    hostServer.sendToAllPlayers(currentPlayerId,"numOfCardsUpdate",connectedPlayers.get(currentPlayerId).getTiles().toString());
                    hostServer.sendToAllPlayers(0, "scoreChanged", currentPlayerId + "," + connectedPlayers.get(currentPlayerId).getScore());
                    hostServer.sendToAllPlayers(requestedId, "challenge", "0");
                } else {
                    requestedId = currentPlayerId;
                    refillPlayerHand();
                    connectedPlayers.get(requestedId).setScore(connectedPlayers.get(requestedId).getScore() - lastWordScore);
                    hostServer.sendToSpecificPlayer(currentPlayerId,"setHand",handToString(connectedPlayers.get(currentPlayerId).getTiles()));
                    hostServer.sendToAllPlayers(currentPlayerId,"numOfCardsUpdate",connectedPlayers.get(currentPlayerId).getTiles().toString());
                    passTheTurn();
                    hostServer.sendToAllPlayers(requestedId, "challenge", "1");
                    requestedId = -1;
                }
                break;
            }
            case "takeTileFromBag" -> {
                requestedId = currentPlayerId;
                takeTileFromBag();
                hostServer.sendToSpecificPlayer(currentPlayerId,"setHand",handToString(connectedPlayers.get(currentPlayerId).getTiles()));
                hostServer.sendToAllPlayers(currentPlayerId,"numOfCardsUpdate",connectedPlayers.get(currentPlayerId).getTiles().toString());
                hostServer.sendToAllPlayers(currentPlayerId,"numOfCardsUpdate",connectedPlayers.get(currentPlayerId).getTiles().toString());
                passTheTurn();
                requestedId = -1;
                break;
            }
        }
    }
}

