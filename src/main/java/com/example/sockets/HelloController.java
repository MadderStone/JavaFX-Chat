package com.example.sockets;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML
    private Text logServer;
    @FXML
    private TextArea input;
    private Client client;

    @FXML
    private Text logs;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.client = new Client();
        ChangeListener c = new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                logServer.setText(logServer.getText()+"\n"+client.getWord().get());
            }
        };
        this.client.getWord().addListener(c);
        this.client.start();
    }

    @FXML
    void sendMessage(ActionEvent event) throws IOException {
        this.client.send(this.input.getText());
        this.input.setText(null);
    }



    @FXML
    void printLogs(ActionEvent event) throws IOException {
        this.client.getLogs();
    }
}