package Model;

import Model.gameClasses.ClientHandler;
import java.io.*;
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
            Scanner fromBookScrabbleServer = new Scanner(HostModel.getHost().hostServer.getSocketToGameServer().getInputStream());
            String answer;
            switch (methodName){
                case "tryPlaceWord": {
                    hostServer.sendToBookScrabbleServer("Q", inputs.split(",")[0]);
                    answer = fromBookScrabbleServer.nextLine();
                    hostServer.hasChanged();
                    hostServer.notifyObservers(id + ":" + "tryPlaceWord:" + inputs + "," + answer.toString());
                    break;

                }
                case "challenge": {
                    hostServer.sendToBookScrabbleServer("C", inputs.split(",")[0]);
                    answer = fromBookScrabbleServer.nextLine();
                    hostServer.hasChanged();
                    hostServer.notifyObservers(id + ":" + "challenge:" + inputs + "," + answer.toString());
                    break;

                }
                default:
                    hostServer.hasChanged();
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
