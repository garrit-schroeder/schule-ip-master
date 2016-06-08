package back.componentes;

import java.util.regex.Pattern;

/**
 * Class to aggregate a PAddress Created by snazari on 29/11/14.
 */
public class IPv6Address implements Comparable<IPv6Address> {
    private final int[] address = {0, 0, 0, 0, 0, 0, 0, 0};

    public IPv6Address(String okt0, String okt1, String okt2, String okt3, String okt4, String okt5, String okt6, String okt7) {
        address[0] = Integer.parseInt(okt0, 16);
        address[1] = Integer.parseInt(okt1, 16);
        address[2] = Integer.parseInt(okt2, 16);
        address[3] = Integer.parseInt(okt3, 16);
        address[4] = Integer.parseInt(okt4, 16);
        address[5] = Integer.parseInt(okt5, 16);
        address[6] = Integer.parseInt(okt6, 16);
        address[7] = Integer.parseInt(okt7, 16);
    }

    @Override
    public String toString() {
        return Integer.toHexString(address[0]) + ":" + address[1] + ":" + address[2] + ":" + address[3] +
                ":" + address[4] + ":" + address[5] + ":" + address[6] + ":" + address[7];
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
}
