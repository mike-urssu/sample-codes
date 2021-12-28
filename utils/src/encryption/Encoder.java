package encryption;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encoder {
    public static String encrypt(String algorithm, String text) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(text.getBytes());
            byte[] digest = md.digest();
            return new BigInteger(1, digest).toString(16);
            /**
             * 암호화 결과를 대문자로 출력하려면 toUpperCase() 메서드를 추가한다.
             * return new BigInteger(1, digest).toString(16).toUpperCase();
             */
        } catch (NoSuchAlgorithmException e) {
            throw new SuchAlgorithmNotExists(algorithm);
        }
    }

    public static void main(String[] args) {
        String algorithm = "SHA-256";
        String text = "1234";
        System.out.println("text: " + text);
        System.out.println("encrypted text: " + encrypt(algorithm, text));
    }
}

class SuchAlgorithmNotExists extends RuntimeException {
    public SuchAlgorithmNotExists(String algorithm) {
        super(algorithm + " 알고리즘을 사용할 수 없습니다.\n" +
                "[MD2, MD5, SHA-1, SHA-256, SHA-384, SHA-512] 알고리즘 중 하나를 사용하세요.");
    }
}
