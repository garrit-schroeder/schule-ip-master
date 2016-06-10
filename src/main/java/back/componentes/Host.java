package back.componentes;

/**
 * Created by Garrit Schröder on 08.06.16.
 * Email: GarritSidney.Schroeder@otto.de
 */
public class Host implements Comparable<Host> {

    IPv4Address iPv4Address;
    IPv6Address iPv6Address;

    public Host(IPv4Address iPv4Address) {
        this.iPv4Address = iPv4Address;
    }
    public Host(IPv6Address iPv6Address) {
        this.iPv6Address = iPv6Address;
    }




    public IPv4Address getiPv4Address() {
        return iPv4Address;
    }

    public void setiPv4Address(IPv4Address iPv4Address) {
        this.iPv4Address = iPv4Address;
    }

    public IPv6Address getiPv6Address() {
        return iPv6Address;
    }

    public void setiPv6Address(IPv6Address iPv6Address) {
        this.iPv6Address = iPv6Address;
    }

    @Override
    public int compareTo(Host o) {
        return this.iPv4Address.toString().equals(o.getiPv4Address().toString()) ? 0 : 1;
    }
}