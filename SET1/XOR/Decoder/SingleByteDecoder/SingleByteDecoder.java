package SET1.XOR.Decoder.SingleByteDecoder;
import java.util.HashMap;

import Encoding.ASCII;
import Encoding.Hex;


public class SingleByteDecoder {
  private byte key;
  private double maxScore;
  private String decryptedMessage;

  public SingleByteDecoder() {
  }

  public double minusPoints(byte[] buffer) {
    double minusPoints = 0;
    double[] multipliers = {10, 8, 0.1, 5, 4, 8, 8, 8, 8};
    int[] counter  = new int[9];

    for (byte x : buffer) {
      if (x < 0) {
        // Not a valid ascii byte
        counter[0] += 1;
      }
      else if (x <= 31 && x >= 0 && x != 10) {
        // Control charadcter such as null, Backspace, etc
        counter[1] += 1;
      }
      else if (x == 34 || x == 39 || x == 10) {
        // ' or "
        counter[2] += 1;
      }
      else if (x == 33 || x == 63) {
        // '!', '?'
        counter[3] += 1;
      }
      else if (x == 44 || x == 46) {
        counter[4] += 1;
      }
      else if ((x <= 38 && x >= 35) || (x <= 43 && x >= 40) || x == 45 || x == 47 || x == 63 || x == 64) {
        // Special character Type 1
        // Used is still frequent
        // '#', '&', '%', '(', ')', '@'
        counter[5] += 1;
      }

      else if (x <= 57 && x >= 48) {
        // Numbers
        // 1, 2, 3, 4, 5, 6, 7, 8, 9, 0
        counter[6] += 1;
      }

      else if ((x >= 58 && x <= 64)) {
        // "<", ">", ...
        counter[7] += 1;
      }

      else if((x <= 96 && x >= 91) || (x <= 127 && x >= 123)) {
        // '{', '}', ...
        counter[8] += 1;
      }

      // else if (x == 10) {
        // // newline
        // counter[8] += 1;
      // }

  //     else {
  //       // Special character Type 3
  //       // Only 0.00000000000001% used in real english text
  //       counter[6] += 1;
      }

      // Calculate the minusPoints
      for (int i = 0; i < 9; i++) {
        minusPoints += counter[i] * multipliers[i]; 
      }
    
      return minusPoints;
    }

  public void decrypt(String hexString) {
    byte[] asciiArr = Hex.fromHexToAscii(hexString);
    decrypt(asciiArr);
  }

  public void decrypt(byte[] asciiArr)  {
    HashMap<Character, Integer> letterFrequency = new HashMap<Character, Integer>();
    byte[] asciiArrCopy = new byte[asciiArr.length];
    byte[] result = new byte[asciiArr.length];
    double maxScore = 0;
    byte theKey = 0; // key used to encode the string 
    boolean isMinInitialized = false;
    double penalizePoints;
    double currScore = 0;
    
    LetterFreq.setFrequency();
    // Brute force key and return result with highest score
    // Key is an 8 bit value, 0 - 255
    byte key = 0;
    for (int i = 0; i < 256; i++){
      for (int j = 0; j < asciiArr.length; j++){
        asciiArrCopy[j] = (byte) (asciiArr[j] ^ key);
      }

      penalizePoints = minusPoints(asciiArrCopy);
      letterFrequency = LetterFreq.getLetterFrequency(asciiArrCopy);
      currScore = Validator.getScore(letterFrequency, asciiArrCopy.length, penalizePoints);
  
      // Update result with good score
       if (!isMinInitialized) {
        // Initialize minimum score with the first check=
        isMinInitialized = true;
        maxScore = currScore;
        theKey = key;
      }

      if (currScore >= maxScore) {
        result = asciiArrCopy.clone();
        maxScore = currScore;
        theKey = key;
      }
      key += 1;
    }
    
    this.maxScore = maxScore;
    System.out.println(maxScore);
    this.key = theKey;
    this.decryptedMessage = ASCII.convertToText(result);
  }

  public double getMaxScore() {
    return maxScore;
  }

  public String getDecryptedMessage() {
    return decryptedMessage;
  }

  public byte getKey() {
    return key;
  }
}
