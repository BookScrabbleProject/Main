package Model;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;
import java.util.Scanner;

public class ClientCommunication extends Observable {
    Socket socket;

    /**
     * Creates a new client communication with the server and starts a new thread to check for messages from the server
     * @param ip   the host to connect to
     * @param port the port to connect to
     */
    public ClientCommunication(String ip, int port) {
        try {
            socket = new Socket(ip, port);
            send(-1, "connect");
            new Thread(this::checkForMessage).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends a message to the server, protocol: id:method:input1,input2,...
     * @param id         the id of the client
     * @param methodName the method name to call
     * @param inputs     the inputs to the method
     */
    public void send(int id, String methodName, String... inputs) {
        String message = id + ":" + methodName + ":" + String.join(",", inputs);
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
                if (in.hasNext()) {
                    String message = in.nextLine();
                    setChanged();
                    notifyObservers(message);
                }
                Thread.sleep(250);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}