package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
import java.util.ArrayList;
import java.util.Collections;

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
    private Pane inputs;

    //@FXML
    //private Pane header;

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

        System.out.println(position);
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
        updatableFieldButton.setDisable(!Boolean.parseBoolean(vehicle.get(2)));
        idField.setText(vehicle.get(1));
        nameField.setText(vehicle.get(3));
        descriptionField.setText(vehicle.get(4));
        priceField.setText(vehicle.get(5));

        FileInputStream input = new FileInputStream("Informacion/" + vehicle.get(7));
        Image image = new Image(input);
        imagenField.setImage(image);
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
        changeView("start.fxml");
    }

    public void handleUpdateVehicle(javafx.event.ActionEvent actionEvent) throws IOException{
        // update the given vehicle
        lecturaArchivosModel Rv = new lecturaArchivosModel();
        Rv.upDateVehicle(fileName);
    }

    public void handleDeleteVehicle(javafx.event.ActionEvent actionEvent) throws ParserConfigurationException, TransformerException, SAXException, IOException {
        // deletes the xml and png.
        // TODO change img field to the real one right now is a substitution
        Window owner = editButtons.getScene().getWindow();

        if(nameField.getText().isEmpty()){
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Vehicle delete", "No vehicle has been selected yet.");
            return;
        }
        
        AlertHelper.showAlert(Alert.AlertType.CONFIRMATION, owner, "Vehicle delete", "Are you sure you want to delete " + fileName);
        File xmlFile = new File("Informacion/"+ fileName);
        File imgFile = new File("Informacion/"+ fileName.replace("xml", "png"));

        if (this.vehicles.get(position).get(7).equals("no-imagen.png")) {
            if (xmlFile.delete()) {
                AlertHelper.showAlert(Alert.AlertType.INFORMATION, owner, "Vehicle delete", fileName + " has been successfully deleted.");
                reStartApp();
            } else {
                AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Vehicle error", " something went wrong deleting" + fileName);
            }
        }else{
            if (xmlFile.delete() & imgFile.delete()) {
                AlertHelper.showAlert(Alert.AlertType.INFORMATION, owner, "Vehicle delete", fileName + " has been successfully deleted.");
                reStartApp();
            } else {
                AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Vehicle error", " something went wrong deleting" + fileName);
            }
        }
    }

    private void reStartApp() throws ParserConfigurationException, IOException, SAXException, TransformerException {
        // Rereads the info when changes are made.
        lecturaArchivosModel Rv = new lecturaArchivosModel();
        this.vehicles = Rv.getVehicules();
        showFirstVehicule();
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
        // show and hide some fields
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
        enableEditMode(true, false);
    }

    public void displayCancelEdit() throws IOException{
        enableEditMode(false, true);
    }

}
