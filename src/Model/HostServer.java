package Model;

import Model.gameClasses.ClientHandler;
import Model.gameClasses.MyServer;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Observable;

public class HostServer extends Observable {
    MyServer hostServer;
    Socket myServerSocket;
    List<Socket> clientsSockets;
    ClientHandler clientHandler;
}
