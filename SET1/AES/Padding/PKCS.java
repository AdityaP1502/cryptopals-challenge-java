package SET1.AES.Padding;

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
}