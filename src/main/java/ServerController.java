import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javax.swing.text.html.ListView;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ServerController implements Initializable {

    @FXML
    private VBox root;

    @FXML
    private HBox header;

    @FXML
    private ListView log;

    @FXML
    VBox root2;

    @FXML
    Text welcomeText;

    @FXML
    TextField textField;

    @FXML
    Button enter;

    int port;

    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
    }

    public void startServer(ActionEvent e) throws IOException {
        port = Integer.parseInt(textField.getText());
    }

}
