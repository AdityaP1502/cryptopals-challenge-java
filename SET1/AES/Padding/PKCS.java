package SET1.AES.Padding;

import Encoding.EncodingFormat;
import Encoding.UnrecognizedEncodingException;

public class PKCS {
  public static void pad(byte[] block, int padLength) {
    // Assume block already reserved 
    // spot for padding 
    // in place
    byte padByte = (byte) padLength;
    int lastIdx = block.length - 1;
    for (int i = 0; i < padLength; i++) {
      block[lastIdx - i] = padByte;
    }
  }

  public static int getPadLength(String text, String encoding) throws InvalidPaddingException {
    // text with padding
    int padLength = -1;
    try {
      byte[] bytes = EncodingFormat.convertToBytes(text, encoding);
      int lastIdx = bytes.length - 1;
      byte lastChar = bytes[lastIdx];
      padLength = lastChar;
      for (int i = lastIdx; i > lastIdx - padLength; i--) {
        if (bytes[i] != lastChar) throw new InvalidPaddingException("Invalid PKCS Padding");
      }
    } catch (UnrecognizedEncodingException e) {
      e.printStackTrace();
    }

    return padLength;
  }

  public static String removePadding(String text, String encoding) throws InvalidPaddingException {
    StringBuilder x = new StringBuilder(text);
    int padLength = getPadLength(text, encoding);

    for (int i = 0; i < padLength; i++) {
      // remove padding
      x.deleteCharAt(x.length() - 1);
    }

    return x.toString();
  }
}