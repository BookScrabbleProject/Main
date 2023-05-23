package Model;

import Model.gameClasses.BookScrabbleHandler;
import Model.gameClasses.MyServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class HostServerTest {

    public static void testSendToAllPlayers() {
        try {
            MyServer server = new MyServer(1234, new BookScrabbleHandler());
            HostModel hostModel = HostModel.getHost();
            Thread.sleep(1000);
            Socket socket = new Socket("localhost", 1235);
            socket.getOutputStream().write(1);
            Socket socket2 = new Socket("localhost", 1235);
            socket2.getOutputStream().write(2);
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
            server.stop();
            hostModel.hostServer.stop();


        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void testSendToSpecificPlayer() {
        try {
            MyServer server = new MyServer(1234, new BookScrabbleHandler());
            HostModel hostModel = HostModel.getHost();
            Thread.sleep(1000);
            Socket socket = new Socket("localhost", 1235);
            socket.getOutputStream().write(1);
            Thread.sleep(10);
            Socket socket2 = new Socket("localhost", 1235);
            socket2.getOutputStream().write(2);
            Thread.sleep(10);
            hostModel.hostServer.sendToSpecificPlayer(1, "test", "test");
            hostModel.hostServer.sendToSpecificPlayer(2, "test2", "test");

            Scanner scanner = new Scanner(socket.getInputStream());
            String line = scanner.nextLine();
            if (!line.equals("1:test:test")) {
                System.out.println("testSendToSpecificPlayer failed (1)");
            } else {
                System.out.println("testSendToSpecificPlayer passed (1)");
            }


            Scanner scanner2 = new Scanner(socket2.getInputStream());
            line = scanner2.nextLine();
            if (!line.equals("2:test2:test")) {
                System.out.println("testSendToSpecificPlayer failed (2)");
            } else {
                System.out.println("testSendToSpecificPlayer passed (2)");
            }

            socket.close();
            socket2.close();
            server.stop();
            hostModel.hostServer.stop();


        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }



    public static void main(String[] args) throws IOException, InterruptedException {
//        System.out.println("testing sendToAllPlayers...");
//        testSendToAllPlayers();
        System.out.println("testing sendToSpecificPlayer...");
        testSendToSpecificPlayer();
        System.out.println("done!");
    }
}
