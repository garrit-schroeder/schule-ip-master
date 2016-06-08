package front;

import back.Data_controller;
import back.componentes.Host;
import back.componentes.IPv4Address;
import back.componentes.Network;
import back.componentes.Subnet;
import front.components.MainFrame;

import javax.swing.*;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by snazari on 29/11/14.
 */
public class Interface_controller {

    private final Data_controller data_controller = new Data_controller();
    private final MainFrame mainFrame;
    private Network currentNetwork;
    private Subnet currentSubnet;

    public Interface_controller() {
        mainFrame = new MainFrame(this);
    }

    public boolean isNetworkSet() {
        return currentNetwork != null;
    }

    public boolean isSubnetSet() {
        return currentSubnet != null;
    }

    public String getNetworkInfo() {
        return currentNetwork.getNetworkIP().toString() + "/" + currentNetwork.getPrefix().toString();
    }

    public String[] getSubnetInfo() {
        String[] tmp = {"", ""};
        if (currentSubnet != null) {
            tmp[0] = currentSubnet.getSubnetIP().toString() + "/" + currentSubnet.getPrefix().toString();
            tmp[1] = currentSubnet.getBroadcastIP();
            return tmp;
        } else {
            return null;
        }
    }

    public void setCurrentNetwork(String ipWithPrefix) {
        String[] tmp = ipWithPrefix.split("/");
        for (Network n : data_controller.getNetworks()) {
            if ((n.getNetworkIP().toString().equals(tmp[0])) &&
                    Integer.parseInt(tmp[1]) == n.getPrefix()) {
                currentNetwork = n;
            }
        }
    }

    public void setCurrentSubnet(String ipWithPrefix) {
        String[] tmp = ipWithPrefix.split(" - ");
        tmp = tmp[0].split("/");
        for (Subnet s : currentNetwork.getSubnets()) {
            if ((s.getSubnetIP().toString().equals(tmp[0])) &&
                    Integer.parseInt(tmp[1]) == s.getPrefix()) {
                currentSubnet = s;
            }
        }
    }

    public Set<String> loadNetworksIntoDialog() {
        Set<String> addressSet = new TreeSet<String>();
        for (Network n : data_controller.getNetworks()) {
            addressSet.add(n.getNetworkIP().toString() + "/" + n.getPrefix());
        }
        return addressSet;
    }

    public Set<String> loadSubnetsIntoDialog() {
        Set<String> addressSet = new TreeSet<String>();
        if (currentNetwork.getSubnets() != null) {
            for (Subnet s : currentNetwork.getSubnets()) {
                addressSet.add(s.getSubnetIP().toString() + "/" + s.getPrefix() + " - ("
                        + s.getDistributedIPVolume() + "/" + s.getMaxHosts() + " Hosts)");
            }
        }
        return addressSet;
    }

    public Set<String> loadHostsIntoDialog() {
        Set<String> addressSet = new TreeSet<String>();
        if (currentSubnet != null) {
            addressSet.addAll(currentSubnet.getHosts().stream().map(Host::getiPv4Address).collect(Collectors.toList()).stream().map((Function<IPv4Address, String>) IPv4Address::toString).collect(Collectors.toList()));
        }
        return addressSet;
    }

    public void addNewNetwork(String ip, String prefix) {
        if (isNetworkValid(ip, prefix)) {
            data_controller.addNetwork(ip, prefix);
        }
    }

    public void addNewSubnet(String ip, String prefix) {
        if (isSubnetInRange(ip, prefix)) {
            currentNetwork.addSubnet(ip, prefix);
        } else {
            errorMessage(ip + "/" + prefix + " not in range of " + currentNetwork.getNetworkIP() + "/" + currentNetwork.getPrefix());
        }
    }

    public void addNewHost(String ip) {
        if (isHostInRange(ip)) {
            currentSubnet.addHost(ip);
        }
    }

    public boolean isHostInRange(String hostIp) {
        String[] hostOkt = hostIp.split("\\.");
        String[] currentSubnetOkt = currentSubnet.getSubnetIP().toString().split("\\.");

        // Host address can not be broadcast address
        if (currentSubnet.getBroadcastIP().equals(hostIp)) {
            errorMessage("Host-address can no be the broadcast-address");
            return false;
        }

        for (int i = 0; i < (int) Math.floor(currentSubnet.getPrefix() / 8); i++) {
            if (!hostOkt[i].equals(currentSubnetOkt[i])) {
                errorMessage("This host is not in the current subnet");
                return false;
            }
        }
        if (String.format("%8s", Integer.toBinaryString(Integer.parseInt
                (hostOkt[(int) Math.floor(currentSubnet.getPrefix() / 8)]))).
                replace(' ', '0').substring(0, currentSubnet.getPrefix() % 8).
                equals(String.format("%8s", Integer.toBinaryString(Integer.
                        parseInt(currentSubnetOkt[(int) Math.floor(currentSubnet.
                                getPrefix() / 8)]))).replace(' ', '0').substring(0, currentSubnet.getPrefix() % 8))) {
            return true;
        } else {
            errorMessage("This host is not in the current subnet");
            return false;
        }
    }

