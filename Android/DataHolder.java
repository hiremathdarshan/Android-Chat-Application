package com.example.nishanth.mymessengerapplication;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;

public class DataHolder {

    private static Socket clientSocket;
    private static PrintWriter pr;
    private static PrintStream os = null;
    private static DataInputStream is = null;
    private static BufferedReader inputline = null;
    private static boolean closed = false;

    public static void createConnection(String ip){
        try {
            clientSocket = new Socket(ip, 4447);
            inputline = new BufferedReader(new InputStreamReader(System.in));
            os = new PrintStream(clientSocket.getOutputStream());
            is = new DataInputStream(clientSocket.getInputStream());
        }
        catch (java.net.UnknownHostException e) {
            System.err.println("Don't know about host ");
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to the host ");
        }
    }

    public static Socket getClientSocket() {return clientSocket;}
    public static BufferedReader getInputline() {return inputline;}
    public static PrintStream getOs() {return os;}
    public static DataInputStream getIs() {return is;}

    private static final DataHolder holder = new DataHolder();
    public static DataHolder getInstance() {return holder;}
}