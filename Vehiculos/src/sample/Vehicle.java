package sample;

import java.util.Comparator;

//public class Vehicle implements Comparable <Vehicle> {
public class Vehicle{
    private String fileName;
    private String imgName;
    private String updatable;
    private String idVeh;
    private String name;
    private String description;
    private String price;
    private String web;
    private String image;

    public Vehicle (String fileName, String imgName, String updatable, String idVeh, String name, String description, String price, String web, String image) {
        this.fileName = fileName;
        this.imgName = imgName;
        this.updatable = updatable;
        this.idVeh = idVeh;
        this.name = name;
        this.description = description;
        this.price = price;
        this.web = web;
        this.image = image;
    }

    // Getters
    public String getFileName() {
        return fileName;
    }
    public String getImgName(){
        return imgName;
    }
    public String getUpdatable(){
        return updatable;
    }
    public String getIdVeh(){
        return idVeh;
    }
    public String getName(){
        return name;
    }
    public String getDescription() {
        return description;
    }
    public String getPrice(){
        return price;
    }
    public String getWeb(){
        return web;
    }
    public String getImage(){
        return image;
    }
    public int getIdSort(){
        // Desafortunadamente elegí dar el id en String para evitar
        // algunos problemas pero aquí hay que hacer un método específico porque no he sido capaz de hacer cast int
        return Integer.parseInt(idVeh);
    }
    public int getPriceSort(){
        // Desafortunadamente elegí dar el precio en String para evitar
        // algunos problemas pero aquí hay que hacer un método específico porque no he sido capaz de hacer cast int
        return Integer.parseInt(price);
    }

    // Setters
    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public void setUpdatable(String updatable) {
        this.updatable = updatable;
    }

    public void setIdVeh(String idVeh) {
        this.idVeh = idVeh;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString(){
        return "Id: "+ this.idVeh + " Name: " + this.name + " Price: " + this.price;
    }

}
