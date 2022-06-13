module com.example.sockets {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.sockets to javafx.fxml;
    exports com.example.sockets;
}