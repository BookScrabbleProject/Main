package Model.tests;

import Model.GuestModel;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class GuestModelTest {

    public static void testConnectionToHostServer(ArrayList<Socket> socketArr, ServerSocket serverSocket, List<GuestModel> gmArr) {
        try {

            GuestModel gm1 = gmArr.get(0);
            GuestModel gm2 = gmArr.get(1);
            GuestModel gm3 = gmArr.get(2);
            Socket socket1 = serverSocket.accept();
            socketArr.add(socket1);
            Scanner scanner = new Scanner(socket1.getInputStream());
            Socket socket2 = serverSocket.accept();
            socketArr.add(socket2);
            Scanner scanner2 = new Scanner(socket2.getInputStream());
            Socket socket3 = serverSocket.accept();
            socketArr.add(socket3);
            Scanner scanner3 = new Scanner(socket3.getInputStream());
            String line = scanner.next();
            String line2 = scanner2.next();
            String line3 = scanner3.next();
            if (!line.equals("-1:connect:") || !line2.equals("-1:connect:") || !line3.equals("-1:connect:")) {
                System.out.println("testGuestModel failed (connect)");
            } else {
                System.out.println("testGuestModel passed (connect)");
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            gm1.update(null, "1:setId:1");
            gm2.update(null, "2:setId:2");
            gm1.update(null, "-1:playersListUpdated:2-guest2,1-guest1");
            gm2.update(null, "-1:playersListUpdated:2-guest2,1-guest1");
            gm3.update(null, "3:setId:3");
            gm1.update(null, "-1:playersListUpdated:3-guest3,1-guest1,2-guest2");
            gm2.update(null, "-1:playersListUpdated:3-guest3,1-guest1,2-guest2");
            gm3.update(null, "-1:playersListUpdated:3-guest3,1-guest1,2-guest2");

            if(gm1.getPlayersScores().size()!=3 || gm2.getPlayersScores().size()!=3 || gm3.getPlayersScores().size()!=3) {
                System.out.println("testGuestModel failed (getPlayersScoreMap)");
            }
            else {
                System.out.println("testGuestModel passed (getPlayersScoreMap)");
            }

            if(!gm1.getPlayersNameMap().get(3).equals("guest3") || !gm2.getPlayersNameMap().get(3).equals("guest3") || !gm3.getPlayersNameMap().get(3).equals("guest3")) {
                System.out.println("testGuestModel failed (getPlayersNameMap)");
            }
            else {
                System.out.println("testGuestModel passed (getPlayersNameMap)");
            }

            System.out.print("GuestsName: -----> ");
            System.out.println(String.join(", ",gm1.getPlayersNameMap().values()));
            serverSocket.close();
        } catch (IOException e) {
        }
    }
    private static void testAddTilesToHand(ArrayList<Socket> sockets, ServerSocket serverSocket, ArrayList<GuestModel> gms) {

        // test tryToPlaceWord
        GuestModel gm1 = gms.get(0);
        GuestModel gm2 = gms.get(1);
        GuestModel gm3 = gms.get(2);
        gm1.update(null, "1:setHand:aaaaaaa");
        gm2.update(null, "2:setHand:bbbbbbb");
        gm3.update(null, "3:setHand:ccccccc");
        if(gm1.getMyHand().size()!=7 || gm2.getMyHand().size()!=7 || gm3.getMyHand().size()!=7) {
            System.out.println("testGuestModel failed (getPlayersNumberOfTiles (1))");
        }
        else {
            System.out.println("testGuestModel passed (getPlayersNumberOfTiles (1))");
        }
        if(!gm1.getPlayersNumberOfTiles().get(2).equals(0) || !gm1.getPlayersNumberOfTiles().get(3).equals(0) || !gm2.getPlayersNumberOfTiles().get(1).equals(0) ||
         !gm2.getPlayersNumberOfTiles().get(3).equals(0) || !gm3.getPlayersNumberOfTiles().get(1).equals(0) || !gm3.getPlayersNumberOfTiles().get(2).equals(0)) {
            System.out.println("testGuestModel failed (getPlayersNumberOfTiles (2))");
        }
        else {
            System.out.println("testGuestModel passed (getPlayersNumberOfTiles (2))");
        }
        gm1.update(null, "1:numOfTilesUpdated:7");
        gm2.update(null, "1:numOfTilesUpdated:7");
        gm3.update(null, "1:numOfTilesUpdated:7");

        if(gm1.getPlayersNumberOfTiles().get(1)!=7 || gm2.getPlayersNumberOfTiles().get(1)!=7 || gm3.getPlayersNumberOfTiles().get(1)!=7) {
            System.out.println("testGuestModel failed (getPlayersNumberOfTiles (3))");
        }
        else {
            System.out.println("testGuestModel passed (getPlayersNumberOfTiles (3))");
        }
        gm1.update(null, "2:numOfTilesUpdated:7");
        gm2.update(null, "2:numOfTilesUpdated:7");
        gm3.update(null, "2:numOfTilesUpdated:7");

        if(gm1.getPlayersNumberOfTiles().get(2)!=7 || gm2.getPlayersNumberOfTiles().get(2)!=7 || gm3.getPlayersNumberOfTiles().get(2)!=7) {
            System.out.println("testGuestModel failed (getPlayersNumberOfTiles (3))");
        }
        else {
            System.out.println("testGuestModel passed (getPlayersNumberOfTiles (3))");
        }
        gm1.update(null, "3:numOfTilesUpdated:7");
        gm2.update(null, "3:numOfTilesUpdated:7");
        gm3.update(null, "3:numOfTilesUpdated:7");

        if(gm1.getPlayersNumberOfTiles().get(3)!=7 || gm2.getPlayersNumberOfTiles().get(3)!=7 || gm3.getPlayersNumberOfTiles().get(3)!=7) {
            System.out.println("testGuestModel failed (getPlayersNumberOfTiles (3))");
        }
        else {
            System.out.println("testGuestModel passed (getPlayersNumberOfTiles (3))");
        }


    }

    private static void testScoreUpdate(ArrayList<Socket> sockets, ServerSocket serverSocket, ArrayList<GuestModel> gms) {

        GuestModel gm1 = gms.get(0);
        GuestModel gm2 = gms.get(1);
        GuestModel gm3 = gms.get(2);
        gm1.update(null, "1:scoreUpdated:10");
        gm2.update(null, "1:scoreUpdated:10");
        gm3.update(null, "1:scoreUpdated:10");
        gm1.update(null, "2:scoreUpdated:20");
        gm2.update(null, "2:scoreUpdated:20");
        gm3.update(null, "2:scoreUpdated:20");
        gm1.update(null, "3:scoreUpdated:30");
        gm2.update(null, "3:scoreUpdated:30");
        gm3.update(null, "3:scoreUpdated:30");
        if(gm1.getPlayersScores().get(1)!=10 || gm1.getPlayersScores().get(2)!=20 || gm1.getPlayersScores().get(3)!=30) {
            System.out.println("testGuestModel failed (getPlayersScores (1))");
        }
        else {
            System.out.println("testGuestModel passed (getPlayersScores (1))");
        }
        if(gm2.getPlayersScores().get(1)!=10 || gm2.getPlayersScores().get(2)!=20 || gm2.getPlayersScores().get(3)!=30) {
            System.out.println("testGuestModel failed (getPlayersScores (2))");
        }
        else {
            System.out.println("testGuestModel passed (getPlayersScores (2))");
        }
        if(gm3.getPlayersScores().get(1)!=10 || gm3.getPlayersScores().get(2)!=20 || gm3.getPlayersScores().get(3)!=30) {
            System.out.println("testGuestModel failed (getPlayersScores (3))");
        }
        else {
            System.out.println("testGuestModel passed (getPlayersScores (3))");
        }

    }

    private static void testBoardUpdate(ArrayList<Socket> sockets, ServerSocket serverSocket, ArrayList<GuestModel> gms) {
        GuestModel gm1 = gms.get(0);
        GuestModel gm2 = gms.get(1);
        GuestModel gm3 = gms.get(2);
        StringBuilder sbA = new StringBuilder();
        StringBuilder sbB = new StringBuilder();
        StringBuilder sbC = new StringBuilder();
        for(int i=0;i<15;i++) {
            for(int j=0;j<15;j++) {
                if(i!=j) {
                    sbA.append("a");
                    sbB.append("b");
                    sbC.append("c");
                }
                else
                {
                    sbA.append("_");
                    sbB.append("_");
                    sbC.append("_");
                }
            }
        }
        String aBoard = sbA.toString();
        String bBoard = sbB.toString();
        String cBoard = sbC.toString();

        gm1.update(null, "-1:boardUpdated:"+aBoard);
        gm2.update(null, "-1:boardUpdated:"+bBoard);
        gm3.update(null, "-1:boardUpdated:"+cBoard);
        for(int i=0;i<15;i++){
            for(int j=0;j<15;j++){
                if(i!=j) {
                    if(gm1.getBoardStatus()[i][j]!='a' || gm2.getBoardStatus()[i][j]!='b' || gm3.getBoardStatus()[i][j]!='c') {
                        System.out.println("testGuestModel failed (getBoard (1))");
                        break;
                    }
                }
                else {
                    if(gm1.getBoardStatus()[i][j]!='_' || gm2.getBoardStatus()[i][j]!='_' || gm3.getBoardStatus()[i][j]!='_') {
                        System.out.println("testGuestModel failed (getBoard (2))");
                        break;
                    }
                }
            }
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        for(int i=0;i<15;i++){
            for (int j = 0; j < 15; j++) {
                System.out.print(gm1.getBoardStatus()[i][j]+", ");
            }
            System.out.println();
        }
        System.out.println('\n');

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        for(int i=0;i<15;i++){
            for (int j = 0; j < 15; j++) {
                System.out.print(gm2.getBoardStatus()[i][j]+", ");
            }
            System.out.println();
        }

        System.out.println('\n');

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        for(int i=0;i<15;i++){
            for (int j = 0; j < 15; j++) {
                System.out.print(gm3.getBoardStatus()[i][j]+", ");
            }
            System.out.println();
        }
        System.out.println("\ntestGuestModel passed (getBoard)");

    }


    private static void testNewPlayerTurn(ArrayList<Socket> sockets, ServerSocket serverSocket, ArrayList<GuestModel> gms) {
        GuestModel gm1 = gms.get(0);
        GuestModel gm2 = gms.get(1);
        GuestModel gm3 = gms.get(2);
        boolean flag = true;

        if(gm1.getCurrentPlayerId()!=0 || gm2.getCurrentPlayerId()!=0 || gm3.getCurrentPlayerId()!=0) {
            System.out.println("testGuestModel failed (getCurrentPlayerId)");
        }
        else {
            System.out.println("testGuestModel passed (getCurrentPlayerId)");
        }
        gm1.update(null, "-1:newPlayerTurn:1");
        gm2.update(null, "-1:newPlayerTurn:1");
        gm3.update(null, "-1:newPlayerTurn:1");
        if(gm1.getCurrentPlayerId()!=1 || gm2.getCurrentPlayerId()!=1 || gm3.getCurrentPlayerId()!=1) {
            flag=false;
        }

        gm1.update(null, "-1:newPlayerTurn:2");
        gm2.update(null, "-1:newPlayerTurn:2");
        gm3.update(null, "-1:newPlayerTurn:2");
        if(gm1.getCurrentPlayerId()!=2 || gm2.getCurrentPlayerId()!=2 || gm3.getCurrentPlayerId()!=2) {
            flag=false;
        }

        gm1.update(null, "-1:newPlayerTurn:3");
        gm2.update(null, "-1:newPlayerTurn:3");
        gm3.update(null, "-1:newPlayerTurn:3");
        if(gm1.getCurrentPlayerId()!=3 || gm2.getCurrentPlayerId()!=3 || gm3.getCurrentPlayerId()!=3) {
            flag=false;
        }
        if(!flag){
            System.out.println("testGuestModel failed (getCurrentPlayerId)");
        }
        else{
            System.out.println("testGuestModel passed (getCurrentPlayerId)");
        }

    }

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        ArrayList<Socket> sockets = new ArrayList<>();
        ArrayList<GuestModel> gms = new ArrayList<>();
        try {
            serverSocket = new ServerSocket(1234);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < 3; i++) {
            gms.add(new GuestModel("localhost", 1234, "guest" + (i + 1)));
        }
        testConnectionToHostServer(sockets, serverSocket, gms);
        GuestModel gm1 = gms.get(0);
        GuestModel gm2 = gms.get(1);
        GuestModel gm3 = gms.get(2);

        testAddTilesToHand(sockets, serverSocket, gms);
        testScoreUpdate(sockets, serverSocket, gms);
        testBoardUpdate(sockets, serverSocket, gms);
        testNewPlayerTurn(sockets, serverSocket, gms);
        try {
            gm1.getClientCommunication().close();
            gm2.getClientCommunication().close();
            gm3.getClientCommunication().close();
            serverSocket.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("\nGuestModelTest done!");
    }




}
