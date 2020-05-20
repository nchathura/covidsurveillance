package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import java.util.ArrayList;
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
    public ArrayList <String> qcList = new ArrayList<>();
    public String newClicked;

    public void initialize(){
        setId();

        loadQCenters();

        getClicked();

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

    //Validating Inputs
    //Validate Phone Numbers
    public void validatePhoneNo (){
        String numberFormat= "^\\d{3}[-]\\d{7}$";
        String contact1 = txtQcContact.getText().trim();
        String contact2 = txtQcContact2.getText().trim();
        String headContact = txtHeadContact.getText().trim();
        Alert myAlert = new Alert(Alert.AlertType.ERROR, "One or more contact numbers are wrong/ empty!\n Enter a correct contact no as below \n (077-1234567)");
        boolean flag = contact1.matches(numberFormat);
        boolean flag3 = headContact.matches(numberFormat);

        if (!flag){
            myAlert.show();
        }

        else if (!flag3){
            myAlert.show();
        }

        else if (!contact2.isEmpty()){
            boolean flag2 = contact2.matches(numberFormat);
            if (!flag2){
                myAlert.show();
            }
        }
    }

    //Validate Capacity Text
    public void validateCapacity (){
        String isNumber= "^[0-9]*$";
        String capacity = txtCapacity.getText().trim();
        boolean matches = capacity.matches(isNumber);

        if (!matches || capacity.equals("")){
            new Alert(Alert.AlertType.ERROR, "Wrong capacity, try again").show();
        }
    }

    //Load Qt Centers into listView
    public void loadQCenters(){
        try {
            PreparedStatement prStm = DBConnection.getInstance().getConnection().prepareStatement("SELECT qcName FROM QcInformation");
            ResultSet resultSet = prStm.executeQuery();
            while (resultSet.next()){
                String qCenter = resultSet.getString(1);
                qcList.add(qCenter);
                ObservableList<String> hospitals = FXCollections.observableArrayList(qcList);
                lstQCenters.setItems(hospitals);
                lstQCenters.refresh();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Get Clicked Item in listView
    public void getClicked(){

        lstQCenters.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                //        Unable Fields
                txtId.setDisable(false);
                txtQcName.setDisable(false);
                txtCity.setDisable(false);
                txtHeadContact.setDisable(false);
                txtHead.setDisable(false);
                txtQcContact.setDisable(false);
                txtQcContact2.setDisable(false);
                txtCapacity.setDisable(false);
                cmbDistrict.setDisable(false);
                btnSave.setDisable(false);
                btnDelete.setDisable(false);

                newClicked = newValue;
                viewQCenters();
            }
        });
    }

    //Set QC ID When Initialize
    public void setId (){
        try {
            PreparedStatement prmStm = DBConnection.getInstance().getConnection().prepareStatement("SELECT id FROM qcInformation");

            ResultSet resultSet = prmStm.executeQuery();
            while (resultSet.next()){
                String id = resultSet.getString(1);
                System.out.println(id);
                txtId.setText(id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //View QCenter Info
    public void viewQCenters(){
        btnSave.setText("Update");

        String SQL = "SELECT * FROM qcInformation WHERE qcName=?";
        try {
            PreparedStatement prStm = DBConnection.getInstance().getConnection().prepareStatement(SQL);
            prStm.setObject(1, newClicked);
            ResultSet resultSet = prStm.executeQuery();
            while (resultSet.next()){
                txtId.setText(resultSet.getString(1));
                txtQcName.setText(resultSet.getString(2));
                cmbDistrict.getSelectionModel().select(resultSet.getString(3));
                txtCity.setText(resultSet.getString(4));
                txtCapacity.setText(resultSet.getString(5));
                txtHead.setText(resultSet.getString(6));
                txtHeadContact.setText(resultSet.getString(7));
                txtQcContact.setText(resultSet.getString(8));
                txtQcContact2.setText(resultSet.getString(9));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        btnSave.setDisable(false);
        btnSave.setText("Save");

        //Clear Fields
        txtQcName.clear();
        txtHead.clear();
        txtHeadContact.clear();
        txtQcContact.clear();
        txtQcContact2.clear();
        txtCity.clear();
        txtCapacity.clear();
        cmbDistrict.getSelectionModel().select("");

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
        if (btnSave.getText().equals("Save")) {
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
                lstQCenters.getItems().add(txtQcName.getText());

                validateCapacity();

                validatePhoneNo();
                int i = prStm.executeUpdate();
                if (i > 0) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Successfully Updated", ButtonType.OK).show();

                }


            } catch (SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.CONFIRMATION, "Oops Something went wrong!", ButtonType.OK).show();
            }
        }

        UpdateQc();

        //Clear All
        txtQcName.clear();
        txtCity.clear();
        cmbDistrict.getSelectionModel().select("");
        txtQcContact.clear();
        txtQcContact2.clear();
        txtCapacity.clear();
        txtHeadContact.clear();
        txtHead.clear();
        btnSave.setDisable(true);
    }

    public void UpdateQc(){
        if (btnSave.getText().equals("Update")){
            try {
                PreparedStatement prStm = DBConnection.getInstance().getConnection().prepareStatement
                        ("UPDATE qcinformation SET qcName=?, district=?, city=?, capacity=?, head=?, headContact=?, qcContact=?, qcContact2=? WHERE id=?");
                prStm.setObject(1,txtQcName.getText());
                prStm.setObject(2,cmbDistrict.getSelectionModel().getSelectedItem());
                prStm.setObject(3,txtCity.getText());
                prStm.setObject(4,txtCapacity.getText());
                prStm.setObject(5,txtHead.getText());
                prStm.setObject(6,txtHeadContact.getText());
                prStm.setObject(7,txtQcContact.getText());
                prStm.setObject(8,txtQcContact2.getText());
                prStm.setObject(9,txtId.getText());

                validateCapacity();

                validatePhoneNo();

                int i = prStm.executeUpdate();
                if (i>0){
                    new Alert(Alert.AlertType.CONFIRMATION, "Successfully Updated!", ButtonType.OK).show();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.CONFIRMATION, "Oops Something went wrong! Try again.", ButtonType.OK).show();
            }
        }
    }

    public void btnDelete_OnAction(ActionEvent actionEvent) {
        try {
            PreparedStatement prStm = DBConnection.getInstance().getConnection().prepareStatement("DELETE FROM qcInformation WHERE id=?");

            lstQCenters.getItems().remove(txtQcName.getText());
            prStm.setString(1,txtId.getText());
            int i = prStm.executeUpdate();

            if (i>0){
                btnDelete.setDisable(true);
                new Alert(Alert.AlertType.CONFIRMATION, "Successfully Deleted!", ButtonType.OK).show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.CONFIRMATION, "Oops Something went wrong! Try again.", ButtonType.OK).show();
        }
        //Clear All
        txtQcName.clear();
        txtCity.clear();
        cmbDistrict.getSelectionModel().select("");
        txtQcContact2.clear();
        txtCapacity.clear();
        txtHeadContact.clear();
        txtQcContact.clear();
        txtHead.clear();
        btnSave.setDisable(true);
    }
}
