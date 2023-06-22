package Model;

import Model.gameClasses.*;
import General.MethodsNames;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.*;

/**
 * Host server class
 */
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
    boolean isGameStarted;

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
        myPlayer = new Player("HOST");
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
        isGameStarted = false;
    }

    /**
     * method that return the host model itself
     *
     * @return hostModel
     */
    public static HostModel getHost() {
        if (hostModel == null)
            hostModel = new HostModel();
        return hostModel;
    }

    /**
     * @return the host server
     */
    public HostServer getHostServer() {
        return hostServer;
    }

    //delete after check
    public Map<Integer, Player> getConnectedPlayers() {
        return connectedPlayers;
    }

    public void loadBooks(String... bookNames) {
        String[] str = new String[bookNames.length];
        for (String s : bookNames)
            hostServer.getBookNames().add(s);
    }

    public void setPlayerName(String name) {
        myPlayer.setName(name);
    }

    /**
     * method that connect and start the connection with the server and open its own server.
     *
     * @param gameServerIp   this parameter is the ip of the server
     * @param gameServerPort this parameter is the port of the server
     * @param myPort         this is my own port to the connection between the hostModel and the server
     *                       add host server to the observer
     */
    public void connectToBookScrabbleServer(int myPort, String gameServerIp, int gameServerPort) {
        hostServer = new HostServer(myPort, new GuestModelHandler(), gameServerIp, gameServerPort);
        hostServer.addObserver(this);
        setChanged();
        notifyObservers(MethodsNames.SET_ID + ":" + myPlayer.getId() + "\n");
    }

    @Override
    public void startGame() {
        isGameStarted = true;
        StringBuilder toAllPlayers = new StringBuilder();
        StringBuilder toNotify = new StringBuilder();

        for (Player p : connectedPlayers.values()) {
            refillPlayerHand(p.getId());
            p.setScore(0);
            toAllPlayers.append(p.getId()).append(":" + MethodsNames.NUM_OF_TILES_UPDATED + ":").append(7).append("\n");
            toAllPlayers.append(p.getId()).append(":" + MethodsNames.SCORE_UPDATED + ":").append(p.getScore()).append("\n");
            hostServer.sendToSpecificPlayer(p.getId(), "0:" + MethodsNames.SET_HAND + ":" + handToString(p.getTiles()) + "\n");
        }
        setCurrentPlayerId(0);

        toAllPlayers.append(0).append(":" + MethodsNames.NEW_PLAYER_TURN + ":").append(getCurrentPlayerId()).append("\n");
        toAllPlayers.append(0).append(":" + MethodsNames.NUMBER_OF_TILES_IN_BAG_UPDATED + ":").append(bag.totalTiles).append("\n");
        toAllPlayers.append(0).append(":" + MethodsNames.START_GAME + ":").append("_\n");
        toNotify.append(MethodsNames.NEW_PLAYER_TURN).append("\n");
        toNotify.append(MethodsNames.START_GAME).append("\n");

        hostServer.sendToAllPlayers(toAllPlayers.toString());
        setChanged();
        notifyObservers(toNotify.toString());
    }

    @Override
    public void closeConnection() {
        try {
            hostServer.sendToAllPlayers("0:" + MethodsNames.DISCONNECT_FROM_SERVER + ":_\n");
            Thread.sleep(500);
            hostServer.close();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method that add player to the game and create player, add the player to the map and create a string builder of ids
     * Sends the information to the hostServer and notify with the format : requestedId + ":" + method + ":" + inputs
     *
     * @param socket - socket parameter that send to the hostServer
     */
    public void addPlayer(Socket socket) {
        if (isGameStarted) {
            try {
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                out.println("0:" + MethodsNames.GAME_ALREADY_STARTED + ":_\n"); // 0 means the game is full
                out.flush();
                return;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (connectedPlayers.size() >= 4) {
            try {
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                out.println("0:" + MethodsNames.CONNECT + ":0\n"); // 0 means the game is full
                out.flush();
                return;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        StringBuilder nameBuilder = new StringBuilder();
        try {
            Scanner s = new Scanner(socket.getInputStream());
            String sMsg = s.next();
            String[] msg = sMsg.split(":");
            nameBuilder.append(msg[2]);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String name = nameBuilder.toString();
        if (name.equals("")) name = "Guest"; // if the player didn't enter a name

        Player p = new Player(generateId(), name, 0, new ArrayList<Character>());
        connectedPlayers.put(p.getId(), p);
        StringBuilder playersIdsAndNames = new StringBuilder();
        playersIdsAndNames.append(p.getId()).append("-").append(p.getName()).append(",");
        for (Integer id : connectedPlayers.keySet())
            if (id != p.getId()) {
                playersIdsAndNames.append(id).append("-");
                playersIdsAndNames.append(connectedPlayers.get(id).getName());
                playersIdsAndNames.append(",");
            }
        playersIdsAndNames.deleteCharAt(playersIdsAndNames.length() - 1);
        hostServer.addSocket(p.getId(), socket);
        StringBuilder toNotify = new StringBuilder();
        StringBuilder toSpecificPlayer = new StringBuilder();
        toSpecificPlayer.append(p.getId()).append(":" + MethodsNames.SET_ID + ":").append(p.getId()).append("\n");
        toSpecificPlayer.append(p.getId()).append(":" + MethodsNames.TILES_WITH_SCORES + ":").append(tilesWithScores()).append("\n");
        toSpecificPlayer.append(p.getId()).append(":" + MethodsNames.CONNECT + ":1").append("\n"); // 1 means success connection

        StringBuilder toAllPlayers = new StringBuilder();
        toAllPlayers.append(-1).append(":" + MethodsNames.PLAYERS_LIST_UPDATED + ":").append(playersIdsAndNames.toString()).append("\n");
        toNotify.append(MethodsNames.PLAYERS_LIST_UPDATED + ":").append(playersIdsAndNames.toString()).append('\n');

        hostServer.sendToSpecificPlayer(p.getId(), toSpecificPlayer.toString());
        hostServer.sendToAllPlayers(toAllPlayers.toString());

        setChanged();
        notifyObservers(toNotify.toString());
    }

    /**
     * method that change the string to be without '_'
     *
     * @param word string of the word we tried to put on board
     * @return new word without '_'
     */
    private String wordNoSpace(String word) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Character c : word.toCharArray())
            if (c != '_')
                stringBuilder.append(c);
        return stringBuilder.toString();
    }

    /**
     * method that change between board to string object
     *
     * @param tilesBoard represent the board
     * @return the board in a string
     */
    private String boardToString(Tile[][] tilesBoard) {
        StringBuilder stringBoard = new StringBuilder();
        for (Tile[] theTile : tilesBoard)
            for (Tile tile : theTile)
                if (tile != null) stringBoard.append(tile.letter);
                else stringBoard.append("_");
        return stringBoard.toString();
    }

    /**
     * @param board represent the board in string
     * @return the board in a matrix of Characters
     */
    private Character[][] boardToCharMatrix(String board) {
        Character[][] boardCharMatrix = new Character[15][15];
        for (int i = 0; i < 15; i++)
            for (int j = 0; j < 15; j++)
                boardCharMatrix[i][j] = board.charAt(15 * i + j);
        return boardCharMatrix;
    }

    /**
     * method that convert list of characters into string
     *
     * @param hand list of characters
     * @return string that represents player hand
     */

    private String handToString(List<Character> hand) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Character character : hand)
            stringBuilder.append(character);
        return stringBuilder.toString();
    }

    /**
     * The generateId function is used to assign a unique id to each player that connects.
     * The function first creates an array of booleans, with the length equal to the maximum number of players allowed in a game (4).
     * Then, it iterates through all connected players and sets their corresponding index in the boolean array as true.
     * Finally, it iterates through this boolean array and returns the first index that is false (i.e., not taken by another player).
     *
     * @return The first available id
     */
    int generateId() {
        boolean[] ids = new boolean[4];
        for (Integer id : connectedPlayers.keySet())
            ids[id] = true;
        for (int i = 0; i < ids.length; i++)
            if (!ids[i])
                return i;
        return -1;
    }

    /**
     * A method that try to place the word on the board
     * create tile[] from the string word, create Word.
     * notify to the binding objects by a format - requestedId + ":" + method + ":" + inputs
     *
     * @param word       a string that represent the word that the player want to place on the board
     * @param col        represent the starting col of the word in the board
     * @param row        represent the starting row of the word in the board
     * @param isVertical represent if the word is vertical or not with boolean parameter
     */
    @Override
    public void tryPlaceWord(String word, int col, int row, boolean isVertical) {//run removeTiles method
        StringBuilder toNotify = new StringBuilder();
        if (requestedId == -1)
            requestedId = myPlayer.getId();
        List<Tile> t = Board.getBoard().getWord(word);
        Tile[] tilesArray = new Tile[t.size()];
        for (int i = 0; i < t.size(); i++)
            tilesArray[i] = t.get(i);
        Word w = new Word(tilesArray, row, col, isVertical);
        if (requestedId == myPlayer.getId()) {
            Socket bookScrabbleSocket = hostServer.sendToBookScrabbleServer("Q", word);
            try {
                Scanner s = new Scanner(bookScrabbleSocket.getInputStream());
                String answerFromBookScrabble = s.next();
                if (!Boolean.getBoolean(answerFromBookScrabble)) {
                    toNotify.append(MethodsNames.TRY_PLACE_WORD + ":").append(0).append('\n');
                    setChanged();
                    notifyObservers(toNotify.toString());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        int score = board.tryPlaceWord(w);
        lastWordScore = score;
        wordFromPlayers = wordNoSpace(word);

        StringBuilder toSpecificPlayer = new StringBuilder();
        StringBuilder toAllPlayers = new StringBuilder();


        if (score > 0) {
            for (Character c : wordFromPlayers.toCharArray())
                connectedPlayers.get(requestedId).getTiles().remove(c);

            connectedPlayers.get(requestedId).addScore(lastWordScore);
            toAllPlayers.append(requestedId).append(":" + MethodsNames.BOARD_UPDATED + ":").append(boardToString(board.getTiles())).append("\n");
            toNotify.append(MethodsNames.BOARD_UPDATED + ":").append(boardToString(board.getTiles())).append('\n');

            toAllPlayers.append(requestedId).append(":" + MethodsNames.SCORE_UPDATED + ":").append(String.valueOf(connectedPlayers.get(requestedId).getScore())).append("\n");
            toNotify.append(MethodsNames.SCORE_UPDATED + ":").append(String.valueOf(connectedPlayers.get(requestedId).getScore())).append("\n");

            if (requestedId != myPlayer.getId()) {
                String playerHand = handToString(connectedPlayers.get(requestedId).getTiles());
                toSpecificPlayer.append(requestedId).append(":" + MethodsNames.SET_HAND + ":").append(playerHand.equals("") ? "_" : playerHand).append("\n");
                String handToSend = handToString(connectedPlayers.get(requestedId).getTiles());
                toNotify.append(MethodsNames.SET_HAND + ":").append(handToSend.equals("") ? "_" : handToSend).append("\n");
            }
            toAllPlayers.append(requestedId).append(":" + MethodsNames.NUM_OF_TILES_UPDATED + ":").append(connectedPlayers.get(requestedId).getTiles().size()).append("\n");
            toNotify.append(MethodsNames.NUM_OF_TILES_UPDATED).append('\n');

            toAllPlayers.append(requestedId).append(":" + MethodsNames.TRY_PLACE_WORD + ":").append(String.valueOf(lastWordScore)).append("\n");
            toNotify.append(MethodsNames.TRY_PLACE_WORD + ":").append(String.valueOf(lastWordScore)).append('\n');
        } else {
            toSpecificPlayer.append(requestedId).append(":" + MethodsNames.TRY_PLACE_WORD + ":0").append("\n");
            toNotify.append(MethodsNames.TRY_PLACE_WORD + ":0").append('\n');
        }

        hostServer.sendToSpecificPlayer(requestedId, toSpecificPlayer.toString());
        hostServer.sendToAllPlayers(toAllPlayers.toString());

        setChanged();
        notifyObservers(toNotify.toString());
        requestedId = -1;
    }

    /**
     * A method which check if the word is valid or not
     * send the information to the handler with the method sendToHandler()
     * notify to the binding objects by a format - requestedId + ":" + method + ":" + inputs
     *
     * @param word a given word to check if it valid or not
     */
    @Override
    public void challenge(String word) {
        if (requestedId == -1)
            requestedId = myPlayer.getId();
        Socket bookScrabbleSocket = hostServer.sendToBookScrabbleServer("C", word);
        try {
            Scanner s = new Scanner(bookScrabbleSocket.getInputStream());
            String str = s.next();
            if (str.equals("false")) challenge0(word);
            else challenge1(word);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void challenge0(String word) {
        StringBuilder toNotify = new StringBuilder();
        StringBuilder toSpecificPlayer = new StringBuilder();
        StringBuilder toAllPlayers = new StringBuilder();
        board.setTiles(prevBoard);
        connectedPlayers.get(currentPlayerId).setScore(connectedPlayers.get(currentPlayerId).getScore() - lastWordScore);
        connectedPlayers.get(currentPlayerId).addTiles(wordFromPlayers);
        toAllPlayers.append(0).append(":" + MethodsNames.BOARD_UPDATED + ":").append(boardToString(board.getTiles())).append("\n");
        toNotify.append(MethodsNames.BOARD_UPDATED).append("\n");
        toSpecificPlayer.append(currentPlayerId).append(":" + MethodsNames.SET_HAND + ":").append(handToString(connectedPlayers.get(currentPlayerId).getTiles())).append("\n");
        toAllPlayers.append(currentPlayerId).append(":" + MethodsNames.NUM_OF_TILES_UPDATED + ":").append(connectedPlayers.get(currentPlayerId).getTiles().size()).append("\n");
        toNotify.append(MethodsNames.NUM_OF_TILES_UPDATED).append("\n");
        toAllPlayers.append(0).append(":" + MethodsNames.SCORE_UPDATED + ":").append(String.valueOf(connectedPlayers.get(currentPlayerId).getScore())).append("\n");
        toNotify.append(MethodsNames.SCORE_UPDATED).append("\n");
        toAllPlayers.append(requestedId).append(":" + MethodsNames.CHALLENGE + ":0,").append(word).append("\n");
        toNotify.append(MethodsNames.CHALLENGE + ":0,").append(word).append('\n');
        setChanged();
        hostServer.sendToSpecificPlayer(currentPlayerId, toSpecificPlayer.toString());
        hostServer.sendToAllPlayers(toAllPlayers.toString());
        notifyObservers(toNotify.toString());
    }

    private void challenge1(String word) {
        StringBuilder toNotify = new StringBuilder();
        StringBuilder toSpecificPlayer = new StringBuilder();
        StringBuilder toAllPlayers = new StringBuilder();
        refillPlayerHand(currentPlayerId);
        connectedPlayers.get(requestedId).setScore(connectedPlayers.get(requestedId).getScore() - lastWordScore);
        toSpecificPlayer.append(currentPlayerId).append(":" + MethodsNames.SET_HAND + ":").append(handToString(connectedPlayers.get(currentPlayerId).getTiles())).append("\n");
        toAllPlayers.append(currentPlayerId).append(":" + MethodsNames.NUM_OF_TILES_UPDATED + ":").append(connectedPlayers.get(currentPlayerId).getTiles().size()).append("\n");
        passTheTurn();
        toAllPlayers.append(requestedId).append(":" + MethodsNames.CHALLENGE + ":1,").append(word).append("\n");
        requestedId = -1;
        toNotify.append(MethodsNames.CHALLENGE + ":").append(word).append("\n");
        hostServer.sendToSpecificPlayer(currentPlayerId, toSpecificPlayer.toString());
        hostServer.sendToAllPlayers(toAllPlayers.toString());

        setChanged();
        notifyObservers(toNotify.toString());
    }

    /**
     * A method which take on tile randomly from the bag and put it in the player list of tiles
     * notify to the binding objects by a format - requestedId + ":" + method + ":" + inputs
     */
    @Override
    public void takeTileFromBag() {
        StringBuilder toNotify = new StringBuilder();
        StringBuilder toAllPlayers = new StringBuilder();
        if (requestedId == -1)
            requestedId = myPlayer.getId();

        List<Character> requestedPlayerTiles = connectedPlayers.get(requestedId).getTiles();
        if (requestedPlayerTiles.size() == 7) {
            for (Character c : requestedPlayerTiles) {
//                bag.addTile(c.charValue());
                Tile.Bag.getBag().addTileToBag(c.charValue());
            }
            requestedPlayerTiles.clear();
            refillPlayerHand(requestedId);
            if (requestedId == myPlayer.getId())
                toNotify.append(MethodsNames.SET_HAND).append("\n");

            setChanged();
            notifyObservers(toNotify.toString());
            requestedId = -1;
            passTheTurn();
            return;
        }

        Tile t = bag.getRand();
        connectedPlayers.get(requestedId).addTiles(String.valueOf(t.letter));

        if (requestedId == myPlayer.getId()) {
            toAllPlayers.append(requestedId).append(":" + MethodsNames.NUM_OF_TILES_UPDATED + ":").append(getMyHand().size()).append("\n");
            toAllPlayers.append(requestedId).append(":" + MethodsNames.NUMBER_OF_TILES_IN_BAG_UPDATED + ":").append(bag.totalTiles).append("\n");
            hostServer.sendToAllPlayers(toAllPlayers.toString());
            toNotify.append(MethodsNames.NUM_OF_TILES_UPDATED).append("\n");
            toNotify.append(MethodsNames.SET_HAND + ":").append("\n");
            toNotify.append(MethodsNames.NUMBER_OF_TILES_IN_BAG_UPDATED).append("\n");
            passTheTurn();
        }
        setChanged();
        notifyObservers(toNotify);
    }

    /**
     * The refillPlayerHand function is called when a player's hand has less than 7 tiles.
     * It adds the missing number of tiles to the player's hand, and updates all players with
     * the new number of tiles in their hands and in the bag.
     *
     * @param playerId int | Identify the player that is requesting to refill his hand
     * @return The number of tiles in the bag
     */
    public void refillPlayerHand(int playerId) {
        StringBuilder toAllPlayers = new StringBuilder();
        StringBuilder toSpecificPlayer = new StringBuilder();
        StringBuilder toNotify = new StringBuilder();
        int numOfTiles = connectedPlayers.get(playerId).getTiles().size();
        if (numOfTiles < 7)
            for (int i = numOfTiles; i < 7; i++)
                connectedPlayers.get(playerId).addTiles(String.valueOf(bag.getRand().letter));

        if (playerId != myPlayer.getId())
            toSpecificPlayer.append(playerId).append(":" + MethodsNames.SET_HAND + ":").append(handToString(connectedPlayers.get(playerId).getTiles())).append("\n");
        else
            toNotify.append(MethodsNames.SET_HAND).append("\n");
        toAllPlayers.append(playerId).append(":" + MethodsNames.NUM_OF_TILES_UPDATED + ":").append(connectedPlayers.get(playerId).getTiles().size()).append("\n");
        toAllPlayers.append(playerId).append(":" + MethodsNames.NUMBER_OF_TILES_IN_BAG_UPDATED + ":").append(bag.totalTiles).append("\n");
        toNotify.append(MethodsNames.NUM_OF_TILES_UPDATED).append("\n");
        toNotify.append(MethodsNames.NUMBER_OF_TILES_IN_BAG_UPDATED).append("\n");

        hostServer.sendToSpecificPlayer(playerId, toSpecificPlayer.toString());
        hostServer.sendToAllPlayers(toAllPlayers.toString());
        setChanged();
        notifyObservers(toNotify.toString());
    }

    /**
     * A method which increase the turns in the game
     * notify to the binding objects by a format - requestedId + ":" + method + ":" + inputs
     */
    public void passTheTurn() {
        // Todo: check if player has the maximum score and end the game
        StringBuilder toNotify = new StringBuilder();
        StringBuilder toAllPlayers = new StringBuilder();
        currentPlayerId++;
        currentPlayerId %= connectedPlayers.size();
        prevBoard = board.getTiles();
        toAllPlayers.append(-1).append(":" + MethodsNames.NEW_PLAYER_TURN + ":").append(String.valueOf(currentPlayerId)).append("\n");
        hostServer.sendToAllPlayers(toAllPlayers.toString());
        toNotify.append(MethodsNames.NEW_PLAYER_TURN + ":").append(String.valueOf(currentPlayerId)).append("\n");
        setChanged();
        notifyObservers(toNotify.toString());
    }

    /**
     * A method that set the prev-board to the new state of the board and notify to the other players that the board has changed
     * notify to the binding objects by a format - requestedId + ":" +method + ":" + inputs
     */
    public void setBoardStatus() {
        setChanged();
        StringBuilder toNotify = new StringBuilder();
        toNotify.append(requestedId).append(":" + MethodsNames.BOARD_UPDATED + ":").append(boardToString(prevBoard)).append("\n");
        notifyObservers(toNotify);
    }

    /**
     * a method that create a string include char and score
     *
     * @return tiles with the scores in string
     */
    public String tilesWithScores() {
        StringBuilder tilesScore = new StringBuilder();
        int i = 0;
        for (char c = 'A'; c <= 'Z'; c++, i++)
            tilesScore.append(c).append("-").append(bag.scores[i]).append(",");
        tilesScore.deleteCharAt(tilesScore.length() - 1);
        return String.valueOf(tilesScore);
    }

    /**
     * A method that return the status of the board
     *
     * @return the board status in a Tile matrix
     */
    @Override
    public Character[][] getBoardStatus() {
        return boardToCharMatrix(boardToString(board.getTiles()));
    }

    /**
     * A method that return the numbers of tile in the bag
     *
     * @return the number of the tile which are in the bag with the parameter totalTiles
     */
    @Override
    public int getNumberOfTilesInBag() {
        return bag.totalTiles;
    }

    /**
     * A method that create a map with all the players scores
     *
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
     *
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
     * @return list of Character of the player hand
     */
    @Override
    public List<Character> getMyHand() {
        return super.getMyTiles();
    }

    /**
     * A method that take care of the reading by the format we have created and calls the method we need
     * format: requestedId+":"+method+":"+inputs
     *
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
            case MethodsNames.TRY_PLACE_WORD: {
                inputs = newRequest[2].split(",");
                String word = inputs[0];
                int col = Integer.parseInt(inputs[1]);
                int row = Integer.parseInt(inputs[2]);
                boolean isVertical = inputs[3].equals("1");
                if (inputs[4].equals("0")) {
                    hostServer.sendToSpecificPlayer(requestedId, MethodsNames.TRY_PLACE_WORD, "0");
                } else {
                    tryPlaceWord(word, col, row, isVertical);
                }
                break;
            }
            case MethodsNames.CHALLENGE: {
                inputs = newRequest[2].split(",");
                String word = inputs[0];
                if (inputs[1].equals("0")) challenge0(word);
                else challenge1(word);
                break;
            }
            case MethodsNames.TAKE_TILE_FROM_BAG: {
//                requestedId = currentPlayerId;
                takeTileFromBag();
                StringBuilder toSpecificPlayer = new StringBuilder();
                StringBuilder toAllPlayers = new StringBuilder();

                toSpecificPlayer.append(requestedId).append(":" + MethodsNames.SET_HAND + ":").append(handToString(connectedPlayers.get(requestedId).getTiles())).append("\n");
                toAllPlayers.append(requestedId).append(":" + MethodsNames.NUM_OF_TILES_UPDATED + ":").append(connectedPlayers.get(requestedId).getTiles().size()).append("\n");
                toAllPlayers.append(requestedId).append(":" + MethodsNames.NUMBER_OF_TILES_IN_BAG_UPDATED + ":").append(getNumberOfTilesInBag()).append("\n");

                hostServer.sendToSpecificPlayer(requestedId, toSpecificPlayer.toString());
                hostServer.sendToAllPlayers(toAllPlayers.toString());

                passTheTurn();
                requestedId = -1;
                break;
            }
        }
    }
}

