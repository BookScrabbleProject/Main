package Model;

import Model.gameClasses.Tile;

import java.util.*;

public class GuestModel extends PlayerModel implements Observer {
   // start to write the guest model-Benda
 // commit to jira
    ClientCommunication clientCommunication;


    GuestModel(String ip,int port,PlayerModel model ){ // build a model for a player that logged in, with the port and ip address of the host of the server
      clientCommunication=new ClientCommunication();
    }

    @Override
    public void tryPlaceWord(String word, int col, int row, boolean isVertical) { //sends to the host the request of the player to try and place a new word on the board
    String vertical="0";
        if(isVertical)
            vertical="1";

        String methodName = new Object() {}
                .getClass()
                .getEnclosingMethod()
                .getName();     clientCommunication.send(this.myPlayer.getId(),methodName,word,String.valueOf(col),String.valueOf(row),vertical);
    }


 @Override
    public void challenge(String word) { // sends to the host the request of the player to challenge the word
     String methodName = new Object() {}
             .getClass()
             .getEnclosingMethod()
             .getName();
     clientCommunication.send(this.myPlayer.getId(),methodName,word);
    }

    @Override
    public void takeTileFromBag() { // takes a tile from the bag and updates the tiles number in the bag
        String methodName = new Object() {}
                .getClass()
                .getEnclosingMethod()
                .getName();
    clientCommunication.send(this.myPlayer.getId(),methodName);
    }

    @Override
    public void passTheTurn() { //switch the turn to the next player
        String methodName = new Object() {}
                .getClass()
                .getEnclosingMethod()
                .getName();
        clientCommunication.send(this.myPlayer.getId(),methodName);
    }

    @Override
    public void setBoardStatus(Tile[][] board) {
    }

    @Override
    public Tile[][] getBoardStatus() {
        String methodName = new Object() {}
                .getClass()
                .getEnclosingMethod()
                .getName();
        clientCommunication.send(this.myPlayer.getId(),methodName);
    }

    @Override
    public int getNumberOfTilesInBag() {
        String methodName = new Object() {}
                .getClass()
                .getEnclosingMethod()
                .getName();
        clientCommunication.send(this.myPlayer.getId(),methodName);
    } // returns the number of tiles currently in the bag

    @Override
    public HashMap<Integer, Integer> getPlayersScores() {
        String methodName = new Object() {}
                .getClass()
                .getEnclosingMethod()
                .getName();
        clientCommunication.send(this.myPlayer.getId(),methodName);
    } // map from player id to the players scores

    @Override
    public HashMap<Integer, Integer> getPlayersNumberOfTiles() { // should be integer to integer? map from players id to the number of tiles that the player has
        String methodName = new Object() {}
                .getClass()
                .getEnclosingMethod()
                .getName();
        clientCommunication.send(this.myPlayer.getId(),methodName);
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
