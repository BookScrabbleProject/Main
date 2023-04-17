package Model.gameClasses;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class MyServer {

    private int port;
    private ClientHandler ch;
    private volatile boolean stop;
    public MyServer(int port, ClientHandler ch){
        this.port = port;
        this.ch = ch;
        stop=false;
    }

    /**
     * runServer function --> to run the server and handle clients
     */
    private void runServer(){
        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
            server.setSoTimeout(1000);
            while (!stop) {
                try {
                    Socket aClient = server.accept();
                    try{
                        ch.handleClient(aClient.getInputStream(), aClient.getOutputStream());
                        aClient.getInputStream().close();
                        aClient.getOutputStream().close();
                        aClient.close();
                    } catch (IOException e){}
                } catch(SocketTimeoutException e){

                }//blocking call

            }
            server.close();
        } catch (IOException e) {}

    }

    public void start(){
        new Thread(()->runServer()).start();
    }
    public void stop(){
        stop=true;
    }
    void close(){
        stop();
        ch.close();
    }



}
