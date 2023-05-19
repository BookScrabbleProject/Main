package Model;

import Model.gameClasses.Player;
import Model.gameClasses.Tile;

import java.util.*;

public class GuestModel extends PlayerModel implements Observer {
   // start to write the guest model-Benda
 // commit to jira
    ClientCommunication clientCommunication;
    Tile[][] boardStatus;
    int numberOfTilesInBag;

    @Override
    public void tryPlaceWord(String word, int col, int row, boolean isVertical) {

    }

    @Override
    public void challenge(String word) {

    }

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
