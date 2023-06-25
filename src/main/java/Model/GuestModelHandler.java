package Model;

import General.MethodsNames;
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
            System.out.println("GuestModelHandler: " + line);
            String[] splitted = line.split(":");
            String id = splitted[0];
            String methodName = splitted[1];
            String inputs = "";
            if(splitted.length > 2)
                inputs = splitted[2];
            String answer;
            switch (methodName){
                case MethodsNames.TRY_PLACE_WORD: {
                    String[] inputsSplitted = inputs.split(",");
                    String fixedWord = HostModel.getHost().getFixedWord(inputsSplitted[0], Integer.parseInt(inputsSplitted[1]), Integer.parseInt(inputsSplitted[2]), inputsSplitted[3].equals("1"));
                    Socket bookScrabbleServerSocket = hostServer.sendToBookScrabbleServer("Q", fixedWord);
                    Scanner scanner = new Scanner(bookScrabbleServerSocket.getInputStream());
                    answer = scanner.next();
                    hostServer.setChanged();
                    hostServer.notifyObservers(line+','+(answer.equals("true") ? "1" : "0"));
                    break;
                }
                case MethodsNames.CHALLENGE: {
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

        } catch (Exception e) {
//            System.out.println("GuestModelHandler Catch: ");
//            e.printStackTrace();
        }
    }

    public void close() {
    }

}
