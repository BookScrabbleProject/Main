package Model;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;

public class ClientCommunication extends Observable {
    Socket socket;

    /**
     * Creates a new client communication
     * @param host the host to connect to
     * @param port the port to connect to
     */
    public ClientCommunication(String host, int port) {
        try {
            socket = new Socket(host, port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends a message to the server
     * @param id the id of the client
     * @param methodName the method name to call
     * @param inputs the inputs to the method
     */
    public void send(int id, String methodName, String... inputs) {
        String message = id+":"+methodName+":"+String.join(",", inputs);
        try {
            PrintWriter out=new PrintWriter(socket.getOutputStream());
            out.println(message);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}