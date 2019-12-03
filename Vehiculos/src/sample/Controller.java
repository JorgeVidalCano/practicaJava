package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static java.lang.Integer.valueOf;

public class Controller {
    String fileName;

    @FXML
    private javafx.scene.control.TextField nameField;

    @FXML
    private javafx.scene.control.TextField imgName;

    @FXML
    private javafx.scene.control.Label imageLabel;

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
    private Pane inputs;

    @FXML
    private HBox editButtons;

    @FXML
    private HBox navigationButtons;

    @FXML
    private Button startRentingButton;

    @FXML
    private Button addNewButton;

    @FXML
    private Button updatableFieldButton;

    private ArrayList<ArrayList<String>> vehicles = new ArrayList();
    private static int position = 0;

    public Controller () throws ParserConfigurationException, TransformerException, SAXException, IOException {
        lecturaArchivosModel Rv = new lecturaArchivosModel();
        this.vehicles = Rv.getVehicules();
    }

    private void reStartApp() throws ParserConfigurationException, IOException, SAXException, TransformerException {
        // Rereads the info when changes are made.
        lecturaArchivosModel Rv = new lecturaArchivosModel();
        this.vehicles = Rv.getVehicules();
        showFirstVehicule();
    }

    private void showFirstVehicule() throws IOException {
        // Return first vehicle
        position = 0;
        printLabels(this.vehicles.get(position));
    }

    private void showLastVehicule() throws IOException {
        // Return last vehicle
        position = this.vehicles.size() - 1;
        printLabels(this.vehicles.get(position));
    }

    private void showNextPreviousVehicule(int pos) throws IOException {
        // Return next/previous vehicle

        position += pos;

        if (nameField.getText().isEmpty()){
            position = 0;
        }

        if(position < 0) {
            Window owner = inputs.getScene().getWindow();
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Info.", "There are no more products.");
            position = 0;
            return;
        }

        if(position > this.vehicles.size() - 1){
            Window owner = inputs.getScene().getWindow();
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Info.", "There are no more products.");
            position = this.vehicles.size() - 1;
            return;
        }
        printLabels(this.vehicles.get(position));
    }

    private void printLabels(ArrayList<String> vehicle) throws FileNotFoundException {
        // Prints the vehicle information return in each situation
        fileName = vehicle.get(0);
        imgName.setText(vehicle.get(7));
        updatableFieldButton.setDisable(!Boolean.parseBoolean(vehicle.get(2)));
        idField.setText(vehicle.get(1));
        nameField.setText(vehicle.get(3));
        descriptionField.setText(vehicle.get(4));
        priceField.setText(vehicle.get(5));

        try {
            FileInputStream input = new FileInputStream("Informacion/" + vehicle.get(7));
            Image image = new Image(input);
            imagenField.setImage(image);
        }catch (java.io.FileNotFoundException e){
            Window owner = editButtons.getScene().getWindow();
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Imagen Error", "The image was not found.\n" + e);
            FileInputStream input = new FileInputStream("Informacion/no-imagen.png");
            Image image = new Image(input);
            imagenField.setImage(image);
        }
        webField.setText(vehicle.get(6));
    }

    public void handleSortMethod(String method){
        sortById("id");
    }

    private void sortById(String method){
        // do it in lecturaArchivosModel
        //Collections.sort(this.vehicles.get(0));
    }

    public void handleFirstVehicule(javafx.event.ActionEvent actionEvent) throws IOException {
        showFirstVehicule();
    }

    public void handleLastVehicule(javafx.event.ActionEvent actionEvent) throws IOException {
        showLastVehicule();
    }

    public void handleNextVehicule(javafx.event.ActionEvent actionEvent) throws IOException {
        showNextPreviousVehicule(1);
    }

    public void handlePreviousVehicule(javafx.event.ActionEvent actionEvent) throws IOException {
        showNextPreviousVehicule(-1);
    }

    public void handleRegistrationNewVehicule(javafx.event.ActionEvent actionEvent) throws ParserConfigurationException, IOException, SAXException, TransformerException{
        // Creates a new vehicle

        //TODO import imagen
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
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error", "Please enter just numbers and no decimals.\nNo currency needed.");
            return;
        }
        if(imgName.getText().isEmpty()){
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error", "Please enter an image.");
            return;
        }
        if(webField.getText().isEmpty()){
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error", "Please enter a webpage.");
            return;
        }

