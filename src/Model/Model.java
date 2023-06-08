package Model;

import Model.gameClasses.Player;
import Model.gameClasses.Tile;

import java.util.HashMap;
import java.util.List;
import java.util.Observable;

public abstract class Model extends Observable{
    public abstract void tryPlaceWord(String word, int col, int row, boolean isVertical);
    public abstract void challenge(String word);
    public abstract void takeTileFromBag();
    public abstract Character[][] getBoardStatus();
    public abstract int getNumberOfTilesInBag();
    public abstract int getCurrentPlayerId();
    public abstract HashMap<Integer, Integer> getPlayersScores();
    public abstract HashMap<Integer, Integer> getPlayersNumberOfTiles();
    public abstract List<Character> getMyHand();
}