    public boolean isNetworkValid(String networkIp, String networkPrefix) {
        String[] networkOkt = networkIp.split("\\.");
        for (int i = 4; i > Integer.parseInt(networkPrefix) / 8 + 1; i--) {
            if (!networkOkt[i - 1].equals("0")) {
                errorMessage("This is no valid network");
                return false;
            }
        }
        String checkString = String.format("%8s", Integer.toBinaryString(Integer.parseInt(networkOkt
                [(int) Math.ceil(Integer.parseInt(networkPrefix) / 8)]))).replace(" ", "0");
        //lastBitsToCheck
        if (Integer.parseInt(checkString.substring(Math.max(checkString.length() -
                (8 - Integer.parseInt(networkPrefix) % 8), 0))) == 0) {
            return true;
        } else {
            errorMessage("Host part of network has to be 0.");
            return false;
        }
    }

    public boolean isSubnetInRange(String subnetIp, String subnetPrefix) {
        String[] subnetOkt = subnetIp.split("\\.");
        String[] currentNetworkOkt = currentNetwork.getNetworkIP().toString().split("\\.");
        // check if all full bytes of the host part are the same
        if (Integer.parseInt(subnetPrefix) > currentNetwork.getPrefix()) {
            for (int i = 0; i < (int) Math.floor(currentNetwork.getPrefix() / 8); i++) {
                if (!subnetOkt[i].equals(currentNetworkOkt[i])) {
                    errorMessage("This subnet is not in the current network");
                    return false;
                }
            }
            // check if the host parts of a byte are the same (eg. with prefix 9, the first bit of the
            // second byte has to be the same)
            if (String.format("%8s", Integer.toBinaryString(Integer.parseInt
                    (subnetOkt[(int) Math.floor(currentNetwork.getPrefix() / 8)]))).
                    replace(' ', '0').substring(0, currentNetwork.getPrefix() % 8).
                    equals(String.format("%8s", Integer.toBinaryString(Integer.
                            parseInt(currentNetworkOkt[(int) Math.
                                    floor(currentNetwork.getPrefix() / 8)]))).
                            replace(' ', '0').substring(0, currentNetwork.getPrefix() % 8))) {

                // check if the full bytes after prefix are 0  <-- not that useful anymore.... :'(
                for (int i = 4; i < Integer.parseInt(subnetPrefix) / 8; i--) {
                    if (!subnetOkt[i - 1].equals(currentNetworkOkt[i - 1])) {
                        errorMessage("Host part of the subnet has to be 0.");
                        return false;
                    }
                }
                String checkString = String.format("%8s", Integer.toBinaryString(Integer.parseInt(subnetOkt
                        [(int) Math.ceil(Integer.parseInt(subnetPrefix) / 8)]))).replace(" ", "0");
                //lastBitsToCheck
                if (Integer.parseInt(checkString.substring(Math.max(checkString.length() -
                        (8 - Integer.parseInt(subnetPrefix) % 8), 0))) == 0) {
                    return true;
                } else {
                    errorMessage("Host part of subnet has to be 0.");
                    return false;
                }
            } else {
                errorMessage("This subnet is not in range of the current network");
                return false;
            }
        } else {
            // prefix of subnet has to be greater than prefix of current network
            errorMessage("The prefix of the subnet has to be greater than the prefix of the current network");
            return false;
        }
    }

    public void errorMessage(String msg) {
        JOptionPane.showMessageDialog(new JDialog(), msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void importData(String path) {
        this.data_controller.importData(path);
    }

    public void exportData(String path) {
        this.data_controller.exportData(path);
    }

    public void changeSubnetsTabIp(String ip) {
        mainFrame.changeSubnetsTabIp(ip);
    }

    public void deleteNetwork(String network, String prefix) {
        data_controller.deleteNetwork(network, prefix);
        currentNetwork = null;
    }

    public void deleteSubnet(String subnet, String prefix) {
        currentNetwork.deleteSubnet(new IPv4Address().parseIP(subnet), Integer.parseInt(prefix));
        currentSubnet = null;
    }

    public void deleteHost(String subnet) {
        currentSubnet.deleteHost(new IPv4Address().parseIP(subnet));
    }

    public boolean verifyPrefix(String ip) {
        return Integer.parseInt(ip) >= 8 && Integer.parseInt(ip) <= 32;
    }

    private Subnet getLastSubnet() {
        if(currentNetwork.getSubnets() != null) {
            Iterator itr = currentNetwork.getSubnets().iterator();
            Object lastSubnet = itr.next();
            while (itr.hasNext()) {
                lastSubnet = itr.next();
            }
            return (Subnet) lastSubnet;
        } else {
            return null;
        }
    }

    public String generateNextSubnet() {
        if (getLastSubnet() != null) {
            Subnet lastSub = getLastSubnet();
            String[] octets = lastSub.getBroadcastIP().split("\\.");
            String[] networkIPAddress = currentNetwork.getNetworkIP().toString().split("\\.");
            if ((Integer.parseInt(octets[3]) - Integer.parseInt(networkIPAddress[3])) == 255) {
                octets[3] = new Integer(0).toString();
                if ((Integer.parseInt(octets[2]) - Integer.parseInt(networkIPAddress[2])) == 255) {
                    octets[2] = new Integer(0).toString();
                    octets[1] = new Integer(Integer.parseInt(octets[1]) + 1).toString();
                } else {
                    octets[2] = new Integer(Integer.parseInt(octets[2]) + 1).toString();
                }
            } else {
                octets[3] = new Integer(Integer.parseInt(octets[3]) + 1).toString();
            }
            if (Integer.parseInt(octets[1]) > 255) {
                return null;
            } else {
                return octets[0] + "." + octets[1] + "." + octets[2] + "." + octets[3];
            }
        } else {
            return null;
        }
    }
}
