package Model.tests;

import Model.HostModel;
import Model.HostServer;
import Model.gameClasses.BookScrabbleHandler;
import Model.gameClasses.MyServer;

import java.io.IOException;
import java.io.PrintWriter;
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
    public static void testSendToAllPlayers(MyServer server, HostModel hostModel, int hostServerPort) {
        try {
//            MyServer server = new MyServer(1234, new BookScrabbleHandler());
//            HostModel hostModel = HostModel.getHost();
            Thread.sleep(10);
            Socket socket = new Socket("localhost", hostServerPort);
            PrintWriter pw1 = new PrintWriter(socket.getOutputStream());
            pw1.println("-1:connect:_");
            pw1.flush();

            Thread.sleep(1000);
            Socket socket2 = new Socket("localhost", hostServerPort);
            PrintWriter pw2 = new PrintWriter(socket2.getOutputStream());
            pw2.println("-1:connect:_");
            pw2.flush();

            Thread.sleep(10);
            hostModel.getHostServer().sendToAllPlayers(1, "test", "test");
            Thread.sleep(1000);
            Scanner scanner = new Scanner(socket.getInputStream());
            Scanner scanner2 = new Scanner(socket2.getInputStream());
            String line = scanner.next();
            String line2 = scanner2.next();
            if (!line.equals("1:test:test") || !line2.equals("1:test:test")) {
                System.out.println("testSendToAllPlayers failed (1)");
            } else {
                System.out.println("testSendToAllPlayers passed (1)");
            }

            hostModel.getHostServer().sendToAllPlayers(1, "test2", "test");
            line = scanner.next();
            line2 = scanner2.next();
            if (!line.equals("1:test2:test") || !line2.equals("1:test2:test")) {
                System.out.println("testSendToAllPlayers failed (2)");
                System.out.println("line: " + line + " line2: " + line2);
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
    public static void testSendToSpecificPlayer(MyServer server, HostModel hostModel, int hostServerPort) {
        try {
//            hostModel.removeAllPlayers();
            Thread.sleep(1000);
            Socket socket = new Socket("localhost", hostServerPort);
            socket.getOutputStream().write("-1:connect:_".getBytes());
            Thread.sleep(10);
            Socket socket2 = new Socket("localhost", hostServerPort);
            socket2.getOutputStream().write("-1:connect:_".getBytes());

            boolean[] flags = new boolean[2];


            try {
                hostModel.getHostServer().sendToSpecificPlayer(3, "test", "test");
                flags[0] = true;
            } catch (Exception e) {
                System.out.println("testSendToSpecificPlayer failed (1)");
                flags[0] = false;
            }
            try {
                hostModel.getHostServer().sendToSpecificPlayer(4, "test2", "test");
                flags[1] = true;
            } catch (Exception e) {
                System.out.println("testSendToSpecificPlayer failed (2)");
                flags[1] = false;
            }

            Scanner scanner = null;
            String line = null;
            socket.setSoTimeout(1000);
            if (flags[0]) {
                scanner = new Scanner(socket.getInputStream());
                line = scanner.nextLine();
                if (!line.equals("3:test:test")) {
                    System.out.println("testSendToSpecificPlayer failed (1) send another message");
                    System.out.println("expected: 3:test:test, got: " + line);
                } else {
                    System.out.println("testSendToSpecificPlayer passed (1)");
                }
            }
            if (flags[1]) {
                Scanner scanner2 = new Scanner(socket2.getInputStream());
                line = scanner2.nextLine();
                if (!line.equals("4:test2:test")) {
                    System.out.println("testSendToSpecificPlayer failed (2) send another message");
                    System.out.println("expected: 4:test2:test, got: " + line);
                } else {
                    System.out.println("testSendToSpecificPlayer passed (2)");
                }
            };


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
    public static void testCheckForMessage(MyServer server, HostModel hostModel, int hostServerPort) {
        try {
//            hostModel.removeAllPlayers();
            Socket socket = new Socket("localhost", hostServerPort);
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
            printWriter.println("-1:connect:_");
            printWriter.flush();
            Thread.sleep(1000);
            if(HostServerObserver.lastMessage.equals("-1:connect:_")){
                System.out.println("testCheckForMessage passed (1)");
            }else {
                System.out.println("testCheckForMessage failed (1)");
            }

//            Thread.sleep(10);
            printWriter.println("0:connect:_");
            printWriter.flush();
            Thread.sleep(1000);
            if(HostServerObserver.lastMessage.equals("0:connect:_")){
                System.out.println("testCheckForMessage passed (2)");
            }else {
                System.out.println("testCheckForMessage failed (2)");
            }

            printWriter.println("4:tryPlaceWord:farm,1,1,1");
            printWriter.flush();
            Thread.sleep(1000);
            if(HostServerObserver.lastMessage.equals("4:tryPlaceWord:farm,1,1,1,0")){
                System.out.println("testCheckForMessage passed (3)");
            }else {
                System.out.println("testCheckForMessage failed (3)");
            }

            printWriter.println("4:challenge:farm");
            printWriter.flush();
            Thread.sleep(1000);
            if(HostServerObserver.lastMessage.equals("4:challenge:farm,0")){
                System.out.println("testCheckForMessage passed (4)");
            }else {
                System.out.println("testCheckForMessage failed (4)");
            }
//            printWriter.println("5:tryPlaceWord:word,1,1,1");
//            printWriter.println("5:challenge:word");
//            printWriter.println("1:TEST:TEST");
//            printWriter.flush();
//            Thread.sleep(500);
//            printWriter.flush();


        } catch (IOException | InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        MyServer server = new MyServer(6000, new BookScrabbleHandler());
        server.start();

        HostModel hostModel = HostModel.getHost();
        HostServerObserver hostServerObserver = new HostServerObserver(hostModel.getHostServer());
        int hostServerPort = 6001;
        Thread.sleep(1000);

        System.out.println(">>> testing sendToAllPlayers... <<<");
        new Thread(()->{testSendToAllPlayers(server, hostModel,hostServerPort);}).start();

        Thread.sleep(1000);

        System.out.println("\n>>> testing sendToSpecificPlayer... <<<");
        new Thread(()-> {testSendToSpecificPlayer(server, hostModel,hostServerPort);}).start();

        Thread.sleep(2000);

        System.out.println("\n>>> testing checkForMessage in the server... <<<");
        new Thread(()-> {testCheckForMessage(server, hostModel,hostServerPort);}).start();

        Thread.sleep(10000);
//        server.stop();
//        hostModel.hostServer.stop();
//        System.out.println("servers stopped");
        System.out.println("\ndone!");
    }

    public static class HostServerObserver implements Observer {
        public static String lastMessage = null;
        public HostServerObserver(HostServer hostServer) {
            hostServer.addObserver(this);
        }
        @Override
        public void update(Observable o, Object arg) {
//            System.out.println("HostServerObserver: " + arg);
            lastMessage = (String) arg;
        }
    }
}
