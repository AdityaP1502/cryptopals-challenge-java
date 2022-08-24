package SET1.Decoder;

public class Base64 {
  // Decoder and Encoder
  public static byte base64Decoder(char character) {
    // base64 encoding character
    Base64Map.setMap();
    return Base64Map.charToByte.get(character);
  }

  public static byte[] base64Decoder(String s) {
    // Decode base64 string into binary data
    // Each byte will hold 6 bits value (0 - 64) the the last 2 bit is not used
    Base64Map.setMap();
    byte[] f = new byte[s.length()];
    for (int i = 0; i < s.length(); i++) {
      f[i] = base64Decoder(s.charAt(i));
    }
    return f;
  }

  public static String basee64Encoder(byte data) {
    // assuming data from ascii where first bit is set to 0
    // encode one byte of data into base64
    
    byte bitmask = 3;
    String encodedString = "";

    // take the first 6 bit
    byte f = (byte) (data >> 2);
    // take the last 2 bit
    byte g = (byte) (data & bitmask);

    encodedString += Base64Map.byteToChar.get(f);
    encodedString += Base64Map.byteToChar.get(g);

    return encodedString;
  }
  public static String base64Encoder(byte[] buffer) {
    // Encode binary data in base64 format
    Base64Map.setMap();
    String encodedString = "";
    int mode = 0;
    int remainder = (buffer.length * 8) % 6;
    byte bitMask = 3; // only last two bit set to 1
    // temp var
    byte f = 0;
    byte g = 0; 
    for (int i = 0; i < buffer.length; i++) {
      mode = mode % 3;
      switch (mode) {
        case 0:
          // take the first 6 bit from the first byte
          f = (byte) (buffer[i] >> 2);
          // change mode
          mode += 1;
          break;
        case 1:
          // take the last 2 bit from prev byte
          // take first 4 bit from current byte
          f = (byte) (buffer[i - 1] & bitMask);
          f = (byte) (f << 4); // promote bit pos
          g = (byte) (buffer[i] >> 4);
          bitMask = (byte) ((bitMask << 2) + 3); // set the last 4 bit
          mode += 1;
          break;
        case 2:
          // take the last 4 bit from prev byte 
          // and the first 2 byte of curr byte
          f = (byte) (buffer[i - 1] & bitMask);
          f = (byte) (f << 2); //  promote bit pos
          g = (byte) (buffer[i] >> 6);
          bitMask = (byte) ((bitMask << 2) + 3);
          encodedString += Base64Map.byteToChar.get((byte) (f + g));
          // take the last 6 bit from curr byte
          f = (byte) (buffer[i] & bitMask);
          g = 0;
          bitMask = 3;
          mode = 0;
          break;
      }
      encodedString += Base64Map.byteToChar.get((byte) (f + g));
    }

    // handle leftoever bit
    if (remainder > 0) {
      // take the last bit
      bitMask = (byte) (Math.pow(2, remainder) - 1);
      f = (byte) (buffer[buffer.length - 1] & bitMask);
      f = (byte) (f << (6 - remainder));
      encodedString += Base64Map.byteToChar.get(f);
    }

    return encodedString;
  }
  // COnverter
  // TODO: Create Base64 - Hex Converter
  public static byte[] fromBase64ToAscii(String base64) {
    byte[] buffer = Base64.base64Decoder(base64);
    // byte only have 6 use bit
    int mode = 0;
    // int remainder = buffer.length % 8;
    int length = (buffer.length * 6) / 8;
    byte[] bytes = new byte[length];
    int bitmask = 15; // The only bit that are set are the last 4 bits
  
    // temp variables
    byte f, g;

    // cursor
    int pointer = 0;
    for (int i = 0; i < buffer.length - 1; i++) {
      mode = mode % 3; // mod can only take value from 0 - 2
      switch (mode) {
        case 0:
          // Take the first 6 bits and only first 2 bit of the next byte
          f = (byte) (buffer[i] << 2);
          g = (byte) (buffer[i + 1] >> 4);
          bytes[pointer] = (byte) (f + g);
          pointer++;
          mode++;
          break;
        case 1:
          // take the remaining 4 bit from last used byte and 4 bit of the next byte
          // Kill the first 2 bits
          f = (byte) (buffer[i] & bitmask);
          f = (byte) (f << 4);
          g = (byte) (buffer[i + 1] >> 2);
          bytes[pointer] = (byte) (f + g);
          mode++;
          pointer++;
          bitmask = bitmask >> 2; // Update bitmask value to be used in case = 2
          break;
        case 2:
          // Bitmask will take the last 2 bit from the last byte
          // take all 6 bit of the next byte
          f = (byte) (buffer[i] & bitmask);
          f = (byte) (f << 6);
          g = buffer[i + 1];
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
    // if (remainder > 0) {
      // // there are leftover bits -> either 2, 4, or 6
      // f = (byte) (buffer[buffer.length - 1] << (6 - remainder));
      // bytes[length - 1] = f;
    // }

    return bytes;
  }
}