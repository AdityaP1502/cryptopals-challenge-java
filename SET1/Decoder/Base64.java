package SET1.Decoder;
import java.util.ArrayList;

public class Base64 {
  public static String fromHexToBase64(String hexString) {
    Base64Map.setMap();
    String base64String = "";
    byte key;

    // Set string into array of bytes
    ArrayList<Byte> temp = Hex.hexDecoder(hexString);

    byte bitmask = 3;
    int idx = 0;
    int mod = 1;
    while (idx < temp.size() - 1) {
      byte f = temp.get(idx);
      byte g = temp.get(idx + 1);
      if (idx % 2 == mod) {
        f = (byte) (f & bitmask);
        f = (byte) (f << 4);
        idx++;
        mod = (mod + 1) % 2;
      } else {
        f = (byte) (f << 2);
        g = (byte) (g >> 2);
      }

      key = (byte) (f + g);
      base64String += Base64Map.byteToChar.get(key);
      idx++;
      
    }
    return base64String;
  }

  public static ArrayList<Byte> base64Decoder(String text) {
    Base64Map.setMap();
    ArrayList<Byte> temp = new ArrayList<>();
    // Each byte will hold 6 bits value (0 - 64) the the last 2 bit is not used
    int index = 0;
    while (index < text.length()) {
      temp.add(Base64Map.charToByte.get(text.charAt(index)));
      index++;
    }
    return temp;
  }

  public static byte base64Decoder(char character) {
    Base64Map.setMap();
    return Base64Map.charToByte.get(character);
  }

}