package Model;

import Model.gameClasses.Player;
import Model.gameClasses.Tile;

import java.util.List;
import java.util.Observable;

abstract public class PlayerModel extends Observable  implements Model {
    Player myPlayer;
    int currentPlayerIndex;
    boolean isGameFinished;

    public List<Character> getMyTiles() {
        return myPlayer.getTiles();
    }
    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }
    public int getCurrentPlayerId(){
        return currentPlayerIndex;
    }
}
