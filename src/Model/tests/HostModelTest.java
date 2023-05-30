package Model.tests;

import Model.HostModel;
import Model.gameClasses.BookScrabbleHandler;
import Model.gameClasses.MyServer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

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
            Checker.checkResult("-1:playersListUpdated:1-guest,0-default",line1,"addPlayerTest");
            PrintWriter printWriter2 = new PrintWriter(socket2.getOutputStream());
            printWriter2.println("-1:connect:_");
            Thread.sleep(1000);
            Scanner scanner2 = new Scanner(socket2.getInputStream());
            String line2 = scanner2.next();
            Checker.checkResult("2:setId:2",line2,"addPlayerTest");
            line2 = scanner2.next();
            Checker.checkResult("-1:playersListUpdated:2-guest,0-default,1-guest",line2,"addPlayerTest");
            line1 = scanner.next();
            Checker.checkResult("-1:playersListUpdated:2-guest,0-default,1-guest",line1,"addPlayerTest");
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


        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
    public static void tryPlaceWordTest(Socket socket,Socket socket2,HostModel hostModel,int hostServerPort){
        hostModel.update(null,"1:tryPlaceWord:HAPPY,3,3,1,0");
        try {
            Scanner scanner1=new Scanner(socket.getInputStream());
            String[] line1=scanner1.next().split(":");
            Checker.checkResult("tryPlaceWord",line1[1],"tryPlaceWordTest-guestToHost");
            Checker.checkResult("0",line1[2],"tryPlaceWordTest-guestToHost");

            String boardStatus = Arrays.deepToString(hostModel.getBoardStatus());
            hostModel.update(null,"1:tryPlaceWord:WHALE,7,7,1,1");
            Checker.checkUnEqual(boardStatus, Arrays.deepToString(hostModel.getBoardStatus()),"tryPlaceWordTest-guestToHost");

            // check if player 1 got the new board status
            line1=scanner1.next().split(":");
            Checker.checkResult("1",line1[0],"tryPlaceWordTest-guestToHost");
            Checker.checkResult("boardUpdated",line1[1],"tryPlaceWordTest-guestToHost");
            Checker.checkUnEqual(boardStatus, line1[2],"tryPlaceWordTest-guestToHost");

            // Check if player 1 got the information about the score update
            line1=scanner1.next().split(":");
            Checker.checkResult("1",line1[0],"tryPlaceWordTest-guestToHost");
            Checker.checkResult("scoreUpdated",line1[1],"tryPlaceWordTest-guestToHost");

            // Check if player 1 got the information about the new hand
            line1=scanner1.next().split(":");
            Checker.checkResult("1",line1[0],"tryPlaceWordTest-guestToHost");
            Checker.checkResult("setHand",line1[1],"tryPlaceWordTest-guestToHost");

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

            boardStatus = Arrays.deepToString(hostModel.getBoardStatus());

            hostModel.tryPlaceWord("_HALE",7,7,false);
            Checker.checkUnEqual(boardStatus, Arrays.deepToString(hostModel.getBoardStatus()),"tryPlaceWordTest-Host");

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





        } catch (IOException e) {
            throw new RuntimeException(e);
        }













    }









    /*  // todo - need to upload words before testing ->BoardUpdatedTest


     *//**
     * Method that test if the board was updated
     * @param socket - represent client
     * @param socket2 - represent client
     *//*
    public static  void BoardUpdatedTest(Socket socket, Socket socket2){
        *//*try {
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {throw new RuntimeException(e);}*//*
    }

    *//**
     * Method that test the hand of the players and number of tiles in other players
     * @param socket - represent client
     * @param socket2 - represent client
     *//*
    public static void HandUpdateTest(Socket socket, Socket socket2){}

    *//**
     * Method that test of the turn was passed
     * @param socket - represent client
     * @param socket2 - represent client
     *//*
    public static  void passTheTurnTest(Socket socket, Socket socket2){}

    *//**
     * Mehod that test if the update in case tryPlaceWord update all the players and worked
     * @param hostModel - represent the host model
     *//*
    public static void tryPlaceWordUpdateTests(HostModel hostModel) {
        *//*hostModel.update(null,"1:tryPlaceWord:1");
        hostModel.update(null,"2:tryPlaceWord:0");
        hostModel.update(null,"3:tryPlaceWord:1");*//*
    }
    *//**
     * Method that test if the update in case challenge update all the players and worked
     * @param hostModel - represent the host model
     *//*
    public static void challengeUpdateTests(HostModel hostModel) {
        *//*hostModel.update(null,"1:challenge:0");
        hostModel.update(null,"2:challenge:0");
        hostModel.update(null,"3:challenge:1");*//*
    }
    *//**
     * Method that test if the update in case takeTileFromBag update all the players and worked
     //* @param hostModel - represent the host model
     *//*
    public static void takeTileFromBagUpdateTests(HostModel hostModel) {
        hostModel.


                hostModel.update(null,"1:takeTileFromBag");
        hostModel.update(null,"2:takeTileFromBag");
        hostModel.update(null,"3:takeTileFromBag");
    }*/
    public static void main(String[] args) {

        HostModel hostModel = HostModel.getHost();
        MyServer myServer = new MyServer(3000,new BookScrabbleHandler());
        myServer.start();
        hostModel.connectToBookScrabbleServer(3001,"localhost",3000);
        int hostServerPort =3001;
        try {
            Socket socket = new Socket("localhost",hostServerPort);
            Socket socket2 = new Socket("localhost",hostServerPort);
            addPlayerTest(socket,socket2, hostModel,hostServerPort); // test adding players to the server
            takeTileFromBagTest(socket,socket2,hostModel,hostServerPort);
            tryPlaceWordTest(socket,socket2,hostModel,hostServerPort);
            /* BoardUpdatedTest(socket,socket2); // test if the board updated
            HandUpdateTest(socket,socket2); // refill hand + take tile from bag
            passTheTurnTest(socket,socket2); // pass the turn test
            tryPlaceWordUpdateTests(hostModel); // test for case try place word in update
            challengeUpdateTests(hostModel); // test for case challenge in update


            takeTileFromBagUpdateTests(hostModel); // test for case take tile from bag in update
*/
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    public static class Checker {
        static Map<String, Integer> testNums = new HashMap<String, Integer>();
        static Map<String, Boolean> testBooleans = new HashMap<String, Boolean>();
        static String green = "\u001B[32m";
        static String red = "\u001B[31m";
        static String reset = "\u001B[0m";


        public static void checkResult(String expected, String actual, String functionTested) {
            if(expected.equals(actual)) {
                System.out.println(green +functionTested + " passed (" + getNumOfTests(functionTested) + ")" + reset);
            }else {
                System.out.println(red+functionTested + " failed (" + getNumOfTests(functionTested) + ") expected: " + expected + " actual: " + actual + reset);
                testBooleans.put(functionTested,false);
            }

            testNums.put(functionTested, testNums.getOrDefault(functionTested, 1) + 1);
        }

        public static void checkUnEqual(String expected, String actual, String functionTested) {
            if(!expected.equals(actual)) {
                System.out.println(green +functionTested + " passed (" + getNumOfTests(functionTested) + ")" + reset);
            }else {
                System.out.println(red+functionTested + " failed (" + getNumOfTests(functionTested) + ") expected: " + expected + " actual: " + actual + reset);
                testBooleans.put(functionTested,false);
            }

            testNums.put(functionTested, testNums.getOrDefault(functionTested, 1) + 1);
        }
        public static boolean getBooleanOfTest(String key){
            return testBooleans.getOrDefault(key,true);
        }
        public static void finishTest(String key){
            if(getBooleanOfTest(key))
                System.out.println(green+"All tests of "+key+" passed\n" + reset);
            else
                System.out.println(red+"One or more of the tests for "+key+" failed\n" + reset);
        }

        public static int getNumOfTests(String key) {
            return testNums.getOrDefault(key, 1);
        }

        public static void incrementTestNum(String key) {
            testNums.put(key, getNumOfTests(key) + 1);
        }
    }


}
