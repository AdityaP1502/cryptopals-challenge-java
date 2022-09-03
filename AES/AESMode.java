package AES;

import AES.Padding.PKCS;

import Encoding.UnrecognizedEncodingException;;

public abstract class AESMode {
  private int mode;
  private String processedString;
  private final String KEY;
  private byte[][] blocks;

  public AESMode(String key, int mode) {
    this.KEY = key;
    this.mode = mode;
  }

  public AESMode(String text, String key, String encoding, int mode) {
    this.KEY = key;
    this.mode = mode;
    this.blocks = toBlocks(text, encoding);
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
      length += mode == 0  ? 16 - remainder : 0;
      
      block = new byte[length];
      // copy data from temp to block
      for (int i = 0; i < temp.length; i++) {
        block[i] = temp[i];
      }
      // Padding
      // if can't be divided, add dummy padding block
      if (mode == 0) PKCS.pad(block, 16 - remainder);
      
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

  public abstract void process();

  public String encrypt() {
    process();
    return processedString;
  } 

  public String decrypt() {
    process();
    return processedString;
  }

  public void setMessage(String text, String encoding) {
    blocks = toBlocks(text, encoding);
  }

  public byte[][] getBlocks() {
    return blocks;
  }

  public String getKEY() {
    return KEY;
  }

  public int getMode() {
    return mode;
  }

  public String getProcessedString() {
    return processedString;
  }

  public void setMode(int mode) {
    this.mode = mode;
  }

  public void setProcessedString(String processedString) {
    this.processedString = processedString;
  }

}
