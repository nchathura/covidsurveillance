package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class GlobalCOVIDController {
    public AnchorPane root;

    public void btnHome_OnAction(ActionEvent actionEvent) throws IOException {
       Scene dashStage = new Scene(FXMLLoader.load(getClass().getResource("/view/Dashboard.fxml")));
       Stage primaryStage = (Stage) (root.getScene().getWindow());
       primaryStage.setScene(dashStage);
       primaryStage.centerOnScreen();
       primaryStage.show();
    }
}
