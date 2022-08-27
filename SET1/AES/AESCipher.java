package SET1.AES;

import SET1.Decoder.ASCII;
// import static SET1.Decoder.Hex.hexEncoder; // for debugging purposes

public class AESCipher {
  public static void XORWord(byte[] word1, byte[] word2) {
    for (int i = 0; i < 4; i++) {
      word1[i] = (byte) (word1[i] ^ word2[i]);
    }
    // System.out.println(Hex.hexEncoder(word1));
  }

  public static void SubWord(byte[] word) {
    // sub word content with conetnt in sbox
    SBox.setBox(); // set box
    for (int i = 0; i < 4; i++) {
      word[i] = SBox.sbox.get(word[i]);
    }
    // System.out.println(Hex.hexEncoder(word));
  }

  private static void subsBytes(byte[][] message) {
    for (int i = 0; i < 4; i++) {
      SubWord(message[i]);
    }
  }

  private static void rowShifter(byte[][] message, int row) {
    byte temp;
    switch (row) {
      case 0:
        break;
      case 1:
        // shift one to the left
        temp = message[0][row];
        for (int i = 0; i < 3; i++) {
          message[i][row] = message[i + 1][row];
        }
        message[3][row] = temp;
        break;
      case 2:
        for (int i = 0; i < 2; i++) {
          temp = message[i][row];
          message[i][row] = message[i + 2][row];
          message[i + 2][row] = temp;
        }
        break;
      case 3:
        // shift one to the right
        temp = message[3][row];
        for (int i = 3; i > 0; i--) {
          message[i][row] = message[i - 1][row];
        }
        message[0][row] = temp;
        break;
    }
  }

  private static void shiftRows(byte[][] message) {
    // Shift rows
    // up - down = 1 column
    // first row = do nothing
    // second row = shift left one
    // third row = shift left two
    // fourth row = shift left three = shift right one
    for (int i = 0; i < 4; i++) {
      rowShifter(message, i);
    }
  }

  private static byte[][] matrixMultiplication(byte[][] a, byte[][] b) {
    byte[][] res = new byte[4][4];
    byte curr;
    byte last;
    int g;
    for (int i = 0; i < 4; i++) {
      // change colm
      for (int j = 0; j < 4; j++) {
        // change row 
        last = 0;
        curr = 0;
        for (int k = 0; k < 4; k++) {
          g = GaloisOperator.polyMultiply(a[k][j], b[i][k]);
          curr = GaloisOperator.multiplicationModulo(g);
          curr = GaloisOperator.add(curr, last);
          last = curr;
        }
        res[i][j] = curr;
      }
    }
    return res;
  }

  private static byte[][] mixColumns(byte[][] message) {
    byte[][] transformMatrix = {
      { 2, 1, 1, 3 }, { 3, 2, 1, 1 }, { 1, 3, 2, 1 }, { 1, 1, 3, 2 }
    };
    return matrixMultiplication(transformMatrix, message);
  }

  // Implement 128 bit AES Encryption and Decryption
  private static byte[][] round(byte[][] key, byte[][] message, int round) {
    /* Do substitute bytes */
    subsBytes(message);
    /* Do Shift Rows */
    shiftRows(message);
    if (round != 10) {
      /* Do Mix Columns */
      // final round don't need to do mix columns
      message = mixColumns(message);
    }
    /* Do AddRoundKey */
    // XOR the message with the key
    XORGrid(message, key);
    return message;
  }
  
  private static void XORGrid(byte[][] grid1, byte[][] grid2) {
    for (int i = 0; i < 4; i++) {
      XORWord(grid1[i], grid2[i]);
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
      stateArray = round(encryptKey, stateArray, i);
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
