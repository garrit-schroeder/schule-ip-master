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
    private IPv4Address networkIP = new IPv4Address();
    private IPv6Address networkIP6;
    private String type = "";

    private Integer prefix;

    public Network(IPv4Address networkIP, Integer prefix) {
        this.networkIP = networkIP;
        this.prefix = prefix;
        this.type = "4";

    }
    public Network(String ip6,Integer prefix){
        this.networkIP6 = new IPv6Address(ip6);
        this.prefix = prefix;
        this.type = "6";
    }

    public IPv4Address getNetworkIP() {
        return networkIP;
    }

    public String getIP(){
        if(isIp6()){
            return this.networkIP6.toString();
         }
        else{
            return this.networkIP.toString();
        }
    }

    public Integer getPrefix() {
        return prefix;
    }

    public Set<Subnet> getSubnets() {
        if(subnets.isEmpty()) {
            return null;
        } else {
            return subnets;
        }
    }

    public void addSubnet(Subnet subnet) {
        subnets.add(subnet);
    }

    public void addSubnet(String subnetIP, String prefix) {
        subnets.add(new Subnet(new IPv4Address().parseIP(subnetIP), Integer.parseInt(prefix)));
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

    public boolean isIp6(){
        boolean res = false;
        if(this.type == "6"){
            res = true;
        }
        return res;
    }
    public boolean isIp4(){
        boolean res = false;
        if(this.type == "4"){
            res = true;
        }
        return res;
    }
    @Override
    public boolean equals(Object object) {
        boolean res = false;
        if (!(object instanceof Subnet)) {
            return res;
        }
        Subnet other = (Subnet) object;
        if(isIp4()){
            res =  (this.getNetworkIP().toString().equals(other.getSubnetIP().toString()) && (this.getPrefix() == other.getPrefix()));
        }
        if(isIp6()){
            //TODO equals ip6
            return res;
        }
        return res;
    }
}
