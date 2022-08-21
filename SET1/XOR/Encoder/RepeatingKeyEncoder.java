package SET1.XOR.Encoder;

public class RepeatingKeyEncoder {
  public static String encrypt(String text, String key) {
    String encryptedString = "";
    for (int i = 0; i < text.length(); i++) {
      encryptedString += SingleByteEncoder.encrypt(text.charAt(i), key.charAt(i % key.length()));
    }

    return encryptedString;
  } 
}
