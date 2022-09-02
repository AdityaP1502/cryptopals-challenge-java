package SET1.AES.CBC;

import java.util.Scanner;

import Encoding.ASCII;
import Encoding.Hex;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CBCTest {
  public static void main(String[] args) {
    // String filepath = "SET1/AES/CBC/message.txt";
    String filepathOut = "SET1/AES/CBC/ciphertext.txt";
    String filepathDecrypted = "SET1/AES/CBC/plaintext.txt";

    // File file = new File(filepath);
    File fileOut = new File(filepathOut);
    File fileDecrypted = new File(filepathDecrypted);
    
    String temp = "";
    String KEY = "YELLOW SUBMARINE"; // in ascii
    String IV = "00000000"; // in hex

    try {
      // fileDecrypted.createNewFile();
      // fileOut.createNewFile();

      // Scanner sc = new Scanner(file);
      // while (sc.hasNextLine()) {
        // temp += sc.nextLine();
      // }
      // sc.close();

      // CBC cbc = new CBC(temp, KEY, IV, "ASCII");
      
      // // Write result into a file
      // FileWriter wr = new FileWriter(fileOut);
      // temp = cbc.encrypt();
      // String text = "";

      // for (int i = 0; i < temp.length(); i++) {
        // text += temp.charAt(i);
        // if (text.length() == 60 || i == temp.length() - 1) {
          // // write on one line if char is 60 or char < 60 and reach the end of file
          // wr.write(text + "\n");
          // text = "";
        // }
      // }
      // wr.close();

      // // decrypt the message back
      Scanner sc = new Scanner(fileOut);
      while (sc.hasNextLine()) {
        temp += sc.nextLine();
      }
      sc.close();
      CBC cbc = new CBC(temp, KEY, IV, "BASE64");

      // write result into text
      FileWriter wr = new FileWriter(fileDecrypted);
      temp = cbc.decrypt(); // in hex
      temp = ASCII.ASCIIDecoder(Hex.fromHexToAscii(temp));
      wr.write(temp);
      wr.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
