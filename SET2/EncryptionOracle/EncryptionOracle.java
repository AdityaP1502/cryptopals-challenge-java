package SET2.EncryptionOracle;

import java.util.Random;

import Encoding.ASCII;
import Encoding.Base64;
import Encoding.EncodingFormat;
import Encoding.Hex;
import Encoding.UnrecognizedEncodingException;
import SET1.AES.AESKey;
import SET1.AES.CBC.CBC;
import SET1.AES.ECB.DetectECB;
import SET1.AES.ECB.ECB;

public class EncryptionOracle {
  final static String unknownString = "Um9sbGluJyBpbiBteSA1LjAKV2l0aCBteSByYWctdG9wIGRvd24gc28gbXkgaGFpciBjYW4gYmxvdwpUaGUgZ2lybGllcyBvbiBzdGFuZGJ5IHdhdmluZyBqdXN0IHRvIHNheSBoaQpEaWQgeW91IHN0b3A/IE5vLCBJIGp1c3QgZHJvdmUgYnkK";
  public final static String DEFAULT_KEY = "bae9e8ddf212132396060a8ff1a4da1e";
  public final static String PREFIX = getPrefixString("ASCII");

  public static String getPrefixString(String encoding) {
    Random rnd = new Random();
    String prefix = "";
    int prefixLength = 36; // for debugging 
    // int prefixLength = rnd.nextInt(40) + 16;
    byte[] prefixString = new byte[prefixLength];

    for (int i = 0; i < prefixLength; i++) {
      rnd.nextBytes(prefixString);
    }

    try {
      prefix = EncodingFormat.fromBytesToText(prefixString, encoding);
    } catch (UnrecognizedEncodingException e) {
      System.exit(-1);
      e.printStackTrace();
    }

    return prefix;
  }

  public static String encryptECB(String text, String encoding) {
    // encode text under default key
    // convert HEX into ASCII
    String keyInASCII = ASCII.ASCIIDecoder(Hex.fromHexToAscii(DEFAULT_KEY));

    String unknownStringDecoded = ASCII.ASCIIDecoder(Base64.fromBase64ToAscii(unknownString));
    // String unknownStringDecoded = "AAAABBBBCCCCDDDD"; 
    text += unknownStringDecoded;

    ECB ecb = new ECB(text, keyInASCII, encoding);
    return ecb.encrypt("HEX");
  }

  public static String encryptECB(String text, String encoding, String key) {
    // key in hex
    String keyInASCII = ASCII.ASCIIDecoder(Hex.fromHexToAscii(key));
    
    String unknownStringDecoded = ASCII.ASCIIDecoder(Base64.fromBase64ToAscii(unknownString));
    // String unknownStringDecoded = "AAAABBBBCCCCDDDD"; 
    text += unknownStringDecoded;

    ECB ecb = new ECB(text, keyInASCII, encoding);
    return ecb.encrypt("HEX");
  }

  public static String encryptECBWithPrefix(String input, String encoding) {
    // System.out.println(PREFIX.length());
    // System.out.println(ASCII.asciiToHex(ASCII.ASCIIEncoder(PREFIX)));
    // text = input || secret
    String message = PREFIX + input;
    return encryptECB(message, "ASCII", DEFAULT_KEY);
    
  } 

  public static String encrypt(String text, String encoding) throws UnrecognizedEncodingException {
    String encryptedString;
    String key = AESKey.generateRandomKey("ASCII");
    Random rnd = new Random();

    if (rnd.nextBoolean()) {
      // do ecb
      ECB ecb = new ECB(text, key, encoding);
      encryptedString = ecb.encrypt("HEX");
    } else {
      // do cbc
      String IV = AESKey.generateRandomKey("HEX");
      CBC CBC = new CBC(text, key, IV, encoding);
      encryptedString = CBC.encrypt();
    }
    // // do ecb
    // System.out.println("ECB");
    // ECB ecb = new ECB(text, key, encoding);
    // encryptedString = ecb.encrypt("HEX");
    return encryptedString;
  }

  public static String check(String text) {
    String x = DetectECB.detect(text);
    System.out.println(x);
    if (x.equals("")) {
      // not ecb
      return "CBC";
    } else {
      return "ECB";
    }
  }
}

