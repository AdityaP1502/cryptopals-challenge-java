package AES.Padding;

import Encoding.ASCII;
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

  public static int getPadLength(byte[] bytes) throws InvalidPaddingException {
    // text with padding
    int padLength = -1;
    int lastIdx = bytes.length - 1;
    byte lastChar = bytes[lastIdx];

    padLength = lastChar;
    if (padLength <= 0) throw new InvalidPaddingException("Invalid PKCS Padding");
    for (int i = lastIdx; i > lastIdx - padLength; i--) {
      if (bytes[i] != lastChar || (bytes[i] > 16 || bytes[i] < 0)) throw new InvalidPaddingException("Invalid PKCS Padding");
    }

    return padLength;
  }

  public static String removePadding(String text, String encoding) throws InvalidPaddingException {
    // change text into ascii
    byte[] bytes;
    StringBuilder x = null;
    try {
      bytes = EncodingFormat.convertToBytes(text, encoding);
      int padLength = getPadLength(bytes);
      x = new StringBuilder(ASCII.ASCIIDecoder(bytes));

      for (int i = 0; i < padLength; i++) {
        // remove padding
        x.deleteCharAt(x.length() - 1);
      }

    } catch (UnrecognizedEncodingException e) {
      e.printStackTrace();
      System.exit(-1);
    }
    
    return x.toString();
  }
}