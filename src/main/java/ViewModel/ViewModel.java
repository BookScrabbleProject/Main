package ViewModel;

import Model.Model;
import Model.gameClasses.Player;
import General.MethodsNames;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.lang.reflect.Method;
import java.util.*;

public class ViewModel extends Observable implements Observer {
    public static ViewModel vm = null;
    private Character[][] board;
    public List<DataChanges> changesList;
    private int currentPlayerId;
    private int numberOfTilesInBag;
    public StringProperty numberOfTilesInBagProperty;

    private Map<Integer, PlayerVVM> players;
    private MyPlayerVVM myPlayer;
    //    data binding
    private Map<Character, Integer> tilesScores;
    private Model model;
    private String word;
    private int wordStartRow;
    private int wordStartCol;

    /**
     * The ViewModel function is the constructor of the ViewModel class.
     * It initializes all of its fields to their default values.
     */
    private ViewModel() {
        model = null;
        this.tilesScores = new HashMap<>();
        this.board = new Character[15][15];
        this.changesList = new ArrayList<>();
        this.currentPlayerId = -1;
        this.numberOfTilesInBag = 0;
        this.numberOfTilesInBagProperty = new SimpleStringProperty("");
        this.players = new HashMap<>();
        this.myPlayer = new MyPlayerVVM(-1, "Me", 0, 0);
    }

    public Map<Integer, PlayerVVM> getPlayers() {
        return players;
    }

    public MyPlayerVVM getMyPlayer() {
        return myPlayer;
    }

    public int getCurrentPlayerId() {
        return currentPlayerId;
    }

    public Character[][] getBoard() {
        return board;
    }

    public void startGame() {
        model.startGame();
    }

    /**
     * create a new ViewModel if it doesn't exist, and return it (singleton)
     *
     * @return the ViewModel
     */
    public static ViewModel getViewModel() {
        if (vm == null) {
            vm = new ViewModel();
        }
        return vm;
    }

    /**
     * set the model only if it is null (to avoid setting it twice)
     *
     * @param model - the model that the ViewModel is observing (GuestModel or HostModel)
     */
    public void setModel(Model model) {
        if (this.model == null) {
            this.model = model;
            this.model.addObserver(this);
        }
    }

    /**
     * The resetModel function is used to reset the model of the game.
     * This function is called when a new game is started, and it deletes
     * any observers that are attached to the old model. It then sets
     * this.model = null; so that we can create a new instance of our
     * GameModel class in our startGame function in GameController class.
     */
    public void resetModel() {
        this.model = null;
    }

    /**
     * The close function closes the connection.
     * If the model is GuestModel, it closes the connection to the server.
     * If the model is HostModel, it closes the connection to the BookScrabbleServer (MyServer) and close the HostServer.
     */
    public void close() {
        model.closeConnection();
    }

    /**
     * Run when the player clicks the "Place Word" button.
     * Tries to place the word on the board if it is valid (from changesList) using the Model.
     * If the word is invalid, notify the view
     */
    public void tryPlaceWord() {
        if (isChangeValid()) {
            model.tryPlaceWord(getWord(), getWordStartRow(), getWordStartCol(), isWordVertical());
            word = null;
            wordStartRow = -1;
            wordStartCol = -1;
            return;
        }
        setChanged();
        notifyObservers("tryPlaceWordInvalid"); // Todo: update Yuval about it (the case he has to handle)
    }

    public void challenge(String word) {
        model.challenge(word);
    }

    public void takeTileFromBag() {
        model.takeTileFromBag();
    }

    /**
     * @return true if the word is valid, false otherwise (from changesList)
     */
    private boolean isChangeValid() {
        List<DataChanges> sortedChangesList = getSortedChangesListByRowCol();
        int numberOfColRowChanges = 0;
        int colForCheck = sortedChangesList.get(0).getNewCol();
        int rowForCheck = sortedChangesList.get(0).getNewRow();
        for (DataChanges dc : sortedChangesList) {
            if (dc.getNewCol() != colForCheck) {
                numberOfColRowChanges++;
                colForCheck = dc.getNewCol();
            }
            if (dc.getNewRow() != rowForCheck) {
                numberOfColRowChanges++;
                rowForCheck = dc.getNewRow();
            }
        }

        return numberOfColRowChanges == sortedChangesList.size() - 1;
    }

