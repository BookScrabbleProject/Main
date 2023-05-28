package Model.tests;

import Model.HostModel;
import Model.gameClasses.BookScrabbleHandler;
import Model.gameClasses.MyServer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
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
            if(line1.equals("1:setId:1"))
                System.out.println("addPlayerTest(1) passed");
            else {
                System.out.println("addPlayerTest(1) failed");
                System.out.println("expected output: 1:setId:1 "+"reasult: " + line1 );
                b = false;
            }
            line1 = scanner.next();
            if(line1.equals("-1:playersListUpdated:1-guest,0-default"))
                System.out.println("addPlayerTest(2) passed");
            else {
                System.out.println("addPlayerTest(2) failed");
                System.out.println("expected output: -1:playersListUpdated:1-guest,0-default "+"result: " + line1 );
                b = false;
            }
            PrintWriter printWriter2 = new PrintWriter(socket2.getOutputStream());
            printWriter2.println("-1:connect:_");
            Thread.sleep(1000);
            Scanner scanner2 = new Scanner(socket2.getInputStream());
            String line2 = scanner2.next();
            if(line2.equals("2:setId:2"))
                System.out.println("addPlayerTest(3) passed");
            else {
                System.out.println("addPlayerTest(3) failed");
                System.out.println("expected output: 2:setId:2 "+"result: " + line2 );
                b = false;
            }
            line2 = scanner2.next();
            if(line2.equals("-1:playersListUpdated:2-guest,0-default,1-guest"))
                System.out.println("addPlayerTest(4) passed");
            else {
                System.out.println("addPlayerTest(4) failed");
                System.out.println("expected output: -1:playersListUpdated:2-guest,0-default,1-guest "+"result: " + line2 );
                b = false;
            }
            line1 = scanner.next();
            if(line1.equals("-1:playersListUpdated:2-guest,0-default,1-guest"))
                System.out.println("addPlayerTest(5) passed");
            else{
                System.out.println("addPlayerTest(5) failed");
                System.out.println("expected output: -1:playersListUpdated:2-guest,0-default,1-guest "+"result: " + line1);
                b = false;
            }
            if(b= true) System.out.println("Add Player Tests all passed ----> Done \n");
            else System.out.println("There is ERROR in one or more tests");
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
            hostModel.update(null,"1:tryPlaceWord:HAPPY,6,6,1,1");
            line1=scanner1.next().split(":");
            Checker.checkResult("-1",line1[0],"tryPlaceWordTest-guestToHost");
            Checker.checkResult("boardUpdated",line1[1],"tryPlaceWordTest-guestToHost");
            line1=scanner1.next().split(":");
            Checker.checkResult("1",line1[0],"tryPlaceWordTest-guestToHost");
            Checker.checkResult("scoreUpdated",line1[1],"tryPlaceWordTest-guestToHost");
            line1=scanner1.next().split(":");
            Checker.checkResult("1",line1[0],"tryPlaceWordTest-guestToHost");
            Checker.checkResult("setHand",line1[1],"tryPlaceWordTest-guestToHost");
            line1=scanner1.next().split(":");
            Checker.checkResult("1",line1[0],"tryPlaceWordTest-guestToHost");
            Checker.checkResult("numOfTilesUpdated",line1[1],"tryPlaceWordTest-guestToHost");
            line1=scanner1.next().split(":");
            Checker.checkResult("1",line1[0],"tryPlaceWordTest-guestToHost");
            Checker.checkResult("tryPlaceWord",line1[1],"tryPlaceWordTest-guestToHost");
            Scanner scanner2=new Scanner(socket2.getInputStream());
            String[] line2=scanner2.next().split(":");
            Checker.checkResult("-1",line2[0],"tryPlaceWordTest-guestToHost");
            Checker.checkResult("boardUpdated",line2[1],"tryPlaceWordTest-guestToHost");
            line2=scanner2.next().split(":");
            Checker.checkResult("1",line2[0],"tryPlaceWordTest-guestToHost");
            Checker.checkResult("scoreUpdated",line2[1],"tryPlaceWordTest-guestToHost");
            line2=scanner2.next().split(":");
            Checker.checkResult("1",line2[0],"tryPlaceWordTest-guestToHost");
            Checker.checkResult("numOfTilesUpdated",line2[1],"tryPlaceWordTest-guestToHost");
            line2=scanner2.next().split(":");
            Checker.checkResult("1",line2[0],"tryPlaceWordTest-guestToHost");
            Checker.checkResult("tryPlaceWord",line2[1],"tryPlaceWordTest-guestToHost");
            Checker.finishTest("tryPlaceWordTest-guestToHost");
            hostModel.tryPlaceWord("Happy",3,3,true);




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

        public static void checkResult(String expected, String actual, String functionTested) {
            if(expected.equals(actual)) {
                System.out.println(functionTested + " passed (" + getNumOfTests(functionTested) + ")");
            }else {
                System.out.println(functionTested + " failed (" + getNumOfTests(functionTested) + ")");
                System.out.println("ERROR: expected: " + expected + " actual: " + actual);
                testBooleans.put(functionTested,false);
            }

            testNums.put(functionTested, testNums.getOrDefault(functionTested, 1) + 1);
        }
        public static boolean getBooleanOfTest(String key){
            return testBooleans.getOrDefault(key,true);
        }
        public static void finishTest(String key){
            if(getBooleanOfTest(key))
                System.out.println("All tests of "+key+" passed");
            else
                System.out.println("One or more of the tests for "+key+" failed");
        }

        public static int getNumOfTests(String key) {
            return testNums.getOrDefault(key, 1);
        }

        public static void incrementTestNum(String key) {
            testNums.put(key, getNumOfTests(key) + 1);
        }
    }


}
