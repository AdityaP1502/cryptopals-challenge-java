package SET2.CBCAttack.BitFlipping;

import Encoding.Hex;
import AES.CBC.CBC;
import Encoding.ASCII;

public class Validator {
  public final static String DEFAULT_KEY = "bae9e8ddf212132396060a8ff1a4da1e";

  public static boolean validate(String decryptedMessage, String IV) {
    String keyInASCII = ASCII.ASCIIDecoder(Hex.fromHexToAscii(DEFAULT_KEY));
    CBC cbc = new CBC(decryptedMessage, keyInASCII, IV, "HEX", 1);
    String plaintextHex = cbc.decrypt(); // in hex

    // convert to ascii
    String plaintext = ASCII.ASCIIDecoder(Hex.fromHexToAscii(plaintextHex));

    String[] splits = plaintext.split(";");
    String[][] splitOfSplits = new String[splits.length][];
    
    // find admin tuple
    for (int i = 0; i < splits.length; i++) {
      splitOfSplits[i] = splits[i].split("=");
      if (splitOfSplits[i][0].equals("admin")) return true;
    }

    return false;
  }
}
