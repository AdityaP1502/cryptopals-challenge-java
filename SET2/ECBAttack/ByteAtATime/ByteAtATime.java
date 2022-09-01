package SET2.ECBAttack.ByteAtATime;

import SET2.EncryptionOracle.EncryptionOracle;

public class ByteAtATime extends ByteAtATimeAttack {
  // simple byte at a time attack
  // without prefix
  private boolean isEBC;

  public ByteAtATime() {
    super();
    getCipherBlockSize();
    isEBC = isEBC();
  }

  public boolean isEBC() {
    String gibberish;
    String[] testBlock = new String[2];
    String input = "";

    for (int i = 0; i < getBlockSize(); i++) {
      input += 'A';
    }

    // duplicate the message
    input += input;

    gibberish = EncryptionOracle.encryptECB(input, "ASCII");

    // get the first 2 *  block size worth of text
    testBlock[0] = gibberish.substring(0, getBlockSize()  * 2);
    testBlock[1] = gibberish.substring(getBlockSize() * 2, getBlockSize() * 3);

    // if same input == same output -> ECB
    if (testBlock[0].equals(testBlock[1])) return true;
    return false;
  }

  public String attack() {
    // attack only work in ebc mode
    if (!isEBC) {
      System.out.println("Not in ECB mode. Attack failed");
      System.out.println("Exiting...");
      System.exit(-1);
    }

    String message = "";
    for (int i = 0; i < getBlockLength(); i++) {
      // System.out.println(i);
      message += decryptBlock(i, 0, "");
      // System.out.println(message);
    }

    return message;
  }

  public void setEBC(boolean isEBC) {
    this.isEBC = isEBC;
  }

  public boolean getEBC() {
    return isEBC;
  }
}
