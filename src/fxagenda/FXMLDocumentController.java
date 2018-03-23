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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 *
 * @author DAW
 */
public class FXMLDocumentController implements Initializable {

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
    private TextField tf_fechaNacimiento;

    @FXML
    private TextField tf_fechaContrato;

    @FXML
    private TextField tf_telefono;

    @FXML
    private Button bt_ultimo;
    @FXML
    private Button bt_next;
    @FXML
    private Button bt_previous;
    @FXML
    private Button bt_first;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Conexion conexion = new Conexion();
        Connection con = conexion.conectar();
        PreparedStatement stmt = null;

        try {

            stmt = con.prepareStatement("SELECT * from Empleados");
            rs = stmt.executeQuery();
            rs.first();
            tf_IdEmpleado.setText(Integer.toString(rs.getInt("IdEmpleado")));
            tf_apellidos.setText(rs.getString("apellidos"));
            tf_nombre.setText(rs.getString("nombre"));
            tf_cargo.setText(rs.getString("cargo"));
            tf_fechaNacimiento.setText(rs.getString("fNacimiento"));
            tf_fechaContrato.setText(rs.getString("fContrato"));
            tf_telefono.setText(rs.getString("telefono"));

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    private void buttons() throws SQLException {
        if (rs.isLast()) {
            bt_ultimo.setDisable(true);
            bt_next.setDisable(true);
        }
        if (rs.isFirst()) {
            bt_first.setDisable(true);
            bt_previous.setDisable(true);
        }

    }

    @FXML
    private void nextRegistry() throws SQLException {
        

        try {
            this.rs.next();
            tf_IdEmpleado.setText(Integer.toString(rs.getInt("IdEmpleado")));
            tf_apellidos.setText(rs.getString("apellidos"));
            tf_nombre.setText(rs.getString("nombre"));
            tf_cargo.setText(rs.getString("cargo"));
            tf_fechaNacimiento.setText(rs.getString("fNacimiento"));
            tf_fechaContrato.setText(rs.getString("fContrato"));
            tf_telefono.setText(rs.getString("telefono"));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        buttons();
    }

    @FXML
    private void previousRegistry() throws SQLException{

        try {
            this.rs.previous();
            tf_IdEmpleado.setText(Integer.toString(rs.getInt("IdEmpleado")));
            tf_apellidos.setText(rs.getString("apellidos"));
            tf_nombre.setText(rs.getString("nombre"));
            tf_cargo.setText(rs.getString("cargo"));
            tf_fechaNacimiento.setText(rs.getString("fNacimiento"));
            tf_fechaContrato.setText(rs.getString("fContrato"));
            tf_telefono.setText(rs.getString("telefono"));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        buttons();
    }

    @FXML
    private void firstRegistry() throws SQLException{

        try {
            this.rs.first();
            tf_IdEmpleado.setText(Integer.toString(rs.getInt("IdEmpleado")));
            tf_apellidos.setText(rs.getString("apellidos"));
            tf_nombre.setText(rs.getString("nombre"));
            tf_cargo.setText(rs.getString("cargo"));
            tf_fechaNacimiento.setText(rs.getString("fNacimiento"));
            tf_fechaContrato.setText(rs.getString("fContrato"));
            tf_telefono.setText(rs.getString("telefono"));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        buttons();
    }

    @FXML
    private void lastRegistry()throws SQLException {

        try {
            this.rs.last();
            tf_IdEmpleado.setText(Integer.toString(rs.getInt("IdEmpleado")));
            tf_apellidos.setText(rs.getString("apellidos"));
            tf_nombre.setText(rs.getString("nombre"));
            tf_cargo.setText(rs.getString("cargo"));
            tf_fechaNacimiento.setText(rs.getString("fNacimiento"));
            tf_fechaContrato.setText(rs.getString("fContrato"));
            tf_telefono.setText(rs.getString("telefono"));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        buttons();

    }

}
