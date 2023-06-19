package Model.tests;

import Model.HostModel;
import Model.gameClasses.BookScrabbleHandler;
import Model.gameClasses.MyServer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class HostModelTest {
    /**
     * Method that tests the connection to the host model and creating the players with the lists of players
     * @param socket - represent client
     * @param socket2 - represent client
     * @param hostModel - represent the host model
     * @param hostServerPort - represent the port
     */
    public static void addPlayerTest(Socket socket,Socket socket2,HostModel hostModel,int hostServerPort){
        try {
            Boolean b = true;
            PrintWriter printWriter1 = new PrintWriter(socket.getOutputStream());
            printWriter1.println("-1:connect:_");
            Thread.sleep(1000);
            Scanner scanner = new Scanner(socket.getInputStream());
            String line1 = scanner.next();
            Checker.checkResult("1:setId:1",line1,"addPlayerTest");
            line1 = scanner.next();
            Checker.checkResult("1:tilesWithScores:A-1,B-3,C-3,D-2,E-1,F-4,G-2,H-4,I-1,J-8,K-5,L-1,M-3,N-1,O-1,P-3,Q-10,R-1,S-1,T-1,U-1,V-4,W-4,X-8,Y-4,Z-10",line1,"addPlayerTest");
            PrintWriter printWriter2 = new PrintWriter(socket2.getOutputStream());
            printWriter2.println("-1:connect:_");
            Thread.sleep(1000);
            Scanner scanner2 = new Scanner(socket2.getInputStream());
            String line2 = scanner2.next();
            Checker.checkResult("2:setId:2",line2,"addPlayerTest");
            line2 = scanner2.next();
            Checker.checkResult("2:tilesWithScores:A-1,B-3,C-3,D-2,E-1,F-4,G-2,H-4,I-1,J-8,K-5,L-1,M-3,N-1,O-1,P-3,Q-10,R-1,S-1,T-1,U-1,V-4,W-4,X-8,Y-4,Z-10",line2,"addPlayerTest");
            line1 = scanner.next();
            Checker.checkResult("-1:playersListUpdated:1-guest,0-default",line1,"addPlayerTest");
            Checker.finishTest("addPlayerTest");
        } catch (IOException | InterruptedException e) {throw new RuntimeException(e);}
    }

    public static void takeTileFromBagTest(Socket socket,Socket socket2,HostModel hostModel,int hostServerPort){
        hostModel.update(null,"1:takeTileFromBag:1");
        try {
            Scanner scanner1=new Scanner(socket.getInputStream());
            Scanner scanner2=new Scanner(socket2.getInputStream());
            String[] line1= scanner1.next().split(":");
            String[] line2=scanner2.next().split(":");
            Checker.checkResult("setHand", line1[1], "takeTileFromBagTest-guestToHost");
            Checker.checkResult("numOfTilesUpdated",line2[1],"takeTileFromBagTest-guestToHost");
            line1=scanner1.next().split(":");
            Checker.checkResult("numOfTilesUpdated",line1[1],"takeTileFromBagTest-guestToHost");
            line1=scanner1.next().split(":");
            Checker.checkResult("newPlayerTurn",line1[1],"takeTileFromBagTest-guestToHost");
            Checker.checkResult("1",line1[2],"takeTileFromBagTest-guestToHost");
            line2=scanner2.next().split(":");
            Checker.checkResult("newPlayerTurn",line2[1],"takeTileFromBagTest-guestToHost");
            Checker.checkResult("1",line2[2],"takeTileFromBagTest-guestToHost");
            Checker.finishTest("takeTileFromBagTest-guestToHost");
            hostModel.takeTileFromBag();
            line1=scanner1.next().split(":");
            Checker.checkResult("numOfTilesUpdated",line1[1],"takeTileFromBagTest-host");
            line2=scanner2.next().split(":");
            Checker.checkResult("numOfTilesUpdated",line2[1],"takeTileFromBagTest-host");
            line1=scanner1.next().split(":");
            Checker.checkResult("newPlayerTurn",line1[1],"takeTileFromBagTest-host");
            Checker.checkResult("2",line1[2],"takeTileFromBagTest-host");
            line2=scanner2.next().split(":");
            Checker.checkResult("newPlayerTurn",line2[1],"takeTileFromBagTest-host");
            Checker.checkResult("2",line2[2],"takeTileFromBagTest-host");
            Checker.finishTest("takeTileFromBagTest-host");
        } catch (IOException e) {throw new RuntimeException(e);}
    }
    public static void tryPlaceWordTest(Socket socket,Socket socket2,HostModel hostModel,int hostServerPort){
        hostModel.update(null,"1:tryPlaceWord:HAPPY,3,3,1,0");
        try {
            Scanner scanner1=new Scanner(socket.getInputStream());
            String[] line1=scanner1.next().split(":");
            Checker.checkResult("tryPlaceWord",line1[1],"tryPlaceWordTest-guestToHost");
            Checker.checkResult("0",line1[2],"tryPlaceWordTest-guestToHost");
            String boardStatus = matrixToString(hostModel.getBoardStatus());
            hostModel.update(null,"1:tryPlaceWord:WHALE,7,7,1,1");
            Checker.checkUnEqual(boardStatus, matrixToString(hostModel.getBoardStatus()),"tryPlaceWordTest-guestToHost");
            // check if player 1 got the new board status
            line1=scanner1.next().split(":");
            Checker.checkResult("1",line1[0],"tryPlaceWordTest-guestToHost");
            Checker.checkResult("setHand",line1[1],"tryPlaceWordTest-guestToHost");
            Checker.checkUnEqual(boardStatus, line1[2],"tryPlaceWordTest-guestToHost");
            // Check if player 1 got the information about the score update
            line1=scanner1.next().split(":");
            Checker.checkResult("1",line1[0],"tryPlaceWordTest-guestToHost");
            Checker.checkResult("boardUpdated",line1[1],"tryPlaceWordTest-guestToHost");
            // Check if player 1 got the information about the new hand
            line1=scanner1.next().split(":");
            Checker.checkResult("1",line1[0],"tryPlaceWordTest-guestToHost");
            Checker.checkResult("scoreUpdated",line1[1],"tryPlaceWordTest-guestToHost");
            // Check if player 1 got the information about the number of tiles updated
            line1=scanner1.next().split(":");
            Checker.checkResult("1",line1[0],"tryPlaceWordTest-guestToHost");
            Checker.checkResult("numOfTilesUpdated",line1[1],"tryPlaceWordTest-guestToHost");
            // Check if player 1 got the information about the tryPlaceWord action and the score (12*2=24 points for WHALE)
            line1=scanner1.next().split(":");
            Checker.checkResult("1",line1[0],"tryPlaceWordTest-guestToHost");
            Checker.checkResult("tryPlaceWord",line1[1],"tryPlaceWordTest-guestToHost");
            Checker.checkResult("24",line1[2],"tryPlaceWordTest-guestToHost");
            Scanner scanner2=new Scanner(socket2.getInputStream());
            // Check if player 2 got the new board status
            String[] line2=scanner2.next().split(":");
            Checker.checkResult("1",line2[0],"tryPlaceWordTest-guestToHost");
            Checker.checkResult("boardUpdated",line2[1],"tryPlaceWordTest-guestToHost");
            Checker.checkUnEqual(boardStatus, line2[2],"tryPlaceWordTest-guestToHost");
            // Check if player 2 got the information about the score update
            line2=scanner2.next().split(":");
            Checker.checkResult("1",line2[0],"tryPlaceWordTest-guestToHost");
            Checker.checkResult("scoreUpdated",line2[1],"tryPlaceWordTest-guestToHost");
            // Check if player 2 got the information about the number of tiles updated
            line2=scanner2.next().split(":");
            Checker.checkResult("1",line2[0],"tryPlaceWordTest-guestToHost");
            Checker.checkResult("numOfTilesUpdated",line2[1],"tryPlaceWordTest-guestToHost");
            // Check if player 2 got the information about the tryPlaceWord action and the score (12*2=24 points for WHALE)
            line2=scanner2.next().split(":");
            Checker.checkResult("1",line2[0],"tryPlaceWordTest-guestToHost");
            Checker.checkResult("tryPlaceWord",line2[1],"tryPlaceWordTest-guestToHost");
            Checker.checkResult("24",line2[2],"tryPlaceWordTest-guestToHost");
            Checker.finishTest("tryPlaceWordTest-guestToHost");
            boardStatus = matrixToString(hostModel.getBoardStatus());
            hostModel.tryPlaceWord("_HALE",7,7,false);
            Checker.checkUnEqual(boardStatus, matrixToString(hostModel.getBoardStatus()),"tryPlaceWordTest-Host");
            // Check if the board is updated in the guests players
            line1=scanner1.next().split(":");
            line2=scanner2.next().split(":");
            Checker.checkResult("0",line1[0],"tryPlaceWordTest-Host");
            Checker.checkResult("boardUpdated",line1[1],"tryPlaceWordTest-Host");
            Checker.checkUnEqual(boardStatus, line1[2], "tryPlaceWordTest-Host");
            Checker.checkResult("0",line2[0],"tryPlaceWordTest-Host");
            Checker.checkResult("boardUpdated",line2[1],"tryPlaceWordTest-Host");
            Checker.checkUnEqual(boardStatus, line2[2], "tryPlaceWordTest-Host");
            // Check if the score is updated in the guests players
            line1=scanner1.next().split(":");
            line2=scanner2.next().split(":");
            Checker.checkResult("0",line1[0],"tryPlaceWordTest-Host");
            Checker.checkResult("scoreUpdated",line1[1],"tryPlaceWordTest-Host");
            Checker.checkResult("0",line2[0],"tryPlaceWordTest-Host");
            Checker.checkResult("scoreUpdated",line2[1],"tryPlaceWordTest-Host");
            // Check if the number of tiles is updated in the guests players
            line1=scanner1.next().split(":");
            line2=scanner2.next().split(":");
            Checker.checkResult("0",line1[0],"tryPlaceWordTest-Host");
            Checker.checkResult("numOfTilesUpdated",line1[1],"tryPlaceWordTest-Host");
            Checker.checkResult("0",line2[0],"tryPlaceWordTest-Host");
            Checker.checkResult("numOfTilesUpdated",line2[1],"tryPlaceWordTest-Host");
            // Check if the guests players get the information about the tryPlaceWord action and the score of the word (12 points for _HALE)
            line1=scanner1.next().split(":");
            line2=scanner2.next().split(":");
            Checker.checkResult("0",line1[0],"tryPlaceWordTest-Host");
            Checker.checkResult("tryPlaceWord",line1[1],"tryPlaceWordTest-Host");
            Checker.checkResult("12",line1[2],"tryPlaceWordTest-Host");
            Checker.checkResult("0",line2[0],"tryPlaceWordTest-Host");
            Checker.checkResult("tryPlaceWord",line2[1],"tryPlaceWordTest-Host");
            Checker.checkResult("12",line2[2],"tryPlaceWordTest-Host");
            Checker.finishTest("tryPlaceWordTest-Host");
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    public static void challengeTest(Socket socket,Socket socket2,HostModel hostModel,int hostServerPort) {
        // id =1 whale try - ok
        //id =2 whale challenge -0
        hostModel.update(null,"2:challenge:_HALE,0"); // challenge success
        try {
            Scanner scanner1 = new Scanner(socket.getInputStream());
            String[] line1 = scanner1.next().split(":");
            Scanner scanner2 = new Scanner(socket2.getInputStream());
            String[] line2 = scanner2.next().split(":");
            //success
            // need to check board unchanged
            Checker.checkResult("0",line1[0],"challenge-guestToHostSuccess");//1
            Checker.checkResult("boardUpdated",line1[1],"challenge-guestToHostSuccess");//2
            String boardStatus = matrixToString(hostModel.getBoardStatus());
            Checker.checkResult(boardStatus.toString(),line1[2],"challenge-guestToHostSuccess");//3 -
            Checker.checkResult("2",line2[0],"challenge-guestToHostSuccess");//4
            Checker.checkResult("setHand",line2[1],"challenge-guestToHostSuccess");//5
            Checker.checkResult("HALE",line2[2],"challenge-guestToHostSuccess");//6 -
            //Hand - need to check setHand + need to check num of tiles changed
            line2 = scanner2.next().split(":");
            Checker.checkResult("0",line2[0],"challenge-guestToHostSuccess");//7
            Checker.checkResult("boardUpdated",line2[1],"challenge-guestToHostSuccess");//8
            List<Character> handTiles =hostModel.getConnectedPlayers().get(hostModel.getCurrentPlayerId()).getTiles();
            StringBuilder handInStringBuilder = new StringBuilder();
            for (Character c: handTiles)
                handInStringBuilder.append(c);
            String handInString = handInStringBuilder.toString();
            // check setHand tiles changed
            Checker.checkResult(boardStatus,line2[2],"challenge-guestToHostSuccess");//9
            //check num of tiles in id =1,2 changed
            line1 = scanner1.next().split(":");
            line2 = scanner2.next().split(":");
            String  myNumOfTiles = String.valueOf(hostModel.getConnectedPlayers().get(hostModel.getCurrentPlayerId()).getTiles().size());
            Checker.checkResult("2",line1[0],"challenge-guestToHostSuccess");//10
            Checker.checkResult("numOfTilesUpdated",line1[1],"challenge-guestToHostSuccess");//11
            Checker.checkResult(myNumOfTiles,line1[2],"challenge-guestToHostSuccess");//12
            Checker.checkResult("2",line2[0],"challenge-guestToHostSuccess");//13 -
            Checker.checkResult("numOfTilesUpdated",line2[1],"challenge-guestToHostSuccess");//14
            Checker.checkResult(myNumOfTiles,line2[2],"challenge-guestToHostSuccess");//15
            // need to check score changed
            line1 = scanner1.next().split(":");
            line2 = scanner2.next().split(":");
            String scoreAfterChange = String.valueOf(hostModel.getConnectedPlayers().get(hostModel.getCurrentPlayerId()).getScore());
            Checker.checkResult("0",line1[0],"challenge-guestToHostSuccess");//16
            Checker.checkResult("scoreUpdated",line1[1],"challenge-guestToHostSuccess");//17
            Checker.checkResult(scoreAfterChange,line1[2],"challenge-guestToHostSuccess");//18
            Checker.checkResult("0",line2[0],"challenge-guestToHostSuccess");//19
            Checker.checkResult("scoreUpdated",line2[1],"challenge-guestToHostSuccess");//20
            Checker.checkResult(scoreAfterChange,line2[2],"challenge-guestToHostSuccess");//21
            // need to check send to all players received - success
            line1 = scanner1.next().split(":");
            line2 = scanner2.next().split(":");
            Checker.checkResult("2",line1[0],"challenge-guestToHostSuccess");//22
            Checker.checkResult("challenge",line1[1],"challenge-guestToHostSuccess");//23
            Checker.checkResult("0,_HALE",line1[2],"challenge-guestToHostSuccess");//24
            Checker.checkResult("2",line2[0],"challenge-guestToHostSuccess");//25
            Checker.checkResult("challenge",line2[1],"challenge-guestToHostSuccess");//26
            Checker.checkResult("0,_HALE",line2[2],"challenge-guestToHostSuccess");//27
            Checker.finishTest("challenge-guestToHostSuccess");
            String preTurnBoardStatus = matrixToString(hostModel.getBoardStatus());
            int playerPreviousScore=hostModel.getPlayersScores().get(2);
            hostModel.update(null,"2:tryPlaceWord:HAPPY,5,9,0,1");// good word - player number 2 successfully puts the word -maybe should be hostModel.tryPlaceWord....
            hostModel.update(null,"1:challenge:_HALE,1"); // challenge by player 1 failed -word was good
            line2=scanner2.next().split(":");
            line2=scanner2.next().split(":");
            Checker.checkResult("-1",line2[0],"challenge-guestToHostFailed"); //1
            Checker.checkResult("newPlayerTurn",line2[1],"challenge-guestToHostFailed");//2
            List<Character> myTiles =hostModel.getConnectedPlayers().get(2).getTiles();
            StringBuilder handStringBuilder = new StringBuilder();
            for (Character c: myTiles)
                handStringBuilder.append(c);
            String handIntoString = handStringBuilder.toString();
            Checker.checkResult("0",line2[2],"challenge-guestToHostFailed"); // 3
            line1=scanner1.next().split(":");
            Checker.checkResult("-1",line1[0],"challenge-guestToHostFailed"); //4
            Checker.checkResult("newPlayerTurn",line1[1],"challenge-guestToHostFailed");//5
            line2=scanner2.next().split(":");
            Checker.checkResult("1",line2[0],"challenge-guestToHostFailed"); //6
            Checker.checkResult("challenge",line2[1],"challenge-guestToHostFailed"); //7
            int playerNumOfTiles=hostModel.getConnectedPlayers().get(2).getTiles().size();
            Checker.checkResult("8", String.valueOf(playerNumOfTiles),"challenge-guestToHostFailed"); //8
            int playerNewScore=hostModel.getConnectedPlayers().get(2).getScore();
            if(playerNewScore==playerPreviousScore)
                System.out.println("The player's score didn't change");
            line1=scanner1.next().split(":");
            Checker.checkResult("1",line1[0],"challenge-guestToHostFailed"); //9
            Checker.checkResult("challenge",line1[1],"challenge-guestToHostFailed"); //10
            line2=scanner2.next().split(":");
            Checker.checkResult("2",line2[0],"challenge-guestToHostFailed"); //11
            Checker.checkResult("numOfTilesUpdated",line2[1],"challenge-guestToHostFailed"); //12
            line1=scanner1.next().split(":");
            Checker.checkResult("2",line1[0],"challenge-guestToHostFailed"); //13
            Checker.checkResult("numOfTilesUpdated",line1[1],"challenge-guestToHostFailed"); //14
            Checker.checkResult("8",line1[2],"challenge-guestToHostFailed"); //`15
            line2=scanner2.next().split(":");
            Checker.checkResult("1",line2[0],"challenge-guestToHostFailed"); //16
            Checker.checkResult("challenge",line2[1],"challenge-guestToHostFailed"); //17
            Checker.checkResult("_HALE",line2[2],"challenge-guestToHostFailed"); //18
            String PostTurnBoardStatus = matrixToString(hostModel.getBoardStatus());
            Checker.checkResult(PostTurnBoardStatus,preTurnBoardStatus,"challenge-guestToHostFailed");//19
            Checker.finishTest("challenge-guestToHostFailed");
            //all work until here
            //all work until here

            //host test
            hostModel.challenge("_HALE");
            //check board updated
            line1=scanner1.next().split(":");
            line2=scanner2.next().split(":");
            Checker.checkResult("1",line1[0],"challenge-Host"); //1
            Checker.checkResult("challenge",line1[1],"challenge-Host");//2
            Checker.checkUnEqual(boardStatus, line1[2], "challenge-Host");//3
            Checker.checkResult("1",line2[0],"challenge-Host");//4
            Checker.checkResult("challenge",line2[1],"challenge-Host");//5
            Checker.checkUnEqual(boardStatus, line2[2], "challenge-Host");//6
            //check if the hands update
            line1=scanner1.next().split(":");
            line2=scanner2.next().split(":");
            Checker.checkResult("0",line2[0],"challenge-Host");//7
            Checker.checkResult("boardUpdated",line2[1],"challenge-Host");//8
            String handTilesGuest = listToString(hostModel.getConnectedPlayers().get(1).getTiles());
            Checker.checkResult(boardStatus,line2[2],"challenge-Host");//9
            Checker.checkResult("1",line1[0],"challenge-Host");//10
            Checker.checkResult("challenge",line1[1],"challenge-Host");//11
            Checker.checkResult("1",line1[2],"challenge-Host");//12
            // Check if the number of tiles is updated in the guests playerss
            line1=scanner1.next().split(":");
            line2=scanner2.next().split(":");
            Checker.checkResult("0",line1[0],"challenge-Host");//13
            Checker.checkResult("boardUpdated",line1[1],"challenge-Host");//14
            Checker.checkResult("0",line2[0],"challenge-Host");//15
            Checker.checkResult("numOfTilesUpdated",line2[1],"challenge-Host");//16
            // Check if the score is updated in the guests player
            line1=scanner1.next().split(":");
            line2=scanner2.next().split(":");
            Checker.checkResult("0",line1[0],"challenge-Host");//17
            Checker.checkResult("numOfTilesUpdated",line1[1],"challenge-Host");//18
            Checker.checkResult("0",line2[0],"challenge-Host");//19
            Checker.checkResult("scoreUpdated",line2[1],"challenge-Host");//20
            // Check if the guests players get the information about the tryPlaceWord action and the score of the word (12 points for _HALE)
            line1=scanner1.next().split(":");
            line2=scanner2.next().split(":");
            Checker.checkResult(String.valueOf(hostModel.getCurrentPlayerId()),line1[0],"challenge-Host");//21
            Checker.checkResult("scoreUpdated",line1[1],"challenge-Host");//22
            Checker.checkResult("12",line1[2],"challenge-Host");//23
            Checker.checkResult(String.valueOf(hostModel.getCurrentPlayerId()),line2[0],"challenge-Host");//24
            Checker.checkResult("challenge",line2[1],"challenge-Host");//25
            Checker.checkResult("0,_HALE",line2[2],"challenge-Host");//26
            Checker.finishTest("challenge-Host");

        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static String listToString(List<Character> l) {
        StringBuilder sb = new StringBuilder();
        for(Character c : l) {
            sb.append(c);
        }
        return sb.toString();
    }
    private static String matrixToString(Character[][] matrix) {return Arrays.deepToString(matrix).replace(" ","").replace("[","").replace("]","").replace(",","");}
    public static void main(String[] args) {
        HostModel hostModel = HostModel.getHost();
        MyServer myServer = new MyServer(3000,new BookScrabbleHandler());
        myServer.start();
        hostModel.connectToBookScrabbleServer(3001,"localhost",3000);
        int hostServerPort =3001;
        try {
            Socket socket = new Socket("localhost",hostServerPort);
            Socket socket2 = new Socket("localhost",hostServerPort);
            System.out.println(">>> Starting addPlayerTest <<<");
            addPlayerTest(socket,socket2, hostModel,hostServerPort); // test adding players to the server
            System.out.println(">>> Starting takeTileFromBagTest <<<");
            takeTileFromBagTest(socket,socket2,hostModel,hostServerPort);
            System.out.println(">>> Starting tryPlaceWordTest <<<");
            tryPlaceWordTest(socket,socket2,hostModel,hostServerPort);
            System.out.println(">>> Starting challengeTest <<<");
            challengeTest(socket,socket2,hostModel,hostServerPort);

            Thread.sleep(5000);
            System.out.println(">>> Close sockets and Threads <<<");
            socket.close();
            socket2.close();
        } catch (IOException | InterruptedException e) {
            System.out.println("Error: " + e.getMessage());
        }
        hostModel.getHostServer().close();
        myServer.stop();
        System.out.println("done!");
    }
    public static class Checker {
        static Map<String, Integer> testNums = new HashMap<String, Integer>();
        static Map<String, Boolean> testBooleans = new HashMap<String, Boolean>();
        static String green = "\u001B[32m";
        static String red = "\u001B[31m";
        static String reset = "\u001B[0m";
        public static void checkResult(String expected, String actual, String functionTested) {
            if(expected.equals(actual)) {System.out.println(green +functionTested + " passed (" + getNumOfTests(functionTested) + ")" + reset);}
            else {
                System.out.println(red+functionTested + " failed (" + getNumOfTests(functionTested) + ") expected: " + expected + " actual: " + actual + reset);
                testBooleans.put(functionTested,false);
            }
            testNums.put(functionTested, testNums.getOrDefault(functionTested, 1) + 1);
        }
        public static void checkUnEqual(String expected, String actual, String functionTested) {
            if(!expected.equals(actual)) {System.out.println(green +functionTested + " passed (" + getNumOfTests(functionTested) + ")" + reset);}
            else{
                System.out.println(red+functionTested + " failed (" + getNumOfTests(functionTested) + ") expected: " + expected + " actual: " + actual + reset);
                testBooleans.put(functionTested,false);
            }
            testNums.put(functionTested, testNums.getOrDefault(functionTested, 1) + 1);
        }
        public static boolean getBooleanOfTest(String key){
            return testBooleans.getOrDefault(key,true);
        }
        public static void finishTest(String key){
            if(getBooleanOfTest(key)) System.out.println(green+"All tests of "+key+" passed\n" + reset);
            else System.out.println(red+"One or more of the tests for "+key+" failed\n" + reset);
        }
        public static int getNumOfTests(String key) {
            return testNums.getOrDefault(key, 1);
        }
        public static void incrementTestNum(String key) {
            testNums.put(key, getNumOfTests(key) + 1);
        }
    }
}
