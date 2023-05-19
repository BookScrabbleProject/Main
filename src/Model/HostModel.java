package Model;

import Model.gameClasses.Board;
import Model.gameClasses.Player;
import Model.gameClasses.Tile;

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


    public HostModel getHost() {
        if (hostModel == null) {
            hostModel = new HostModel();
        }
        return hostModel;
    }

    public HostModel() {
        //do something
    }

    @Override
    public void tryPlaceWord(String word, int col, int row, boolean isVertical) {

    }

    @Override
    public void challenge(String word) {

    }
// ilay

    @Override
    public void takeTileFromBag() {

    }

    @Override
    public void passTheTurn() {

    }

    @Override
    public void setBoardStatus(Tile[][] board) {

    }

    @Override
    public Tile[][] getBoardStatus() {
        return new Tile[0][];
    }

    @Override
    public int getNumberOfTilesInBag() {
        return 0;
    }

    @Override
    public HashMap<Integer, Integer> getPlayersScores() {
        return null;
    }

    @Override
    public HashMap<Integer, String> getPlayersNumberOfTiles() {
        return null;
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
