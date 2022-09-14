package AES.CTR;

import AES.InvalidBlockSizeException;
import Encoding.ASCII;
import Encoding.Hex;
import Encoding.UnrecognizedEncodingException;

public class CTRTest {
  public static void main(String[] args) {
    String nonce = "0000000000000000";
    String KEY = "YELLOW SUBMARINE";
    String text = "L77na/nrFsKvynd6HzOoG7GHTLXsTVu9qvY/2syLXzhPweyyMTJULu/6/kXX0KSvoOLSFQ";
    String message = "Hello, my name is I Made Aditya Putra Jaya Diwangsa";
    try {
      CTR ctr = new CTR(text, "BASE64", KEY, nonce);
      String originalMessage = ctr.decrypt();
      originalMessage = ASCII.ASCIIDecoder(Hex.fromHexToBytes(originalMessage));
      System.out.println(originalMessage);

      ctr.setMessage(message, "ASCII");
      String gibberish = ctr.encrypt();
      ctr.setMessage(gibberish, "HEX");
      originalMessage = ctr.decrypt();
      originalMessage = ASCII.ASCIIDecoder(Hex.fromHexToBytes(originalMessage));
      System.out.println(originalMessage);
    
    } catch (UnrecognizedEncodingException | InvalidBlockSizeException e) {
      System.exit(-1);
      e.printStackTrace();
    }
  }
}