    /**
     * @return the word that the player is trying to place
     */
    private String getWord() {
        List<DataChanges> sortedChangesList = getSortedChangesListByRowCol();
        StringBuilder sb = new StringBuilder();
        if (isWordVertical()) {
            int startRow = sortedChangesList.get(0).getNewRow();
            while (startRow > 0 && board[startRow - 1][sortedChangesList.get(0).getNewCol()] != null) {
                startRow--;
            }
            for (int i = startRow; i < sortedChangesList.get(0).getNewRow(); i++) {
                sb.append('_');
            }
        } else {
            int startCol = sortedChangesList.get(0).getNewCol();
            while (startCol > 0 && board[sortedChangesList.get(0).getNewRow()][startCol - 1] != null) {
                startCol--;
            }
            for (int i = startCol; i < sortedChangesList.get(0).getNewCol(); i++) {
                sb.append('_');
            }
        }
        int row = changesList.get(0).getNewRow();
        int col = changesList.get(0).getNewCol();
        for (DataChanges dc : sortedChangesList) {
            if (dc.getNewCol() > col + 1 || dc.getNewRow() > row + 1) {
                for (int i = 0; i < dc.getNewCol() - col - 1; i++) {
                    sb.append('_');
                }
            }
            sb.append(dc.getLetter());
            col = dc.getNewCol();
            row = dc.getNewRow();
        }

        if (isWordVertical()) {
            int endRow = sortedChangesList.get(sortedChangesList.size() - 1).getNewRow();
            while (endRow < 14 && board[endRow + 1][sortedChangesList.get(0).getNewCol()] != null) {
                endRow++;
            }
            for (int i = endRow; i > sortedChangesList.get(sortedChangesList.size() - 1).getNewRow(); i--) {
                sb.append('_');
            }
        } else {
            int endCol = sortedChangesList.get(sortedChangesList.size() - 1).getNewCol();
            while (endCol < 14 && board[sortedChangesList.get(0).getNewRow()][endCol + 1] != null) {
                endCol++;
            }
            for (int i = endCol; i > sortedChangesList.get(sortedChangesList.size() - 1).getNewCol(); i--) {
                sb.append('_');
            }
        }
        this.word = sb.toString();
        return this.word;
    }

    /**
     * @return the row of the first letter of the word
     */
    private int getWordStartRow() {
        int minRow = changesList.get(0).getNewRow();
        for (DataChanges dc : changesList) {
            if (dc.getNewRow() < minRow) minRow = dc.getNewRow();
        }
        if (isWordVertical()) {
            for (Character c : this.word.toCharArray()) {
                if (c == '_') minRow--;
                else break;
            }
        }
        this.wordStartRow = minRow;
        return this.wordStartRow;
    }

    /**
     * @return the column of the first letter of the word
     */
    private int getWordStartCol() {
        int minCol = changesList.get(0).getNewCol();
        for (DataChanges dc : changesList) {
            if (dc.getNewCol() < minCol) minCol = dc.getNewCol();
        }
        if (!isWordVertical()) {
            for (Character c : this.word.toCharArray()) {
                if (c == '_') minCol--;
                else break;
            }
        }
        this.wordStartCol = minCol;
        return this.wordStartCol;
    }

    /**
     * @return true if the word is vertical, false if it is horizontal
     */
    private boolean isWordVertical() {
        List<DataChanges> sortedChangesList = getSortedChangesListByRowCol();
        return sortedChangesList.get(0).getNewCol() == sortedChangesList.get(1).getNewCol();
    }

    private List<DataChanges> getSortedChangesListByRowCol() {
        List<DataChanges> sortedChangesList = new ArrayList<>();
        sortedChangesList.addAll(changesList);
        sortedChangesList.sort((o1, o2) -> {
            if (o1.getNewRow() == o2.getNewRow()) {
                return o1.getNewCol() - o2.getNewCol();
            }
            return o1.getNewRow() - o2.getNewRow();
        });
        return sortedChangesList;
    }

