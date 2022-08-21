package SET1.XOR.Decoder.SingleByteDecoder;
import java.util.Scanner;
public class DecoderTest {
  public static void main(String[] args) {
    String s;
    System.out.println("Masukkan string dalam encoding Hex:");
    Scanner sc = new Scanner(System.in);
    s = sc.next();
    XORDecoder decoder = new XORDecoder();
    decoder.decrypt(s);
    System.out.println(decoder.getDecryptedMessage());
    // result is Cooking MC's like a pound of bacon
    // Key = 88
    sc.close();
  }
}
