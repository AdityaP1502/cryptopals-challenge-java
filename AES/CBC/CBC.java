package AES.CBC;

import java.util.Random;

import AES.AESCipher;
import AES.AESMode;
import AES.InvalidBlockSizeException;
import AES.Words;
import Encoding.Hex;

public class CBC extends AESMode{
  private String IV;

  public CBC(String text, String key, String IV, String encoding, int mode) {
    super(text, key, encoding, mode);
    this.IV = IV;
    // change text into bytes
  }

  public CBC(String text, String key, String encoding, int mode) {
    super(text, key, encoding, mode);
    setRandomIV();
  }

  public void  setRandomIV() {
    Random rnd = new Random();
    byte[] bytesIV = new byte[16];

    for (int i = 0; i < 16; i++) {
      rnd.nextBytes(bytesIV);
    }

    IV = Hex.hexEncoder(bytesIV);
  }

  @Override
  public void process() {
    String text = "";
    byte[] lastCipherText = Hex.fromHexToBytes(IV);
    int totalBlock = getBlocks().length;
    String f;

    for (int i = 0; i < totalBlock; i++) {
      try {
        if (getMode() == 0) {
          // Encryption
          Words.XOR(getBlocks()[i], lastCipherText);
          // System.out.println(Hex.hexEncoder(getBlocks()[i]));
          f = AESCipher.encrypt(getBlocks()[i], getKEY()).replace(" ", "");
          text += f;
          lastCipherText = Hex.fromHexToBytes(f);
        }
        else {
          // Decryption
          byte[] g;
          f = AESCipher.decrypt(getBlocks()[i], getKEY()).replace(" ", "");
          // Convert to bytes
          g = Hex.fromHexToBytes(f);
          // Do XOR
          // System.out.println(Hex.hexEncoder(g));
          Words.XOR(g, lastCipherText);
          // Convert back to hex
          f = Hex.hexEncoder(g);

          text += f;
          lastCipherText = getBlocks()[i];
        }
      } catch (InvalidBlockSizeException e) {
        e.printStackTrace();
        System.exit(-1);
      }
    } 
    
    setProcessedString(text);
  }

  public String getIV() {
    return IV;
  }
}
