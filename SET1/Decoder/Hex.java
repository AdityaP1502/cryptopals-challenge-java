package SET1.Decoder;
import java.util.ArrayList;
public class Hex {
  public static ArrayList<Byte> hexDecoder(String s) {
    HexMap.setMap();
    ArrayList<Byte> temp = new ArrayList<>();
    int index = 0;
    while (index < s.length()) {
      temp.add(HexMap.charToByte.get(s.charAt(index)));
      index++;
    }
    return temp;
  }

  public static String hexEncoder(ArrayList<Byte> buffer){
    HexMap.setMap();
    String encodedString = "";
    for (byte x : buffer) encodedString += HexMap.byteToChar.get(x);
    return encodedString;
  }

  public static String hexEncoder(byte[] buffer){
    HexMap.setMap();
    String encodedString = "";
    for (byte x : buffer) encodedString += HexMap.byteToChar.get(x);
    return encodedString;
  }

  public static String hexEncoder(byte data) {
    HexMap.setMap();
    byte bitMask = 15; // Have all last 4 bit set
    byte shifter = 4;
    String hex = "";
    if (data > 15) {
      // Decode the first 4 digit from the left
      byte temp = data;
      temp = (byte) (temp >> shifter);
      hex += HexMap.byteToChar.get(temp);
    } else {
      hex += "0";
    }
    // Decode the last 4 digit, set the first 4 bit to zero
    data = (byte) (data & bitMask);
    hex += HexMap.byteToChar.get(data);
    return hex;
  }
}