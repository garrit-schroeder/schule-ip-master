package back;

import back.componentes.IPv4Address;
import back.componentes.IPv6Address;
import back.componentes.ImportExport;
import back.componentes.Network;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by snazari on 29/11/14.
 */
public class Data_controller {

    private final Set<Network> networks = new TreeSet<Network>();

    public void addNetwork(String ip, String prefix) {
        if (ip.matches(".*[a-zA-Z]+.*")) {
            networks.add(new Network(new IPv6Address(ip), Integer.parseInt(prefix)));
        } else {
            networks.add(new Network(new IPv4Address(ip), Integer.parseInt(prefix)));
        }
    }

    public Set<Network> getNetworks() {
        return networks;
    }

    public void deleteNetwork(String ip, String prefix) {
        Iterator<Network> itr = networks.iterator();
        while (itr.hasNext()) {
            Network n = itr.next();
            if ((n.getNetworkIP().toString().equals(ip)) && (Integer.parseInt(prefix) == n.getPrefix())) {
                itr.remove();
            }
        }
    }

    public void importData(String path) {
        ImportExport transfer = new ImportExport(path, networks);
        try {
            transfer.loadDataFromFile();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void exportData(String path) {
        ImportExport transfer = new ImportExport(path, networks);
        try {
            transfer.saveDataToFile();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
}
