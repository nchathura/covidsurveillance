package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
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
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import util.UserTM;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ManageUsersController {
    public AnchorPane root;
    public JFXTextField txtName;
    public JFXTextField txtContact;
    public JFXTextField txtUserName;
    public JFXTextField txtEmail;
    public JFXPasswordField txtPassword;
    public JFXComboBox <String> cmbUserRole;
    public JFXComboBox <String> cmbHospitalOrQC;
    public JFXButton btnSave;
    public JFXButton btnNew;
    public JFXTextField txtSearch;
    public TableView <UserTM> tblUser;
    public Button remove=null;

    public void initialize(){
        assignRoles();
        assignHospitals();
        loadUsers();

        //Disable/ Visible
        txtName.setDisable(true);
        txtUserName.setDisable(true);
        txtContact.setDisable(true);
        txtEmail.setDisable(true);
        txtPassword.setDisable(true);
        btnSave.setDisable(true);
        cmbUserRole.setDisable(true);
        cmbHospitalOrQC.setDisable(true);
        cmbHospitalOrQC.setVisible(false);
    }

    public void btnHome_OnAction(ActionEvent actionEvent) throws IOException {
        Scene dashScene = new Scene(FXMLLoader.load(getClass().getResource("/view/Dashboard.fxml")));
        Stage primaryStage = (Stage) (root.getScene().getWindow());
        primaryStage.setScene(dashScene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    //Validate E-mail
    public boolean validateEmail () {
        String mailFormat = "^[A-Za-z0-9+_.-]+@(.+)$";
        String mail = txtEmail.getText();

        boolean matches = mail.matches(mailFormat);

        if (!matches) {
            new Alert(Alert.AlertType.ERROR, "Wrong E-mail address \n Try again!").show();
        }
        return matches;
    }

    //Assign roles to the ComboBox
    public void assignRoles(){
        cmbUserRole.getItems().add("Admin");
        cmbUserRole.getItems().add("PSTF");
        cmbUserRole.getItems().add("Hospital IT");
        cmbUserRole.getItems().add("Q Center IT");

        setLocation();

    }

//    public void deleteSelected(){
//        tblUser.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//
//            }
//        });
//    }

    public void setLocation(){
        cmbUserRole.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue.longValue()==2 || newValue.longValue()==3){
                    cmbHospitalOrQC.setVisible(true);
                    cmbHospitalOrQC.setPromptText(newValue.longValue()==2?"Select Hospital":"Select QT Center");
                }

                else if (newValue.longValue()==0){
                    cmbHospitalOrQC.setVisible(false);
                }
                else if (newValue.longValue()==1){
                    cmbHospitalOrQC.setVisible(false);
                }
            }
        });
    }

    public void assignHospitals(){
        ArrayList<String> hospital = new ArrayList<>();
        ArrayList<String> qCenter = new ArrayList<>();
        cmbUserRole.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue.longValue()==2){
                    try {
                        PreparedStatement prStm = DBConnection.getInstance().getConnection().prepareStatement("SELECT id FROM hospitalInformation");
                        ResultSet resultSet = prStm.executeQuery();

                        while (resultSet.next()){
                            hospital.add(resultSet.getString(1));
                        }

                        ObservableList<String> hospitalsList = FXCollections.observableArrayList(hospital);
                        cmbHospitalOrQC.setItems(hospitalsList);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                else if (newValue.longValue()==3){
                    try {
                        PreparedStatement prStm = DBConnection.getInstance().getConnection().prepareStatement("SELECT id FROM qcInformation");
                        ResultSet resultSet = prStm.executeQuery();

                        while (resultSet.next()){
                            qCenter.add(resultSet.getString(1));
                        }

                        ObservableList<String> qcList = FXCollections.observableArrayList(qCenter);
                        cmbHospitalOrQC.setItems(qcList);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void btnNew_OnAction(ActionEvent actionEvent) {
        txtName.setDisable(false);
        txtUserName.setDisable(false);
        txtContact.setDisable(false);
        txtEmail.setDisable(false);
        txtPassword.setDisable(false);
        btnNew.setDisable(true);
        btnSave.setDisable(false);
        cmbUserRole.setDisable(false);
        cmbHospitalOrQC.setDisable(false);
        cmbHospitalOrQC.setVisible(false);
        cmbUserRole.getSelectionModel().clearSelection();
    }

    public void btnSave_OnAction(ActionEvent actionEvent) {
        btnNew.setDisable(false);
        btnSave.setDisable(true);

        try {
            PreparedStatement prStm = DBConnection.getInstance().getConnection().prepareStatement("INSERT INTO userinformation VALUES (?,?,?,?,?,?)");
            prStm.setString(1, txtName.getText());
            prStm.setString(2, txtContact.getText());
            prStm.setString(3, txtUserName.getText());
            prStm.setString(4, txtEmail.getText());
            prStm.setString(5, txtPassword.getText());
            prStm.setString(6, cmbUserRole.getSelectionModel().getSelectedItem());

            prStm.executeUpdate();

            if (cmbHospitalOrQC.getSelectionModel().getSelectedItem().contains("H")){
                PreparedStatement prStm1 = DBConnection.getInstance().getConnection().prepareStatement("INSERT INTO user_hospital VALUES (?,?)");
                prStm1.setString(1, txtUserName.getText());
                prStm1.setString(2, cmbHospitalOrQC.getSelectionModel().getSelectedItem());

                prStm1.executeUpdate();
            }
            else {
                if (cmbHospitalOrQC.getSelectionModel().getSelectedItem().contains("C")){
                    PreparedStatement prStm1 = DBConnection.getInstance().getConnection().prepareStatement("INSERT INTO user_qccenter VALUES (?,?)");
                    prStm1.setString(1,txtUserName.getText());
                    prStm1.setString(2, cmbHospitalOrQC.getSelectionModel().getSelectedItem());

                    prStm1.executeUpdate();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadUsers(){
        PreparedStatement prStm = null;
        ArrayList<UserTM> userTable = new ArrayList<>();
        tblUser.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblUser.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("userName"));
        tblUser.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("role"));
        tblUser.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("remove"));

        try {
            prStm = DBConnection.getInstance().getConnection().prepareStatement("SELECT name,username,userRole FROM userInformation ");
            ResultSet resultSet = prStm.executeQuery();
            while (resultSet.next()){
                userTable.add(new UserTM(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        remove=new Button("remove")));

            }

            ObservableList<UserTM> userOList = FXCollections.observableArrayList(userTable);
            tblUser.setItems(userOList);
            tblUser.refresh();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
