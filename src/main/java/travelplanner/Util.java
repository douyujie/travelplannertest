package travelplanner;

import java.math.BigInteger;
import java.security.MessageDigest;

public class Util {
    public static String getSHA512(String input){
        String output = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            digest.reset();
            digest.update(input.getBytes("utf8"));
            output = String.format("%0128x", new BigInteger(1, digest.digest()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }
}
