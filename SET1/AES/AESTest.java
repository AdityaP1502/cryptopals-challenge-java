package SET1.AES;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import SET1.Decoder.ASCII;
// import SET1.Decoder.ASCII;
import SET1.Decoder.Hex;

public class AESTest {
  public static String displayWordAsHex(byte[][] word) {
    byte[] f;
    String wordInHex = "";
    for (int j = 0; j < 4; j++) {
      f = word[j];
      wordInHex += Hex.hexEncoder(f);
      wordInHex += " ";
    }
    return wordInHex.trim();
  }
  public static void main(String[] args) throws IOException {
    SBox.setBox();
    // Open file
    String filepath = "SET1/AES/ciphertext.txt";
    File file = new File(filepath);
    // Create a new file
    file.createNewFile();

    FileWriter wr = new FileWriter(file);
    
    // temp var
    byte[][] f;
    String temp;

    // Key Expansion PASS TESTING
    String key = "YELLOW SUBMARINE";
    System.out.println("Key in hex: " + Hex.hexEncoder(ASCII.convertTextToBytes(key)));
    String message = "I Am Aditya PPPP";
    System.out.println("message in hex: " + Hex.hexEncoder(ASCII.convertTextToBytes(message)));
    try {
      f = AESCipher.encrypt(message, key);
      temp = displayWordAsHex(f);
      wr.write(temp);
      wr.close();
    } catch (InvalidBlockSizeException e) {
      System.out.println(e.getMessage());
      System.out.println(e.getStackTrace());
    }
    
  }
}