    private void setBoard(Character[][] boardStatus) {
        this.board = boardStatus;
    }

    private void setNumberOfTilesInBag(int numberOfTilesInBag) {
        this.numberOfTilesInBag = numberOfTilesInBag;
        numberOfTilesInBagProperty.setValue(String.valueOf(this.numberOfTilesInBag));
    }

    private void setCurrentPlayerId(int currentPlayerId) {
        this.currentPlayerId = currentPlayerId;
        setChanged();
        notifyObservers(MethodsNames.NEW_PLAYER_TURN);
    }

    /**
     * this method start when the observable object (the model [HostModel/GuestModel]) notify to the observer (VM)
     *
     * @param o   the observable object.
     * @param arg an argument passed to the {@code notifyObservers}
     *            method. (the message from the model to the VM, type: String)
     * @Details The method update the VM with the changes that happened in the model.
     * Types of updates: method_name:args & method_name
     */
    @Override
    public void update(Observable o, Object arg) {
        String messages = (String) arg;
        Queue<String> messagesQ = new LinkedList<>(Arrays.asList(messages.split("\n")));
        while (!messagesQ.isEmpty()) {
            String message = messagesQ.poll();
            System.out.println("VM update: " + message); // Todo: delete this line after debug
            String methodName = message.split(":")[0];
            String[] args = null;

            try {
                args = message.split(":")[1].split(",");
            } catch (Exception e) {}

            switch (methodName) {
                case MethodsNames.BOARD_UPDATED:
                    setBoard(model.getBoardStatus());
                    break;

                case MethodsNames.SCORE_UPDATED:
                    Map<Integer, Integer> scores = model.getPlayersScores();
                    for (Integer key : scores.keySet())
                        this.players.get(key).setScore(scores.get(key));
                    break;

                case MethodsNames.NUM_OF_TILES_UPDATED:
                    Map<Integer, Integer> tiles = model.getPlayersNumberOfTiles();
                    for (Integer key : tiles.keySet())
                        this.players.get(key).setNumberOfTiles(tiles.get(key));
                    break;

                case MethodsNames.SET_HAND:
                    this.myPlayer.setHand(model.getMyHand());
                    setChanged();
                    notifyObservers(MethodsNames.SET_HAND);
                    break;

                case MethodsNames.NEW_PLAYER_TURN:
                    setCurrentPlayerId(model.getCurrentPlayerId());
                    setChanged();
                    notifyObservers(MethodsNames.NEW_PLAYER_TURN);
                    break;

                case MethodsNames.SET_ID:
                    this.myPlayer.setId(Integer.parseInt(args[0]));
                    this.players.put(this.myPlayer.getId(), this.myPlayer);
                    break;

                case MethodsNames.PLAYERS_LIST_UPDATED:
                    for (String player : args) {
                        String[] playerInfo = player.split("-");
                        int id = Integer.parseInt(playerInfo[0]);
                        String name = playerInfo[1];
                        if (!this.players.containsKey(id))
                            this.players.put(id, new PlayerVVM(id, name));
                    }
                    setChanged();
                    notifyObservers(MethodsNames.PLAYERS_LIST_UPDATED);
                    break;

                case MethodsNames.TILES_WITH_SCORES:
                    for (String tile : args) {
                        String[] tileInfo = tile.split("-");
                        char letter = Character.toUpperCase(tileInfo[0].charAt(0));
                        int score = Integer.parseInt(tileInfo[1]);
                        this.tilesScores.put(letter, score);
                    }
                    break;

                case MethodsNames.START_GAME:
                case MethodsNames.CHALLENGE:
                case MethodsNames.TRY_PLACE_WORD:
                case MethodsNames.DISCONNECT_FROM_SERVER:
                    setChanged();
                    notifyObservers(methodName);
                    break;

                case MethodsNames.NUMBER_OF_TILES_IN_BAG_UPDATED:
                    setNumberOfTilesInBag(model.getNumberOfTilesInBag());
                    break;

                case MethodsNames.CONNECT:
                    setChanged();
                    notifyObservers(MethodsNames.CONNECT + ":" + args[0]);
                    break;
            }

        }
    }
}
