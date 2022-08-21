package SET1.Decoder;
import java.util.HashMap;
public class Base64Map {
  static boolean hasSet = false;
  static HashMap<Byte, Character> byteToChar = new HashMap<Byte, Character>();
  static HashMap<Character, Byte> charToByte = new HashMap<Character, Byte>();
  static char[] characterSet = {
    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
    'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd',
    'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 
    't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', 
    '8', '9', '+', '/'
  };

  static void setMap() {
    if (hasSet) return;
    hasSet = true;
    byte f = 0;
    
    // Set byteToChar map
    for (char character : characterSet){
      byteToChar.put(f, character);
      f++;
    }

    // Set charToByte map
    f = 0;
    for (char character : characterSet){
      charToByte.put(character, f);
      f++;
    }
  }
}
