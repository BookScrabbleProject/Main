package Model;

import Model.gameClasses.ClientHandler;
import java.io.*;
import java.util.Scanner;


public class GuestModelHandler implements ClientHandler {
    public void handleClient(InputStream inFromclient, OutputStream outToClient) {
        String line;
        HostServer hostServer = HostModel.getHost().hostServer;
        String[] args = new String[0];
        try{
            BufferedReader readFromGuest = new BufferedReader(new InputStreamReader(inFromclient));
            PrintWriter sendToGuest = new PrintWriter(outToClient);
            line = readFromGuest.readLine();
            String[] splitted = line.split(":");
            String id = splitted[0];
            String methodName = splitted[1];
            if(splitted.length>2) {
                args = new String[splitted.length - 2];
                System.arraycopy(splitted, 2, args, 0, splitted.length - 2);
            }
            PrintWriter toGameServer = new PrintWriter(HostModel.getHost().hostServer.getSocketToGameServer().getOutputStream());
            Scanner fromGameServer = new Scanner(HostModel.getHost().hostServer.getSocketToGameServer().getInputStream());
            String answer;
            switch (methodName){
                case "tryPlaceWord": {
                    toGameServer.println("Q," + String.join(",", HostModel.getHost().hostServer.bookNames) + ","+args[0]);
                    toGameServer.flush();
                    answer = fromGameServer.nextLine();
                    hostServer.notifyObservers(id + ":" + "tryPlaceWord:" + args[0] + ":" + args[1]+ ":" + args[2] + ":" + answer);
                    break;
                }
                case "challenge": {
                    toGameServer.println("C," + String.join(",", HostModel.getHost().hostServer.bookNames) + ",");
                    toGameServer.flush();
                    answer = fromGameServer.nextLine();
                    hostServer.notifyObservers(id + ":" + "challenge:" + args[0] + ":" + args[1]+ ":" + args[2] + ":" + answer);
                    break;
                }
                case "passTheTurn":{
                    hostServer.notifyObservers(id + ":" + "passTheTurn");
                    break;
                }
                case "getTileFromBag":{
                    hostServer.notifyObservers(id + ":" + "getTileFromBag");
                    break;
                }



            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        // TODO implement here
    }

}
