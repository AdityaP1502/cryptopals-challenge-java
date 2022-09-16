package Hash;

import Encoding.UnrecognizedEncodingException;

public class MAC {
  public static String sign(String message) throws UnrecognizedEncodingException {
    String key = "YELLOW SUBMARINE";
    String signedMessage = key + message;
    SHA1 hash = new SHA1(signedMessage, "ASCII");
    signedMessage = hash.digest();
    return signedMessage;
   }
  }
