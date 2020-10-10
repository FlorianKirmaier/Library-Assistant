
package library.assistant.ui.listmember;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import library.assistant.WindowManager;


public class MemberListLoader extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = WindowManager.load(getClass().getResource("member_list.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
