import java.security.*;
import java.nio.charset.*;
/**
* This class contains methods for creating a hexidecimal representation of a string.
*/
public class SHA512 {
    /**
    * Creates hexidecimal string from string input.
    * @param message a string.
    * @return sha512ValueHexa a hexidecimal string.
    */
    protected static String hashSHA512(String message) {
        String sha512ValueHexa = "";
        try {
            MessageDigest digest512 = MessageDigest.getInstance("SHA-512");
            sha512ValueHexa = byteToHex(digest512.digest(message.getBytes(StandardCharsets.UTF_8)));
        }
        catch(NoSuchAlgorithmException exp) {
            exp.getMessage();
        }
        return sha512ValueHexa;
    }
    /**
    * Converts bytes to hexidecimal.
    * @param digest the bytes to convert.
    * @return out the hexidecimal string.
    */
    public static String byteToHex(byte[] digest) {
        StringBuilder vector = new StringBuilder();
        for (byte c : digest) {
            vector.append(String.format("%02X", c));
        }
        String output = vector.toString();
        return output;
    }
}
