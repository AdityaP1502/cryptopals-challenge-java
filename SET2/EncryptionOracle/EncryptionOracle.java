package SET2.EncryptionOracle;

import java.util.Random;

import AES.InvalidBlockSizeException;
import AES.CBC.CBC;
import AES.CTR.CTR;
import AES.ECB.DetectECB;
import AES.ECB.ECB;
import Encoding.ASCII;
import Encoding.Base64;
import Encoding.EncodingFormat;
import Encoding.Hex;
import Encoding.UnrecognizedEncodingException;
import SET2.ECBAttack.CutAndPaste.Parser;

public class EncryptionOracle {
  final static String unknownString = "Um9sbGluJyBpbiBteSA1LjAKV2l0aCBteSByYWctdG9wIGRvd24gc28gbXkgaGFpciBjYW4gYmxvdwpUaGUgZ2lybGllcyBvbiBzdGFuZGJ5IHdhdmluZyBqdXN0IHRvIHNheSBoaQpEaWQgeW91IHN0b3A/IE5vLCBJIGp1c3QgZHJvdmUgYnkK";
  public final static String DEFAULT_KEY = "bae9e8ddf212132396060a8ff1a4da1e";
  public final static String DEFAULT_NONCE = "191f87ef86a15479";
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

    ECB ecb = new ECB(text, keyInASCII, encoding, 0);
    return ecb.encrypt();
  }

  public static String encryptECB(String text, String encoding, String key) {
    // key in hex
    String keyInASCII = ASCII.ASCIIDecoder(Hex.fromHexToAscii(key));
    
    // String unknownStringDecoded = ASCII.ASCIIDecoder(Base64.fromBase64ToAscii(unknownString));
    // // String unknownStringDecoded = "AAAABBBBCCCCDDDD"; 
    // text += unknownStringDecoded;

    ECB ecb = new ECB(text, keyInASCII, encoding, 0);
    return ecb.encrypt();
  }

  public static String encryptECBWithPrefix(String input, String encoding) {
    // System.out.println(PREFIX.length());
    // System.out.println(ASCII.asciiToHex(ASCII.ASCIIEncoder(PREFIX)));
    // text = input || secret
    String message = PREFIX + input;
    return encryptECB(message, "ASCII", DEFAULT_KEY);
  } 
  
  public static String[] encyptCBC(String input) {
    String[] res = new String[2];
    // input is assumed to used ASCII
    String keyInASCII = ASCII.ASCIIDecoder(Hex.fromHexToAscii(DEFAULT_KEY));

    // prefix and suffix message
    String prefix = "comment1=cooking%20MCs;userdata=";
    String suffix = ";comment2=%20like%20a%20pound%20of%20bacon";

    // sanitize user input
    input = Parser.sanitizeInput(input);
    String message = prefix + input + suffix;

    // encrypt
    String IV = "1f265dfcef538e67a3c71230ec4c72cb";
    // CBC cbc = new CBC(message, keyInASCII, "ASCII");
    CBC cbc = new CBC(message, keyInASCII, IV, "ASCII", 0);
    res[0] = cbc.encrypt();
    res[1] = cbc.getIV();

    return res;
  }
  
  public static String encryptCTR(String input) throws UnrecognizedEncodingException, InvalidBlockSizeException {
    // input is assumed to used ASCII
    // prefix and suffix message
    String prefix = "comment1=cooking%20MCs;userdata=";
    String suffix = ";comment2=%20like%20a%20pound%20of%20bacon";

    // sanitize user input
    input = Parser.sanitizeInput(input);
    String message = prefix + input + suffix;

    // encrypt
    String keyInASCII = ASCII.ASCIIDecoder(Hex.fromHexToAscii(DEFAULT_KEY));
    CTR ctr = new CTR(message, "ASCII", keyInASCII, DEFAULT_NONCE);
    
    return ctr.encrypt();
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

