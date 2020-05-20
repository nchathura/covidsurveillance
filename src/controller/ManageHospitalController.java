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
    public JFXTextField txtSearchHospital;
    public ArrayList<String> hosList = new ArrayList<>();
    public String newClicked;
    public JFXButton btnSave;
    public JFXButton btnDelete;
    public JFXButton btnAddHost;

    public void initialize (){

        setId();

        loadHospitals();

        searchHospital();

        getClicked();

        ManageQuarantineCentersController.dis(cmbDistricts);

        //Disable/Unable Fields
        txtId.setDisable(true);
        txtEmail.setDisable(true);
        txtFax.setDisable(true);
        txtHospitalContact1.setDisable(true);
        txtHospitalContact2.setDisable(true);
        txtDirectorContact.setDisable(true);
        txtDirector.setDisable(true);
        txtCity.setDisable(true);
        txtCapacity.setDisable(true);
        txtHospitalName.setDisable(true);
        cmbDistricts.setDisable(true);
        btnSave.setDisable(true);
        btnDelete.setDisable(true);
    }

    //Home Button On Action
    public void btnHome_OnAction(ActionEvent actionEvent) throws IOException {
        Scene dashScene = new Scene(FXMLLoader.load(getClass().getResource("/view/Dashboard.fxml")));
        Stage primaryStage = (Stage) (root.getScene().getWindow());
        primaryStage.setScene(dashScene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    //Get Clicked Item in listView
    public void getClicked(){

        lstHospitals.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                //        Unable Fields
                txtId.setDisable(false);
                txtEmail.setDisable(false);
                txtFax.setDisable(false);
                txtHospitalContact1.setDisable(false);
                txtHospitalContact2.setDisable(false);
                txtDirectorContact.setDisable(false);
                txtDirector.setDisable(false);
                txtCity.setDisable(false);
                txtCapacity.setDisable(false);
                txtHospitalName.setDisable(false);
                cmbDistricts.setDisable(false);
                btnSave.setDisable(false);
                btnDelete.setDisable(false);

                newClicked = newValue;
                viewHospital();
            }
        });
    }

    //Search Hospital
    public void searchHospital (){
        txtSearchHospital.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                lstHospitals.getItems().clear();
                for (String hostName:hosList){
                    if (hostName.contains(newValue)){
                        lstHospitals.getItems().add(hostName);
                        lstHospitals.refresh();
                    }
                }
            }
        });
    }

    //Set Hospital ID When Initialize
    public void setId (){
//        if (txtId.getText().isEmpty()) {
//            txtId.setText("H001");
//        }else if(txtId.getText().equals("H001")){
            try {
                PreparedStatement prmStm = DBConnection.getInstance().getConnection().prepareStatement("SELECT id FROM hospitalInformation");

                ResultSet resultSet = prmStm.executeQuery();
                while (resultSet.next()){
                    String id = resultSet.getString(1);
                    System.out.println(id);
                    txtId.setText(id);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
//        }
    }

    //Load Hospitals into listView
    public void loadHospitals(){
        try {
            PreparedStatement prStm = DBConnection.getInstance().getConnection().prepareStatement("SELECT hospitalName FROM hospitalInformation");
            ResultSet resultSet = prStm.executeQuery();

            while (resultSet.next()){
                String hospital = resultSet.getString(1);
                hosList.add(hospital);
                System.out.println(hosList);
                ObservableList<String> hospitals = FXCollections.observableArrayList(hosList);
                lstHospitals.setItems(hospitals);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Validating Inputs
    //Validate Phone Numbers
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

    //Validate FAX
    public void validateFax(){
        String numberFormat= "^\\d{3}[-]\\d{7}$";
        String faxNo = txtFax.getText().trim();
        boolean matches = faxNo.matches(numberFormat);

        if (!matches){
            Alert myAlert = new Alert(Alert.AlertType.ERROR, "Enter a correct FAX no: as below \n (057-2345677)");
            myAlert.show();
        }
    }

    //Validate E-mail
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

    //On Action
    //Hospital Button On Action
    public void btnAddHospital_OnAction(ActionEvent actionEvent) {
        btnSave.setText("Save");
//        Unable Fields
        txtId.setDisable(false);
        txtEmail.setDisable(false);
        txtFax.setDisable(false);
        txtHospitalContact1.setDisable(false);
        txtHospitalContact2.setDisable(false);
        txtDirectorContact.setDisable(false);
        txtDirector.setDisable(false);
        txtCity.setDisable(false);
        txtCapacity.setDisable(false);
        txtHospitalName.setDisable(false);
        cmbDistricts.setDisable(false);
        btnSave.setDisable(false);
        btnDelete.setDisable(true);

        //Clear Fields
        txtEmail.clear();
        txtFax.clear();
        txtHospitalContact1.clear();
        txtHospitalContact2.clear();
        txtDirectorContact.clear();
        txtDirector.clear();
        txtCity.clear();
        txtCapacity.clear();
        txtHospitalName.clear();

        //Generating an ID
        if (txtId.getText().isEmpty()){
            txtId.setText("H001");
        }else {
            try {
                PreparedStatement prStm = DBConnection.getInstance().getConnection().prepareStatement("SELECT id FROM hospitalinformation");
                ResultSet resultSet = prStm.executeQuery();
                while (resultSet.next()) {
//                    txtId.setText(resultSet.getString(1));
                    String value = resultSet.getString(1);
                    txtId.setText(value);
                    if (!value.isEmpty()){
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

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

//        String number = txtId.getText().trim();
//        String newNo = number.replace("H", "");
//        int no = Integer.parseInt(newNo);
//
//        if (no>=1){
//            String temp = "H00"+(no+1);
//            txtId.setText(temp);
//        }
//        if (no>=9){
//            String temp = "H0"+(no+1);
//            txtId.setText(temp);
//        }
//        if (no>=99){
//            String temp = "H"+(no+1);
//            txtId.setText(temp);
//        }
    }

    //Save Button On Action
    public void btnSave_OnAction(ActionEvent actionEvent) {
        if (btnSave.getText().equals("Save")) {
            validatePhoneNo();
            validateFax();
            validateEmail();
            validateCapacity();

            String SQL = "INSERT INTO hospitalInformation VALUES (?,?,?,?,?,?,?,?,?,?,?)";
            try {
                PreparedStatement prStm = DBConnection.getInstance().getConnection().prepareStatement(SQL);
                String id = txtId.getText();
                String name = txtHospitalName.getText();
                String district = cmbDistricts.getSelectionModel().getSelectedItem();
                String city = txtCity.getText();
                String director = txtDirector.getText();
                String dirContact = txtDirectorContact.getText();
                String hosContact1 = txtHospitalContact1.getText();
                String hosContact2 = txtHospitalContact2.getText();
                String fax = txtFax.getText();
                String mail = txtEmail.getText();
                String capacity = txtCapacity.getText();

                prStm.setObject(1, id);
                prStm.setObject(2, name);
                prStm.setObject(3, city);
                prStm.setObject(4, district);
                prStm.setObject(5, Integer.parseInt(capacity));
                prStm.setObject(6, director);
                prStm.setObject(7, dirContact);
                prStm.setObject(8, hosContact1);
                prStm.setObject(9, hosContact2);
                prStm.setObject(10, fax);
                prStm.setObject(11, mail);

                lstHospitals.getItems().add(txtHospitalName.getText());
                int i = prStm.executeUpdate();
                if (i > 0) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Successfully Saved", ButtonType.OK).show();
                }

            } catch (SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Something went wrong, try again", ButtonType.OK).show();
            }

            //Clear Fields
            txtEmail.clear();
            txtFax.clear();
            txtHospitalContact1.clear();
            txtHospitalContact2.clear();
            txtDirectorContact.clear();
            txtDirector.clear();
            txtCity.clear();
            txtCapacity.clear();
            txtHospitalName.clear();

            //Disable/Unable Fields
            txtId.setDisable(true);
            txtEmail.setDisable(true);
            txtFax.setDisable(true);
            txtHospitalContact1.setDisable(true);
            txtHospitalContact2.setDisable(true);
            txtDirectorContact.setDisable(true);
            txtDirector.setDisable(true);
            txtCity.setDisable(true);
            txtCapacity.setDisable(true);
            txtHospitalName.setDisable(true);
            cmbDistricts.setDisable(true);
            btnSave.setDisable(true);
        }
        updateHospital();

        //Clear Fields
        txtEmail.clear();
        txtFax.clear();
        txtHospitalContact1.clear();
        txtHospitalContact2.clear();
        txtDirectorContact.clear();
        txtDirector.clear();
        txtCity.clear();
        txtCapacity.clear();
        txtHospitalName.clear();
    }

    //View Hospital Info
    public void viewHospital(){
        btnSave.setText("Update");

        String SQL = "SELECT * FROM hospitalInformation WHERE HospitalName=?";
        try {
            PreparedStatement prStm = DBConnection.getInstance().getConnection().prepareStatement(SQL);
            prStm.setObject(1, newClicked);
            ResultSet resultSet = prStm.executeQuery();
            while (resultSet.next()){
                String id = resultSet.getString(1);
                String name = resultSet.getString(2);
                String city = resultSet.getString(3);
                String district = resultSet.getString(4);
                String capacity = resultSet.getString(5);
                String director = resultSet.getString(6);
                String dirContact = resultSet.getString(7);
                String hosContact1 = resultSet.getString(8);
                String hosContact2 = resultSet.getString(9);
                String fax = resultSet.getString(10);
                String mail = resultSet.getString(11);

                txtId.setText(id);
                txtHospitalName.setText(name);
                txtCity.setText(city);
                cmbDistricts.getSelectionModel().select(district);
                txtCapacity.setText(capacity);
                txtDirector.setText(director);
                txtDirectorContact.setText(dirContact);
                txtHospitalContact1.setText(hosContact1);
                txtHospitalContact2.setText(hosContact2);
                txtFax.setText(fax);
                txtEmail.setText(mail);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Update Hospital
    public void updateHospital (){
        if (btnSave.getText().equals("Update")){
            validatePhoneNo();
            validateFax();
            validateEmail();
            validateCapacity();
            try {
                PreparedStatement prStm = DBConnection.getInstance().getConnection().prepareStatement
                        ("UPDATE hospitalinformation SET hospitalName=?, city=?, district=?, capacity=?, director=?, directorContact=?, hospitalContact2=?, hospitalContact2=?, hospitalFax=?, hospitalEmail=? WHERE id=?");
                prStm.setObject(1,txtHospitalName.getText());
                prStm.setObject(2,txtCity.getText());
                prStm.setObject(3,cmbDistricts.getSelectionModel().getSelectedItem());
                prStm.setObject(4,txtCapacity.getText());
                prStm.setObject(5,txtDirector.getText());
                prStm.setObject(6,txtDirectorContact.getText());
                prStm.setObject(7,txtHospitalContact1.getText());
                prStm.setObject(8,txtHospitalContact2.getText());
                prStm.setObject(9,txtFax.getText());
                prStm.setObject(10,txtEmail.getText());
                prStm.setString(11,txtId.getText());
                System.out.println(txtId.getText());
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

    //Delete Button On Action
    public void btnDelete_OnAction(ActionEvent actionEvent) {
        try {
            PreparedStatement prStm = DBConnection.getInstance().getConnection().prepareStatement("DELETE FROM hospitalinformation WHERE id=?");
            lstHospitals.getItems().remove(txtHospitalName.getText());

            prStm.setString(1,txtId.getText());
            int i = prStm.executeUpdate();

            if (i>0){
                new Alert(Alert.AlertType.CONFIRMATION, "Successfully Deleted!", ButtonType.OK).show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.CONFIRMATION, "Oops Something went wrong! Try again.", ButtonType.OK).show();
        }
        //Clear Fields
        txtEmail.clear();
        txtFax.clear();
        txtHospitalContact1.clear();
        txtHospitalContact2.clear();
        txtDirectorContact.clear();
        txtDirector.clear();
        txtCity.clear();
        txtCapacity.clear();
        txtHospitalName.clear();
    }
}
