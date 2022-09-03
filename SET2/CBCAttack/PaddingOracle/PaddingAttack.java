package SET2.CBCAttack.PaddingOracle;

import Encoding.EncodingFormat;
import Encoding.UnrecognizedEncodingException;

public class PaddingAttack {
  private byte[] C2;
  private byte[] AESDecryptedBlock = new byte[16];
  private String[] ciphers;
  private String IV;
  private PaddingOracle oracle;
  public  byte[] tamperedCipher = {
    88, 88, 88, 88, 88, 88, 88, 88,
    88, 88, 88, 88, 88, 88, 88, 88
  };
  
  public PaddingAttack(String[] ciphers, String IV, PaddingOracle oracle) throws UnrecognizedEncodingException {
    this.ciphers = ciphers;
    this.IV = IV;
    this.oracle = oracle;
    C2 = EncodingFormat.convertToBytes(ciphers[ciphers.length - 1], "HEX");
  }

  public byte findByteWithValidPadding(int pos, String cipher) {
    String message;
    
  
    try {
      byte f = 0;
      int length = 16;

      for (int i = 0; i < pos;i++) {
        tamperedCipher[length - (i + 1)] = (byte) ((pos + 1) ^ AESDecryptedBlock[length - (i + 1)]);
      }
      
      for (int i = 0; i < 256; i++) {
        tamperedCipher[length - (pos + 1)] = f;
        message = EncodingFormat.fromBytesToText(tamperedCipher, "HEX") + cipher;
        if (oracle.receive(message, IV)) return f;
        f++;
      }
    } catch (UnrecognizedEncodingException e) {
      e.printStackTrace();
      System.exit(-1);
    }
    
    System.out.println("Error : Can't find bytes with correct padding");
    System.exit(-1);

    return -1;
  }

  public String decipherBlock(int blockPos) throws UnrecognizedEncodingException {
    byte f, g;
    int length = 16;
    
    byte[] C1 = EncodingFormat.convertToBytes(ciphers[blockPos - 1], "HEX");

    for (int i = 0; i < length; i++) {
      // find the byte that lead to a valid padding
      f = findByteWithValidPadding(i, ciphers[blockPos]);
      g = (byte) (f ^ (i + 1)); // byte after cipher decrypted by AES
      AESDecryptedBlock[length - (i + 1)] = g;
      f = (byte) (g ^ C1[length - (i + 1)]);
      C2[length - (i + 1)] = f;
    }

    String message = EncodingFormat.fromBytesToText(C2, "HEX");
    C2 = C1;
    return message;
  }

  public String attack() {
    String originalMessage = "";
    for (int i = ciphers.length - 1; i > 0; i--) {
      try {
        originalMessage = decipherBlock(i) + originalMessage;
        System.out.println(originalMessage);
      } catch (UnrecognizedEncodingException e) {
        e.printStackTrace();
        System.exit(-1);
      }
    }
    return originalMessage;
  }

  public void setCiphers(String[] ciphers) {
    this.ciphers = ciphers;
  }

  public void setIV(String iV) {
    IV = iV;
  }

  public void setOracle(PaddingOracle oracle) {
    this.oracle = oracle;
  }
}
