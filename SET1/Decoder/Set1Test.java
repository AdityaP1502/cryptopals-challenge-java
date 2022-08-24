package SET1.Decoder;

public class Set1Test {
  public static void main(String[] args) {
    String text = "There is nothing to see here :)";
    byte[] f = ASCII.convertTextToBytes(text);
    String base64 = Base64.base64Encoder(f);
    System.out.println(base64);
    text = ASCII.convertToText(Base64.fromBase64ToAscii(base64));
    System.out.println(text);
  }
  
}
