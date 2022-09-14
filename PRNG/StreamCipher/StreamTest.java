package PRNG.StreamCipher;

import Encoding.UnrecognizedEncodingException;

// import java.util.Random;

import Encoding.ASCII;
import Encoding.Hex;

public class StreamTest {
  public static void main(String[] args) throws UnrecognizedEncodingException {
    // Random rnd = new Random();
    String message = "KKKKKKKKKKAAAAAAAAAAAAAA";

    // for (int i = 0; i < 10 + rnd.nextInt(40); i++) {
      // message = (char) rnd.nextInt(256) + message;
    // }

    Cipher stream = new Cipher(1321, message, "ASCII");

    String encryptString = stream.encrypt();
    stream.setText(encryptString, "HEX");

    String originalMessage = stream.decrypt();
    originalMessage = ASCII.ASCIIDecoder(Hex.fromHexToBytes(originalMessage));

    System.out.println(encryptString);
    System.out.println(originalMessage);
    
    // break the seed
    int seed = stream.breakSeed();
    System.out.println(seed);
  }
}
