package SET1.AES.ECB;

import SET1.Decoder.Hex;

import java.util.HashMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SBox {
  public static HashMap<Byte, Byte> sbox = new HashMap<>();
  final public static String FILEPATH = "SET1/AES/ECB/table.txt";
  private static boolean isSet = false;

  public static void setBox() {
    if (isSet) return;
    // set flag isSet to true so no need to set multiple times
    isSet = true;

    String temp;
    String[] value;
    byte f;
    
    File file = new File(FILEPATH);
    byte key = 0;
    
    try (Scanner sc = new Scanner(file)) {
      while (sc.hasNextLine()) {
        temp = sc.nextLine();
        // Each string in value only has 2 character
        value = temp.split(" ", 16);
        for (int i = 0; i < 16; i++) {
          // add to hashmap
          f = Hex.hexDecoder(value[i].charAt(0), value[i].charAt(1));
          sbox.put(key, f);
          key++;
        }
      }
      
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }
}
