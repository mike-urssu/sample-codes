package encryption;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256 {
    public static String encrypt(String text) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(text.getBytes());
        byte[] digest = md.digest();
        return new BigInteger(1, digest).toString(16);

        /**
         * 암호화 결과를 대문자로 출력하려면 toUpperCase() 메서드를 추가한다.
         */
//        return new BigInteger(1, digest).toString(16).toUpperCase();
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        String text = "1234";
        System.out.println("text: " + text);
        System.out.println("encrypted text: " + encrypt(text));
    }
}
