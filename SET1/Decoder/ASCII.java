package SET1.Decoder;
import java.util.ArrayList;
public class ASCII {
  public static byte[] fromBase64ToAscii(String base64) {
    ArrayList<Byte> buffer = Base64.base64Decoder(base64);
    // byte only have 6 use bit
    int mode = 0;
    int remainder = buffer.size() % 8;
    int length = (buffer.size() * 6) / 8 + (remainder > 0 ? 1 : 0);
    byte[] bytes = new byte[length];
    int bitmask = 15; // The only bit that are set are the last 4 bits
  
    // temp variables
    byte f, g;

    // cursor
    int pointer = 0;
    for (int i = 0; i < buffer.size() - 1; i++) {
      mode = mode % 3; // mod can only take value from 0 - 2
      switch (mode) {
        case 0:
          // Take the first 6 bits and only first 2 bit of the next byte
          f = (byte) (buffer.get(i) << 2);
          g = (byte) (buffer.get(i + 1) >> 4);
          bytes[pointer] = (byte) (f + g);
          pointer++;
          mode++;
          break;
        case 1:
          // take the remaining 4 bit from last used byte and 4 bit of the next byte
          // Kill the first 2 bits
          f = (byte) (buffer.get(i) & bitmask);
          f = (byte) (f << 4);
          g = (byte) (buffer.get(i + 1) >> 2);
          bytes[pointer] = (byte) (f + g);
          mode++;
          pointer++;
          bitmask = bitmask >> 2; // Update bitmask value to be used in case = 2
          break;
        case 2:
          // Bitmask will take the last 2 bit from the last byte
          // take all 6 bit of the next byte
          f = (byte) (buffer.get(i) & bitmask);
          f = (byte) (f << 6);
          g = buffer.get(i + 1);
          bytes[pointer] = (byte) (f + g);
          // because the next byte is fully used, after iteration, skip one
          i++;
          pointer++;
          mode++;
          bitmask = 15; // set back to the original value;
        default:
          break;
      }
    }
    
    // handle for leftover bits
    if (remainder > 0) {
      // there are leftover bits -> either 2, 4, or 6
      f = (byte) (buffer.get(buffer.size() - 1) << (6 - remainder));
      bytes[length - 1] = f;
    }

    return bytes;
  }
  public static byte[] fromHexToAscii(String hex) {
    ArrayList<Byte> buffer = Hex.hexDecoder(hex);

    int length = (int) (buffer.size() / 2);
    byte[] asciiArr = new byte[length];

    // temp variables
    byte f, g;
    
    int pointer1 =  0;
    int pointer2 = 0;

    while (pointer1 < buffer.size()) {
      f = (byte) (buffer.get(pointer1) << 4);
      g = (byte) (f + buffer.get(pointer1 + 1));
      pointer1 += 2;
      asciiArr[pointer2] = g;
      pointer2++;
    }

    return asciiArr;
  }

  public static byte[] convertTextToBytes(String text) {
    byte[] buffer = new byte[text.length()];
    for (int i = 0; i < text.length(); i++) {
      buffer[i] = (byte) text.charAt(i);
    }
    return buffer;
  }

  public static String convertToText(byte[] buffer){
    String text = "";
    for (byte x : buffer){
      text += (char) x;
    }

    return text;
  }
}
