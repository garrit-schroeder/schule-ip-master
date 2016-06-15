package back.componentes;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

public class ImportExport {

    private final String path;

    private Set<Network> networks = new TreeSet<Network>();

    public ImportExport(String path, Set<Network> networks) {
        this.path = path;
        this.networks = networks;
    }

    public void loadDataFromFile() throws IOException, SAXException, ParserConfigurationException {

        File file = new File(path);
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
        doc.getDocumentElement().normalize();
        NodeList nodeLst = doc.getElementsByTagName("network");
        //Parse CIDR & IP && Create network element
        for (int n = 0; n < nodeLst.getLength(); n++) {
            Node networkNode = nodeLst.item(n);
            Element NetworkElmnt = (Element) networkNode;
            //Add new Network to Networklist
            Network tmpN = new Network(new IPv4Address(NetworkElmnt.getAttribute("ip")),
                    Integer.parseInt(NetworkElmnt.getAttribute("CIDR")));
            networks.add(tmpN);
            //Parse CIDR & IP && Create subnet element and add to Network
            NodeList SubLst = ((Element) networkNode).getElementsByTagName("subnet");
            for (int s = 0; s < SubLst.getLength(); s++) {
                Node SubnetNode = SubLst.item(s);
                Element SubnetElmnt = (Element) SubnetNode;
                //Add new Subnet into Networklist
                Subnet tmpS = new Subnet(new IPv4Address(SubnetElmnt.getAttribute("ip")),
                        Integer.parseInt(SubnetElmnt.getAttribute("CIDR")));
                tmpN.addSubnet(tmpS);
                //Parse Host and add to Subnet
                NodeList hostLst = ((Element) SubnetNode).getElementsByTagName("host");
                for (int h = 0; h < hostLst.getLength(); h++) {
                    Element hostElmnt = (Element) hostLst.item(h);
                    //Add Host into Subnet
                    tmpS.addHost(hostElmnt.getFirstChild().getNodeValue());
                }
            }
        }
    }

    public void saveDataToFile() throws ParserConfigurationException, TransformerException {

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        // root elements
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("networks");
        doc.appendChild(rootElement);
        for (Network n : networks) {
            Element network = doc.createElement("network");
            rootElement.appendChild(network);
            // set attribute to network element
            network.setAttribute("ip", n.getNetworkIP().toString());
            network.setAttribute("CIDR", n.getPrefix().toString());
            for (Subnet s : n.getSubnets()) {
                Element subnet = doc.createElement("subnet");
                network.appendChild(subnet);
                subnet.setAttribute("ip", s.getSubnetIp().toString());
                subnet.setAttribute("CIDR", s.getPrefix().toString());
                // Subnet Address as String e.g. "10.0.1.1,10.0.1.2,10.0.1.3,;;"
                Set<IPv4Address> iPv4Addresses = new TreeSet<>();
                for (Host host : s.getHosts()) {
                    iPv4Addresses.add(host.getiPv4Address());
                }

                for (IPv4Address i : iPv4Addresses) {
                    Element host = doc.createElement("host");
                    host.appendChild(doc.createTextNode(i.toString()));
                    subnet.appendChild(host);
                }
            }
        }
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.transform(new DOMSource(doc), new StreamResult(new File(path)));
    }
}