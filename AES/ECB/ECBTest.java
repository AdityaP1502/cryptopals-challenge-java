package AES.ECB;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ECBTest {
  public static void main(String[] args) {
    String filepath = "SET1/AES/ECB/message.txt";
    String filepathOut = "SET1/AES/ECB/plaintext.txt";
    File file = new File(filepath);
    File fileOut = new File(filepathOut);
    String temp = "";
    String KEY = "YELLOW SUBMARINE";
    try {
      Scanner sc = new Scanner(file);
      while (sc.hasNextLine()) {
        temp += sc.nextLine();
      }
      ECB ecb = new ECB(temp, KEY, "BASE64");
      sc.close();
      // Write result into a file
      FileWriter wr = new FileWriter(fileOut);
      wr.write(ecb.decrypt("ASCII"));
      wr.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
