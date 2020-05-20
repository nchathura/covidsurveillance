package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class ManageQuarantineCentersController {
    public AnchorPane root;
    public JFXTextField txtSearch;
    public JFXTextField txtId;
    public JFXTextField txtQcName;
    public JFXTextField txtCity;
    public JFXComboBox <String> cmbDistrict;
    public JFXTextField txtCapacity;
    public JFXTextField txtHead;
    public JFXTextField txtHeadContact;
    public JFXTextField txtQcContact;
    public JFXTextField txtQcContact2;
    public JFXButton btnSave;
    public JFXButton btnDelete;
    public JFXListView <String> lstQCenters;

    public void initialize(){


        dis(cmbDistrict);


        //Disable Fields
        txtId.setDisable(true);
        txtQcName.setDisable(true);
        txtCity.setDisable(true);
        txtCapacity.setDisable(true);
        txtHead.setDisable(true);
        txtHeadContact.setDisable(true);
        txtQcContact.setDisable(true);
        txtQcContact2.setDisable(true);
        cmbDistrict.setDisable(true);
        btnDelete.setDisable(true);
        btnSave.setDisable(true);
    }

    static void dis(JFXComboBox<String> cmbDistrict) {
        String districtsText =
                "Colombo\n" +
                        "Gampaha\n" +
                        "Kalutara\n" +
                        "Kandy\n" +
                        "Matale\n" +
                        "Nuwara Eliya\n" +
                        "Galle\n" +
                        "Matara\n" +
                        "Hambantota\n" +
                        "Jaffna\n" +
                        "Mannar\n" +
                        "Vauniya\n" +
                        "Mullativue\n" +
                        "Ampara\n" +
                        "Trincomalee\n" +
                        "Batticaloa\n" +
                        "Kilinochchi\n" +
                        "Kurunegala\n" +
                        "Puttalam\n" +
                        "Anuradhapura\n" +
                        "Polonnaruwa\n" +
                        "Badulla\n" +
                        "Moneragala\n" +
                        "Ratnapura\n" +
                        "Kegalle";
        String[] districts = districtsText.split("\n");
        ObservableList<String> olDistricts = FXCollections.observableArrayList(Arrays.asList(districts));
        cmbDistrict.setItems(olDistricts);
    }

    public void btnHome_OnAction(ActionEvent actionEvent) throws IOException {
        Scene dashScene = new Scene(FXMLLoader.load(getClass().getResource("/view/Dashboard.fxml")));
        Stage primaryStage = (Stage) (root.getScene().getWindow());
        primaryStage.setScene(dashScene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    public void btnAddQc_OnAction(ActionEvent actionEvent) {
        txtId.setDisable(false);
        txtQcName.setDisable(false);
        txtCity.setDisable(false);
        txtCapacity.setDisable(false);
        txtHead.setDisable(false);
        txtHeadContact.setDisable(false);
        txtQcContact.setDisable(false);
        txtQcContact2.setDisable(false);
        cmbDistrict.setDisable(false);
        btnDelete.setDisable(false);

        //Generating an ID
        if (txtId.getText().isEmpty()){
            txtId.setText("C001");
        }else {
            try {
                PreparedStatement prStm = DBConnection.getInstance().getConnection().prepareStatement("SELECT id FROM Qcinformation");
                ResultSet resultSet = prStm.executeQuery();
                while (resultSet.next()) {
                    String value = resultSet.getString(1);
                    txtId.setText(value);
                    if (!value.isEmpty()){
                        String number = txtId.getText().trim();
                        String newNo = number.replace("C", "");
                        int no = Integer.parseInt(newNo);

                        if (no>=1){
                            String temp = "C00"+(no+1);
                            txtId.setText(temp);
                        }
                        if (no>=9){
                            String temp = "C0"+(no+1);
                            txtId.setText(temp);
                        }
                        if (no>=99){
                            String temp = "C"+(no+1);
                            txtId.setText(temp);
                        }
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public void btnSave_OnAction(ActionEvent actionEvent) {
        try {
            PreparedStatement prStm = DBConnection.getInstance().getConnection().prepareStatement("INSERT INTO QcInformation VALUES (?,?,?,?,?,?,?,?,?)");
            prStm.setString(1, txtId.getText());
            prStm.setString(2, txtQcName.getText());
            prStm.setString(3, cmbDistrict.getSelectionModel().getSelectedItem());
            prStm.setString(4, txtCity.getText());
            prStm.setString(5, txtCapacity.getText());
            prStm.setString(6, txtHead.getText());
            prStm.setString(7, txtHeadContact.getText());
            prStm.setString(8, txtQcContact.getText());
            prStm.setString(9, txtQcContact2.getText());

            int i = prStm.executeUpdate();
            if (i>0){
                new Alert(Alert.AlertType.CONFIRMATION, "Successfully Updated", ButtonType.OK).show();
            }


        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.CONFIRMATION, "Oops Something went wrong!", ButtonType.OK).show();
        }
    }

    public void btnDelete_OnAction(ActionEvent actionEvent) {
    }
}
