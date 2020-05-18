package controller;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ManageHospitalController {
    public AnchorPane root;
    public JFXTextField txtId;


    public void initialize (){
        if (txtId.getText().isEmpty()) {
            txtId.setText("H001");
        }


    }

    public void btnHome_OnAction(ActionEvent actionEvent) throws IOException {
        Scene dashScene = new Scene(FXMLLoader.load(getClass().getResource("/view/Dashboard.fxml")));
        Stage primaryStage = (Stage) (root.getScene().getWindow());
        primaryStage.setScene(dashScene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    public void btnAddHospital_OnAction(ActionEvent actionEvent) {
        String number = txtId.getText().trim();
        String newNo = number.replace("H", "");
        int no = Integer.parseInt(newNo);

        if (no>=1){
            String temp = "H00"+(no+1);
            txtId.setText(temp);
        }
        if (no>=9){
            String temp = "H0"+(no+1);
            txtId.setText(temp);
        }
        if (no>=99){
            String temp = "H"+(no+1);
            txtId.setText(temp);
        }
    }
}
