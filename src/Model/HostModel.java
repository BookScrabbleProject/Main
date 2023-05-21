package Model;

import Model.gameClasses.Board;
import Model.gameClasses.Player;
import Model.gameClasses.Tile;
import Model.gameClasses.Word;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
//
public class HostModel extends PlayerModel implements Observer {
    private static HostModel hostModel = null;
    HashMap<Integer, Player> connectedPlayers;
    HostServer hostServer;
    Board board;
    Tile[][] prevBoard;
    Tile.Bag bag;


    static HostModel getHost() {
        if (hostModel == null) {
            hostModel = new HostModel();
        }
        return hostModel;
    }

    public HostModel() {

        connectedPlayers = new HashMap<>();
        connectedPlayers.put(myPlayer.getId(),myPlayer);
        hostServer = new HostServer();
        hostServer.hostServer.start();
        board = new Board();
        board.buildBoard();
        prevBoard = board.getTiles();
        bag = Tile.Bag.getBag();

    }
    @Override
    public void tryPlaceWord(String word, int col, int row, boolean isVertical) {
        Word w = new Word((Tile[]) myPlayer.getTiles().toArray(),row,col,isVertical);
       int score = board.tryPlaceWord(w);
       if(score>0)
       {

       }
       //if the score 0 - can't place the word
       else{

       }
       hasChanged();
       notifyObservers();

    }

    @Override
    public void challenge(String word) {


        hasChanged();
        notifyObservers();
    }

    @Override
    public void takeTileFromBag() {
        Tile t = bag.getRand();
        myPlayer.addTile(t);
        hasChanged();
        notifyObservers();
    }

    @Override
    public void passTheTurn() {
        currentPlayerIndex++;
        currentPlayerIndex %= 4;
        hasChanged();
        notifyObservers();
    }

    @Override
    public void setBoardStatus(Tile[][] board) {
        prevBoard = board;
        hasChanged();
        notifyObservers();
    }

    @Override
    public Tile[][] getBoardStatus() {
        return board.getTiles();
    }

    @Override
    public int getNumberOfTilesInBag() {return bag.totalTiles;}

    @Override
    public HashMap<Integer, Integer> getPlayersScores() {
        HashMap<Integer, Integer> playersScore = new HashMap<>();
        for (Integer idP: connectedPlayers.keySet())
            playersScore.put(idP,myPlayer.getScore());
        return playersScore;
    }

    @Override
    public HashMap<Integer, Integer> getPlayersNumberOfTiles() {
        HashMap<Integer, Integer> playerNumOfTiles = new HashMap<>();
        for (Integer idP: connectedPlayers.keySet())
            playerNumOfTiles.put(idP,myPlayer.getTiles().size());
        return playerNumOfTiles;
    }

    @Override
    public void update(Observable o, Object arg) {
        this.setChanged();
        addObserver((Observer) o);
    }
}
