package com.example.sockets;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML
    private Text logClient;

    @FXML
    private Text logServer;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Client client = new Client();
        ChangeListener c = new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                logClient.setText(logClient.getText()+"\n"+client.getWord());
            }
        };
        client.getWord().addListener(c);
        client.start();
    }
}