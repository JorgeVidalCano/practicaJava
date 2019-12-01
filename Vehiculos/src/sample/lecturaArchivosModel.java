package sample;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.print.Doc;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

public class lecturaArchivosModel {

    private final static String folder = "Informacion";
    private static int position = 0;
    private ArrayList<String> docsXml = new ArrayList<>();
    private ArrayList<ArrayList<String>> todosVehiculos = new ArrayList<>();

    public lecturaArchivosModel(){
        this.docsXml = selectXMLs();
    }

    public ArrayList<String> getIds(){
        // returns only the ids
        ArrayList<String> onlyIds= new ArrayList<>();
        for(int i = 0; i < this.todosVehiculos.size(); i++){
            onlyIds.add(this.todosVehiculos.get(i).get(1));
        }
        return onlyIds;
    }

    public void getVehicules () throws ParserConfigurationException, IOException, SAXException, TransformerException {
        // Reads all cars
        reading();
    }

    public ArrayList<String> getFirstVehicule () {
        position = 0;
        return this.todosVehiculos.get(position);
    }

    public ArrayList<String> getNextPreviousVehicule (int p) {
        position += p;

        if(position < 0){
            position = 0;
        }else if (position == this.todosVehiculos.size()){
            position = this.todosVehiculos.size() - 1;
        }
        return this.todosVehiculos.get(position);
    }

    public ArrayList<String> getLastVehicule () {
        position = this.todosVehiculos.size() - 1;
        return this.todosVehiculos.get(position);
    }

    public void registrationNewVehicule (String idVeh, String updatable, String name, String description, String price, String web, String imagen) throws ParserConfigurationException, TransformerException {
        // Writes a new xml with a new vehicle

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document document = builder.newDocument();
        Element root = document.createElement("camper");
        root.setAttribute("id", idVeh);
        root.setAttribute("updatable", updatable);

        document.appendChild(root);

        Element nameXml = document.createElement("name");
        nameXml.setTextContent(name);
        root.appendChild(nameXml);

        Element descXml = document.createElement("p");
        descXml.setTextContent(description);
        root.appendChild(descXml);

        Element priceXml = document.createElement("price");
        priceXml.setTextContent(price);
        root.appendChild(priceXml);

        Element webXml = document.createElement("web");
        webXml.setTextContent(web);
        root.appendChild(webXml);

        Element imagenXml = document.createElement("image");
        imagenXml.setTextContent(imagen);
        root.appendChild(imagenXml);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);

        Path path = Paths.get(folder);

        StreamResult streamResult = new StreamResult(path.toFile() + "/" + name + ".xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(domSource, streamResult);

        System.out.println("New vehicle registered");

    }

    private ArrayList<ArrayList<String>> reading() throws ParserConfigurationException, SAXException, TransformerException, IOException {
        // return all cars in an arraylist<arraylist<String>>
        for (String filename:this.docsXml) {
            Path path = Paths.get(filename);
            todosVehiculos.add(readFromFile(path));
        }
        return todosVehiculos;
    }

    private ArrayList<String> selectXMLs(){
        // Reads only the xmls in the folder, returns arraylist<String> with the routes
        File folder = new File("Informacion");

        try(Stream<Path> paths = Files.walk(Paths.get(String.valueOf(folder)))) {
            paths.forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
                    try {
                        if (String.valueOf(filePath).endsWith(".xml")){
                            docsXml.add(String.valueOf(filePath));
                            //System.out.println(filePath); //debugging
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (docsXml.isEmpty()){
            // TODO throw an exception if I have time
            docsXml.add("No vehicules");
            return docsXml;
        }
        return docsXml;
    }

    private static ArrayList<String> readFromFile(Path path) throws ParserConfigurationException, IOException, SAXException {
        // Gets each car and returns an array that will later be added to another arraylist.
        ArrayList<String> vehiculo = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document document = builder.parse(path.toFile());
        document.getDocumentElement().normalize();
        Element root = document.getDocumentElement();
        NodeList nList = document.getElementsByTagName("camper");

        for (int temp = 0; temp < nList.getLength(); temp++){
            Node node = nList.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE){
                Element datoVehiculo = (Element) node;
                //vehiculo.add(path.toFile().getName().replace(".xml", ""));
                vehiculo.add(path.toFile().getName());
                vehiculo.add(String.valueOf(datoVehiculo.getAttribute("id")));
                vehiculo.add(datoVehiculo.getAttribute("updatable"));
                vehiculo.add(datoVehiculo.getElementsByTagName("name").item(0).getTextContent());

                String parrafo = "";
                for (int i=0; i < datoVehiculo.getElementsByTagName("p").getLength(); i++) {
                    parrafo += datoVehiculo.getElementsByTagName("p").item(i).getTextContent() + "\n";
                    //System.out.println("P:  " + datoVehiculo.getElementsByTagName("p").item(i).getTextContent());
                }
                vehiculo.add(parrafo);
                vehiculo.add(datoVehiculo.getElementsByTagName("price").item(0).getTextContent());
                vehiculo.add(datoVehiculo.getElementsByTagName("web").item(0).getTextContent());
                vehiculo.add(datoVehiculo.getElementsByTagName("image").item(0).getTextContent());
            }
        }
        return vehiculo;
    }

}
