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

    public void upDateVehicle (String fileName, String name, String description, String price, String web, String imagen) throws ParserConfigurationException, IOException, SAXException {
        // Updates the xml info
        String[] tags = {"name", "p", "price", "web", "image"};
        String[] newDataVej = {name, description, price, web, imagen};

        File xmlFile = new File("Informacion/" + fileName);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            /*NodeList vehicles = doc.getElementsByTagName("camper");
            Element veh = null;
            veh  = (Element) vehicles.item(0);

            Node e = veh.getElementsByTagName().item(0).getFirstChild();
            e.setNodeValue("it was successful");*/
            for (int i = 0; i < newDataVej.length; i++) {
                if(newDataVej[i] != null) {
                    System.out.println(tags[i] + " space " + newDataVej[i]);
                    updateElementVehicule(doc, tags[i], newDataVej[i]);
                }
            }
            doc.getDocumentElement().normalize();
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(xmlFile);
            transformer.transform(source, result);
            System.out.println("Updated.");

        }catch (SAXException | ParserConfigurationException | IOException | TransformerException e) {
            e.printStackTrace();
            System.out.println("Something went wrong whiles updating.\n" + e);
        }
    }

    private static void updateElementVehicule(Document doc, String nameTag, String dataVeh){
        NodeList vehicles = doc.getElementsByTagName("camper");
        Element veh = null;
        veh  = (Element) vehicles.item(0);
        Node e = veh.getElementsByTagName(nameTag).item(0).getFirstChild();
        e.setNodeValue(dataVeh);
    }

    public ArrayList<ArrayList<String>> getVehicules() throws ParserConfigurationException, SAXException, TransformerException, IOException {
        // return all cars in an arraylist<arraylist<String>>
        for (String filename:this.docsXml) {
            Path path = Paths.get(filename);
            todosVehiculos.add(readFromFile(path));
        }
        return todosVehiculos;
    }

    private ArrayList<String> selectXMLs(){
        // Reads only the xmls in the folder, returns arraylist<String> with the routes
        File folder = new File(this.folder);

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
        ArrayList<String> vehicle = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document document = builder.parse(path.toFile());
        document.getDocumentElement().normalize();
        Element root = document.getDocumentElement();
        NodeList nList = document.getElementsByTagName("camper");

        for (int t = 0; t < nList.getLength(); t++){
            Node node = nList.item(t);
            if (node.getNodeType() == Node.ELEMENT_NODE){
                Element datoVehiculo = (Element) node;

                vehicle.add(path.toFile().getName());
                vehicle.add(String.valueOf(datoVehiculo.getAttribute("id")));
                vehicle.add(datoVehiculo.getAttribute("updatable"));
                vehicle.add(datoVehiculo.getElementsByTagName("name").item(0).getTextContent());

                String parrafo = "";
                for (int i=0; i < datoVehiculo.getElementsByTagName("p").getLength(); i++) {
                    parrafo += datoVehiculo.getElementsByTagName("p").item(i).getTextContent() + "\n";
                    //System.out.println("P:  " + datoVehiculo.getElementsByTagName("p").item(i).getTextContent());
                }
                vehicle.add(parrafo);
                vehicle.add(datoVehiculo.getElementsByTagName("price").item(0).getTextContent());
                vehicle.add(datoVehiculo.getElementsByTagName("web").item(0).getTextContent());
                vehicle.add(datoVehiculo.getElementsByTagName("image").item(0).getTextContent());
            }
        }
        return vehicle;
    }

}
