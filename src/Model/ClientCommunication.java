package Model;

import java.io.IOException;
import java.net.Socket;
import java.util.Observable;

public class ClientCommunication extends Observable {
    Socket socket;

    public ClientCommunication(String host, int port) {
        try {
            socket = new Socket(host, port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}