package Hash;

import Encoding.UnrecognizedEncodingException;

public class SHATest {
  public static void main(String[] args) throws UnrecognizedEncodingException {
    // if (args.length == 2) {
      // System.out.println("Message that want to be hashed is: " + args[0]);
      // MD4 md4 = new MD4(args[0], "ASCII");
      // String hash = md4.digest();
      // System.out.println("Expected: " + args[1]);
      // System.out.println("Actual: " + hash);
      // System.out.println("Status: " + args[1].equals(hash));
    // } else {
      // String message = "12345678901234567890123456789012345678901234567890123456789012345678901234567890";
      // MD4 md4 = new MD4(message, "ASCII");
      // String hash = md4.digest();
      // System.out.println(hash);
    // }

    SHA1 sha1 = new SHA1("6f737a7a7961166563747b77647f78733636363636363636363636363636363636363636363636363636363636363636363636363636363636363636363636367061737377642e747874");
    String hash = sha1.digest();
    System.out.println(hash);
  }
}
