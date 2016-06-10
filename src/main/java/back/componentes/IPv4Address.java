package back.componentes;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Class to aggregate a PAddress Created by snazari on 29/11/14.
 */
public class IPv4Address implements Comparable<IPv4Address> {
    public final int[] address = {0, 0, 0, 0};
    public String addressString = "";

    public IPv4Address() {
    }

    public IPv4Address(int okt0, int okt1, int okt2, int okt3) {
        address[0] = okt0;
        address[1] = okt1;
        address[2] = okt2;
        address[3] = okt3;
        addressString = this.toString();

    }
    public IPv4Address(String ip){
       String[] res = ip.split("\\.");
        address[0] = Integer.parseInt(res[0]);
        address[1] = Integer.parseInt(res[1]);
        address[2] = Integer.parseInt(res[2]);
        address[3] = Integer.parseInt(res[3]);
        addressString = ip;
    }

    @Override
    public String toString() {
        return address[0] + "." + address[1] + "." + address[2] + "." + address[3];
    }

    @Override
    public int compareTo(IPv4Address o) {
        return this.toString().equals(o.toString()) ? 0 : 1;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return this.toString().equals(o.toString());
    }

    /*
     * Funktion to Parse a String to a IPAddress-Object
	 */
    public IPv4Address parseIP(String line) {
        String[] tmpIPString = line.split("\\.");
        return new IPv4Address(Integer.parseInt(tmpIPString[0]), Integer.parseInt(tmpIPString[1]),
                Integer.parseInt(tmpIPString[2]), Integer.parseInt(tmpIPString[3]));
    }

    /*
     * Regex to verify a IP-Address
	 */
    public boolean verifyIP(String ip) {
        Pattern pattern = Pattern
                .compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
        return pattern.matcher(ip).matches();
    }
}
