package SET1.AES;

public class AESCipher {
  // Implement 128 bit AES Encryption and Decryption
  private static void round(Words key, Words message, int round, int mode) {
    /* Do substitute bytes */
    message.subsBytes(mode);

    /* Do Shift Rows */
    message.shiftRows(mode);

    if (mode == 0) {
      if (round != 10) {
        /* Do Mix Columns */
        // final round don't need to do mix columns
        message.mixColumns(mode);
      }
      /* Do AddRoundKey */
      // XOR the message with the key
      message.XORWords(key);
    }

    else {
      /* Do AddRoundKey */
      // XOR the message with the key
      message.XORWords(key);
      if (round != 10) {
        /* Do Mix Columns */
        // final round don't need to do mix columns
        message.mixColumns(mode);
      }
    }
  }
  
  public static String encrypt(byte[] block, String key) throws InvalidBlockSizeException {
    // byte[] block = ASCII.convertTextToBytes(text);
    if (block.length < 16) {
      throw new InvalidBlockSizeException("Block must have size of 128 bits or 16 bytes");
    }

    // First step is to put block in a grid
    Words stateArray = new Words(block);

    /* Key Expansion */
    // init
    // get subkey 0
    Words encryptKey = AESKey.getWords(key);
    stateArray.XORWords(encryptKey);
    for (int i = 1; i <= 10; i++) {
      encryptKey = AESKey.getWords(encryptKey, i);
      round(encryptKey, stateArray, i, 0);
    }
    return stateArray.toString();
  }

  public static String decrypt(byte[] block, String key) throws InvalidBlockSizeException {
    if (block.length != 16) {
      throw new InvalidBlockSizeException("Block must have 128 bit size or 16 bytes");
    }

    Words[] subRoundKey = new Words[11];
    Words stateArray = new Words(block);
    
    Words encryptKey = AESKey.getWords(key);
    subRoundKey[0] = encryptKey;
    // Get all subRoundKey
    for (int i = 1; i <= 10; i++) {
      subRoundKey[i] = AESKey.getWords(subRoundKey[i - 1], i);
    }

    // Use subRoundKey in reverse
    stateArray.XORWords(subRoundKey[10]);
    for (int i = 1; i <= 10; i++) {
      encryptKey = subRoundKey[10 - i];
      round(encryptKey, stateArray, i, 1);
    }

    return stateArray.toString();
  }
}
