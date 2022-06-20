package com.example.sockets;

import javafx.beans.property.SimpleStringProperty;

import java.io.*;
import java.net.Socket;

public class Thread implements Runnable {

    private BufferedReader is;
    private BufferedWriter os;
    private SimpleStringProperty word = new SimpleStringProperty("");
    private Socket socket;

    public Thread(BufferedReader is, BufferedWriter os, Socket socket) throws IOException {
        this.is = is;
        this.os = os;

        this.socket = socket;

        this.os = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));

        this.is = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
    }

    @Override
    public synchronized void run() {
        try{
            int responseLine;

            responseLine = is.read();
            while(responseLine != 0){
                this.word.set(this.word.get()+(char)responseLine);
                responseLine = is.read();
            }
            return;
            /*
            while (true) {
                this.wait(1000);
                while(responseLine != 0){
                    word.set(word.get()+(char)responseLine);
                    this.wait(1000);
                }
                if(word.get().equals("EXIT")) {
                    os.close();
                    is.close();
                    socket.close();
                    return;
                }
                else System.out.println("Server: "+word.get());
                responseLine = is.read();
            }*/
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    public SimpleStringProperty getWord() {
        return word;
    }
}
