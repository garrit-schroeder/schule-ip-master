package back.componentes;

import java.util.regex.Pattern;

/**
 * Class to aggregate a PAddress Created by snazari on 29/11/14.
 */
public class IPv6Address implements Comparable<IPv6Address> {
    private final int[] address = {0, 0, 0, 0};

    public IPv6Address() {
    }

    public IPv6Address(int okt0, int okt1, int okt2, int okt3) {
        address[0] = okt0;
        address[1] = okt1;
        address[2] = okt2;
        address[3] = okt3;
    }

    @Override
    public String toString() {
        return address[0] + "." + address[1] + "." + address[2] + "." + address[3];
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
     * Funktion to Parse a String to a IPAddress-Object
	 */
    public IPv6Address parseIP(String line) {
        String[] tmpIPString = line.split("\\.");
        return new IPv6Address(Integer.parseInt(tmpIPString[0]), Integer.parseInt(tmpIPString[1]),
                Integer.parseInt(tmpIPString[2]), Integer.parseInt(tmpIPString[3]));
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
