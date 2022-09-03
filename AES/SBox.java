package AES;

import java.util.HashMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import Encoding.Hex;

public class SBox {
  public static HashMap<Byte, Byte> sbox = new HashMap<>();
  final public static String FILEPATH = "AES/table.txt";
  final public static String FILEPATH_REVERSE = "AES/table_reverse.txt";  
  private static boolean isSet = false;
  private static int lastState = -1;

  public static void setBox(int mode) {
    if (isSet & mode == lastState) return;
    // set flag isSet to true so no need to set multiple times
    isSet = true;
    lastState = mode == 1 ? 1 :  0;
    String temp;
    File file;
    String[] value;
    byte f;
    
    if (mode == 1) file = new File(FILEPATH_REVERSE);
    else file = new File(FILEPATH);
    
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
