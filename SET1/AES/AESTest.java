package SET1.AES;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import Encoding.ASCII;
import Encoding.Hex;

public class AESTest {
  
  public static void main(String[] args) throws IOException {
    // Open file
    String filepath = "SET1/AES/ciphertext.txt";
    String filepathDecrypt = "SET1/AES/plaintext.txt";
    File file = new File(filepath);
    File fileDecrypt = new File(filepathDecrypt);
    // Create a new file
    file.createNewFile();
    fileDecrypt.createNewFile();

    FileWriter wr = new FileWriter(file);
    
    // temp var
    String temp;

    String key = "YELLOW SUBMARINE";
    System.out.println("Key in hex: " + Hex.hexEncoder(ASCII.convertTextToBytes(key)));
    String message = "I Am Aditya PPPP";
    System.out.println("message in hex: " + Hex.hexEncoder(ASCII.convertTextToBytes(message)));

    try {
      // encrypt file
      temp = AESCipher.encrypt(message, key);
      wr.write(temp);
      wr.close();

      // decrypt file
      wr = new FileWriter(fileDecrypt);
      byte[] block = Hex.fromHexToAscii(temp.replace(" ", ""));
      temp = AESCipher.decrypt(block, key);
      wr.write(temp);
      wr.close();
      // decrypt the message
    } catch (InvalidBlockSizeException e) {
      System.out.println(e.getMessage());
      System.out.println(e.getStackTrace());
    }
  }
}
