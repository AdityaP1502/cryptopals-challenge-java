package SET1.XOR.Decoder.SingleByteDecoder;
import SET1.Decoder.ASCII;

import java.util.HashMap;
import java.lang.Math;

public class XORDecoder {
  private double minScore;
  private String decryptedMessage;

  public XORDecoder() {
  }

  public double minusPoints(byte[] buffer) {
    double minusPoints = 0;
    for (byte x : buffer) {
       if (x < 0) {
         minusPoints += 2.5;
       }
      if (x <= 31 && x >= 0) {
        minusPoints += 2;
      }
      else if (x <= 41 && x >= 32 || x <= 46 && x >= 44) {
        minusPoints += 0.09;
      }
      else if (x <= 57 && x >= 48) {
        minusPoints += 0.35;
      }
      else if (x <= 64 && x >= 58) {
        minusPoints += 0.5;
      }
      else if(x <= 96 && x >= 91) {
        minusPoints += 0.8;
      }
      else if (x <= 127 && x >= 123) {
        minusPoints += 1.5;
      }
    }
    return minusPoints;
  }

  public double getScore(HashMap<Character, Integer> letterFrequency, int length, double minusScore) {
    // Calculate the average difference to the english text distribution
    double sumDifferenceBetweenDistribution = 0;
    char[] characterSet = {
      'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 
      'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 
      'U', 'V', 'W', 'X', 'Y', 'Z'
    };
    for (char c : characterSet) {
      sumDifferenceBetweenDistribution += Math.abs(LetterFreq.letterMap.get(c) - ((letterFrequency.getOrDefault(c, 0) / (double) length) * 100));
    }
    return sumDifferenceBetweenDistribution / 26 + minusScore;
  }   

  public void decrypt(String hexString)  {
    // File file = new File("SET1/SecretMessage/Decryption/" + filename + ".txt");
    // file.createNewFile();
    // PrintWriter writer = new PrintWriter(file, "UTF-8");

    HashMap<Character, Integer> letterFrequency = new HashMap<Character, Integer>();
    byte[] asciiArr = ASCII.fromHexToAscii(hexString);
    byte[] asciiArrCopy = new byte[asciiArr.length];
    byte[] result = new byte[asciiArr.length];
    double minScore = 0;
    boolean isMinInitialized = false;
    double currScore;
    LetterFreq.setFrequency();
    // Brute force key and return result with highest score
    // Key is an 8 bit value, 0 - 255
    byte key = 0;
    for (int i = 0; i < 256; i++){
      // System.out.println("key=" + i);
      for (int j = 0; j < asciiArr.length; j++){
        asciiArrCopy[j] = (byte) (asciiArr[j] ^ key);
      }
      double minusPoints = minusPoints(asciiArrCopy);
      letterFrequency = LetterFreq.getLetterFrequency(asciiArrCopy);
      currScore = getScore(letterFrequency, asciiArrCopy.length, minusPoints);
      // writer.println(ASCII.convertToText(asciiArrCopy));
      // writer.println("Ends");
      // System.out.println("score=" + currScore);
      // Update result with good score
      if (!isMinInitialized) {
        // Initialize minimum score with the first check=
        isMinInitialized = true;
        minScore = currScore;
      }

      if (currScore < minScore) {
        result = asciiArrCopy.clone();
        minScore = currScore;
      }
      key += 1;
    }
    
    // System.out.println("Min score is: " + minScore);
    this.minScore = minScore;
    this.decryptedMessage = ASCII.convertToText(result);
    // writer.close();

  }

  public double getMinScore() {
    return minScore;
  }

  public String getDecryptedMessage() {
    return decryptedMessage;
  }
}
