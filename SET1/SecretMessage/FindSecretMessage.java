package SET1.SecretMessage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import SET1.XOR.Decoder.SingleByteDecoder.XORDecoder;

public class FindSecretMessage {
  public static String getSecret(File file) throws FileNotFoundException, IOException {
    // Brute force method
    Scanner sc = new Scanner(file);
    
    XORDecoder decoder = new XORDecoder();

    String hexString;
    String secretMessage = "";
    double minScore = 0;
    boolean isMinInitialized = false;
    while (sc.hasNextLine()) {
      hexString = sc.nextLine();
      System.out.println(hexString);
      decoder.decrypt(hexString);
      // Update : secretMessage need to have the lowest score
      if (!isMinInitialized) {
        // Initialize minScore
        isMinInitialized = true;
        secretMessage = decoder.getDecryptedMessage();
        minScore = decoder.getMinScore();
      }

      if (decoder.getMinScore() < minScore) {
        secretMessage = decoder.getDecryptedMessage();
        minScore = decoder.getMinScore();
      }
      // System.out.println(decoder.getDecryptedMessage());
      System.out.println(decoder.getMinScore());
    }
    System.out.println(minScore);
    sc.close();
    return secretMessage;
  }
  public static void main(String[] args) throws FileNotFoundException, IOException {
    String filename = "SET1/SecretMessage/SecretMessage.txt";
    File file = new File(filename);
    String secret = getSecret(file);
    System.out.println(secret);
  }
}
