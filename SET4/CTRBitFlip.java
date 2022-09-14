package SET4;

import AES.InvalidBlockSizeException;
import Encoding.Hex;
import Encoding.UnrecognizedEncodingException;
import SET2.CBCAttack.BitFlipping.Validator;
import SET2.EncryptionOracle.EncryptionOracle;

public class CTRBitFlip {
  public static String flip() throws UnrecognizedEncodingException, InvalidBlockSizeException {
    String input = ";dmi=rue";
    String ciphertext = EncryptionOracle.encryptCTR(input);
    // attack byte at pos 32, 34, 38, and 40
    byte ascii = (byte) '\'';
    int magic_1 = ascii ^ (byte) ('a');
    int magic_2 = ascii ^ (byte) ('n');
    int magic_3 = ascii ^ (byte) ('t');

    int[] bytePos = {34, 38, 40};
    int[] magics = {magic_1, magic_2, magic_3};

    byte[] bytes = Hex.fromHexToBytes(ciphertext);
    for (int i = 0; i < 3; i++) {
      bytes[bytePos[i]] = (byte) (bytes[bytePos[i]] ^ magics[i]);
    }
    
    return Hex.hexEncoder(bytes);
  }
  public static void main(String[] args) throws UnrecognizedEncodingException, InvalidBlockSizeException {
    String message = flip();
    // message in hex
    boolean isAdmin = Validator.validateCTR(message);
    System.out.println("Are you an admin: "+ isAdmin);
  }
}
