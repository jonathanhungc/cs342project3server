/**
 * FILE: ServerController.java
 *
 * Controller for the server GUI. It switches the screen where you enter a port number, creates the Server and
 * displays a log of all the actions from different clients in the GUI.
 */

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ServerController implements Initializable {

    @FXML
    private VBox root;

    @FXML
    private ListView<String> log;

    @FXML
    private VBox root2;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label portLabel;

    @FXML
    private TextField textField;

    @FXML
    private Button enter;

    private int port;
    private Server server;

    public void initialize(URL location, ResourceBundle resources) {

    }

    // method to set the port number for the server
    public void setPort(int port) {
        this.port = port;
    }

    // method to set text for label with port number
    public void setPortLabel(String text) {
        portLabel.setText(text);
    }

    // starts the server object
    public void server() {
        server = new Server(port, data -> {
            Platform.runLater(() -> {
                log.getItems().add(data.toString());
            });
        });
    }

    // setter for the height of the log (ListView)
    public void setLogHeight(int height) {
        log.setPrefHeight(height);
    }

    // method used to start server and change scenes, from welcome scene to server scene
    public void startServer(ActionEvent e) throws IOException {
        port = Integer.parseInt(textField.getText());

        System.out.println("Using port:" + port);

        //get instance of the loader class
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/server.fxml"));
        Parent root2 = loader.load(); //load view into parent
        ServerController myctr = loader.getController();//get controller created by FXMLLoader
        myctr.setPort(port);
        myctr.setPortLabel("Port number: " + port);
        myctr.server();
        myctr.setLogHeight(600);

        root2.getStylesheets().add("/styles/style2.css");//set style

        root.getScene().setRoot(root2);//update scene graph
    }
}
