package library.assistant.ui.main;

import com.jpro.webapi.WebAPI;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import library.assistant.WindowManager;
import library.assistant.database.DatabaseHandler;
import library.assistant.exceptions.ExceptionUtil;
import library.assistant.util.LibraryAssistantUtil;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main extends Application {

    private final static Logger LOGGER = LogManager.getLogger(Main.class.getName());

    static {
        System.setSecurityManager(null);
        new Thread(() -> {
            ExceptionUtil.init();
            DatabaseHandler.getInstance();
        }).start();
    }

    @Override
    public void start(Stage stage) throws Exception {

        Parent root = WindowManager.load(getClass().getResource("/library/assistant/ui/login/login.fxml"));

        if(WebAPI.isBrowser()) {
            StackPane pane = new StackPane();
            pane.getProperties().put(WindowManager.POPUP_CONTEXT,WindowManager.POPUP_CONTEXT);
            Scene scene = new Scene(pane);
            stage.setScene(scene);
            stage.show();
            WindowManager.showWindow(root,pane,"Library Assistant Login",() -> {});
        } else {
            WindowManager.showWindow(root,null,"Library Assistant Login",() -> {});
        }

    }

    public static void main(String[] args) {
        Long startTime = System.currentTimeMillis();
        LOGGER.log(Level.INFO, "Library Assistant launched on {}", LibraryAssistantUtil.formatDateTimeString(startTime));
        launch(args);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                Long exitTime = System.currentTimeMillis();
                LOGGER.log(Level.INFO, "Library Assistant is closing on {}. Used for {} ms", LibraryAssistantUtil.formatDateTimeString(startTime), exitTime);
            }
        });
    }

}
