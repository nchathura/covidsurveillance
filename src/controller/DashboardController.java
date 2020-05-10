package controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {
    public JFXButton btnGlobal;
    public JFXButton btnHospital;
    public JFXButton btnQuarantine;
    public JFXButton btnUsers;
    public AnchorPane root;

    public void btnGlobal_OnAction(ActionEvent actionEvent) throws IOException {
        Scene globalCOVID = new Scene(FXMLLoader.load(getClass().getResource("/view/GlobalCOVID.fxml")));
        Stage primaryStage = (Stage) (root.getScene().getWindow());
        primaryStage.setScene(globalCOVID);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    public void btnHospital_OnAction(ActionEvent actionEvent) throws IOException {
        Scene hospitalScene = new Scene(FXMLLoader.load(getClass().getResource("/view/ManageHospital.fxml")));
        Stage primaryStage = (Stage) (root.getScene().getWindow());
        primaryStage.setScene(hospitalScene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    public void btnQuarantine_OnAction(ActionEvent actionEvent) throws IOException {
        Scene quarantineScene = new Scene(FXMLLoader.load(getClass().getResource("/view/ManageQuarantineCenters.fxml")));
        Stage primaryStage = (Stage) (root.getScene().getWindow());
        primaryStage.setScene(quarantineScene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    public void btnUsers_OnAction(ActionEvent actionEvent) throws IOException {
        Scene usersScene = new Scene(FXMLLoader.load(getClass().getResource("/view/ManageUsers.fxml")));
        Stage primaryStage = (Stage) (root.getScene().getWindow());
        primaryStage.setScene(usersScene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }
}
