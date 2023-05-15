package Model;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Observable;

public class HostServer extends Observable {
    ServerSocket serverSocket;
    Socket myServerSocket;
    List<Socket> clientsSockets;
}
