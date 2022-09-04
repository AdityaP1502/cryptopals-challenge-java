package SET1.XOR;
import Encoding.Hex;

public class XOR {
  public static String XORCombination(byte[] bytes1, byte[] bytes2, int start) {
    // Change to array of bytes
    int n = bytes1.length - start;
    byte temp = 0;
    String XORString = "";
    
    for (int i = 0; i < Math.min(n, bytes2.length); i++) {
      temp = (byte) (bytes1[start + i] ^ bytes2[i]);
      XORString += Hex.hexEncoder(temp);
    }
    
    return XORString;
  }
}
