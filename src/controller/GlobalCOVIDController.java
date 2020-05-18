package controller;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import util.GlobalTM;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

public class GlobalCOVIDController {
    public AnchorPane root;
    public JFXDatePicker txtDate;
    public JFXTextField txtConfirmed;
    public JFXTextField txtRecovered;
    public JFXTextField txtDeaths;
    public Text lblLastUpdated;
    public Text lblConfirmedCases;
    public Text lblRecoveredCases;
    public Text lblDeathsCases;
    public GlobalTM globalTM;

    public void initialize () throws SQLException {
        int confirmed = 0;
        int recovered = 0;
        int deaths =0;
        String date="";

        Statement stm = DBConnection.getInstance().getConnection().createStatement();
        ResultSet resultSet = stm.executeQuery("SELECT * FROM globalcovidtwo");

        while (resultSet.next()){
            date = resultSet.getString(1);
            confirmed = resultSet.getInt(2);
            recovered = resultSet.getInt(3);
            deaths = resultSet.getInt(4);
        }

        lblLastUpdated.setText(date);
        lblConfirmedCases.setText(confirmed+"");
        lblRecoveredCases.setText(recovered+"");
        lblDeathsCases.setText(deaths+"");

        txtConfirmed.setText(String.valueOf(confirmed));
        txtDeaths.setText(String.valueOf(deaths));
        txtRecovered.setText(String.valueOf(recovered));

    }

    public void btnHome_OnAction(ActionEvent actionEvent) throws IOException {
       Scene dashStage = new Scene(FXMLLoader.load(getClass().getResource("/view/Dashboard.fxml")));
       Stage primaryStage = (Stage) (root.getScene().getWindow());
       primaryStage.setScene(dashStage);
       primaryStage.centerOnScreen();
       primaryStage.show();
    }

    public void validateDate () {

        LocalDate date = txtDate.getValue();
        if (date==null) {
            new Alert(Alert.AlertType.ERROR, "Please Enter Correct Values", ButtonType.OK).show();
        }
        else {
        int year = txtDate.getValue().getYear();
        int month = txtDate.getValue().getMonthValue();
        System.out.println(year);
        if (year <= 2019) {
            if (month <= 11) {
                new Alert(Alert.AlertType.ERROR, "Please specify a valid date", ButtonType.OK).show();
                txtDate.setValue(null);
                return;
                 }
            }
        }
    }

    public void validateFields () {
        String deaths = txtDeaths.getText();
        String confirmed = txtConfirmed.getText();
        String recovered = txtRecovered.getText();
        LocalDate date = txtDate.getValue();

        if (date==null) {
            new Alert(Alert.AlertType.ERROR, "Please Enter Correct Values", ButtonType.OK).show();
            return;
        }

        else if (deaths.trim().equals("0") || confirmed.trim().equals("0") || recovered.trim().equals("0")) {
            new Alert(Alert.AlertType.ERROR, "Please Enter Correct Values", ButtonType.OK).show();
            txtDate.setValue(null);

        }

        else if (deaths.trim().equals("") || confirmed.trim().equals("") || recovered.trim().equals("")) {
            new Alert(Alert.AlertType.ERROR, "Please Enter Correct Values", ButtonType.OK).show();
            txtDate.setValue(null);

        }
        return;
    }

    public void btnUpdate(ActionEvent actionEvent) throws SQLException {
        validateDate();
        validateFields();

        PreparedStatement stm;
        try {
            stm = DBConnection.getInstance().getConnection().
                    prepareStatement
                            ("INSERT INTO globalcovidtwo (date, confirmed, recovered, deaths) VALUE (?,?,?,?)");

            LocalDate date = txtDate.getValue();
            String recovered = txtRecovered.getText();
            String confirmed = txtConfirmed.getText();
            String deaths = txtDeaths.getText();

            stm.setDate(1, Date.valueOf(date));
            stm.setInt(2, Integer.parseInt(confirmed));
            stm.setInt(3, Integer.parseInt(recovered));
            stm.setInt(4, Integer.parseInt(deaths));

            stm.execute();

            /////////////////////////////////////////////////////////
            stm = DBConnection.getInstance().getConnection().
                    prepareStatement
                            ("INSERT INTO globalcovidone (date, confirmed, recovered, deaths) VALUE (?,?,?,?)");

            stm.setDate(1, Date.valueOf(date));
            stm.setInt(2, Integer.parseInt(confirmed));
            stm.setInt(3, Integer.parseInt(recovered));
            stm.setInt(4, Integer.parseInt(deaths));

            boolean b = stm.execute();
            if (!b) {
                new Alert(Alert.AlertType.CONFIRMATION, "Success", ButtonType.OK).show();
                initialize();
            }
            if (b){
                new Alert(Alert.AlertType.ERROR, "Fail", ButtonType.OK).show();
            }
            //////////////////////////////////////////////////////////////////////////////



        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
