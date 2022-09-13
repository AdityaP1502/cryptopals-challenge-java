package AES.ECB;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import Encoding.ASCII;
import Encoding.Hex;

public class ECBTest {
  public static void main(String[] args) {
    String filepath = "AES/ECB/message.txt";
    String filepathOut = "AES/ECB/plaintext.txt";
    File file = new File(filepath);
    File fileOut = new File(filepathOut);
    String temp = "";
    String KEY = "YELLOW SUBMARINE";
    try {
      Scanner sc = new Scanner(file);
      while (sc.hasNextLine()) {
        temp += sc.nextLine();
      }
      ECB ecb = new ECB(temp, KEY, "BASE64", 1);
      sc.close();
      // Write result into a file
      FileWriter wr = new FileWriter(fileOut);
      String hex = ecb.decrypt();
      String ascii = ASCII.ASCIIDecoder(Hex.fromHexToAscii(hex));
      wr.write(ascii);
      wr.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
