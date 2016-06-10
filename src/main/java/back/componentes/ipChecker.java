package back.componentes;

import java.util.regex.Pattern;

/**
 * Created by meganoob on 10.06.16.
 */
public abstract class ipChecker {
    public ipChecker(){

    }
    public static boolean isIp6(String ip){
        if(ip.split("\\:").length > 1){
            return true;
        }
        else{
            return false;
        }
    }
    public static boolean isIp4(String ip){
        if(ip.split("\\.").length > 1){
            return true;
        }
        else{
            return false;
        }
    }
    public static boolean verifyIP4(String ip) {
        Pattern pattern = Pattern
                .compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
        return pattern.matcher(ip).matches();
    }
    public static boolean verifyIP6(String ip) {
        //TODO better REGEX for ipv6
        Pattern pattern = Pattern
                .compile("^(((([a-f]|[0-9]){1,4})|:):){7}\\2");
        //return pattern.matcher(ip).matches();
        return true;
    }
    public static boolean verifyIP(String ip){
        if(verifyIP4(ip) || verifyIP6(ip)){
            return true;
        }
        return false;
    }
}
