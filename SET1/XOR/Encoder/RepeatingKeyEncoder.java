package SET1.XOR.Encoder;
import java.util.Scanner;

import Encoding.ASCII;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class RepeatingKeyEncoder {
  public static String encrypt(String text, String key) {
    String encryptedString = "";
    for (int i = 0; i < text.length(); i++) {
      encryptedString += SingleByteEncoder.encrypt(text.charAt(i), key.charAt(i % key.length()));
    }

    return encryptedString;
  } 

  public static String encrypt(byte[] buff, byte[] key) {
    String encryptedString = "";
    for (int i = 0; i < buff.length; i++) {
      encryptedString += SingleByteEncoder.encrypt(buff[i], key[(i % key.length)]);
    }

    return encryptedString;
  }
  
  public static void encrypt(String inPath, String outPath, String key, String encoding) {
    // Load data from a file
    // Encrypt
    // Save to different file
    File in = new File(inPath);
    File out = new File(outPath);
    
    // temp var
    String f = "";
    byte[] g;

    try {
      Scanner sc = new Scanner(in);
      while (sc.hasNextLine()) {
        f += sc.nextLine() +"\n"; // Normal Text Encoding
      }
      sc.close();

      f = encrypt(f , key);
      g = ASCII.convertTextToBytes(f);
      out.createNewFile();
      FileWriter wr = new FileWriter(out);
      
      switch (encoding) {
        case "BASE64":
          f = ASCII.asciiToBase64(g);
          break;
        case "HEX":
        default:
        f = ASCII.asciiToHex(g);
      }

      wr.write(f);
      wr.close();
    } catch (IOException e) {
      System.out.println(e.getMessage());
      System.exit(-1);
    }
  }
}
