package Model;

import Model.gameClasses.Tile;

import java.util.HashMap;
import java.util.List;

public interface Model {
    public void tryPlaceWord(String word, int col, int row, boolean isVertical);
    public void challenge(String word);
    public void takeTileFromBag();
    public void passTheTurn();

    public void setBoardStatus(Character[][] board);

    public Character[][] getBoardStatus();
    public int getNumberOfTilesInBag();
    public int getCurrentPlayerId();
    public HashMap<Integer, Integer> getPlayersScores();
    public HashMap<Integer, Integer> getPlayersNumberOfTiles(); // ?? String? - num of tiles supposed to be integer
    public List<Tile> getMyTiles();


}
