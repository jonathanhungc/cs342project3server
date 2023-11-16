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
import javafx.scene.layout.HBox;
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
        // TODO Auto-generated method stub

    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setPortLabel(String text) {
        portLabel.setText(text);
    }

    public void server() {
        server = new Server(port, data -> {
            Platform.runLater(() -> {
                log.getItems().add(data.toString());
            });
        });
    }

    public void startServer(ActionEvent e) throws IOException {
        port = Integer.parseInt(textField.getText());

        System.out.println(port);

        //get instance of the loader class
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/server.fxml"));
        Parent root2 = loader.load(); //load view into parent
        ServerController myctr = loader.getController();//get controller created by FXMLLoader
        myctr.setPort(port);
        myctr.setPortLabel("Port number: " + port);
        myctr.server();

        root2.getStylesheets().add("/styles/style2.css");//set style

        root.getScene().setRoot(root2);//update scene graph

    }

}
