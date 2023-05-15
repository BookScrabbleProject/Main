package Model;

import Model.gameClasses.Tile;

import java.util.HashMap;

public interface Model {
    public void tryPlaceWord(String word, int col, int row, boolean isVertical);
    public void challenge(String word);
    public void takeTileFromBag();
    public void passTheTurn();

    public void setBoardStatus(Tile[][] board);

    public Tile[][] getBoardStatus();
    public int getNumberOfTilesInBag();
    public Player getCurrentPlayer();
    public HashMap<Integer, Integer> getPlayersScores();
    public HashMap<Integer, String> getPlayersNumberOfTiles();
    public Tile[] getMyTiles();
}
