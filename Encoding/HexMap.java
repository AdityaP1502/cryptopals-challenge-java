package Encoding;
import java.util.HashMap;

public class HexMap {
  static boolean hasSet = false;
  static HashMap<Byte, Character> byteToChar = new HashMap<Byte, Character>();
  static HashMap<Character, Byte> charToByte = new HashMap<Character, Byte>();
  static char[] characterSet = {'0', '1', '2', '3', '4', '5',
    '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

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
