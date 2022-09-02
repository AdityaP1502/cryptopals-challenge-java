package SET1.AES.Padding;

public class PKCSTest {
  public static void main(String[] args) {
    String x = "AAAAAAAAAAAAAA";
    byte c = 4;
    for (int i = 0; i < 3; i++) {
      x += (char) c;
    }
    try {
      System.out.println(PKCS.removePadding(x, "ASCII"));
    } catch (InvalidPaddingException e) {
      e.printStackTrace();
      System.exit(-1);
    }
  }
}
