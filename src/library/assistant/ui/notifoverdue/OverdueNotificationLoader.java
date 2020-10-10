package library.assistant.ui.notifoverdue;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import library.assistant.WindowManager;

public class OverdueNotificationLoader extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = WindowManager.load(getClass().getResource("overdue_notification.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
