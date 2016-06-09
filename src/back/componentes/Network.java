package back.componentes;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Network Class to aggregate a Network
 * Created by snazari on 29/11/14.
 */
public class Network implements Comparable<Network> {

    private final Set<Subnet> subnets = new TreeSet<Subnet>();
    private IPAddress networkIP;
    private Integer prefix;

    public Network(IPAddress networkIP, Integer prefix) {
        this.networkIP = networkIP;
        this.prefix = prefix;
    }

    public IPAddress getNetworkIP() {
        return networkIP;
    }

    public Integer getPrefix() {
        return prefix;
    }

    public Set<Subnet> getSubnets() {
        if (subnets.isEmpty()) {
            return null;
        } else {
            return subnets;
        }
    }

    public void addSubnet(Subnet subnet) {
        subnets.add(subnet);
    }

    public void addSubnet(IPAddress subnetIP, String prefix) {
        subnets.add(new Subnet(subnetIP, Integer.parseInt(prefix)));
    }

    public void deleteSubnet(IPv4Address subnetIP, int prefix) {

        Iterator<Subnet> itr = subnets.iterator();
        while (itr.hasNext()) {
            Subnet e = itr.next();
            if (e.getSubnetIP().toString().equals(subnetIP.toString()) && e.getPrefix() == prefix) {
                itr.remove();
            }
        }
    }

    /*
    CompareTo is needed for a Set
     */
    @Override
    public int compareTo(Network o) {
        final int BEFORE = -1;
        final int EQUAL = 0;
        final int AFTER = 1;
        if (this.getNetworkIP().toString().equals(o.getNetworkIP().toString())
                && (this.getPrefix().equals(o.getPrefix()))) return EQUAL;
        if ((this.getPrefix() < o.getPrefix())) return BEFORE;
        if ((this.getPrefix() > o.getPrefix())) return AFTER;
        return AFTER;
    }

    @Override
    public int hashCode() {
        return networkIP.toString().hashCode() + prefix.toString().hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Subnet)) {
            return false;
        }
        Subnet other = (Subnet) object;
        return (this.getNetworkIP().toString().equals(other.getSubnetIP().toString()) && (this.getPrefix() == other.getPrefix()));
    }
}
