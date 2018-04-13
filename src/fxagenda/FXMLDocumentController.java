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
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
    private ResultSet rs2;
    Conexion conexion = new Conexion();
    Connection con = conexion.conectar();
        

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

    //IDs botones de edición
    @FXML
    private Button bt_editText;
    @FXML
    private Button bt_cancelEdit;
    @FXML
    private Button bt_saveEdit;

    //IDs botones nuevo registro
    @FXML
    private Button bt_newEntry;
    @FXML
    private Button bt_cancelNewEntry;

    @FXML
    private Button bt_deleteEntry;
    @FXML
    private Button bt_saveNewEntry;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //usado para rellenar el checkBox cb_estadoCivil
        cb_estadoCivil.setItems(estadoList);
        //valor inicial del checkBox cb_estadoCivil
        cb_estadoCivil.setValue("Casado");
        

        PreparedStatement stmt;

        try {

            stmt = con.prepareStatement("SELECT e.IdEmpleado,e.Apellidos,e.Nombre,e.Cargo,e.FNacimiento,e.FContrato,e.Telefono,ea.Nombre as nombreJefe,ea.Apellidos as apellidoJefe from Empleados e left JOIN empleados ea on ea.IdEmpleado=e.Jefe");
            rs = stmt.executeQuery();
            rs.first();
            fillOutForm();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        
        //comprobacion de botones
        buttons();

        //deshabilitar textFields al cargar el programa
        disableTextFieldEditable();
        cancelPressed();
    }
    private void refreshRS(){
        try {
            PreparedStatement stmt;
            stmt = con.prepareStatement("SELECT e.IdEmpleado,e.Apellidos,e.Nombre,e.Cargo,e.FNacimiento,e.FContrato,e.Telefono,ea.Nombre as nombreJefe,ea.Apellidos as apellidoJefe from Empleados e left JOIN empleados ea on ea.IdEmpleado=e.Jefe");
            rs = stmt.executeQuery();
            rs.first();
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
    }
    private void disableTextFieldEditable() {

        tf_IdEmpleado.setDisable(true);
        tf_nombre.setDisable(true);
        tf_apellidos.setDisable(true);
        tf_cargo.setDisable(true);
        tf_telefono.setDisable(true);
        dp_fechaNacimiento.setDisable(true);
        dp_fechaContrato.setDisable(true);
        cb_estadoCivil.setDisable(true);
        tf_jefe.setDisable(true);

    }
    
    private void enableTextFieldEditable(){
        tf_IdEmpleado.setDisable(true);//siempre deshabilitado
        tf_nombre.setDisable(false);
        tf_apellidos.setDisable(false);
        tf_cargo.setDisable(false);
        tf_telefono.setDisable(false);
        dp_fechaNacimiento.setDisable(false);
        dp_fechaContrato.setDisable(false);
        cb_estadoCivil.setDisable(false);
        tf_jefe.setDisable(false);
    }
    //boton de edición
    @FXML
    private void editText() { 
        enableTextFieldEditable();
        editTextPressed();
    }

    //boton cancelar, dentro de edición
    @FXML   
    private void cancelEditText(){ 
        disableTextFieldEditable();
        cancelEditPressed();
    }

    @FXML
    private void newEntry(){
        clearForm();
        enableTextFieldEditable();
        newEntryPressed();
        navigationButtonsDisabled();
    }
    
    @FXML
    private void cancelNewEntry(){
        fillOutForm();
        disableTextFieldEditable();
        cancelPressed();
        buttons();
    }
    //Acciones para el botón guardar nuevo registro
    @FXML
    private void saveForm(){
        saveNewEntry();
        refreshRS();
        try{rs.last();}catch(Exception ex){System.out.println(ex.getMessage());}
        fillOutForm();
        cancelPressed();
        buttons();
    }
    
    //Acciones para el botón borrar
    @FXML
    private void delete(){
        
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Borrado de datos");
        alert.setHeaderText("");
        alert.setContentText("¿Se va a eliminar el registro actual. Seguro que quieres continuar?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            deleteEntry();
            refreshRS();
            try{rs.last();}catch(Exception ex){System.out.println(ex.getMessage());}
            fillOutForm();
            cancelPressed();
            buttons();
            System.out.println("OK");
        } else {
            System.out.println("CANCEL");
        }
    }
    
    @FXML
    private void refresh(){
        refreshRS();
        fillOutForm();
        buttons();
    }
    //Contador de registros
    private String registryCounter(){
        PreparedStatement stmt;
        ResultSet rsCont;
        String contador = "";
        System.out.println(contador);
        try {
            stmt = con.prepareStatement("SELECT count(idempleado) FROM empleados");
            rsCont = stmt.executeQuery();
           
            contador = Integer.toString(rsCont.getInt(1)+1);
            System.out.println(contador);
            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return contador;
    }
    //mostrar formulario en blanco
    private void clearForm() {
        
        
        tf_IdEmpleado.setText(registryCounter());
        tf_apellidos.setText("");
        tf_nombre.setText("");
        tf_cargo.setText("");
        //para mostrar en el datePicker
        // dp_fechaNacimiento.setText("");
        // dp_fechaContrato.setText("");
        tf_telefono.setText("");
        tf_jefe.setText(""); 
    }
    
//estado de los botones para navegar entre los registros
    private void buttons() {
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
    
    private void navigationButtonsDisabled(){
        bt_last.setDisable(true);
        bt_next.setDisable(true);
        bt_first.setDisable(true);
        bt_previous.setDisable(true);
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

    private void editTextInvisible(){bt_editText.setVisible(false);}
    private void editTextVisible(){bt_editText.setVisible(true);}
    
        private void cancelEditInvisible() {bt_cancelEdit.setVisible(false);}
        private void cancelEditVisible() {bt_cancelEdit.setVisible(true);}
    
        private void saveEditInvisible() {bt_saveEdit.setVisible(false);}
        private void saveEditVisible() {bt_saveEdit.setVisible(true);}
    
    private void newEntryInvisible() {bt_newEntry.setVisible(false);}
    private void newEntryVisible() {bt_newEntry.setVisible(true);}
    
        private void cancelNewEntryInvisible() {bt_cancelNewEntry.setVisible(false);}
        private void cancelNewEntryVisible() {bt_cancelNewEntry.setVisible(true);}
        
        private void saveNewEntryInvisible() {bt_saveNewEntry.setVisible(false);}
        private void saveNewEntryVisible() {bt_saveNewEntry.setVisible(true);}
        
    private void deleteEntryInvisible() {bt_deleteEntry.setVisible(false);}
    private void deleteEntryVisible() {bt_deleteEntry.setVisible(true);}
    
    private void editTextPressed(){
        editTextInvisible();
            cancelEditVisible();
            saveEditVisible();        
        newEntryInvisible();
            saveNewEntryInvisible();
            deleteEntryInvisible();
        deleteEntryInvisible();
    }
    
    private void cancelEditPressed(){
        editTextVisible();
            cancelEditInvisible();
            saveEditInvisible();
        newEntryVisible();
            cancelNewEntryInvisible();
            saveNewEntryInvisible();
        deleteEntryVisible();
    }
    private void cancelPressed(){
        editTextVisible();
            cancelEditInvisible();
            saveEditInvisible();
        newEntryVisible();
            cancelNewEntryInvisible();
            saveNewEntryInvisible();
        deleteEntryVisible();
        buttons();
    }
    
    private void newEntryPressed(){
        editTextInvisible();
            cancelEditInvisible();
            saveEditInvisible();
        newEntryInvisible();
            cancelNewEntryVisible();
            saveNewEntryVisible();
        deleteEntryInvisible();
    }
    
    private void cancelNewEntryPressed(){
        editTextVisible();
            cancelEditInvisible();
            saveEditInvisible();
        newEntryVisible();
            cancelNewEntryInvisible();
            saveNewEntryInvisible();
        deleteEntryVisible();
        buttons();
    }
     private void saveNewEntry(){
        PreparedStatement stmt;
        
        try {
            
            String apellidos = tf_apellidos.getText();
            String nombre = tf_nombre.getText();
            String cargo = tf_cargo.getText();
            //para mostrar en el datePicker
            //String fNac = dp_fechaNacimiento.getConverter().toString(LocalDate.MIN);
            //String fCon = dp_fechaContrato.getConverter().toString(LocalDate.MIN);
            String telefono = tf_telefono.getText();
            String jefe = tf_jefe.getText();
             
            
            stmt = con.prepareStatement("INSERT INTO empleados ( Apellidos, Nombre,Cargo, FNacimiento, FContrato,Telefono ,Jefe) " +
                                        " VALUES ('eeee', 'dddd', 'pin', 20120101,20120101,12345,2");
            stmt.executeUpdate();
            
            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
     private void deleteEntry(){
        PreparedStatement stmt2;
        
        try {
            stmt2 = con.prepareStatement("DELETE FROM empleados where idempleado=?");
            stmt2.setInt(1, rs.getInt("IdEmpleado"));
            stmt2.executeUpdate();
            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }


}
//crear un menu que contenga las diferentes aplicaciones
