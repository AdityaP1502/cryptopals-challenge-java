package SET1.XOR.Decoder.SingleByteDecoder;
import java.util.HashMap;
import java.util.Set;
import java.lang.Math;

public class Validator {
  public static double getScore(HashMap<Character, Integer> letterFrequency, int length, double penalizePoints) {
    double coefficient = 0;
    char f;
    double g;
    Set<Character> keys = letterFrequency.keySet();
    for (char c : keys) {
      f = Character.toUpperCase(c);
      g = Math.sqrt((LetterFreq.letterMap.getOrDefault(f, 0.0) / 100) * (letterFrequency.get(c)) / (double) length);
      coefficient += g;
    }
    return coefficient - penalizePoints;
  }
}
