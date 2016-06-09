package back.componentes;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Subnet Class to aggregate a Subnet
 * Created by snazari on 29/11/14.
 */

public class Subnet implements Comparable<Subnet> {

    private final IPAddress subnetIp;
    private final IPv4Address broadcastIp;
    private final Integer prefix;
    private final Set<Host> hosts = new TreeSet<>();

    public Subnet(IPAddress ip, Integer prefix) {
        this.subnetIp = ip;
        this.prefix = prefix;
        this.broadcastIp = getBroadcastAddress();
    }

    public String getBroadcastIp() {
        return this.broadcastIp.toString();
    }

    public IPAddress getSubnetIp() {
        return subnetIp;
    }

    public Integer getPrefix() {
        return prefix;
    }

    public void addHost(String ip) {
        Host host = new Host(ip);
        //todo ipv6 generieren
        //  host.setiPv6Address(new IPv6Address());
        hosts.add(host);
    }

    public Set<Host> getHosts() {
        return hosts;
    }

    public void deleteHost(IPv4Address ip) {
        Iterator<Host> itr = hosts.iterator();
        while (itr.hasNext()) {
            Host i = itr.next();
            if (i.getiPv4Address().toString().equals(ip.toString())) {
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

    private IPv4Address getBroadcastAddress() {
        if (subnetIp instanceof IPv6Address) {
            return null;
        }
        //todo nur wenn ipv4 addresse. ipv6 habrn keine broadcast id
        int baseIPnumeric = 0;
        int i = 24;

        String[] st = subnetIp.toString().split("\\.");
        for (String aSt : st) {
            baseIPnumeric += Integer.parseInt(aSt) << i;
            i -= 8;
        }
        //Calculate Broadcastaddress without Subnets
        int netmaskNumeric = 0xffffffff << (32 - this.prefix);
        if (netmaskNumeric == 0xffffffff) {
            return new IPv4Address("0.0.0.0");
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
        return new IPv4Address(convertNumericIpToSymbolic((baseIPnumeric & netmaskNumeric) + numberOfIPs));
    }

    @Override
    public int compareTo(Subnet o) {
        final int BEFORE = -1;
        final int EQUAL = 0;
        final int AFTER = 1;
        if (this.getSubnetIp().toString().equals(o.getSubnetIp().toString()) && (this.getPrefix() == o.getPrefix()))
            return EQUAL;
        if ((this.getPrefix() < o.getPrefix())) return BEFORE;
        if ((this.getPrefix() > o.getPrefix())) return AFTER;
        return AFTER;
    }

    @Override
    public int hashCode() {
        return subnetIp.toString().hashCode() + prefix.toString().hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Subnet)) {
            return false;
        }
        Subnet other = (Subnet) object;
        return (this.getSubnetIp().toString().equals(other.getSubnetIp().toString()) && (this.getPrefix() == other.getPrefix()));
    }
}