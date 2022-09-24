package SET4.Challenge31;

import Encoding.ASCII;
import Encoding.Hex;
import Hash.SHA1;
import SET1.XOR.XOR;

public class HMAC {
  private static String KEY = "YELLOW SUBMARINE";
  public final static int BLOCK_SIZE = 64;
  private static SHA1 hash = new SHA1();

  public static String sign(String message) {
    String messageHex = ASCII.asciiToHex(message);
    return process(messageHex);
  }

  private static String process(String message) {
    String f;
    System.out.println(message);
    String blockKey = generateRoundKey();
    System.out.println("Block key is= " + blockKey);
    byte[] blockKeyBytes = Hex.fromHexToBytes(blockKey);

    String opad = "5c".repeat(BLOCK_SIZE);
    byte[] opadBytes = Hex.fromHexToBytes(opad);

    String ipad = "36".repeat(BLOCK_SIZE);
    byte[] ipadBytes = Hex.fromHexToBytes(ipad);

    String o_key_pad = XOR.XORCombination(blockKeyBytes, opadBytes, 0);
    String i_key_pad = XOR.XORCombination(blockKeyBytes, ipadBytes, 0);

    System.out.println("o_key_pad= " + o_key_pad + "\ni_key_pad= " + i_key_pad);
    hash.setMessage(i_key_pad + message, "HEX");
    f = hash.digest();
    System.out.println("first round hash="+f);
    System.out.println("Second round message: " + o_key_pad + f);
    hash.setMessage(o_key_pad + f, "HEX");
    String signature = hash.digest();
    System.out.println("Signature= " + signature);
    return signature;
  }

  private static String generateRoundKey() {
    String KEY_HEX = ASCII.asciiToHex(KEY);
    if (KEY_HEX.length() > 128) {
      hash.setMessage(KEY_HEX, "HEX");
      KEY_HEX = hash.digest();
    } 

    if (KEY_HEX.length() < 128) {
      // append 0
      KEY_HEX += "0".repeat(128 - KEY_HEX.length());
    }

    return KEY_HEX;
  }
}
