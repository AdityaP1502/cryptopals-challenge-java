package SET2.ECBAttack;

import java.util.HashMap;

import SET2.EncryptionOracle.EncryptionOracle;

public class ByteAtATimePrefix extends ByteAtATimeAttack {
  private int prefixBlock;
  private int prefixLength;
  private int prefixPad;
  private HashMap<String, Integer> duplicateMemo = new HashMap<>();

  public ByteAtATimePrefix() {
    super();
    getPrefixLength();
  }

  public void setMap(String cipher) {
    HashMap<String, Boolean> map = new HashMap<>();
    for (int i = 0; i < cipher.length() / (getBlockSize() * 2); i++) {
      findNewDuplicate(cipher, i, map);
    }
  }

  public int[] findNewDuplicate(String cipher, int block, HashMap<String, Boolean> map) {
    boolean x;
    
    int start = block * getBlockSize() * 2; // in hex
    int[] res = {0, 0};

    String blockString = "";
    for (int i = start; i < start + getBlockSize() * 2; i++) {
      blockString += cipher.charAt(i);
    }

    x = map.getOrDefault(blockString, false);
    if (!x) {
      map.put(blockString, true);
    } else {

      int y = duplicateMemo.getOrDefault(blockString, -1);

      if (y == -1) {
        // found new duplicate
        res[0] = 1;
        res[1] = block;
        duplicateMemo.put(blockString, block);
      }
    }

    return res;
  }
  
  public int existNewDuplicate(String cipher) {
    int[] g;
    HashMap<String, Boolean> map = new HashMap<>();
    for (int i = 0; i < cipher.length() / (getBlockSize() * 2); i++) {
     g = findNewDuplicate(cipher, i, map);
     if (g[0] == 1) return g[1]; // find new duplicate
    }

    return -1;
  }
  
  public void getPrefixLength() {
    // feed the oracle with Repeating byte
    // feed until found two repeating block
    // prefixLength = input_length - 2 * blockSize
    String cipher;
    String feedBytes = "";
    int f;
    boolean isEBC = false;

    for (int i = 0; i < 2 * getBlockSize() - 1; i++) {
      feedBytes += "X";
    }
    // init
    cipher = EncryptionOracle.encryptECBWithPrefix(feedBytes, "ASCII");
    setMap(cipher);

    for (int i = 0; i < getBlockSize(); i++) {
      feedBytes += "X";
      cipher = EncryptionOracle.encryptECBWithPrefix(feedBytes, "ASCII");
      f = existNewDuplicate(cipher);
      if (f != -1) {
        // feedBytes create new duplicate
        prefixLength = (f - 1) * (getBlockSize() * 2) - (i * 2);
        prefixLength = prefixLength / 2; // change to bytes

        prefixPad = i;
        prefixBlock = f;

        setBlockLength(Math.ceilDiv(cipher.length(), (getBlockSize() * 2)) - (f + 1));
        isEBC = true;
        break;
      }
    }

    if (!isEBC) {
      System.out.println("Not using EBC Mode");
      System.out.println("Exiting...");
      System.exit(-1);
    }
  }
  
  public String attack() {
    String message = "";
    String template = "";

    for (int i = 0; i < prefixPad; i++) {
      template += "A";
    }

    for (int i = 0; i < getBlockLength(); i++) {
      // System.out.println(i);
      message += decryptBlock(i, (prefixBlock - 1) * getBlockSize() * 2, template);
      // System.out.println(message);
    }

    return message;
  }
}