        idVeh = selectUniqueId();
        Rv.registrationNewVehicule(idVeh, toString().valueOf(updatableField.isSelected()), nameField.getText(), descriptionField.getText(), priceField.getText(), webField.getText(), imgName.getText());

        AlertHelper.showAlert(Alert.AlertType.INFORMATION, owner, "New vehicle added", "The vehicle has been successfully added.\nName: " + nameField.getText());
        changeView("start.fxml");
    }

    public void handleUpdateVehicle(javafx.event.ActionEvent actionEvent) throws IOException, ParserConfigurationException, SAXException, TransformerException {
        // update the given vehicle
        String newName = null;
        String newDescription = null;
        String newPrice = null;
        String newWeb = null;
        String newImage = null;

        lecturaArchivosModel Rv = new lecturaArchivosModel();

        if(!nameField.getText().equals(this.vehicles.get(position).get(3))){
            newName = nameField.getText();
        }
        if(!descriptionField.getText().equals(this.vehicles.get(position).get(4))){
            newDescription = descriptionField.getText();
        }
        if(!priceField.getText().equals(this.vehicles.get(position).get(5))){
            newPrice = priceField.getText();
        }
        if(!webField.getText().equals(this.vehicles.get(position).get(6))){
            newWeb = webField.getText();
        }
        if(!imgName.getText().equals(this.vehicles.get(position).get(7))){
            newImage = imgName.getText();
        }

        Rv.upDateVehicle(fileName, newName, newDescription, newPrice, newWeb, newImage);
        enableEditMode(false, true);
        reStartApp();
    }

    public void handleDeleteVehicle(javafx.event.ActionEvent actionEvent) throws ParserConfigurationException, TransformerException, SAXException, IOException {
        // deletes the xml and png.
        // TODO change img field to the real one. Right now is a substitution
        Window owner = editButtons.getScene().getWindow();

        if(nameField.getText().isEmpty()){
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Vehicle delete", "No vehicle has been selected yet.");
            return;
        }

        Optional<ButtonType> option = AlertHelper.showConfirmation(owner, "Caution", "Do you really want to delete " + fileName + "?");

        if (option.get() == ButtonType.CANCEL){
            return;
        }

        File xmlFile = new File("Informacion/"+ fileName);

        if (xmlFile.delete()) {
            AlertHelper.showAlert(Alert.AlertType.INFORMATION, owner, "Vehicle delete", fileName + " has been successfully deleted.");
            reStartApp();
        } else {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Vehicle error", " something went wrong deleting " + fileName);
        }

        // Only need it if img has to be deleted
        //String imgName = this.vehicles.get(position).get(7);
        //File imgFile = new File("Informacion/"+ imgName);
        /*if (this.vehicles.get(position).get(7).equals("no-imagen.png")) {
            if (xmlFile.delete()) {
                AlertHelper.showAlert(Alert.AlertType.INFORMATION, owner, "Vehicle delete", fileName + " has been successfully deleted.");
                reStartApp();
            } else {
                AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Vehicle error", " something went wrong deleting " + fileName);
            }
        }else{
            if (xmlFile.delete() & imgFile.delete()) {
                AlertHelper.showAlert(Alert.AlertType.INFORMATION, owner, "Vehicle delete", fileName + " has been successfully deleted.");
                reStartApp();
            } else {
                AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Vehicle error", " something went wrong deleting " + fileName);
            }
        }*/
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

    private void enableEditMode(boolean t, boolean f) {
        // show and hide some fields
        navigationButtons.setVisible(f);
        imgName.setVisible(t);
        imageLabel.setVisible(t);
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

    public void handleNewVehicleWindow() throws IOException{
        changeView("newVehicule.fxml");
    }

    public void mainMenu() throws IOException{
        changeView("start.fxml");
    }

    public void changeView(String fxml) throws IOException {
        Stage stage = (Stage) nameField.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource(fxml));
        stage.setScene(new Scene(root, 700, 600));
        stage.show();
    }

    public void displayEdit () throws IOException{
        if (nameField.getText().isEmpty()){
            Window owner = editButtons.getScene().getWindow();
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Vehicle update", "No vehicle has been selected yet.");
            return;
        }
        enableEditMode(true, false);
    }

    public void displayCancelEdit() throws IOException{
        enableEditMode(false, true);
}

}
