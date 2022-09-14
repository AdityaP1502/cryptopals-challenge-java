package SET4;

import AES.CBC.CBC;
import Encoding.Hex;

public class WebApp {
  public final static String DEFAULT_KEY = "bae9e8ddf212132396060a8ff1a4da1e";

  public String sendMessage(String message) {
    String keyInASCII = Hex.fromHexToAscii(DEFAULT_KEY);
    CBC cbc = new CBC(message, keyInASCII, DEFAULT_KEY, "ASCII", 0);

    return cbc.encrypt();
  }
  
  public boolean complianceCheck(byte ascii) {
    if (ascii < 0) {
      return false;
    }
    return true;
  }

  public String getMessage(String ciphertext) {
    String keyInASCII = Hex.fromHexToAscii(DEFAULT_KEY);
    CBC cbc = new CBC(ciphertext, keyInASCII, DEFAULT_KEY, "HEX", 1);
    String message = cbc.decrypt();
    message = Hex.fromHexToAscii(message);
    // check for ascii compliance
    boolean f;
    for (int i = 0; i < message.length(); i++) {
      f = complianceCheck((byte) message.charAt(i));
      if (!f) {
        // simulate sending an error message
        return "ERROR: " + message + " doesn't meet ASCII compliance check";
      }
    }

    return "SUCCESS: " + message;
  }
}
