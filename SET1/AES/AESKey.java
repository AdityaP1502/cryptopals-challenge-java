package SET1.AES;

import SET1.Decoder.ASCII;

public class AESKey {
  private static byte[] rc = {1, 2, 4, 8, 16, 32, 64, -128, 27, 54};

  private static void rotWord(byte[] word) {
    byte temp = word[0];
    // move i -> i - 1
    for (int i = 1; i < 4; i++) {
      word[i - 1] = word[i];
    }
    // 0 move to the end
    word[3] = temp;
  }

  private static void SubWord(byte[] word) {
    // sub word content with conetnt in sbox
    SBox.setBox(); // set box
    for (int i = 0; i < 4; i++) {
      word[i] = SBox.sbox.get(word[i]);
    }
  }

  public static void XORWord(byte[] word1, byte[] word2) {
    for (int i = 0; i < 4; i++) {
      word1[i] = (byte) (word1[i] ^ word2[i]);
    }
  }

  private static void XORWithConstant(byte word[], int round) {
    byte[] rcon = {rc[round], 0, 0, 0};
    XORWord(word, rcon);
  }

  private static byte[] functionG(byte[] word, int round) {
    byte[] word_copy = word.clone();
    // Do RotWord
    rotWord(word_copy);
    // Do SubWord
    SubWord(word_copy);
    // Do XOR with round constant
    XORWithConstant(word_copy, round);
    return word_copy;
  }

  public static byte[][] getWords(String key) throws InvalidBlockSizeException {
    // for getting subkey 0
    return changeKeyToWord(key);
  }

  public static byte[][] getWords(byte[][] words, int round) {
    // words consitst of 4 word (1 word = 1 bytes
    byte[][] generatedWords = new byte[4][4];

    // init step (W0)
    generatedWords[0] = functionG(words[3], round);
    XORWord(words[0], generatedWords[0]);

    // XOR prev generatedWords with words from prev round
    for (int i = 1; i < 4; i++) {
      generatedWords[i] = generatedWords[0].clone();
      XORWord(generatedWords[i], words[i]);
    }

    return generatedWords;
  }
  
  public static byte[][] changeKeyToWord(String key) throws InvalidBlockSizeException{
    // key must be 128 bit
    byte[] keyInBytes = ASCII.convertTextToBytes(key);
    if (keyInBytes.length < 16) {
      throw new InvalidBlockSizeException("Key must have 128 bit length or 16 bytes");
    }

    byte[][] words = new byte[4][4];
    // one row = one word, 
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++ ){
        words[i][j] = keyInBytes[4 * i + j];
      }
    }

    return words;
  }
}
