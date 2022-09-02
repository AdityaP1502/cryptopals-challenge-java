package AES.ECB;

import java.util.HashMap;

public class DetectECB {
  private static String divide(String text) {
    String dividedText = "";
    int pointer = 0;

    for (int i = 0; i < text.length(); i++) {
      dividedText += text.charAt(i);
      pointer++;
      // restart cursor, a word is completed add a " "
      if (pointer == 8) {
        pointer = 0;
        dividedText += " ";
      }
    }

    // Padding
    if (pointer != 0) {
      for (int i = pointer; i >= 0; i++) {
        dividedText += "0";
      }
    }

    return dividedText.trim();
  }
  
  public static String detect(String text) {
    // text encoded uisng hex
    // Detect cipher text that are encrypted using AES in ECB Mode
    String x;
    String ECBdetectedString = "";
    HashMap<String, Integer> map = new HashMap<>(); // to check whether a word exist
    // Divide text into word (4 byte -> 8 hex character)
    text = divide(text);
    String[] words = text.split(" ", -1);
    for (int i = 0; i < words.length; i++) {
      x = words[i];
      // Append new word into the map
      if (map.getOrDefault(x, -1) == -1) {
        map.put(x, i);
      } else {
        // found same word
        ECBdetectedString += x;
      }
    } 
    
    return ECBdetectedString.trim();
  }
}
