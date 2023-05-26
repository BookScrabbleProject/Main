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
            BufferedReader readFromGuest = new BufferedReader(new InputStreamReader(inFromclient));
            PrintWriter sendToGuest = new PrintWriter(outToClient);
            line = readFromGuest.readLine();
            String[] splitted = line.split(":");
            String id = splitted[0];
            String methodName = splitted[1];
            String inputs = splitted[2];
            String answer;
            switch (methodName){
                case "tryPlaceWord": {
                    Socket bookScrabbleServerSocket = hostServer.sendToBookScrabbleServer("Q", inputs.split(",")[0]);
                    Scanner scanner = new Scanner(bookScrabbleServerSocket.getInputStream());
                    answer = scanner.next();
                    hostServer.setChanged();
                    hostServer.notifyObservers(line+','+(Boolean.getBoolean(answer) ? "1" : "0"));
                    break;

                }
                case "challenge": {
                    Socket bookScrabbleServerSocket = hostServer.sendToBookScrabbleServer("C", inputs.split(",")[0]);
                    Scanner scanner = new Scanner(bookScrabbleServerSocket.getInputStream());
                    answer = scanner.next();
                    hostServer.setChanged();
                    hostServer.notifyObservers(line+','+(Boolean.getBoolean(answer) ? "1" : "0"));
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
        // TODO implement here
    }

}
