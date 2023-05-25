package Model;

import Model.gameClasses.BookScrabbleHandler;
import Model.gameClasses.MyServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

public class HostServerTest {

    /**
     * test if the server can send to all players who are connected to the server
     */
    public static void testSendToAllPlayers(MyServer server, HostModel hostModel) {
        try {
//            MyServer server = new MyServer(1234, new BookScrabbleHandler());
//            HostModel hostModel = HostModel.getHost();
            Thread.sleep(10);
            Socket socket = new Socket("localhost", 1236);
            socket.getOutputStream().write("-1:connect: ".getBytes());
            Thread.sleep(10);
            Socket socket2 = new Socket("localhost", 1236);
            socket2.getOutputStream().write("-1:connect: ".getBytes());
            Thread.sleep(10);
            hostModel.hostServer.sendToAllPlayers(1, "test", "test");

            Scanner scanner = new Scanner(socket.getInputStream());
            Scanner scanner2 = new Scanner(socket2.getInputStream());
            String line = scanner.nextLine();
            String line2 = scanner2.nextLine();
            if (!line.equals("1:test:test") || !line2.equals("1:test:test")) {
                System.out.println("testSendToAllPlayers failed (1)");
            } else {
                System.out.println("testSendToAllPlayers passed (1)");
            }

            hostModel.hostServer.sendToAllPlayers(1, "test2", "test");
            line = scanner.nextLine();
            line2 = scanner2.nextLine();
            if (!line.equals("1:test2:test") || !line2.equals("1:test2:test")) {
                System.out.println("testSendToAllPlayers failed (2)");
            } else {
                System.out.println("testSendToAllPlayers passed (2)");
            }

            socket.close();
            socket2.close();
            Thread.sleep(1000);


        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test if the server can send a message to a specific player
     */
    public static void testSendToSpecificPlayer(MyServer server, HostModel hostModel) {
        try {
//            MyServer server = new MyServer(1234, new BookScrabbleHandler());
//            HostModel hostModel = HostModel.getHost();
            hostModel.removeAllPlayers();
            Thread.sleep(1000);
            Socket socket = new Socket("localhost", 1236);
            socket.getOutputStream().write("-1:connect: ".getBytes());
            Thread.sleep(10);
            Socket socket2 = new Socket("localhost", 1236);
            socket2.getOutputStream().write("-1:connect: ".getBytes());

            boolean[] flags = new boolean[2];


            try {
                hostModel.hostServer.sendToSpecificPlayer(1, "test", "test");
                flags[0] = true;
            } catch (Exception e) {
                System.out.println("testSendToSpecificPlayer failed (1)");
                flags[0] = false;
            }
            try {
                hostModel.hostServer.sendToSpecificPlayer(2, "test2", "test");
                flags[1] = true;
            } catch (Exception e) {
                System.out.println("testSendToSpecificPlayer failed (2)");
                flags[1] = false;
            }

            Scanner scanner = null;
            String line = null;
            if (flags[0]) {
                scanner = new Scanner(socket.getInputStream());
                line = scanner.nextLine();
                if (!line.equals("1:test:test")) {
                    System.out.println("testSendToSpecificPlayer failed (1) send another message");
                    System.out.println("expected: 1:test:test, got: " + line);
                } else {
                    System.out.println("testSendToSpecificPlayer passed (1)");
                }
            }
            if (flags[1]) {
                Scanner scanner2 = new Scanner(socket2.getInputStream());
                line = scanner2.nextLine();
                if (!line.equals("2:test2:test")) {
                    System.out.println("testSendToSpecificPlayer failed (2) send another message");
                    System.out.println("expected: 2:test2:test, got: " + line);
                } else {
                    System.out.println("testSendToSpecificPlayer passed (2)");
                }
            }

            socket.close();
            socket2.close();
            Thread.sleep(1000);


        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Test if the server can handle a messages
     */
    public static void testCheckForMessage(MyServer server, HostModel hostModel) {
        try {
            hostModel.removeAllPlayers();
            Socket socket = new Socket("localhost", 1236);
            socket.getOutputStream().write("-1:connect: ".getBytes());
            Thread.sleep(1000);
        } catch (IOException | InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        MyServer server = new MyServer(1234, new BookScrabbleHandler());
        HostModel hostModel = HostModel.getHost();

        System.out.println(">>> testing sendToAllPlayers... <<<");
        testSendToAllPlayers(server, hostModel);
        System.out.println("<<< Finished testing sendToAllPlayers >>>");

        Thread.sleep(1000);

        System.out.println("\n>>> testing sendToSpecificPlayer... <<<");
        testSendToSpecificPlayer(server, hostModel);
        System.out.println("<<< Finished testing sendToSpecificPlayer >>>");

        System.out.println("\n>>> testing checkForMessage in the server... <<<");
        testCheckForMessage(server, hostModel);
        System.out.println("<<< Finished testing checkForMessage in the server >>>");

        System.out.println("\ndone!");

        server.stop();
        hostModel.hostServer.stop();
    }
}
