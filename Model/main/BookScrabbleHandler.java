package main;


import java.io.*;

public class BookScrabbleHandler implements ClientHandler {


    @Override
    public void handleClient(InputStream inFromclient, OutputStream outToClient) {
        String line;
        try {
            BufferedReader readFromClient = new BufferedReader(new InputStreamReader(inFromclient));
            PrintWriter sendToClient = new PrintWriter(outToClient);
            line = readFromClient.readLine();
            String[] splitted = line.split(",");
            String type = splitted[0];
            String[] args = new String[splitted.length-1];
            System.arraycopy(splitted, 1, args, 0, splitted.length - 1);
            if(type.equals("Q")) {
                sendToClient.println(DictionaryManager.get().query(args));
                sendToClient.flush();
            }
            else if(type.equals("C")) {
                sendToClient.println(DictionaryManager.get().challenge(args));
                sendToClient.flush();
            }
            readFromClient.close();
            sendToClient.close();

        } catch (IOException e) {}

    }

    @Override
    public void close() {

    }


    /*public void start(String ip, int port){
        try{
            Socket theServer = new Socket(ip, port);
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader serverInput = new BufferedReader(new InputStreamReader(theServer.getInputStream()));
            PrintWriter outToServer = new PrintWriter(theServer.getOutputStream());
            PrintWriter outToScreen = new PrintWriter(System.out);
            readInputsAndSend(userInput, outToServer, "\n");
            readInputsAndSend(serverInput, outToScreen, "\n");
            userInput.close();
            serverInput.close();
            outToServer.close();
            outToScreen.close();
        }catch (UnknownHostException e){}
        catch (IOException e){}

    }*/
}



