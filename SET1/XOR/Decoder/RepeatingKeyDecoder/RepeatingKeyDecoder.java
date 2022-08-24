package SET1.XOR.Decoder.RepeatingKeyDecoder;

import SET1.Decoder.ASCII;
import SET1.Decoder.Base64;
import SET1.XOR.Decoder.SingleByteDecoder.SingleByteDecoder;
import SET1.XOR.Encoder.RepeatingKeyEncoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.lang.Math;


public class RepeatingKeyDecoder {
  private int nBlock = 2;
  private int keyRangeMin = 2;
  private int keyRangeMax = 40;
  private String decryptedMessage;
  private List<Pair> keys = new ArrayList<>();


  public RepeatingKeyDecoder(int keyRangeMax, int keyRangeMin, int nBlock) {
    this.keyRangeMin = keyRangeMin;
    this.keyRangeMax = keyRangeMax;
    this.nBlock = nBlock;
  }


  public byte[][] readBlocks(int SIZE, byte[] buffer) {
    int totalBlocks = buffer.length / SIZE;
    byte[][] blocks = new byte[totalBlocks][SIZE];
    for (int i = 0; i < totalBlocks; i++) {
      for (int j = 0; j < SIZE; j++) {
        blocks[i][j] = buffer[i * SIZE + j];
      }
    }
    return blocks;
  }

  public byte[] readBase64(File file) {
    // int[] remainders = {0, 4, 2}; // valid remainder for all 6 multiples divide by 8
    byte[] bytes = null;
    try {
      Scanner sc = new Scanner(file);
      // length of base64 string needed to create n char depend on 8 * n mod 6
      // int cons = 8 * keyRangeMax * maxBlock; // Total needed character in the buffer
      // int remainder = (cons % 6) / 2;
      // int base64LengthString = (cons + remainders[remainder]) / 6;
      String base64String = "";
      // temp variable
      String s;
      while (sc.hasNextLine()) {
        // Read content 
        s = sc.nextLine();
        // Append all of the content
        base64String += s;
      }
      sc.close();
      return Base64.fromBase64ToAscii(base64String);
    } catch (FileNotFoundException e) {
      System.out.println(e.getMessage());
      e.getStackTrace();
      System.exit(1);
      return bytes;
    }
  }

  public static int hammingDistance(byte[] x, byte[] y) {
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
    double keyVal;
    // Sort keys based on their distance, and pick the first four element
    Collections.sort(keys, Comparator.comparingDouble(o -> o.getB()));
    for (int i = 0; i < 4; i++) {
      keyVal = keys.get(i).getA();
      potentialKeys[i] = (int) keyVal;
      System.out.println(potentialKeys[i]);
    }
    return potentialKeys;
  }

  public double averageDistanceBetweenBlocks(byte[][] blocks) {
    // Find the distance between all the blocks 
    int sumDistance = 0;
    double averageDistance = 0.0;
    double sumAverageDistance = 0.0;
    double totalCombination = 0;
    // temp variables
    byte[] f;
    byte[] g;

    for (int j = 0; j < nBlock; j++) {
      f = blocks[j];
      for (int k = j + 1; k < blocks.length; k++) {
        // Available combination => 2C4 = 6
        g = blocks[k];
        sumDistance += hammingDistance(f, g);
        totalCombination += 1;
      }
      // calculate distance per block
      averageDistance = sumDistance / totalCombination;
      sumAverageDistance += averageDistance;
      // reset counter
      totalCombination = 0;
      sumDistance = 0;
    }

    return sumAverageDistance / nBlock;
  }

  public double normalizedDistance(double averageDistance, int keyLength) {
    return averageDistance / keyLength;
  }

  public void findKeyLength(byte[] buffer) {
    for (int i = keyRangeMin; i <= keyRangeMax; i++) {

      if (buffer.length < i) {
        // no need to search any furhter 
        break;
      }

      byte[][] blocks = readBlocks(i, buffer);
      if (blocks.length == 1) {
        // Can't create other block to test other than itself
        break;
      }

      double averageDistance = averageDistanceBetweenBlocks(blocks);
      double normalizedAverageDistance = normalizedDistance(averageDistance, i);

      Pair pair = new Pair(i, normalizedAverageDistance);
      keys.add(pair);
    }
  }

  public byte[][] transposeBlocks(byte[][] blocks) {
    // group by key value
    byte[][] transposeBlocks = new byte[blocks[0].length][blocks.length];
    for (int j = 0; j < blocks[0].length;j++) {
      for (int i = 0; i < blocks.length;i++) {
        transposeBlocks[j][i] = blocks[i][j];
      }
    }
    return transposeBlocks;
  }

  public byte[] decipherKey(int keyLength, byte[] buf) {
    byte[][] blocks = transposeBlocks(readBlocks(keyLength, buf));
    // Do Single - byte XOR Decoder on each blocks to find the char of the key
    SingleByteDecoder decoder = new SingleByteDecoder();
    byte[] key = new byte[keyLength];
    for (int i = 0; i < keyLength; i++) {
      byte[] asciiArr = blocks[i];
      decoder.decrypt(asciiArr);
      key[i] = decoder.getKey();
      System.out.println(decoder.getDecryptedMessage());
    }

    return key;
  }

  public void decrypt(String filepath) {
    int counter = 1;
    File file = new File(filepath);
    byte[] buff = readBase64(file);
    findKeyLength(buff);
    int[] potentialKeys = getPotentialKeys();
    ArrayList<byte[]> potentialKeysBytes = new ArrayList<>();
    for (int i = 0; i < potentialKeys.length; i++) {
      potentialKeysBytes.add(decipherKey(potentialKeys[i], buff));
    }
    
    // find all possible result
    for (byte[] key : potentialKeysBytes) {
      System.out.println(ASCII.convertToText(key));
      decrypt(file, key, Integer.toString(counter));
      counter++;
    }
  }
  
  public void decrypt(File file, byte[] key, String filename) {
    // Decrypt file content using key and write it to a file
    try {
      // Open resource
      File writeFile = new File("SET1/XOR/Decoder/RepeatingKeyDecoder/" + filename + ".txt");
      FileWriter wr = new FileWriter(writeFile);
      Scanner sc = new Scanner(file);
      // Create file
      writeFile.createNewFile();
      // temp var
      String f = "";
      byte[] g;
      while(sc.hasNextLine()) {
        f += sc.nextLine(); // Base64 String
      }
      // Decrypt message
      g = Base64.fromBase64ToAscii(f);
      String text = RepeatingKeyEncoder.encrypt(g, key);
      wr.write(text);

      wr.close();
      sc.close();
    } catch (IOException e) {
      System.out.println(e.getMessage());
      // e.getStackTrace();
      System.exit(-1);
    }
  }
  public String getDecryptedMessage() {
    return decryptedMessage;
  }
}
