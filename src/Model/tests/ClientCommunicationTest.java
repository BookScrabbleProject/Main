package Model.tests;

import Model.ClientCommunication;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

public class ClientCommunicationTest {

    public static class Test implements Observer {

        public Test(Observable o) {
            o.addObserver(this);
        }

        public void update(java.util.Observable o, Object arg) {
            System.out.println("Received message: " + arg);
        }
    }

    public static void testCheckForMessage() {
        ServerSocket server = null;
        ClientCommunication client = null;
        Test test = null;
        try {
            server = new ServerSocket(1234);
            client = new ClientCommunication("localhost", 1234);
            new Test(client);
            Socket socket = server.accept();
            socket.getOutputStream().write("test\n".getBytes());
            Thread.sleep(1000);
            socket.getOutputStream().write("test2\n".getBytes());
            Thread.sleep(1000);
            socket.getOutputStream().write("test3\n".getBytes());
            Thread.sleep(1000);

            client.close();

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage() + " -100pts");
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        testCheckForMessage();

        System.out.println("All tests passed!");
    }
}
