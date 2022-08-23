package SET1.XOR.Decoder.SingleByteDecoder;
import java.util.HashMap;

public class LetterFreq {
  public static HashMap<Character, Double> letterMap = new HashMap<Character, Double>();
  private static  boolean hasSet = false;
  public static void setFrequency() {
    if (hasSet) return;
    char[] characterSet = {
      'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 
      'K', 'L', 'M', 'N', 'O', 'P', 'R', 'S', 'T', 'V', 
      'V', 'W', 'X', 'Y', 'Z', ' '
    };

    double[] frequencyLetter = {
      6.53, 1.25, 2.23, 3.28, 10.21, 1.98, 1.62, 4.97, 5.66, 0.097, 
      0.56, 3.31, 2.02, 5.71, 6.15, 1.5, 0.083, 4.98, 5.31, 7.51, 
      2.27, 0.79, 1.7, 0.14, 1.42, 0.051, 18.28
    };
    
    int index = 0;
    while (index < characterSet.length) {
      letterMap.put(characterSet[index], frequencyLetter[index]); 
      index++;
    }
  }

  static HashMap<Character, Integer> getLetterFrequency(byte[] buffer) {
    char temp;
    HashMap<Character, Integer> letterFreq = new HashMap<Character, Integer>();
    // Track work frequency 
    for (byte x : buffer) {
      temp = (char) x;
      temp = Character.toUpperCase(temp);
      letterFreq.put(temp, letterFreq.getOrDefault(temp, 0) + 1);
    }

    return letterFreq;
  }
}
