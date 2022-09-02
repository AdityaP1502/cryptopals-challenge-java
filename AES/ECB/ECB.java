package AES.ECB;

import AES.AESCipher;
import AES.InvalidBlockSizeException;
import AES.Padding.PKCS;
import Encoding.EncodingFormat;
import Encoding.Hex;
import Encoding.UnrecognizedEncodingException;

public class ECB {
  private final byte[][] BLOCKS;
  private final String KEY;
  private String processedString;

  // AES in ECB mode
  public ECB(String text, String key, String encoding) {
    this.KEY = key;
    // change text into bytes
    BLOCKS = toBlocks(text, encoding);
  }

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
      if (remainder > 0) PKCS.pad(block, 16 - remainder);
      
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

  private void process(int mode, String encoding) {
    processedString = "";
    int totalBlock = BLOCKS.length;
    String x;

    for (int i = 0; i < totalBlock; i++) {
      try {
        x = (mode == 0) ? AESCipher.encrypt(BLOCKS[i], KEY) : AESCipher.decrypt(BLOCKS[i], KEY);
        processedString += x;
      } catch (InvalidBlockSizeException e) {
        e.printStackTrace();
        System.exit(-1);
      }
    }

    processedString = processedString.replace(" ", "");
    byte[] binaryData = Hex.fromHexToAscii(processedString);

    try {
      processedString = EncodingFormat.fromBytesToText(binaryData, encoding);
    } catch (UnrecognizedEncodingException e) {
      e.printStackTrace();
    }

    processedString.replace(" ", "");
  }

  public String encrypt(String encoding) {
    process(0, encoding);
    return processedString;
  }

  public String decrypt(String encoding) {
    process(1, encoding);
    // remove padding
    int pad = 0;
    while ((byte) processedString.charAt(processedString.length() - 1 - pad) < 16) {
      pad++;
    }
    return processedString.substring(0, processedString.length() - pad);
  }
}
