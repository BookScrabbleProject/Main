package Model;

import Model.gameClasses.Tile;

import java.util.*;

public class GuestModel extends PlayerModel implements Observer {
   // start to write the guest model-Benda
 // commit to jira
    ClientCommunication clientCommunication;
    int numOfTileInBag;
    Character[][] currentBoard;
    HashMap<Integer,Integer> scoreMap;
    HashMap<Integer,Integer> numberOfTilesMap;


    GuestModel(String ip,int port,String name){ // build a model for a player that logged in, with the port and ip address of the host server.
        //super(name); Mekler ya maniyyak
        scoreMap=new HashMap<>();
        numberOfTilesMap=new HashMap<>();
        numOfTileInBag=0;
        currentBoard=new Character[15][15];
        for(int i=0;i<15;i++)
        {
            Arrays.fill(currentBoard[i], '_');
        }
        clientCommunication=new ClientCommunication(ip,port);
    }

    @Override
    public void tryPlaceWord(String word, int col, int row, boolean isVertical) { //sends to the host the request of the player to try and place a new word on the board
    String vertical="0";
        if(isVertical)
            vertical="1";

        String methodName = new Object() {}
                .getClass()
                .getEnclosingMethod()
                .getName();
        clientCommunication.send(this.myPlayer.getId(),methodName,word,String.valueOf(col),String.valueOf(row),vertical);
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
    public void setBoardStatus(Character[][] board) {// need to implement
        currentBoard=board;
    }

    @Override
    public Character[][] getBoardStatus() { // need to implement
        return currentBoard;
    }

    @Override
    public int getNumberOfTilesInBag() {
        return numOfTileInBag;

    } // returns the number of tiles currently in the bag

    @Override
    public HashMap<Integer, Integer> getPlayersScores() { // map from player id to the player's scores
        return scoreMap;
    }

    @Override
    public HashMap<Integer, Integer> getPlayersNumberOfTiles() { //map from players id to the number of tiles that the player has
        return numberOfTilesMap;
    }

    public void refillPlayerHand(){
        String methodName = new Object() {}
                .getClass()
                .getEnclosingMethod()
                .getName();
        clientCommunication.send(this.myPlayer.getId(),methodName);
    }

    @Override
    public List<Character> getMyHand() {
        return null;
    }

    @Override
    public void update(Observable o, Object arg) {
        String argString=(String)arg;
        String[] splitedArgString = argString.split(",");
        int id=Integer.parseInt(splitedArgString[0]);
        String methodName=splitedArgString[1];
        String[] arguments=splitedArgString[2].split(",");
        switch(methodName){
            case "setId":
                myPlayer.setId(Integer.parseInt(arguments[0]));
                break;
            case"newPlayerConnected":
                for (String ids:arguments) {
                    scoreMap.put(Integer.parseInt(ids),0);
                    numberOfTilesMap.put(Integer.parseInt(ids),0);// either 0 or 7 depends if we create the hand before the game started.
                    break;
                }
            case"tryPlaceWord":
            {
                if(arguments[0].equals("1"))
                    refillPlayerHand();
                else // let him put another word? or pass the turn automaticaly-take a tile from the bag and pass the turn
                    takeTileFromBag();
                passTheTurn();
                break;
            }
            case"challenge":
            {
                if(arguments[0].equals("1")) // need to update the player who challenged score, and decrease the player that put the word score
                break;
            }
            case "takeTileFromBag": { // the host adds me a tile, or i get char and score and add them as a new Tile to my hand
                myPlayer.addTile(new Tile(arguments[0],Integer.parseInt(arguments[1]))); //כושילדודה של זה
                break;
            }


            case "refillPlayerHand": {

                break;
            }

        }
    }
}
