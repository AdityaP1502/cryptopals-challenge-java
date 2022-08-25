package SET1.AES.ECB;

import SET1.Decoder.ASCII;

public class AESCipher {
  // Implement 128 bit AES Encryption and Decryption
  private static void round(byte[][] key, byte[][] message, int round) {
    /* TODO: Do substitute bytes */
    /* TODO: Do Shift Rows */
    if (round != 10) {
      /* TODO: Do Mix Columns */
      // final round don't need to do mix columns
    }
    /* TODO: Do AddRoundKey */
    // XORGrid with key
  }
  
  private static void XORGrid(byte[][] grid1, byte[][] grid2) {
    for (int i = 0; i < 4; i++) {
      AESKey.XORWord(grid1[i], grid2[i]);
    }
  }

  public static byte[][] encrypt(String text, String key) throws InvalidBlockSizeException {
    byte[] block = ASCII.convertTextToBytes(text);

    if (block.length < 16) {
      throw new InvalidBlockSizeException("Block must have size of 128 bits or 16 bytes");
    }

    // First step is to put block in a grid
    byte[][] stateArray = putBytesInGrid(block);

    /* Key Expansion */
    // init
    // get subkey 0
    byte[][] encryptKey = AESKey.getWords(key);
    XORGrid(stateArray, encryptKey);
    for (int i = 1; i <= 10; i++) {
      encryptKey = AESKey.getWords(encryptKey, i);
      round(encryptKey, stateArray, i);
    }

    return stateArray;
  }

  public static byte[][] putBytesInGrid(byte[] block) {
    // fill the grid from up to bottom
    // from left to right is row 
    // from up to bottom is column
    // mormal matrix that are transposed
    byte[][] byteGrid = new byte[4][4];
    for (int j = 0; j < 4; j++) {
      for (int i = 0; i < 4; i++) {
        byteGrid[j][i] = block[4 * j + i];
      }
    }
    return byteGrid;
  }
}
