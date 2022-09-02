package SET2.EncryptionOracle;

// import Encoding.UnrecognizedEncodingException;
// import SET1.AES.AESKey;

public class RandomTest {
  public static void main(String[] args) {
    try {
      // System.out.println(AESKey.generateRandomKey("HEX"));
      // String message = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
      // String gibberish = EncryptionOracle.encrypt(message, "ASCII");
      // String mode = EncryptionOracle.check(gibberish);
      // System.out.println(gibberish);
      // System.out.println(mode);
      String message = "PUT message in here and here and here and here and here and here";
      String gibberish = EncryptionOracle.encryptECB(message, "ASCII");
      System.out.println(gibberish);

    } catch (Exception e) {
      e.printStackTrace();

    }
  }
}
