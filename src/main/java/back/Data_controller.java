package back;

import back.componentes.*;
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
        if(ipChecker.isIp4(ip)){
            networks.add(new Network(new IPv4Address(ip), Integer.parseInt(prefix)));
        }
        else{
            networks.add(new Network(ip, Integer.parseInt(prefix)));
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
        SaveLoad transfer = new SaveLoad(path,networks);
        transfer.load();

    }

    public void exportData(String path) {
        SaveLoad transfer = new SaveLoad(path,networks);
        transfer.save();

    }
}
