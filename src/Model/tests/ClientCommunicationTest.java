package Model.tests;

import Model.ClientCommunication;
import Model.gameClasses.BookScrabbleHandler;
import Model.gameClasses.ClientHandler;
import Model.gameClasses.DictionaryManager;
import Model.gameClasses.MyServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

public class ClientCommunicationTest {

    public static void testSend() {

    }

    public static void testCheckForMessage() {
        ServerSocket server = null;
        ClientCommunication client = null;
        Test test = null;
        try {
            server = new ServerSocket(1234);
            client = new ClientCommunication("localhost", 1234);
            test = new Test(client);
            Socket socket = server.accept();
            socket.getOutputStream().write("test1\n".getBytes());
            Thread.sleep(1000);
            if(test.lastMessage == null || !test.lastMessage.equals("test1")) {
                System.out.println("ERROR: testCheckForMessage failed -100pts");
                throw new RuntimeException("testCheckForMessage failed");
            }else {
                System.out.println("testCheckForMessage test1 passed");
            }

            socket.getOutputStream().write("test2\n".getBytes());
            Thread.sleep(1000);
            if(test.lastMessage == null || !test.lastMessage.equals("test2")) {
                System.out.println("ERROR: testCheckForMessage failed -100pts");
                throw new RuntimeException("testCheckForMessage failed");
            }else {
                System.out.println("testCheckForMessage test2 passed");
            }

            socket.getOutputStream().write("test3\n".getBytes());
            Thread.sleep(1000);
            if(test.lastMessage == null || !test.lastMessage.equals("test3")) {
                System.out.println("ERROR: testCheckForMessage failed -100pts");
                throw new RuntimeException("testCheckForMessage failed");
            }
            else {
                System.out.println("testCheckForMessage test3 passed");
            }

            client.close();

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage() + " -100pts");
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        testCheckForMessage();
        System.out.println("CheckForMessage passed!");
        testSend();
        System.out.println("Send passed!");

        System.out.println("All tests passed!");
    }

    public static class Test implements Observer {

        public String lastMessage = null;

        public Test(Observable o) {
            o.addObserver(this);
        }

        public void update(java.util.Observable o, Object arg) {
            lastMessage = (String) arg;
            System.out.println("Received message: " + arg);
        }
    }
}
