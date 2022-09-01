package SET1.AES;

import Encoding.ASCII;
import Encoding.EncodingFormat;
import Encoding.UnrecognizedEncodingException;

import java.util.Random;
// import SET1.Decoder.Hex;
public class AESKey {
  private static byte[] rc = {1, 2, 4, 8, 16, 32, 64, -128, 27, 54};


  private static void XORWithConstant(Words generatedWords, int round) {
    byte[] rcon = {rc[round - 1], 0, 0, 0};
    Words.XORWord(generatedWords.words[0], rcon);
  }

  private static void functionG(Words generatedWords, int round) {
    // Do RotWord 
    generatedWords.colmShifterOne(0);
    // Do SubWord
    generatedWords.SubWord(0, 0);
    // Do XOR with round constant
    XORWithConstant(generatedWords, round);
  }

  public static Words getWords(String key) throws InvalidBlockSizeException {
    // for getting subkey 0
    return new Words(ASCII.ASCIIEncoder(key));
  }

  public static Words getWords(Words words, int round) {
    // words consitst of 4 word (1 word = 1 bytes
    Words generatedWords = new Words();

    // init step (W0)
    generatedWords.words[0] = words.words[3].clone();
    functionG(generatedWords, round);
    Words.XORWord(generatedWords, words, 0);
    // AESCipher.XORWord(generatedWords[0], words[0]);

    // XOR prev generatedWords with words from prev round
    for (int i = 1; i < 4; i++) {
      // generatedWords[i] = generatedWords[i - 1].clone();
      // AESCipher.XORWord(generatedWords[i], words[i]);
      generatedWords.words[i] = generatedWords.words[i - 1].clone();
      Words.XORWord(generatedWords, words, i);
    }

    return generatedWords;
  }
  
  public static String generateRandomKey(String encoding) throws UnrecognizedEncodingException {
    // key consist of 16 bytes
    Random rnd = new Random();
    byte[] bytes = new byte[16];

    for (int i = 0; i < 16; i++) {
      rnd.nextBytes(bytes);
    }

    // convert bytes into string 
    return EncodingFormat.fromBytesToText(bytes, encoding);

  }
}
