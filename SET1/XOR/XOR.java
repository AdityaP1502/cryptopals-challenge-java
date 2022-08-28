package SET1.XOR;
import Encoding.Hex;

public class XOR {
  public static String XORCombination(String hex1, String hex2) {
    // Change to array of bytes
    byte[] byte1 = Hex.hexDecoder(hex1);
    byte[] byte2 = Hex.hexDecoder(hex2);
    int index = 0;
    byte temp = 0;
    String xOString = "";
    while (index < byte1.length) {
      temp = (byte) (byte1[index] ^ byte2[index]);
      xOString += Hex.hexEncoder(temp);
      index++;
    }
    
    return xOString;
  }
}
