package SET2.ECBAttack.ByteAtATime;

import java.util.HashMap;

import SET2.EncryptionOracle.EncryptionOracle;

abstract class ByteAtATimeAttack {
  private int blockLength;
  private int blockSize;
  public StringBuilder attackString;

  abstract String attack();

  public void getCipherBlockSize() {
    char x = 'A';
    String gibbs;
    String input = "";
    int prevLength = 0;

    // append x until the length of the ciphertext change
    // the amount of change = block used 
    do {
      input += x;
      gibbs = EncryptionOracle.encryptECB(input, "ASCII");
      if (prevLength == 0) {
        prevLength = gibbs.length();
      }
    }
    while (prevLength == gibbs.length());
    
    blockSize = (gibbs.length() - prevLength) / 2; 
    blockLength = (prevLength) / (blockSize * 2);
  }

  public char process(String input, String template, int blockIdx, int offset) {
    String result, temp;
    String g = "";
    // assume byte used only ascii character
    // decrypted message in hex
    int start = offset + blockIdx * blockSize * 2;
    int end = start + 2 * blockSize;
    

    String blockResult = EncryptionOracle.encryptECBWithPrefix(template + input, "ASCII");
    // System.out.println(blockResult);
    blockResult = blockResult.substring(start, end);
    // System.out.println(blockResult);
    HashMap<String, String> map = new HashMap<>();

    for (int i = 0; i < 256; i++) {
      attackString.append((char) i);
      temp = attackString.toString();
      // decrypt and take the first block
      temp = template + temp;
      result = EncryptionOracle.encryptECBWithPrefix(temp, "ASCII");
      result = result.substring(offset, offset + blockSize * 2);
      map.put(result, temp);

       // rempove the appendded char
       attackString.deleteCharAt(blockSize - 1);

      if (!(g = map.getOrDefault(blockResult, "")).equals("")) {
        break;
      }

    }

    if (g.length() == 0) {
      System.out.println("Can't find char");
      System.out.println("Seems trying to find a padding byte");
      return (char) 0;
    }

    return g.charAt(g.length() - 1);
  }

  public String decryptBlock(int blockNumber, int offset, String template) {
    // try to decipher the last byte
    char f;
    String input = "";
    String blockMessage = "";

    for (int i = 0; i < blockSize - 1; i++) {
      input += "A";
    }

    // initialize attackString
    if (attackString == null) {
      attackString = new StringBuilder(input);
    }

    for (int i = blockSize - 1; i >= 0; i--) {
      f = process(input.substring(0, i), template, blockNumber, offset);
      if ((int) f == 0) {
        if (blockMessage.length() > 0) blockMessage = blockMessage.substring(0, blockMessage.length() - 1);
        break;
      }
      blockMessage += f;
      attackString.deleteCharAt(0);
      attackString.append(f);
    }
    // after this attackString == blockMessage
    // attackString need to be blockSize - 1 in length
    return blockMessage;
  }
  
  // getter and setter
  public int getBlockSize() {
    return blockSize;
  }

  public int getBlockLength() {
    return blockLength;
  }

  public void setBlockLength(int blockLength) {
    this.blockLength = blockLength;
  }

  public void setBlockSize(int blockSize) {
    this.blockSize = blockSize;
  }
}
