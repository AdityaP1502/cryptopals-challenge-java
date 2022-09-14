package SET2.CBCAttack.BitFlipping;

import Encoding.Hex;
import Encoding.UnrecognizedEncodingException;
import AES.InvalidBlockSizeException;
import AES.CBC.CBC;
import AES.CTR.CTR;
import Encoding.ASCII;

public class Validator {
  public final static String DEFAULT_KEY = "bae9e8ddf212132396060a8ff1a4da1e";
  public final static String DEFAULT_NONCE = "191f87ef86a15479";

  public static boolean validate(String decryptedMessage, String IV) {
    String keyInASCII = ASCII.ASCIIDecoder(Hex.fromHexToBytes(DEFAULT_KEY));
    CBC cbc = new CBC(decryptedMessage, keyInASCII, IV, "HEX", 1);
    String plaintextHex = cbc.decrypt(); // in hex

    // convert to ascii
    String plaintext = ASCII.ASCIIDecoder(Hex.fromHexToBytes(plaintextHex));

    String[] splits = plaintext.split(";");
    String[][] splitOfSplits = new String[splits.length][];
    
    // find admin tuple
    for (int i = 0; i < splits.length; i++) {
      splitOfSplits[i] = splits[i].split("=");
      if (splitOfSplits[i][0].equals("admin")) return true;
    }

    return false;
  }

  public static boolean validateCTR(String decryptedMessage) throws UnrecognizedEncodingException, InvalidBlockSizeException {
    String keyInASCII = ASCII.ASCIIDecoder(Hex.fromHexToBytes(DEFAULT_KEY));
    CTR ctr = new CTR(decryptedMessage, "HEX", keyInASCII, DEFAULT_NONCE);
    String plaintextHex = ctr.decrypt(); // in hex

    // convert to ascii
    String plaintext = ASCII.ASCIIDecoder(Hex.fromHexToBytes(plaintextHex));

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
