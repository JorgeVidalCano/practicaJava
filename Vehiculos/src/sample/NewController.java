package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.Button;

public class NewController {

    @FXML
    private javafx.scene.control.TextField nameField;

    @FXML
    private javafx.scene.control.TextField descriptionField;

    @FXML
    private javafx.scene.control.TextField priceField;

    @FXML
    private javafx.scene.control.TextField webField;

    @FXML
    private javafx.scene.control.CheckBox updatableField;

    @FXML
    private Button addNewButton;

    @FXML
    private Button loadImgs;

    @FXML
    private ListView selectImg;

    public void handleShowImg(){
        // shows all img available
        lecturaArchivosModel imgs = new lecturaArchivosModel();
        loadImgs.setVisible(false);
        selectImg.setVisible(true);
        ObservableList<String> items = FXCollections.observableArrayList (imgs.getImg());
        selectImg.setItems(items);
    }

    public void handleRegistrationNewVehicule(javafx.event.ActionEvent actionEvent) throws ParserConfigurationException, IOException, SAXException, TransformerException {
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
        //if(Boolean.parseBoolean(String.valueOf(selectImg.getSelectionModel().selectedItemProperty().isNull()))){
        if(selectImg.getSelectionModel().isEmpty()){
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error", "Please select an image.");
            return;
        }

        if(webField.getText().isEmpty()){
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error", "Please enter a webpage.");
            return;
        }

        idVeh = selectUniqueId();
        String img;
        img = (String) selectImg.getSelectionModel().selectedItemProperty().getValue();
        Rv.registrationNewVehicle(idVeh, toString().valueOf(updatableField.isSelected()), nameField.getText(), descriptionField.getText(), priceField.getText(), webField.getText(), img.replace("Informacion\\", ""));

        AlertHelper.showAlert(Alert.AlertType.INFORMATION, owner, "New vehicle added", "The vehicle has been successfully added.\nName: " + nameField.getText());
        Log l = new Log();
        l.writeFile("New vehicle added.", idVeh, null);
        changeView("start.fxml");
    }
    public void changeView(String fxml) throws IOException {
        Stage stage = (Stage) nameField.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource(fxml));
        stage.setScene(new Scene(root, 700, 600));
        stage.show();
    }

    private String selectUniqueId() throws ParserConfigurationException, SAXException, IOException {
        int idveh = 1;

        lecturaArchivosModel Rv = new lecturaArchivosModel();
        Rv.getVehicles();

        while(Rv.getIds().contains(String.valueOf(idveh))) {
            idveh += 1;
        }
        return String.valueOf(idveh);
    }

    public void mainMenu() throws IOException{
        changeView("start.fxml");
    }

    private static boolean isPrice (String price){
        String regex = "\\b[0-9]+\\b";
        return price.matches(regex);
    }
}
