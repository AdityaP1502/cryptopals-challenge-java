package SET1.XOR.Encoder;

import SET1.Decoder.ASCII;
import SET1.Decoder.Hex;

public class SingleByteEncoder {
  public static String encrypt(String text, char key) {
    byte[] buffTxt = ASCII.convertTextToBytes(text);
    byte encryptKey = (byte) key;
    for (int i = 0; i < buffTxt.length; i++) {
      buffTxt[i] = (byte) (buffTxt[i] ^ encryptKey);
    }
    return Hex.hexEncoder(buffTxt);
  }

  public static String encrypt(char character, char key) {
    byte ascii = (byte) character;
    byte encryptKey = (byte) key;
    byte encryptMessage = (byte) (ascii ^ encryptKey);
    return Hex.hexEncoder(encryptMessage);
  }

  public static char encrypt(byte ch, byte key) {
    byte encryptMessage = (byte) (ch ^ key);
    return (char) encryptMessage;
  }
}
