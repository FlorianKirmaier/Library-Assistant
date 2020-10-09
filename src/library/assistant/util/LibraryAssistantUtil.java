package library.assistant.util;

import com.jfoenix.controls.JFXButton;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import com.jpro.webapi.WebAPI;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import library.assistant.WindowManager;
import library.assistant.alert.AlertMaker;
import library.assistant.export.pdf.ListToPDF;
import library.assistant.ui.settings.Preferences;
import library.assistant.ui.main.MainController;

public class LibraryAssistantUtil {

    public static final String ICON_IMAGE_LOC = "/resources/icon.png";
    public static final String MAIL_CONTENT_LOC = "/resources/mail_content.html";
    private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");

    public static void setStageIcon(Stage stage) {
        stage.getIcons().add(new Image(ICON_IMAGE_LOC));
    }

    public static Object loadWindow(URL loc, String title, Window parentStage, Stage reuseStage) {
        Object controller = null;
        try {
            FXMLLoader loader = new FXMLLoader(loc);
            Parent parent = loader.load();
            controller = loader.getController();
            if(!WebAPI.isBrowser()) {
                Stage stage = null;
                if (parentStage != null) {
                    stage = reuseStage;
                } else {
                    stage = new Stage(StageStyle.DECORATED);
                }
                stage.setTitle(title);
                stage.setScene(new Scene(parent));
                stage.show();
                setStageIcon(stage);
            } else {
                //AlertMaker.showMaterialDialog(parent,null,new LinkedList<>(),title,null);
                WindowManager.showWindow(parent,parentStage,title,() -> {});
            }
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return controller;
    }

    public static Float getFineAmount(int totalDays) {
        Preferences pref = Preferences.getPreferences();
        Integer fineDays = totalDays - pref.getnDaysWithoutFine();
        Float fine = 0f;
        if (fineDays > 0) {
            fine = fineDays * pref.getFinePerDay();
        }
        return fine;
    }

    public static void initPDFExprot(StackPane rootPane, Node contentPane, Stage stage, List<List> data) {
        if(!WebAPI.isBrowser()) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save as PDF");
            FileChooser.ExtensionFilter extFilter
                    = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
            fileChooser.getExtensionFilters().add(extFilter);
            File saveLoc = fileChooser.showSaveDialog(stage);
            ListToPDF ltp = new ListToPDF();
            boolean flag = ltp.doPrintToPdf(data, saveLoc, ListToPDF.Orientation.LANDSCAPE);
            JFXButton okayBtn = new JFXButton("Okay");
            JFXButton openBtn = new JFXButton("View File");
            openBtn.setOnAction((ActionEvent event1) -> {
                try {
                    Desktop.getDesktop().open(saveLoc);
                } catch (Exception exp) {
                    AlertMaker.showErrorMessage("Could not load file", "Cant load file");
                }
            });
            if (flag) {
                AlertMaker.showMaterialDialog(rootPane, contentPane, Arrays.asList(okayBtn, openBtn), "Completed", "Member data has been exported.");
            }
        } else {
            try {
                ListToPDF ltp = new ListToPDF();
                File file = File.createTempFile("library-assistant", ".pdf");
                boolean flag = ltp.doPrintToPdf(data, file, ListToPDF.Orientation.LANDSCAPE);
                WebAPI webAPI = WebAPI.getWebAPI(rootPane.getScene());
                webAPI.downloadURL(file.toURI().toURL());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String formatDateTimeString(Date date) {
        return DATE_TIME_FORMAT.format(date);
    }

    public static String formatDateTimeString(Long time) {
        return DATE_TIME_FORMAT.format(new Date(time));
    }

    public static String getDateString(Date date) {
        return DATE_FORMAT.format(date);
    }

    public static boolean validateEmailAddress(String emailID) {
        String regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(emailID).matches();
    }

    public static void openFileWithDesktop(File file) {
        try {
            Desktop desktop = Desktop.getDesktop();
            desktop.open(file);
        } catch (IOException ex) {
            Logger.getLogger(LibraryAssistantUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
