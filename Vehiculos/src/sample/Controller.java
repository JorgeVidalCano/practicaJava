package sample;

import javafx.event.ActionEvent;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    private javafx.scene.image.ImageView imagenField;

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
    private Button filterButton;

    @FXML
    private Button addNewButton;

    @FXML
    private Button updatableFieldButton;

    private ArrayList<Vehicle> vehicles = new ArrayList();
    private static int position = 0;
    private static int sorting = 0;

    public Controller () throws ParserConfigurationException, TransformerException, SAXException, IOException {
        lecturaArchivosModel Rv = new lecturaArchivosModel();
        this.vehicles = Rv.getVehicles();
    }

    private void reStartApp() throws IOException{
        // Rereads the info when changes are made.
        lecturaArchivosModel Rv = new lecturaArchivosModel();
        Log l = new Log();
        try {
            this.vehicles = Rv.getVehicles();
            l.writeFile("Reading", null, "Vehicles loaded.");
        }catch (IOException | SAXException | TransformerException | ParserConfigurationException e){
            Window owner = addNewButton.getScene().getWindow();
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Error", "Something happened while reading the files.");
            l.writeFile("Error", null, String.valueOf(e));
        }
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

    private void printLabels(Vehicle vehicle) throws IOException {
        // Prints the vehicle information return in each situation

        fileName = vehicle.getFileName();
        updatableFieldButton.setDisable(!Boolean.parseBoolean(vehicle.getUpdatable()));
        imgName.setText(vehicle.getImgName());
        idField.setText(vehicle.getIdVeh());
        nameField.setText(vehicle.getName());
        descriptionField.setText(vehicle.getDescription());
        priceField.setText(vehicle.getPrice());

        try {
            FileInputStream input = new FileInputStream("Informacion/" + vehicle.getImgName());
            Image image = new Image(input);
            imagenField.setImage(image);
        }catch (java.io.FileNotFoundException e){
            Window owner = editButtons.getScene().getWindow();
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Imagen Error", "The image was not found.\n" + e);
            FileInputStream input = new FileInputStream("Informacion/no-imagen.png");
            Image image = new Image(input);
            imagenField.setImage(image);
            Log l = new Log();
            l.writeFile("Error", null, String.valueOf(e));
        }
        webField.setText(vehicle.getWeb());
    }

    public void handleSortingMethod(ActionEvent actionEvent) throws IOException {

        if(nameField.getText().isEmpty()){
            System.out.println("empty");
            return;
        }
        Log l = new Log();
        if(sorting == 0) {
            sorting += 1;
            Comparator<Vehicle> sortId = Comparator.comparingInt(Vehicle::getIdSort);
            this.vehicles.sort(sortId);
            filterButton.setText("Id");
            l.writeFile("Sorting", null, "Id");
        } else if (sorting == 1){
            sorting += 1;
            Comparator<Vehicle> sortName = Comparator.comparing((Vehicle v) -> v.getName().toLowerCase());
            this.vehicles.sort(sortName);
            filterButton.setText("Name");
            l.writeFile("Sorting", null, "Name");
        } else if (sorting == 2) {
            sorting = 0;
            Comparator<Vehicle> sortPrice = Comparator.comparingInt(Vehicle::getPriceSort);
            this.vehicles.sort(sortPrice);
            filterButton.setText("Price");
            l.writeFile("Sorting", null, "Price");
        }
        showFirstVehicule();
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
        Rv.registrationNewVehicle(idVeh, toString().valueOf(updatableField.isSelected()), nameField.getText(), descriptionField.getText(), priceField.getText(), webField.getText(), imgName.getText());

        AlertHelper.showAlert(Alert.AlertType.INFORMATION, owner, "New vehicle added", "The vehicle has been successfully added.\nName: " + nameField.getText());
        Log l = new Log();
        l.writeFile("New vehicle added.", idVeh, null);
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

        if(!nameField.getText().equals(this.vehicles.get(position).getName())){
            newName = nameField.getText();
        }
        if(!descriptionField.getText().equals(this.vehicles.get(position).getDescription())){
            newDescription = descriptionField.getText();
        }
        if(!priceField.getText().equals(this.vehicles.get(position).getPrice())){
            newPrice = priceField.getText();
        }
        if(!isPrice(priceField.getText())){
            Window owner = addNewButton.getScene().getWindow();
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error", "Please enter just numbers and no decimals.\nNo currency needed.");
            return;
        }

        if(!webField.getText().equals(this.vehicles.get(position).getWeb())){
            newWeb = webField.getText();
        }
        if(!imgName.getText().equals(this.vehicles.get(position).getImgName())){
            newImage = imgName.getText();
        }

        Rv.upDateVehicle(fileName, newName, newDescription, newPrice, newWeb, newImage);
        enableEditMode(false, true);
        reStartApp();
        Window owner = editButtons.getScene().getWindow();
        AlertHelper.showAlert(Alert.AlertType.INFORMATION, owner, "New vehicle updated", "The " + nameField.getText() + " has been successfully updated." );
        Log l = new Log();
        l.writeFile("Updated vehicle", idField.getText(), null);
    }

    public void handleDeleteVehicle(javafx.event.ActionEvent actionEvent) throws IOException {
        // deletes the xml and png.
        Window owner = editButtons.getScene().getWindow();

        if(nameField.getText().isEmpty()){
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Vehicle delete", "No vehicle has been selected yet.");
            return;
        }

        Optional<ButtonType> option = AlertHelper.showConfirmation(owner, "Caution", "Do you really want to delete " + fileName + "?");

        if (option.get() == ButtonType.CANCEL){
            return;
        }

        String idVeh = idField.getText();
        File xmlFile = new File("Informacion/"+ fileName);
        Log l = new Log();

        try{
            if (xmlFile.delete()) {
                AlertHelper.showAlert(Alert.AlertType.INFORMATION, owner, "Vehicle delete", fileName + " has been successfully deleted.");
                l.writeFile("Deleted vehicle", idVeh, null);
                reStartApp();
            }
        }catch (Exception e){
                AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Vehicle error", " something went wrong deleting " + fileName);
                l.writeFile("Error", null, String.valueOf(e));
        }

    }

    private String selectUniqueId() throws ParserConfigurationException, TransformerException, SAXException, IOException {
        int idveh = 1;

        lecturaArchivosModel Rv = new lecturaArchivosModel();
        Rv.getVehicles();

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

    public void displayEdit () {
        if (nameField.getText().isEmpty()){
            Window owner = editButtons.getScene().getWindow();
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Vehicle update", "No vehicle has been selected yet.");
            return;
        }
        enableEditMode(true, false);
    }

    public void displayCancelEdit() {
        enableEditMode(false, true);
    }

}
