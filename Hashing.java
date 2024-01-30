/**
* This class drives the methods of the SHA512 class.
*/
public class Hashing extends SHA512 {
    /**
    * Creates a hexidecimal hash from a string input.
    * @param s a string.
    * @return digest.substring(0,128) the hash.
    */
    public static String cryptHash(String s) {
        String digest = hashSHA512(s);
        return digest.substring(0,128);
    }
}
