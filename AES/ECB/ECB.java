package AES.ECB;

import AES.AESCipher;
import AES.AESMode;
import AES.InvalidBlockSizeException;


public class ECB extends AESMode {
  // AES in ECB mode
  public ECB(String text, String key, String encoding, int mode) {
    super(text, key, encoding, mode);
  }

  @Override
  public void process() {
    String processedString = "";
    int totalBlock = getBlocks().length;
    String x;

    for (int i = 0; i < totalBlock; i++) {
      try {
        x = (getMode() == 0) ? AESCipher.encrypt(getBlocks()[i], getKEY()) : AESCipher.decrypt(getBlocks()[i], getKEY());
        processedString += x;
      } catch (InvalidBlockSizeException e) {
        e.printStackTrace();
        System.exit(-1);
      }
    }

    setProcessedString(processedString.replace(" ", ""));
  }
}
