package Hash;

import Encoding.UnrecognizedEncodingException;

public class MAC {
  public static String sign(String message, String type) throws UnrecognizedEncodingException {
    String key = "YELLOW SUBMARINE";
    String signedMessage = key + message;
    
    switch (type) {
      case "MD4":
        MD4 hashMD4 = new MD4(signedMessage, "ASCII");
        signedMessage = hashMD4.digest();
        break;
      case "SHA1":
      default:
        SHA1 hashSHA = new SHA1(signedMessage, "ASCII");
        signedMessage = hashSHA.digest();
        break;
    }
    
    return signedMessage;
   }
  }
