package SET4.Challenge31;
import Encoding.Hex;
import static SET4.Challenge31.TimingAttack.sendRequest;

import java.io.IOException;
import java.io.FileWriter;
import java.io.File;

public class TimingDump {
  public static void main(String[] args) throws IOException {
    long startTime, endTime, f;
    String g;
    String signature = "0".repeat(36);
    byte x = 0;
    String filepath = "SET4/Challenge31/Dump/timing.txt";

    File file = new File(filepath);
    file.createNewFile();
    FileWriter wr = new FileWriter(file);
    String temp;

    for (int i = 0; i < 50; i++) {
      System.out.println(i);
      for (int j = 0; j < 256; j++) {
        g = "89" + Hex.hexEncoder(x) + signature;
        // time request
        startTime = System.currentTimeMillis();
        sendRequest(g);
        endTime = System.currentTimeMillis();
        // get process time
        f = endTime - startTime;
        // if (Hex.hexEncoder(x).equals("89")) System.out.println(f);
        temp = Long.toString(f) + ",";
        wr.write(temp);
        x++;
      } 
      wr.write("\n");
    }

    wr.close();
  }
}
