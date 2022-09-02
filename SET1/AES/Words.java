package SET1.AES;

import Encoding.Hex;

public class Words {
  public byte[][] words;

  public String toString() {
    byte[] f;
    String wordInHex = "";
    for (int j = 0; j < 4; j++) {
      f = words[j];
      wordInHex += Hex.hexEncoder(f);
      wordInHex += " ";
    }
    return wordInHex.trim();
  }

  private static byte[][] putBytesInGrid(byte[] block) {
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

  public Words() {
    words = new byte[4][4];
  }

  public Words(byte[] block) {
    // block has length 16
    words = putBytesInGrid(block);
  }

  public Words(byte[][] grid) {
    words = grid;
  }

  public void SubWord(int mode, int row) {
    // sub word content with conetnt in sbox
    if (mode == 0) SBox.setBox(mode); // set box for not inverse
    if (mode == 1) SBox.setBox(mode); // set box for reverse
    
    for (int i = 0; i < 4; i++) {
      words[row][i] = SBox.sbox.get(words[row][i]);
    }
  }

  public void subsBytes(int mode) {
    // 0 -> forward
    // 1 -> reverse
    for (int i = 0; i < 4; i++) {
      SubWord(mode, i);
    }
  }

  public static void XOR(byte[] word1, byte[] word2) {
    for (int i = 0; i < word1.length; i++) {
      word1[i] = (byte) (word1[i] ^ word2[i]);
    }
  }

  public static void XORWord(Words words1, Words words2, int row) {
    XOR(words1.words[row], words2.words[row]);
  }

  public void XORWords(Words words2) {
    for (int i = 0; i < 4; i++) {
      XORWord(this, words2, i);
    }
  }

  public void colmShifterOne(int row) {
    byte temp = words[row][0];
    // move i -> i - 1
    for (int i = 1; i < 4; i++) {
      words[row][i - 1] = words[row][i];
    }
    // 0 move to the end
    words[row][3] = temp;
  }

  private void rowShifterLeftOne(int row) {
    // shift one to the left = shift 3 to the right
    byte temp = words[0][row];
    for (int i = 0; i < 3; i++) {
      words[i][row] = words[i + 1][row];
    }
    words[3][row] = temp;
  }

  private void rowShifterLeftTwo(int row) {
    // shift two to the left
    byte temp;
    for (int i = 0; i < 2; i++) {
      temp = words[i][row];
      words[i][row] = words[i + 2][row];
      words[i + 2][row] = temp;
    }
  }

  private void rowShifterLeftThree(int row) {
    // shift three to the left = shift one to the right
    byte temp = words[3][row];
    for (int i = 3; i > 0; i--) {
      words[i][row] = words[i - 1][row];
    }
    words[0][row] = temp;
  }
  
  public void shiftRows(int mode) {
    // Shift rows
    // mode = 0 -> forward
    // mode = 1 -> reverse
    for (int i = 0; i < 4; i++) {
      switch (i) {
        case 0:
          break;
        case 1:
          // for forward, row 1 shift 1 to left
          // for reverse, row 1 shift 1 to the right = shift 3 to the left
          if (mode == 0) rowShifterLeftOne(i);
          if (mode == 1) rowShifterLeftThree(i);
          break;
        case 2:
          // reverse 2 to the left = reverse 2 to the right
          rowShifterLeftTwo(i);
          break;
        case 3:
          // for forward, row 3 shift 3 to the left
          // for reverse, row 3 shift 3 to the right = shift 1 to the left
          if (mode == 0) rowShifterLeftThree(i);
          if (mode == 1) rowShifterLeftOne(i);
          break;
      }
    }
  }

  public static Words matrixMultiplication(Words a, Words b) {
    Words res = new Words();
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
          g = GaloisOperator.polyMultiply((a.words)[k][j], (b.words)[i][k]);
          curr = GaloisOperator.multiplicationModulo(g);
          curr = GaloisOperator.add(curr, last);
          last = curr;
        }
        (res.words)[i][j] = curr;
      }
    }
    return res;
  }

  public void mixColumns(int mode) {
    byte[][] transformMatrix;
    if (mode == 0) {
      // forward mode
      byte[][] temp = {
        { 2, 1, 1, 3 }, { 3, 2, 1, 1 }, { 1, 3, 2, 1 }, { 1, 1, 3, 2 }
      };
      transformMatrix = temp;
    }
    else {
      // reverse mode
      byte[][] temp = {
        { 14, 9, 13, 11 }, { 11, 14, 9, 13 }, { 13, 11, 14, 9 }, { 9, 13, 11, 14 }
      };
      transformMatrix = temp;
    }
    
    Words transformWord = new Words(transformMatrix);
    words = matrixMultiplication(transformWord, this).words;
  }
}
