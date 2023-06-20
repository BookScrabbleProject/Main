package Model;

import Model.gameClasses.ClientHandler;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;


public class GuestModelHandler implements ClientHandler {
    public void handleClient(InputStream inFromclient, OutputStream outToClient) {
        String line;
        HostServer hostServer = HostModel.getHost().hostServer;
        String[] args = null;
        try{
            Scanner lineScanner =  new Scanner(new BufferedReader(new InputStreamReader(inFromclient)));
            PrintWriter sendToGuest = new PrintWriter(outToClient);
            line = lineScanner.next();
            String[] splitted = line.split(":");
            String id = splitted[0];
            String methodName = splitted[1];
            String inputs = "";
            if(splitted.length > 2)
                inputs = splitted[2];
            String answer;
            switch (methodName){
                case "tryPlaceWord": {
                    Socket bookScrabbleServerSocket = hostServer.sendToBookScrabbleServer("Q", inputs.split(",")[0]);
                    Scanner scanner = new Scanner(bookScrabbleServerSocket.getInputStream());
                    answer = scanner.next();
                    hostServer.setChanged();
                    hostServer.notifyObservers(line+','+(answer.equals("true") ? "1" : "0"));
                    break;

                }
                case "challenge": {
                    Socket bookScrabbleServerSocket = hostServer.sendToBookScrabbleServer("C", inputs.split(",")[0]);
                    Scanner scanner = new Scanner(bookScrabbleServerSocket.getInputStream());
                    answer = scanner.next();
                    hostServer.setChanged();
                    hostServer.notifyObservers(line+','+(answer.equals("true") ? "1" : "0"));
                    break;

                }
                default:
                    hostServer.setChanged();
                    hostServer.notifyObservers(line);


            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
    }

}
