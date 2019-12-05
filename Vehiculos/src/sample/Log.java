package sample;

import javafx.util.converter.LocalDateStringConverter;
import org.w3c.dom.ls.LSOutput;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

public class Log {

    private String time;
    private String action;
    private static String file;
    private LocalDateTime now;
    private final static Path path = Paths.get("Log/log.txt");

    public Log(){
        this.now = LocalDateTime.now();

    }

    public static void createFile() throws IOException {
        // Creates the log

        File folder = new File("Log");
        if (!folder.exists()) {
            folder.mkdir();
        }
        /*File file = new File(path);

        if (file.createNewFile()) {
            System.out.println(path + " File Created");
        } else {
            System.out.println("File " + path + " already exists");
        }*/
    }

    public void writeFile(String type, String idVeh, String message) throws IOException{
        // Writes the log
        File folder = new File("Log");
        File file = new File("Log/log.txt");
        if (!folder.exists()) {
            folder.mkdir();
        }
        if(!file.exists()){
            BufferedWriter bufferedWriter = Files.newBufferedWriter(path, java.nio.charset.StandardCharsets.UTF_8, StandardOpenOption.CREATE);
            bufferedWriter.close();
        }
        String date = this.now.getYear() + "/" + this.now.getMonth() + "/" + this.now.getDayOfMonth() + " " + this.now.getHour() + ":" + this.now.getMinute() + " ";

        try {

            BufferedWriter bufferedWriter = Files.newBufferedWriter(path, java.nio.charset.StandardCharsets.UTF_8, StandardOpenOption.APPEND);
            //bufferedWriter.write(date + "Tipo: " + type);

            if(idVeh != null){
                bufferedWriter.write(" id vehicle: " + idVeh);
            }
            if(message != null){
                bufferedWriter.write(" Mensaje: " + message);
            }
            
            bufferedWriter.newLine();
            bufferedWriter.flush();
            bufferedWriter.close();

        }catch (IOException ex){
            System.err.println("I/O Error: " + ex);
        }
    }

}
