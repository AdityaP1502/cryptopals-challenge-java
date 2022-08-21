package SET1.XOR.Decoder.RepeatingKeyDecoder;

public class XORDecoderTest {
  public static void main(String[] args) {
    String filepath = "SET1/XOR/Decoder/RepeatingKeyDecoder/Message.txt";
    XORDecoder decoder = new XORDecoder(40, 2, 4);
    decoder.decrypt(filepath);
  }
}
