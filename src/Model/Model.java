package Model;

import Model.gameClasses.Player;
import Model.gameClasses.Tile;

import java.util.HashMap;
import java.util.List;
import java.util.Observable;

public interface Model {
    public void tryPlaceWord(String word, int col, int row, boolean isVertical);
    public void challenge(String word);
    public void takeTileFromBag();
    public void passTheTurn();

    public void setBoardStatus(Tile[][] board);

    public Tile[][] getBoardStatus();
    public int getNumberOfTilesInBag();
    public int getCurrentPlayerId();
    public HashMap<Integer, Integer> getPlayersScores();
    public HashMap<Integer, Integer> getPlayersNumberOfTiles();
    public List<Tile> getMyTiles();


}
