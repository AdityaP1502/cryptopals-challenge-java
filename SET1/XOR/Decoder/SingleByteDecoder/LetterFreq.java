package SET1.XOR.Decoder.SingleByteDecoder;
import java.util.HashMap;

public class LetterFreq {
  static HashMap<Character, Double> letterMap = new HashMap<Character, Double>();
  private static  boolean hasSet = false;
  static void setFrequency() {
    if (hasSet) return;
    double[] arrScore = {
      8.2, 1.5, 2.8, 4.3, 13, 2.2, 2, 6.1, 7, 0.15, 0.77, 
      4, 2.4, 6.7, 7.5, 1.9, 0.095, 6, 6.3, 9.1, 2.8, 0.98, 
      2.4, 0.15, 2, 0.074
    };
    char[] characterSet = {
      'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 
      'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 
      'U', 'V', 'W', 'X', 'Y', 'Z'
    };
    
    int index = 0;
    while (index < arrScore.length) {
      letterMap.put(characterSet[index], arrScore[index]); 
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
