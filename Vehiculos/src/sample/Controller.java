package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
//import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import static java.lang.Integer.valueOf;

public class Controller {
    String fileName;
    @FXML
    private javafx.scene.control.TextField nameField;

    @FXML
    private javafx.scene.control.TextField descriptionField;

    @FXML
    private javafx.scene.control.TextField priceField;

    @FXML
    private javafx.scene.image.ImageView imagenField;  //Image imageField;

    @FXML
    private javafx.scene.control.TextField webField;

    @FXML
    private javafx.scene.control.TextField idField;

    @FXML
    private javafx.scene.control.CheckBox updatableField;

    @FXML
    private HBox editButtons;

    @FXML
    private HBox navigationButtons;

    @FXML
    private Button addNewButton;

    @FXML
    private Button updatableFieldButton;

    private void showFirstVehicule() throws ParserConfigurationException, TransformerException, SAXException, IOException {
        // Return first vehicule
        lecturaArchivosModel Rv = new lecturaArchivosModel();
        Rv.getVehicules();
        printLabels(Rv.getFirstVehicule());
    }

    private void showLastVehicule() throws ParserConfigurationException, TransformerException, SAXException, IOException {
        // Returns last vehicule
        lecturaArchivosModel Rv = new lecturaArchivosModel();
        Rv.getVehicules();
        printLabels(Rv.getLastVehicule());
    }

    private void showNextPreviousVehicule(int position) throws ParserConfigurationException, TransformerException, SAXException, IOException {
        // Return next vehicule
        lecturaArchivosModel Rv = new lecturaArchivosModel();
        Rv.getVehicules();
        printLabels(Rv.getNextPreviousVehicule(position));
    }

    private void printLabels(ArrayList<String> vehicule) throws FileNotFoundException {
        // Prints the vehicule information return in each situation
        fileName = vehicule.get(0);
        updatableFieldButton.setDisable(!Boolean.parseBoolean(vehicule.get(2)));
        idField.setText(vehicule.get(1));
        nameField.setText(vehicule.get(3));
        descriptionField.setText(vehicule.get(4));
        priceField.setText(vehicule.get(5));

        FileInputStream input = new FileInputStream("Informacion/" + vehicule.get(7));
        Image image = new Image(input);
        imagenField.setImage(image);
        webField.setText(vehicule.get(6));
    }

    public void handleFirstVehicule(javafx.event.ActionEvent actionEvent) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        showFirstVehicule();
    }

    public void handleLastVehicule(javafx.event.ActionEvent actionEvent) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        showLastVehicule();
    }

    public void handleNextVehicule(javafx.event.ActionEvent actionEvent) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        showNextPreviousVehicule(1);
    }

    public void handlePreviousVehicule(javafx.event.ActionEvent actionEvent) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        showNextPreviousVehicule(-1);
    }

    public void handleRegistrationNewVehicule(javafx.event.ActionEvent actionEvent) throws ParserConfigurationException, IOException, SAXException, TransformerException{
        // Creates a new vehicle
        String idVeh;

        lecturaArchivosModel Rv = new lecturaArchivosModel();
        // Alert if an input is empty or if price is not money etc
        Window owner = addNewButton.getScene().getWindow();

        if(nameField.getText().isEmpty()){
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error", "Please enter a name.");
            return;
        }
        if(descriptionField.getText().isEmpty()){
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error", "Please enter a description.");
            return;
        }
        if(priceField.getText().isEmpty()){
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error", "Please enter a price.");
            return;
        }
        if(!isPrice(priceField.getText())){
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error", "Please enter just numbers and no decimals.\nNo currency needed");
            return;
        }
        /*if(imagenField.getImage(){
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error", "Please enter a webpage");
            return;
        }*/
        if(webField.getText().isEmpty()){
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error", "Please enter a webpage");
            return;
        }

        idVeh = selectUniqueId();
        Rv.registrationNewVehicule(idVeh, toString().valueOf(updatableField.isSelected()), nameField.getText(), descriptionField.getText(), priceField.getText(), webField.getText(), "no-imagen.png");

        AlertHelper.showAlert(Alert.AlertType.CONFIRMATION, owner, "New vehicle added", "\nName: " + nameField.getText());
        displayMainWindow();
    }

    private String selectUniqueId() throws ParserConfigurationException, TransformerException, SAXException, IOException {
        int idveh = 1;

        lecturaArchivosModel Rv = new lecturaArchivosModel();
        Rv.getVehicules();

        while(Rv.getIds().contains(String.valueOf(idveh))) {
            idveh += 1;
        }
        return String.valueOf(idveh);
    }

    private void enableEditMode(boolean t, boolean f) throws IOException{
        navigationButtons.setVisible(f);
        editButtons.setVisible(t);
        nameField.setEditable(t);
        webField.setEditable(t);
        priceField.setEditable(t);
        descriptionField.setEditable(t);
    }

    private static boolean isPrice (String price){
        String regex = "\\b[0-9]+\\b";
        return price.matches(regex);
    }

    public void handleUpdateVehicle() throws ParserConfigurationException, IOException, SAXException, TransformerException{
        lecturaArchivosModel Rv = new lecturaArchivosModel();
        System.out.println("this is the file:" + fileName);
        Rv.registrationNewVehicule(idField.getText(), toString().valueOf(updatableField.isSelected()), nameField.getText(), descriptionField.getText(), priceField.getText(), "no-imagen.png", webField.getText());
        System.out.println("updated done: " + nameField.getText() );
    }

    public void displayAddNew() throws IOException {
        Stage stage = (Stage) nameField.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("newVehicule.fxml"));
        stage.setScene(new Scene(root, 700, 600));
        stage.show();
    }

    public void displayMainWindow() throws IOException {
        Stage stage = (Stage) nameField.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        stage.setScene(new Scene(root, 700, 600));
        stage.show();
    }

    public void displayEdit () throws IOException{
        enableEditMode(true, false);
    }

    public void displayCancelEdit() throws IOException{
        enableEditMode(false, true);
    }
}
