package Model;

import General.MethodsNames;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;
import java.util.Scanner;

/**
 * This class is responsible for the communication between the client and the server
 * It sends messages to the server and notifies the observers when a message is received
 * The protocol is: id:method:input1,input2,...
 * The id is the id of the client, -1 means that the client is not connected yet
 */
public class ClientCommunication extends Observable {
    Socket socket;

    public ClientCommunication() {
        socket = new Socket();
    }

    /**
     * Creates a new client communication with the server and starts a new thread to check for messages from the server
     * @param ip   the host to connect to
     * @param port the port to connect to
     */
    public ClientCommunication(String ip, int port, String name) {
        try {
            socket = new Socket(ip, port);
            send(-1, MethodsNames.CONNECT, name);
            new Thread(this::checkForMessage).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void connect(String ip, int port, String name) {
        try {
            socket = new Socket(ip, port);
            send(-1, MethodsNames.CONNECT, name);
            new Thread(this::checkForMessage).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends a message to the server, protocol: id:method:input1,input2,...
     * @param id         the id of the client
     * @param methodName the method name to call
     * @param args     the inputs to the method
     */
    public void send(int id, String methodName, String... args) {
        String message = id + ":" + methodName + ":" + String.join(",", args);
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            out.println(message);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * check if the server send a message and notify the observers, 4 times a second
     */
    private void checkForMessage() {
        while (socket.isConnected() && !socket.isClosed()) {
            try {
                Scanner in = new Scanner(socket.getInputStream());
                while (in.hasNext()) {
                    String message = in.next();
                    setChanged();
                    notifyObservers(message);
                }
//                Thread.sleep(250);
            } catch (IOException e) {
            }
        }
    }

    /**
     * close the socket connection to the server -> stop the thread
     */
    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}