package SET4;

import Encoding.Hex;
import Encoding.UnrecognizedEncodingException;
import Hash.MAC;
import Hash.MD4;
import Hash.SHA1;

public class LengthExtensionAttack {
  public static String padding(String message, int keySize, String type) {
    int messageLength = (message.length() + keySize) * 8; // message and key in bytes
    String padding = messageLength % 8 == 0 ? "80" : "";

    int n = messageLength + padding.length() * 4;
    n = n % 512;
    n = n <= 448 ? (448 - n) : 448 + (512 - n);
    n /= 4;

    for (int i = 0; i < n; i++) {
      padding += '0';
    }

    padding += type.equals("SHA1") ? Hex.hexEncoder((long) messageLength) : MD4.convertToLittleEndian(Hex.hexEncoder((long) messageLength));
    return padding;
  }
  public static void main(String[] args) throws UnrecognizedEncodingException {
    // SHA1 Length Extension Attack
    String message = "comment1=cooking%20MCs;userdata=foo;comment2=%20like%20a%20pound%20of%20bacon";
    String hash = MAC.sign(message, "SHA1");
    // key is 16 bytes
    String newMessage = ";admin=true";
    message = message + Hex.fromHexToAscii(padding(message, 16, "SHA1")) + newMessage;
    // attack
    String sign = MAC.sign(message, "SHA1");
    hash = SHA1.forge(newMessage, hash, (message.length() + 16) * 8);
    System.out.println("Attack success: " + sign.equals(hash));

    System.out.println("This is MD4 Attack");
    // MD4 Length Extension Attack
    message = "comment1=cooking%20MCs;userdata=foo;comment2=%20like%20a%20pound%20of%20bacon";
    hash = MAC.sign(message, "MD4");
    message = message + Hex.fromHexToAscii(padding(message, 16, "MD4")) + newMessage;
    // attack
    sign = MAC.sign(message, "MD4");
    hash = MD4.forge(newMessage, hash, (message.length() + 16) * 8);
    System.out.println("Attack success: " + sign.equals(hash));
  }
}
