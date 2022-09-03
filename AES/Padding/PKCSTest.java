package AES.Padding;

public class PKCSTest {
  public static void main(String[] args) {
    String x = "AAAAAAAAAAAAAAA";
    byte c = 2;
    for (int i = 0; i < 1; i++) {
      x += (char) c;
    }
    try {
      String y = (PKCS.removePadding(x, "ASCII"));
      System.out.println(y);
    } catch (InvalidPaddingException e) {
      e.printStackTrace();
      System.exit(-1);
    }
  }
}
