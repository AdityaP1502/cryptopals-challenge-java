package SET1.XOR.Decoder.RepeatingKeyDecoder;

import SET1.Decoder.ASCII;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.lang.Math;

public class XORDecoder {
  private int maxBlock = 4;
  private int keyRangeMin = 2;
  private int keyRangeMax = 40;
  private String decryptedMessage;
  private double[][] keys;

  public XORDecoder(int keyRangeMax, int keyRangeMin, int maxBlock) {
    this.keyRangeMin = keyRangeMin;
    this.keyRangeMax = keyRangeMax;
    this.maxBlock = maxBlock;
    // for stroring key and it's hamming distance
    keys = new double[(keyRangeMax - keyRangeMin) + 1][2];
  }


  public byte[][] readBlocks(int SIZE, byte[] buffer) {
    byte[][] blocks = new byte[maxBlock][SIZE];
    for (int i = 0; i < maxBlock; i++) {
      for (int j = 0; j < SIZE; j++) {
        blocks[i][j] = buffer[i * SIZE + j];
      }
    }
    return blocks;
  }

  public byte[] readBase64(File file) {
    int[] remainders = {0, 2, 4}; // valid remainder for all 6 multiples divide by 8
    byte[] bytes = null;
    try {
      Scanner sc = new Scanner(file);
      // length of base64 string needed to create n char depend on 8 * n mod 6
      int cons = 8 * keyRangeMax * maxBlock; // Total needed character in the buffer
      int remainder = (cons % 6) / 2;
      int base64LengthString = (cons + remainders[remainder]) / 6;
      String base64String = "";
      // temp variable
      String s;
      // counter
      int i = 0;
      while (sc.hasNextLine() && base64String.length() < base64LengthString) {
        // Read content 
        s = sc.nextLine();
        if (s.length() + base64String.length() <= base64LengthString) {
          // Append all of the content
          base64String += s;
        } else {
          i = 0;
          // Append partial of the content
          while (base64String.length() < base64LengthString) {
            base64String += s.charAt(i);
            i++;
          }
        }
      }
      sc.close();
      return ASCII.fromBase64ToAscii(base64String);
    } catch (FileNotFoundException e) {
      e.getMessage();
      e.getStackTrace();
      System.exit(1);
      return bytes;
    }
    
  }
  public static int hammingDistance(byte[] x ,byte[] y) {
    int distance = 0;

    // temp variables
    byte f;
    byte g;
    
    for (int i = 0; i < Math.min(x.length, y.length);i++) {
      // use bitmask to bit at desired location
      int bitmask = 128; // only the first bit is set
      for (int j = 7; j > -1; j--) {
        // use xor to test for difference in bit
        f = (byte) ((x[i] & bitmask) >> j);
        g = (byte) ((y[i] & bitmask) >> j);
        distance += (f ^ g); 
        bitmask = (bitmask >> 1); // move the "1" to the right, one step
      }
    }
    return distance;
  }

  public static int hammingDistance(String x, String y) {
    byte[] xBuff = ASCII.convertTextToBytes(x);
    byte[] yBuff = ASCII.convertTextToBytes(y);
    return hammingDistance(xBuff, yBuff);
  }

  public int[] getPotentialKeys() {
    // Take about 4 keys with smallest distance
    int[] potentialKeys = new int[4];
    // Sort keys based on their distance, and pick the first four element
    Arrays.sort(keys, Comparator.comparingDouble(o -> o[1]));
    for (int i = 0; i < 4; i++) {
      potentialKeys[i] = (int) (keys[i][0]);
    }
    return potentialKeys;
  }

  public double averageDistanceBetweenBlocks(byte[][] blocks) {
    // Average hamming distance between two blocks (for all combination)
    int sumDistance = 0; 
    // temp variables
    byte[] f;
    byte[] g;

    for (int j = 0; j < blocks.length; j++) {
      f = blocks[j];
      for (int k = j + 1; k < blocks.length; k++) {
        // Available combination => 2C4 = 6
        g = blocks[k];
        sumDistance += hammingDistance(f, g);
      }
    }
    return sumDistance / 6.0;
  }

  public double normalizedDistance(double averageDistance, int keyLength) {
    return averageDistance / keyLength;
  }

  public void findKeyLength(byte[] buffer) {
    int counter = 0;
    for (int i = keyRangeMin; i <= keyRangeMax; i++) {
      byte[][] blocks = readBlocks(i, buffer);
      double averageDistance = averageDistanceBetweenBlocks(blocks);
      double normalizedAverageDistance = normalizedDistance(averageDistance, i);
      keys[counter][0] = i;
      keys[counter][1] = normalizedAverageDistance;
      counter++;
    }
  }
  public void decrypt(String filepath) {
    File file = new File(filepath);
    byte[] buff = readBase64(file);
    findKeyLength(buff);
    int[] potentialKeys = getPotentialKeys();
    for (int x : potentialKeys) {
      System.out.println(x);
    }
  }
  public String getDecryptedMessage() {
    return decryptedMessage;
  }
}
