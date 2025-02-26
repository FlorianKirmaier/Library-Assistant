package library.assistant.ui.login;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import library.assistant.WindowManager;
import library.assistant.ui.settings.Preferences;
import library.assistant.util.LibraryAssistantUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginController implements Initializable {

    private final static Logger LOGGER = LogManager.getLogger(LoginController.class.getName());

    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;

    Preferences preference;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        preference = Preferences.getPreferences();
        username.setText("admin");
        password.setText("admin");
    }

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        String uname = StringUtils.trimToEmpty(username.getText());
        String pword = DigestUtils.shaHex(password.getText());

        if (uname.equals(preference.getUsername()) && pword.equals(preference.getPassword())) {
            loadMain(username.getScene().getWindow());
            closeStage();
            LOGGER.log(Level.INFO, "User successfully logged in {}", uname);
        }
        else {
            username.getStyleClass().add("wrong-credentials");
            password.getStyleClass().add("wrong-credentials");
        }
    }

    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
        System.exit(0);
    }

    private void closeStage() {
        WindowManager.closePopup(username);
    }

    void loadMain(Window parentWindow) {
        Parent parent = WindowManager.load(getClass().getResource("/library/assistant/ui/main/main.fxml"));
        WindowManager.showWindow(parent,username, "Library Assistant", () -> {});
        /*try {
        }
        catch (IOException ex) {
            LOGGER.log(Level.ERROR, "{}", ex);
        }*/
    }

}
