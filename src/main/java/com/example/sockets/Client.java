package com.example.sockets;

import javafx.beans.property.SimpleStringProperty;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    private BufferedWriter os;
    private BufferedReader is;
    private SimpleStringProperty word = new SimpleStringProperty("");

    public Client(){

    }

    public void start() {

        // Server Host
        final String serverHost = "localhost";

        Socket socketOfClient = null;

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


            os.write("HELO"+'\0');
            os.newLine();
            os.flush();
            int responseLine;
            responseLine = is.read();

            while (true) {
                while(responseLine != 0){
                    word.set(word.get()+(char)responseLine);
                    responseLine = is.read();
                }
                if(word.get().equals("EXIT")) break;
                else System.out.println("Server: "+word.get());
                responseLine = is.read();
            }


            os.close();
            is.close();
            socketOfClient.close();
        } catch (UnknownHostException e) {
            System.err.println("Trying to connect to unknown host: " + e);
        } catch (IOException e) {
            System.err.println("IOException:  " + e);
        }
    }

    public void send(String message) throws IOException {
        os.write(message+'\0');
        os.newLine();
        os.flush();
    }

    public SimpleStringProperty getWord(){
        return this.word;
    }

}