package back.componentes;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Subnet Class to aggregate a Subnet
 * Created by snazari on 29/11/14.
 */

public class Subnet implements Comparable<Subnet> {

    private final IPAddress subnetIP;
    private final IPAddress broadcastIP;
    private final Integer prefix;
    private final Set<IPAddress> hosts = new TreeSet<IPAddress>();

    public Subnet(IPAddress ip, Integer prefix) {
        this.subnetIP = ip;
        this.prefix = prefix;
        this.broadcastIP = getBroadcastAddress();
    }

    public String getBroadcastIP() {
        return this.broadcastIP.toString();
    }

    public IPAddress getSubnetIP() {
        return subnetIP;
    }

    public Integer getPrefix() {
        return prefix;
    }

    public void addHost(String ip) {
        hosts.add(new IPAddress().parseIP(ip));
    }

    public Set<IPAddress> getHosts() {
        return hosts;
    }

    public void deleteHost(IPAddress ip) {
        Iterator<IPAddress> itr = hosts.iterator();
        while (itr.hasNext()) {
            IPAddress i = itr.next();
            if (i.toString().equals(ip.toString())) {
                itr.remove();
            }
        }
    }

    /*
    Functions returns the Amount of Set IPAddresses
     */
    public int getDistributedIPVolume() {
        return hosts.size();
    }

    /*
    Functions returns the amount of Possible Hosts for a Subnet
     */
    public int getMaxHosts() {
        return (int) Math.pow(2, (32 - this.getPrefix())) - 2;
    }

    /*
    Function Parses Bit-Arrangement into a String Representation
     */
    private String convertNumericIpToSymbolic(Integer ip) {
        StringBuilder sb = new StringBuilder(15);
        for (int shift = 24; shift > 0; shift -= 8) {
            // process 3 bytes, from high order byte down.
            sb.append(Integer.toString((ip >>> shift) & 0xff));
            sb.append('.');
        }
        sb.append(Integer.toString(ip & 0xff));
        return sb.toString();
    }

    private IPAddress getBroadcastAddress() {
        int baseIPnumeric = 0;
        int i = 24;

        String[] st = subnetIP.toString().split("\\.");
        for (String aSt : st) {
            baseIPnumeric += Integer.parseInt(aSt) << i;
            i -= 8;
        }
        //Calculate Broadcastaddress without Subnets
        int netmaskNumeric = 0xffffffff << (32 - this.prefix);
        if (netmaskNumeric == 0xffffffff) {
            return new IPAddress().parseIP("0.0.0.0");
        }
        int numberOfBits;
        for (numberOfBits = 0; numberOfBits < 32; numberOfBits++) {
            if ((netmaskNumeric << numberOfBits) == 0)
                break;
        }
        Integer numberOfIPs = 0;
        for (int n = 0; n < (32 - numberOfBits); n++) {
            numberOfIPs = (numberOfIPs << 1) | 0x01;
        }
        return new IPAddress().parseIP(convertNumericIpToSymbolic((baseIPnumeric & netmaskNumeric) + numberOfIPs));
    }

    @Override
    public int compareTo(Subnet o) {
        final int BEFORE = -1;
        final int EQUAL = 0;
        final int AFTER = 1;
        if (this.getSubnetIP().toString().equals(o.getSubnetIP().toString()) && (this.getPrefix() == o.getPrefix()))
            return EQUAL;
        if ((this.getPrefix() < o.getPrefix())) return BEFORE;
        if ((this.getPrefix() > o.getPrefix())) return AFTER;
        return AFTER;
    }

    @Override
    public int hashCode() {
        return subnetIP.toString().hashCode() + prefix.toString().hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Subnet)) {
            return false;
        }
        Subnet other = (Subnet) object;
        return (this.getSubnetIP().toString().equals(other.getSubnetIP().toString()) && (this.getPrefix() == other.getPrefix()));
    }
}
