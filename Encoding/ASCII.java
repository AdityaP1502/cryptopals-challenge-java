package Encoding;
public class ASCII {
  public static byte[] ASCIIEncoder(String text) {
    byte[] buffer = new byte[text.length()];
    for (int i = 0; i < text.length(); i++) {
      buffer[i] = (byte) text.charAt(i);
    }
    return buffer;
  }

  public static String ASCIIDecoder(byte[] buffer){
    String text = "";
    for (byte x : buffer){
      text += (char) x;
    }

    return text;
  }

  public static String asciiToHex(String text) {
    byte[] buff = ASCIIEncoder(text);
    return Hex.hexEncoder(buff);
  }

  public static String asciiToHex(byte[] buff) {
    return Hex.hexEncoder(buff);
  }

  public static String asciiToBase64(byte[] buff) {
    return Base64.base64Encoder(buff);
  }

  public static String asciiToBase64(String text) {
    byte[] buff = ASCIIEncoder(text);
    return Base64.base64Encoder(buff);
  }
}
