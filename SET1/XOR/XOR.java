package SET1.XOR;
import SET1.Decoder.Hex;
import java.util.ArrayList;
public class XOR {
  public static String XORCombination(String hex1, String hex2) {
    // Change to array of bytes
    ArrayList<Byte> byte1 = Hex.hexDecoder(hex1);
    ArrayList<Byte> byte2 = Hex.hexDecoder(hex2);
    int index = 0;
    byte temp = 0;
    String xOString = "";
    while (index < byte1.size()) {
      temp = (byte) (byte1.get(index) ^ byte2.get(index));
      xOString += Hex.hexEncoder(temp);
      index++;
    }
    
    return xOString;
  }
}
