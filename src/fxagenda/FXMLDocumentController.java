/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxagenda;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author DAW
 */
public class FXMLDocumentController implements Initializable {

    //Usado para rellenar el contenido del checkBox cb_estadoCivil
    private ObservableList<String> estadoList = FXCollections.observableArrayList("Soltero", "Casado", "Divorciado", "Viudo");

    private ResultSet rs;

    @FXML
    private TextField tf_IdEmpleado;

    @FXML
    private TextField tf_nombre;

    @FXML
    private TextField tf_apellidos;

    @FXML
    private TextField tf_cargo;

    @FXML
    private TextField tf_telefono;

    @FXML
    private Button bt_last;
    @FXML
    private Button bt_next;
    @FXML
    private Button bt_previous;
    @FXML
    private Button bt_first;

    @FXML
    private DatePicker dp_fechaNacimiento;
    @FXML
    private DatePicker dp_fechaContrato;
    @FXML
    private ComboBox cb_estadoCivil;
    @FXML
    private TextField tf_jefe;
    @FXML
    private Hyperlink id_salir;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //usado para rellenar el checkBox cb_estadoCivil
        cb_estadoCivil.setItems(estadoList);
        //valor inicial del checkBox cb_estadoCivil
        cb_estadoCivil.setValue("Casado");

        Conexion conexion = new Conexion();
        Connection con = conexion.conectar();
        PreparedStatement stmt;

        try {

            stmt = con.prepareStatement("SELECT e.IdEmpleado,e.Apellidos,e.Nombre,e.Cargo,e.FNacimiento,e.FContrato,e.Telefono,ea.Nombre as nombreJefe,ea.Apellidos as apellidoJefe from Empleados e left JOIN empleados ea on ea.IdEmpleado=e.Jefe");
            rs = stmt.executeQuery();
            rs.first();
            fillOutForm();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }

    private void buttons() {
        //definicion de estado de los botones
        try {
            if (rs.isLast()) {
                bt_last.setDisable(true);
                bt_next.setDisable(true);
                bt_first.setDisable(false);
                bt_previous.setDisable(false);
            }
            if (!rs.isLast() && !rs.isFirst()) {
                bt_last.setDisable(false);
                bt_next.setDisable(false);
                bt_first.setDisable(false);
                bt_previous.setDisable(false);
            }
            if (rs.isFirst()) {
                bt_last.setDisable(false);
                bt_next.setDisable(false);
                bt_first.setDisable(true);
                bt_previous.setDisable(true);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }

    private void fillOutForm() {
        try {
            tf_IdEmpleado.setText(Integer.toString(rs.getInt("IdEmpleado")));
            tf_apellidos.setText(rs.getString("apellidos"));
            tf_nombre.setText(rs.getString("nombre"));
            tf_cargo.setText(rs.getString("cargo"));
            //para mostrar en el datePicker
            dp_fechaNacimiento.setValue(rs.getDate("fNacimiento").toLocalDate());
            dp_fechaContrato.setValue(rs.getDate("fContrato").toLocalDate());
            tf_telefono.setText(rs.getString("telefono"));

            String nombreJefe = rs.getString("nombreJefe");
            String apellidoJefe = rs.getString("apellidoJefe");

            if (nombreJefe == null) {
                nombreJefe = "";
            }
            if (apellidoJefe == null) {
                apellidoJefe = "";
            }

            tf_jefe.setText(nombreJefe + " " + apellidoJefe);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        //Comprobación de botones activos
        buttons();
    }

    @FXML
    private void nextRegistry() {

        try {
            this.rs.next();

            fillOutForm();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        //comprobacion de botones
        buttons();
    }

    @FXML
    private void previousRegistry() {

        try {
            this.rs.previous();
            fillOutForm();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        //comprobacion de botones
        buttons();
    }

    @FXML
    private void firstRegistry() {

        try {
            this.rs.first();
            fillOutForm();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        //comprobacion de botones
        buttons();
    }

    @FXML
    private void lastRegistry() {

        try {
            this.rs.last();
            fillOutForm();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        //comprobacion de botones
        buttons();
    }

    @FXML
    private void salir() {
        Stage stage = (Stage) id_salir.getScene().getWindow();
        stage.close();
    }

}
//crear un menu que contenga las diferentes aplicaciones
