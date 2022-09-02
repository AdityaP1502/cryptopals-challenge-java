package AES.ECB;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


public class DetectECBTest {
  public static void main(String[] args) {
    String filepath = "SET1/AES/ECB/hex.txt";
    String filepathOut = "SET1/AES/ECB/detectedECB.txt";
    String temp = "";

    try {
      File file = new File(filepath);
      File fileOut = new File(filepathOut);
      fileOut.createNewFile();

      Scanner sc = new Scanner(file);
      while (sc.hasNextLine()) {
        temp += sc.nextLine();
      }
      sc.close();

      String detectedECB = DetectECB.detect(temp);

      // Write result into a file
      FileWriter wr = new FileWriter(fileOut);
      wr.write(detectedECB);
      wr.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
