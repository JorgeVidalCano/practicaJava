package sample;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Controller {

    @FXML
    private javafx.scene.control.TextField nameField;

    @FXML
    private javafx.scene.control.TextField descriptionField;

    @FXML
    private javafx.scene.control.TextField priceField;

    @FXML
    //private javafx.scene.control.TextField imagenField;
    private javafx.scene.image.ImageView imagenField;  //Image imageField;

    @FXML
    private javafx.scene.control.TextField webField;

    @FXML
    private javafx.scene.control.TextField idField;

    @FXML
    private javafx.scene.control.TextField updateableField;

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
        //showPreviousVehicule();
        showNextPreviousVehicule(-1);
    }

    public void handleNewVehicule(javafx.event.ActionEvent actionEvent) throws ParserConfigurationException, IOException, SAXException, TransformerException{
        lecturaArchivosModel Rv = new lecturaArchivosModel();
        Rv.registrationNewVehicule("2", "true", "ibiza", "IT's good, cheap and realiable", "14000", "www.antonia.com", "mercedes.png");
    }
}
