package back.componentes;

import java.util.regex.Pattern;

/**
 * Class to aggregate a PAddress Created by snazari on 29/11/14.
 */
public class IPv6Address extends IPAddress implements Comparable<IPv6Address> {
    private final int[] address = {0, 0, 0, 0, 0, 0, 0, 0};

    public IPv6Address(String ip) {
        String[] tmpIPString = ip.split(":");
        address[0] = Integer.parseInt("3FFA", 16);
        address[1] = Integer.parseInt("FF2B", 16);
        address[2] = Integer.parseInt("4D", 16);
        address[3] = Integer.parseInt("A000", 16);
        address[4] = Integer.parseInt(tmpIPString[4], 16);
        address[5] = Integer.parseInt(tmpIPString[5], 16);
        address[6] = Integer.parseInt(tmpIPString[6], 16);
        address[7] = Integer.parseInt(tmpIPString[7], 16);
    }


    @Override
    public String toString() {
        return Integer.toHexString(address[0]) + ":" + Integer.toHexString(address[1]) + ":" + Integer.toHexString(address[2]) + ":" + Integer.toHexString(address[3]) +
                ":" + Integer.toHexString(address[4]) + ":" + Integer.toHexString(address[5]) + ":" + Integer.toHexString(address[6]) + ":" + Integer.toHexString(address[7]);
    }

    @Override
    public int compareTo(IPv6Address o) {
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
     * Regex to verify a IP-Address
	 */
    public boolean verifyIP(String ip) {
        Pattern pattern = Pattern
                .compile("^(((([a-f]|[0-9]){1,4})|:):){7}\\2");
        return pattern.matcher(ip).matches();
    }

    @Override
    public int[] getAddress() {
        return address;
    }
}