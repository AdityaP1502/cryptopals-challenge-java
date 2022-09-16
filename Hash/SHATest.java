package Hash;

import Encoding.ASCII;
import Encoding.Hex;

public class SHATest {
  public static void main(String[] args) {
    String message = "The quick brown fox jumps over the lazy dog";
    SHA1 sha = new SHA1(message);
    String hash = sha.digest();
  }
}
