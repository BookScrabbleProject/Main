package Model.tests;

import Model.HostModel;
import Model.gameClasses.BookScrabbleHandler;
import Model.gameClasses.MyServer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
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
        if(line1.equals("-1:playersListUpdated:1-null,0-default"))
            System.out.println("addPlayerTest(2) passed");
        else {
            System.out.println("addPlayerTest(2) failed");
            System.out.println("expected output: -1:playersListUpdated:1-null,0-default "+"result: " + line1 );
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
        if(line2.equals("-1:playersListUpdated:2-null,0-default,1-null"))
            System.out.println("addPlayerTest(4) passed");
        else {
            System.out.println("addPlayerTest(4) failed");
            System.out.println("expected output: -1:playersListUpdated:2-null,0-default,1-null "+"result: " + line2 );
            b = false;
        }
        line1 = scanner.next();
        if(line1.equals("-1:playersListUpdated:2-null,0-default,1-null"))
            System.out.println("addPlayerTest(5) passed");
        else{
            System.out.println("addPlayerTest(5) failed");
            System.out.println("expected output: -1:playersListUpdated:2-null,0-default,1-null "+"result: " + line1);
            b = false;
        }
        if(b= true) System.out.println("Add Player Tests all passed ----> Done");
        else System.out.println("There is ERROR in one or more tests");
    } catch (IOException | InterruptedException e) {throw new RuntimeException(e);}
}
    // todo - need to upload words before testing ->BoardUpdatedTest

    /**
     * Method that test if the board was updated
     * @param socket - represent client
     * @param socket2 - represent client
     */
    public static  void BoardUpdatedTest(Socket socket, Socket socket2){
        try {
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    /**
     * Method that test the hand of the players and number of tiles in other players
     * @param socket - represent client
     * @param socket2 - represent client
     */
    public static void HandUpdateTest(Socket socket, Socket socket2){}

    /**
     * Method that test of the turn was passed
     * @param socket - represent client
     * @param socket2 - represent client
     */
    public static  void passTheTurnTest(Socket socket, Socket socket2){}

    /**
     * Mehod that test if the update in case tryPlaceWord update all the players and worked
     * @param hostModel - represent the host model
     */
    public static void tryPlaceWordUpdateTests(HostModel hostModel) {
        hostModel.update(null,"1:tryPlaceWord:1");
        hostModel.update(null,"2:tryPlaceWord:0");
        hostModel.update(null,"3:tryPlaceWord:1");
    }
    /**
     * Method that test if the update in case challenge update all the players and worked
     * @param hostModel - represent the host model
     */
    public static void challengeUpdateTests(HostModel hostModel) {
        hostModel.update(null,"1:challenge:0");
        hostModel.update(null,"2:challenge:0");
        hostModel.update(null,"3:challenge:1");
    }
    /**
     * Method that test if the update in case takeTileFromBag update all the players and worked
     * @param hostModel - represent the host model
     */
    public static void takeTileFromBagUpdateTests(HostModel hostModel) {
        hostModel.update(null,"1:takeTileFromBag");
        hostModel.update(null,"2:takeTileFromBag");
        hostModel.update(null,"3:takeTileFromBag");
    }
    public static void main(String[] args) {

        HostModel hostModel = HostModel.getHost();
        MyServer myServer = new MyServer(3000,new BookScrabbleHandler());
        hostModel.connectToBookScrabbleServer(3001,"localhost",3000);
        int hostServerPort =3001;
        try {
            Socket socket = new Socket("localhost",hostServerPort);
            Socket socket2 = new Socket("localhost",hostServerPort);
            addPlayerTest(socket,socket2, hostModel,hostServerPort); // test adding players to the server
            BoardUpdatedTest(socket,socket2); // test if the board updated
            HandUpdateTest(socket,socket2); // refill hand + take tile from bag
            passTheTurnTest(socket,socket2); // pass the turn test
            tryPlaceWordUpdateTests(hostModel); // test for case try place word in update
            challengeUpdateTests(hostModel); // test for case challenge in update
            takeTileFromBagUpdateTests(hostModel); // test for case take tile from bag in update

        } catch (IOException e) {throw new RuntimeException(e);}
    }


}
