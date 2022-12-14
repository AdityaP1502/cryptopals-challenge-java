package Encoding;
// import java.util.ArrayList;
public class Hex {
  // public static ArrayList<Byte> hexDecoder(String s) {
    // HexMap.setMap();
    // ArrayList<Byte> temp = new ArrayList<>();
    // int index = 0;
    // while (index < s.length()) {
      // temp.add(HexMap.charToByte.get(s.charAt(index)));
      // index++;
    // }
    // return temp;
  // }

  // public static String hexEncoder(ArrayList<Byte> buffer){
    // // Encode binary data from buffer
    // HexMap.setMap();
    // String encodedString = "";
    // for (byte x : buffer) encodedString += HexMap.byteToChar.get(x);
    // return encodedString;
  // }

  // Hex Encoder and Decoder
  public static byte hexDecoder(char character) {
    // char in hex encoding
    HexMap.setMap();
    return HexMap.charToByte.get(character); 
  }

  public static byte hexDecoder(char character1, char character2) {
    // one byte is reprsented by two hex character
    HexMap.setMap();
    byte f;
    byte x = HexMap.charToByte.get(character1);
    byte y = HexMap.charToByte.get(character2);
    x = (byte) (x << 4); // promote bit from 3 to 7
    f = (byte) (x + y);
    return f;
  }

  public static byte[] hexDecoder(String s) {
    // s is hex encoded
    // f is the byte data of the encoded message
    // each byte consist of 4 bit of information
    HexMap.setMap();
    byte[] f = new byte[s.length()];
    for (int i = 0; i < s.length(); i++) {
      f[i] = hexDecoder(s.charAt(i));
    }
    return f;
  }

  public static String hexEncoder(int decimal) {
    HexMap.setMap();
    // long is 64 bit number
    int bitmask = 0xF0000000;
    int bitmask2 = 0xF000000;
    int bitmask3 = 15;
    int shiftCount = 28;
    byte x;
    String hex = "";
    
    for (int i = 0; i < 8; i++) {
      x = (byte) (((decimal & bitmask) >> shiftCount) & bitmask3);
      hex += HexMap.byteToChar.get(x); 
      shiftCount -= 4;
      bitmask = (bitmask >> 4) & bitmask2;
      bitmask2 = bitmask2 >> 4;
    }

    return hex;
  }
  
  public static String hexEncoder(long decimal) {
    HexMap.setMap();
    // long is 64 bit number
    long bitmask = 0xF000000000000000L;
    long bitmask2 = 0xF00000000000000L;
    int bitmask3 = 15;
    int shiftCount = 60;
    byte x;
    String hex = "";
    
    for (int i = 0; i < 16; i++) {
      x = (byte) (((decimal & bitmask) >> shiftCount) & bitmask3);
      hex += HexMap.byteToChar.get(x); 
      shiftCount -= 4;
      bitmask = (bitmask >> 4) & bitmask2;
      bitmask2 = bitmask2 >> 4;
    }

    return hex;
  }

  public static String hexEncoder(byte data) {
    // Encode one byte of data
    HexMap.setMap();

    byte bitMask = 15; // Have all last 4 bit set to 1
    byte shifter = 4;
    String hex = "";

    if (data > 15 || data < 0) {
      // Decode the first 4 digit from the left
      byte temp = data;
      // for byte that are < 0, shifting will create leading 1
      // >> will promote temp to int, create a leading 1
      // ex : 1000000 >> 4 -> (1111)1000
      // bitmask with all first (shift amount) bit set to 0
      temp = (byte) (temp >> shifter);
      temp = (byte) (temp & bitMask);
      hex += HexMap.byteToChar.get(temp);
    } else {
      hex += "0";
    }
    // Decode the last 4 digit, set the first 4 bit to zero
    data = (byte) (data & bitMask);
    hex += HexMap.byteToChar.get(data);
    return hex;
  }

  public static String hexEncoder(byte[] buffer){
    // Encode byte data in hex format
    HexMap.setMap();
    String encodedString = "";
    for (byte x : buffer) encodedString += hexEncoder(x);
    return encodedString;
  }

  // Hex converter
  public static String fromHexToBase64(String hexString) {
    Base64Map.setMap();
    String base64String = "";
    byte key;

    // Set string into array of bytes
    byte[] temp = hexDecoder(hexString);

    byte bitmask = 3;
    int idx = 0;
    int mode = 1;
    for (int i = 0; i < temp.length - 1; i++) {
      byte f = temp[idx];
      byte g = temp[idx + 1];

      if (idx % 2 == mode) {
        // Take 2 unused bit from before and take 4 bit from the next byte
        f = (byte) (f & bitmask);
        f = (byte) (f << 4);
        // Next byte doesn't have unused bit
        idx++;
        // Update the mode
        mode = (mode + 1) % 2;
      } else {
        // Take all 4 bit of current byte and take 2 bit from next byte
        f = (byte) (f << 2);
        g = (byte) (g >> 2);
      }
      // Convert to base64
      key = (byte) (f + g);
      base64String += Base64Map.byteToChar.get(key);
      idx++;
    }
    
    return base64String;
  }

  public static byte[] fromHexToBytes(String hex) {
    byte[] buffer = hexDecoder(hex);

    int length = (int) (buffer.length / 2);
    byte[] asciiArr = new byte[length];

    // temp variables
    byte f, g;
    
    int pointer1 =  0;
    int pointer2 = 0;

    while (pointer1 < buffer.length) {
      f = (byte) (buffer[pointer1] << 4);
      g = (byte) (f + buffer[pointer1 + 1]);
      pointer1 += 2;
      asciiArr[pointer2] = g;
      pointer2++;
    }

    return asciiArr;
  }

  public static String fromHexToAscii(String hex) {
    byte[] x = fromHexToBytes(hex);
    return ASCII.ASCIIDecoder(x);
  }
}