package library.assistant;

import com.jpro.webapi.WebAPI;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import library.assistant.alert.AlertMaker;
import library.assistant.util.LibraryAssistantUtil;

public class WindowManager {

    public static void closePopup(Node node) {
        if(WebAPI.isBrowser()) {
            StackPane windowRoot = (StackPane) node.getScene().getRoot();
            Node baseNode = node;
            while(!windowRoot.getChildren().contains(baseNode)) {
                baseNode = baseNode.getParent();
            }
            windowRoot.getChildren().remove(baseNode);
            windowRoot.getChildren().stream().forEach((child) -> child.setEffect(null));
        } else {
            ((Stage) node.getScene().getWindow()).close();
        }
    }

    public static void showWindow(Parent root, Window parent, String title, Runnable onClosed) {

        if(WebAPI.isBrowser()) {
            StackPane toAdd = new StackPane(root);
            root.setStyle("-fx-background-color: #2A2E37;");
            toAdd.setStyle("-fx-background-color: #88888888;");
            StackPane windowRoot = (StackPane) parent.getScene().getRoot();
            windowRoot.getChildren().stream().forEach((child) -> child.setEffect(new GaussianBlur()));
            windowRoot.getChildren().add(toAdd);
            root.parentProperty().addListener((p,o,n) -> {
                if(n == null) {
                    onClosed.run();
                }
            });
        } else {
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
            LibraryAssistantUtil.setStageIcon(stage);

            stage.setOnHiding((e) -> {
                onClosed.run();
            });
        }
    }
}
