package Model.tests;

import Model.ClientCommunication;
import Model.gameClasses.BookScrabbleHandler;
import Model.gameClasses.ClientHandler;
import Model.gameClasses.DictionaryManager;
import Model.gameClasses.MyServer;

import java.io.*;
import java.net.InterfaceAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class ClientCommunicationTest {

    public static void testSend() {
        int serverSocketPort = 3000;
        ServerSocket server = null;
        ClientCommunication client = null;
        Test test = null;
        try{
            server = new ServerSocket(serverSocketPort);
            client = new ClientCommunication("localhost", serverSocketPort);
            Socket socket = server.accept();
            Thread.sleep(1000);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = in.readLine();
            Checker.checkResult("-1:connect: ", line, "testSend");

            client.send(1, "testMethod", "input1", "input2");
            line = in.readLine();
            Checker.checkResult("1:testMethod:input1,input2", line, "testSend");

            client.close();

        }catch (Exception e) {
            System.out.println("ERROR: " + e);
            throw new RuntimeException("testSend failed");
        }
    }

    public static void testCheckForMessage() {
        String functionName = "testCheckForMessage";
        ServerSocket server = null;
        ClientCommunication client = null;
        ClientCommunication client2 = null;
        Test test = null;
        Test test2 = null;
        try {
            int serverSocketPort = 1234;
            server = new ServerSocket(serverSocketPort);
            client = new ClientCommunication("localhost", serverSocketPort);
            test = new Test(client);

            Socket socket = server.accept();
            client2 = new ClientCommunication("localhost", serverSocketPort);
            test2 = new Test(client2);
            Socket socket2 = server.accept();
            socket.getOutputStream().write("test1\n".getBytes());
            socket2.getOutputStream().write("socket2, test1\n".getBytes());

            while(test.lastMessage == null || test2.lastMessage == null) {
                Thread.sleep(1);
            }

            Checker.checkResult("test1", test.lastMessage, functionName);
            test.lastMessage = null;

            Checker.checkResult("socket2, test1", test2.lastMessage, functionName);
            test2.lastMessage = null;

            socket.getOutputStream().write("test2\n".getBytes());
            socket2.getOutputStream().write("socket2, test2\n".getBytes());
            while(test.lastMessage == null || test2.lastMessage == null) {
                Thread.sleep(1);
            }
            Checker.checkResult("test2", test.lastMessage, functionName);
            test.lastMessage = null;

            Checker.checkResult("socket2, test2", test2.lastMessage, functionName);
            test2.lastMessage = null;

            socket.getOutputStream().write("test3\n".getBytes());
            socket2.getOutputStream().write("socket2, test3\n".getBytes());
            while(test.lastMessage == null || test2.lastMessage == null) {
                Thread.sleep(1);
            }

            Checker.checkResult("test3", test.lastMessage, functionName);
            test.lastMessage = null;

            Checker.checkResult("socket2, test3", test2.lastMessage, functionName);
            test2.lastMessage = null;

            client.close();
            client2.close();

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage() + " -100pts");
            throw new RuntimeException(e);
        }
    }

    public static void checkResult(String expected, String actual) {
        if(expected.equals(actual)) {
            System.out.println("passed");
        }else {
            System.out.println("ERROR: expected: " + expected + " actual: " + actual);
        }
    }

    public static void main(String[] args) {
        System.out.println("- - - - Testing ClientCommunication - - - -");
        System.out.println("> > > checkForMassage < < <");
        testCheckForMessage();

        System.out.println("> > > send < < <");
        testSend();

        System.out.println("\nAll tests passed!");
    }

    public static class Test implements Observer {

        public String lastMessage = null;

        public Test(Observable o) {
            o.addObserver(this);
        }

        public void update(java.util.Observable o, Object arg) {
            lastMessage = (String) arg;
        }
    }

    public static class Checker {
//        static int testNum = 1;
        static  Map<String, Integer> testNums = new HashMap<String, Integer>();

        public static void checkResult(String expected, String actual, String functionTested) {
            if(expected.equals(actual)) {
                System.out.println(functionTested + " passed (" + getNumOfTests(functionTested) + ")");
            }else {
                System.out.println(functionTested + " failed (" + getNumOfTests(functionTested) + ")");
                System.out.println("ERROR: expected: " + expected + " actual: " + actual);
            }

            testNums.put(functionTested, testNums.getOrDefault(functionTested, 1) + 1);
        }

        public static int getNumOfTests(String key) {
            return testNums.getOrDefault(key, 1);
        }

        public static void incrementTestNum(String key) {
            testNums.put(key, getNumOfTests(key) + 1);
        }
    }
}
