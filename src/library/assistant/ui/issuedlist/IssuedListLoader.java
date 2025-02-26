package library.assistant.ui.issuedlist;


import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import library.assistant.WindowManager;


public class IssuedListLoader extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = WindowManager.load(getClass().getResource("issued_list.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
