package SET4;

import Encoding.Hex;
import Encoding.UnrecognizedEncodingException;
import Hash.MAC;
import Hash.SHA1;

public class LengthExtensionAttack {
  public static String padding(String message, int keySize) {
    int messageLength = (message.length() + keySize) * 8; // message and key in bytes
    String padding = messageLength % 8 == 0 ? "80" : "";

    int n = messageLength + padding.length() * 4;
    n = n <= 448 ? (448 - n) : 448 + (512 - n);
    n /= 4;

    for (int i = 0; i < n; i++) {
      padding += '0';
    }

    padding += Hex.hexEncoder((long) messageLength);
    return padding;
  }
  public static void main(String[] args) throws UnrecognizedEncodingException {
    String message = "comment1=cooking%20MCs;userdata=foo;comment2=%20like%20a%20pound%20of%20bacon";
    String hash = MAC.sign(message);
    // key is 16 bytes)
    String newMessage = ";admin=true";
    message = message + Hex.fromHexToAscii(padding(message, 16)) + newMessage;
    // attack
    String sign = MAC.sign(message);
    hash = SHA1.forge(newMessage, hash, (message.length() + 16) * 8);
    System.out.println("Attack success: " + sign.equals(hash));
  }
}
