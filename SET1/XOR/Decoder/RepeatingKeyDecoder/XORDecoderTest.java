package SET1.XOR.Decoder.RepeatingKeyDecoder;

public class XORDecoderTest {
  public static void main(String[] args) {
    String filepath = "SET1/XOR/Decoder/RepeatingKeyDecoder/Message.txt";
    RepeatingKeyDecoder decoder = new RepeatingKeyDecoder(40, 2, 10);
    decoder.decrypt(filepath);
  }
}
