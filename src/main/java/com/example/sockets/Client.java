package com.example.sockets;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    public static void main(String[] args) {

        // Server Host
        final String serverHost = "localhost";

        Socket socketOfClient = null;
        BufferedWriter os = null;
        BufferedReader is = null;

        try {

            socketOfClient = new Socket(serverHost, 5000);

            os = new BufferedWriter(new OutputStreamWriter(socketOfClient.getOutputStream()));

            is = new BufferedReader(new InputStreamReader(socketOfClient.getInputStream()));

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
            String word = "";
            responseLine = is.read();
            while (true) {
                while(responseLine != 0){
                    word = word+(char)responseLine;
                    responseLine = is.read();
                }
                if(word.equals("EXIT")) break;
                else System.out.println("Server: "+word);
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

}