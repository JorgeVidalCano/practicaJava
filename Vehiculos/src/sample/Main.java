package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        /*File path = new File("\"Log/log.txt\"");
        File file = new File(String.valueOf(path));
        if(!file.exists()) {
            Log log = new Log("log");
            Log.createFile();
        }*/

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("RV Renting");
        primaryStage.setScene(new Scene(root, 700, 600));
        primaryStage.show();
    }

    @Override
    public void stop() {
        //System.out.println("its closing.");
    }
    public static void main(String[] args){
        launch(args);
    }

}
