package SET1.XOR.Encoder;
// import java.util.Scanner;
public class RepeatingKeyEncoderTest {
  public static void main(String[] args) {
    // String text = "Burning 'em, if you ain't quick and nimble I go crazy when I hear a cymbal";
    // String key = "ICE";
    // System.out.println("Masukkan text:");
    // System.out.println("Masukkan key:");
    String text = "PPeanuts";
    String key = "Peanuts : XYZ";
    String encryptedMessage = RepeatingKeyEncoder.encrypt(text, key);
    System.out.println(encryptedMessage);
    RepeatingKeyEncoder.encrypt("SET1/XOR/Encoder/message.txt", "SET1/XOR/Encoder/cipher.txt", key, "BASE64");
  }
  
}
