package SET2.CBCAttack.BitFlipping;

import Encoding.ASCII;
import Encoding.Hex;
import SET2.EncryptionOracle.EncryptionOracle;

public class BitFlipAttack {
  public boolean attack() {
    int cipherBlockSize = 16; // in bytes
    String input = "XXXXXXXXXXXXXXXXXXXX;dmi=rue;abcdefghij";
    // input will be sanitized into 

    // XXXXXXXXXXXXXXXX    XXXX';'dmi'='rue
    //   first block         second block
    
    // first 16 bytes of the input is the controlled input
    // will be at the third block of the encrypted message

    String[] res = EncryptionOracle.encyptCBC(input);

    String message = res[0];
    String IV = res[1];

    String controlledBlock = message.substring(cipherBlockSize * 4, cipherBlockSize * 6);
    byte[] controlledBlockBytes = Hex.fromHexToBytes(controlledBlock);

    // bit error will be propagated through all blocks and error will occur at the same pos
    // use controlledBlockBytes to change ' into a, n, and t, 

    byte a = 70;
    byte n = 73;
    byte t = 83;

    controlledBlockBytes[6] = (byte) (controlledBlockBytes[6] ^ a);
    controlledBlockBytes[10] = (byte) (controlledBlockBytes[10] ^ n);
    controlledBlockBytes[12] = (byte) (controlledBlockBytes[12] ^ t);

    String modifiedMessage = ASCII.asciiToHex(controlledBlockBytes);
    message = message.substring(0, 4 * cipherBlockSize) + modifiedMessage + message.substring(6 * cipherBlockSize);

    return Validator.validate(message, IV);
  } 
  
}
