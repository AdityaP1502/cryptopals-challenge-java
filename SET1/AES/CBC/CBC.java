package SET1.AES.CBC;

import Encoding.Hex;
import Encoding.UnrecognizedEncodingException;

import SET1.AES.AESCipher;
import SET1.AES.InvalidBlockSizeException;
import SET1.AES.Words;
import SET1.AES.Padding.PKCS;

public class CBC {
  private String IV;
  private String KEY;
  private String processedString;
  private byte[][] BLOCKS;

  public byte[][] toBlocks(String text, String encoding) {
    int length, remainder;
    // convert text into bytes
    byte[] temp;
    byte[] block = null;
    byte[][] blocks = null;

    try {
      temp = Encoding.EncodingFormat.convertToBytes(text, encoding);
      
      // padding length
      length = temp.length;
      remainder = length % 16;
      length += remainder > 0 ? (16 - remainder) : 0;
      
      block = new byte[length];
      // copy data from temp to block
      for (int i = 0; i < temp.length; i++) {
        block[i] = temp[i];
      }
      // Padding
      PKCS.pad(block, 16 - remainder);

      blocks = new byte[block.length / 16][16];
      for (int i = 0; i < blocks.length; i++) {
        for (int j = 0; j < 16; j++) {
          blocks[i][j] = block[16 * i + j];
        }
      }

    } catch (UnrecognizedEncodingException e) {
      e.printStackTrace();
      System.exit(-1);
    }
    return blocks;
  }

  public CBC(String text, String key, String IV, String encoding) {
    this.KEY = key;
    this.IV = IV;
    // change text into bytes
    BLOCKS = toBlocks(text, encoding);
  }

  public void process(int mode) {
    processedString = "";
    byte[] lastCipherText = Hex.fromHexToAscii(IV);
    int totalBlock = BLOCKS.length;
    String f;

    for (int i = 0; i < totalBlock; i++) {
      try {
        if (mode == 0) {
          // Encryptoin
          Words.XORWord(BLOCKS[i], lastCipherText);
          f = AESCipher.encrypt(BLOCKS[i], KEY).replace(" ", "");
          processedString += f;
          lastCipherText = Hex.fromHexToAscii(f);
        }
        else {
          // Decryption
          byte[] g;
          f = AESCipher.decrypt(BLOCKS[i], KEY).replace(" ", "");
          // Convert to bytes
          g = Hex.fromHexToAscii(f);
          // Do XOR
          Words.XORWord(g, lastCipherText);
          // Convert back to hex
          f = Hex.hexEncoder(g);

          processedString += f;
          lastCipherText = BLOCKS[i];
        }
      } catch (InvalidBlockSizeException e) {
        e.printStackTrace();
        System.exit(-1);
      }
    }    
  }

  public String encrypt() {
    process(0);
    return processedString;
  } 

  public String decrypt() {
    process(1);
    return processedString;
  }
}
