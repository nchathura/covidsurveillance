package controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;

public class ManageHospitalController {
    public AnchorPane root;
    public JFXTextField txtId;
    public JFXComboBox <String> cmbDistricts;
    public JFXListView <String> lstHospitals;
    public JFXTextField txtHospitalName;
    public JFXTextField txtCity;
    public JFXTextField txtCapacity;
    public JFXTextField txtDirector;
    public JFXTextField txtDirectorContact;
    public JFXTextField txtHospitalContact1;
    public JFXTextField txtHospitalContact2;
    public JFXTextField txtFax;
    public JFXTextField txtEmail;


    public void initialize (){
        if (txtId.getText().isEmpty()) {
            txtId.setText("H001");
        }

        String districtsText =
                " Colombo\n" +
                " Gampaha\n" +
                " Kalutara\n" +
                " Kandy\n" +
                " Matale\n" +
                " Nuwara Eliya\n" +
                " Galle\n" +
                " Matara\n" +
                " Hambantota\n" +
                " Jaffna\n" +
                " Mannar\n" +
                " Vauniya\n" +
                " Mullativue\n" +
                " Ampara\n" +
                " Trincomalee\n" +
                " Batticaloa\n" +
                " Kilinochchi\n" +
                " Kurunegala\n" +
                " Puttalam\n" +
                " Anuradhapura\n" +
                " Polonnaruwa\n" +
                " Badulla\n" +
                " Moneragala\n" +
                " Ratnapura\n" +
                " Kegalle";
        String[] districts = districtsText.split("\n");
        ObservableList<String> olDistricts = FXCollections.observableArrayList(Arrays.asList(districts));
        cmbDistricts.setItems(olDistricts);
    }

    public void btnHome_OnAction(ActionEvent actionEvent) throws IOException {
        Scene dashScene = new Scene(FXMLLoader.load(getClass().getResource("/view/Dashboard.fxml")));
        Stage primaryStage = (Stage) (root.getScene().getWindow());
        primaryStage.setScene(dashScene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    public void btnAddHospital_OnAction(ActionEvent actionEvent) {
        //Generating an ID
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

    public void btnSave_OnAction(ActionEvent actionEvent) {
        validatePhoneNo();
        validateEmail();
        validateCapacity();
    }

    //Validating Inputs
    public void validatePhoneNo (){
        String numberFormat= "^\\d{3}[-]\\d{7}$";
        String contact1 = txtHospitalContact1.getText().trim();
        String contact2 = txtHospitalContact2.getText().trim();
        String directorContact = txtDirectorContact.getText().trim();
        Alert myAlert = new Alert(Alert.AlertType.ERROR, "One or more contact numbers are wrong/ empty!\n Enter a correct contact no as below \n (077-1234567)");
        boolean flag = contact1.matches(numberFormat);
        boolean flag3 = directorContact.matches(numberFormat);

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

    public void validateEmail () {
            String mailFormat = "^[A-Za-z0-9+_.-]+@(.+)$";
            String mail = txtEmail.getText();

            boolean matches = mail.matches(mailFormat);

            if (!matches) {
                new Alert(Alert.AlertType.ERROR, "Wrong E-mail address \n Try again!").show();
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

    public void btnDelete_OnAction(ActionEvent actionEvent) {
    }
}
