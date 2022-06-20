package com.example.sockets;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    private BufferedWriter os;
    private BufferedReader is;
    private SimpleStringProperty word = new SimpleStringProperty("");
    private Thread thread;

    private Socket socketOfClient;
    public Client(){

    }

    public void start() {

        // Server Host
        final String serverHost = "localhost";


        try {

            socketOfClient = new Socket(serverHost, 5000);

            this.os = new BufferedWriter(new OutputStreamWriter(socketOfClient.getOutputStream()));

            this.is = new BufferedReader(new InputStreamReader(socketOfClient.getInputStream()));

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + serverHost);
            return;
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + serverHost);
            return;
        }

        try {

            this.thread = new Thread(is, os, socketOfClient);
        } catch (UnknownHostException e) {
            System.err.println("Trying to connect to unknown host: " + e);
        } catch (IOException e) {
            System.err.println("IOException:  " + e);
        }
    }

    public void send(String message) throws IOException {
        this.thread = new Thread(is, os, socketOfClient);
        os.write(message+'\0');
        os.newLine();
        os.flush();
        this.thread.run();
        this.word.set(this.thread.getWord().get());
    }

    public SimpleStringProperty getWord(){
        return this.word;
    }

    public Thread getThread() {
        return thread;
    }


}