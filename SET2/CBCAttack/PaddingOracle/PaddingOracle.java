package SET2.CBCAttack.PaddingOracle;

import java.util.Random;

import AES.AESKey;
import AES.CBC.CBC;
import AES.Padding.InvalidPaddingException;
import AES.Padding.PKCS;
import Encoding.UnrecognizedEncodingException;

public class PaddingOracle {
  private String[] messages = {
    "MDAwMDAwTm93IHRoYXQgdGhlIHBhcnR5IGlzIGp1bXBpbmc",
    "MDAwMDAxV2l0aCB0aGUgYmFzcyBraWNrZWQgaW4gYW5kIHRoZSBWZWdhJ3MgYXJlIHB1bXBpbic",
    "MDAwMDAyUXVpY2sgdG8gdGhlIHBvaW50LCB0byB0aGUgcG9pbnQsIG5vIGZha2luZw",
    "MDAwMDAzQ29va2luZyBNQydzIGxpa2UgYSBwb3VuZCBvZiBiYWNvbg",
    "MDAwMDA0QnVybmluZyAnZW0sIGlmIHlvdSBhaW4ndCBxdWljayBhbmQgbmltYmxl",
    "MDAwMDA1SSBnbyBjcmF6eSB3aGVuIEkgaGVhciBhIGN5bWJhbA",
    "MDAwMDA2QW5kIGEgaGlnaCBoYXQgd2l0aCBhIHNvdXBlZCB1cCB0ZW1wbw",
    "MDAwMDA3SSdtIG9uIGEgcm9sbCwgaXQncyB0aW1lIHRvIGdvIHNvbG8",
    "MDAwMDA4b2xsaW4nIGluIG15IGZpdmUgcG9pbnQgb2g",
    "MDAwMDA5aXRoIG15IHJhZy10b3AgZG93biBzbyBteSBoYWlyIGNhbiBibG93"
  };
  // messages in base64

  private final String KEY;
  
  public PaddingOracle() throws UnrecognizedEncodingException {
    KEY = AESKey.generateRandomKey("ASCII");
    // String key = "bae9e8ddf212132396060a8ff1a4da1e";
    // KEY = ASCII.ASCIIDecoder(Hex.fromHexToAscii(key));
  }

  public String[] send() {
    Random rnd = new Random();
    int pick = rnd.nextInt(11);
    String message = messages[pick];
    // String message = "Aditya Putra";

    String[] res = new String[2];
    // String IV = "1f265dfcef538e67a3c71230ec4c72cb";

    

    // CBC cbc = new CBC(message, KEY, IV, "ASCII", 0);
    CBC cbc = new CBC(message, KEY, "BASE64", 0);

    res[0] = cbc.encrypt();
    res[1] = cbc.getIV();

    return res;
  }

  public boolean receive(String message, String IV) {
    CBC cbc = new CBC(message, KEY, IV, "HEX", 1);
    String originalMessageHEX = cbc.decrypt();

    try {
      PKCS.removePadding(originalMessageHEX, "HEX");
    } catch (InvalidPaddingException e) {
      return false;
    }

    return true;

  }
}
