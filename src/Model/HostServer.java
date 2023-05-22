package Model;

import Model.gameClasses.ClientHandler;
import Model.gameClasses.MyServer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class HostServer extends Observable {
    MyServer hostServer;
    Socket myServerSocket;
    List<Socket> clientsSockets;
    GuestModelHandler clientHandler;
    List<String> bookNames;


    public HostServer(int myPort, String gameServerIp, int gameServerPort) {
        //do something
        try {
            myServerSocket = new Socket(gameServerIp, gameServerPort);
            clientHandler = new GuestModelHandler();
            hostServer = new MyServer(myPort, clientHandler);
            bookNames = new ArrayList<>();
            hostServer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public Socket getSocketToGameServer(){
        return myServerSocket;
    }


}
