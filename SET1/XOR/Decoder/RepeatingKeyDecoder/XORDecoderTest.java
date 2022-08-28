package SET1.XOR.Decoder.RepeatingKeyDecoder;

// import java.io.File;
// import SET1.Decoder.ASCII;

public class XORDecoderTest {
  public static void main(String[] args) {
    String filepath = "SET1/XOR/Encoder/cipher.txt";
    // File file = new File(filepath);
    RepeatingKeyDecoder decoder = new RepeatingKeyDecoder(40, 2, 4);
    decoder.decrypt(filepath);
    // decoder.decrypt(file, ASCII.convertTextToBytes("A Key"), "out");
  }
}
