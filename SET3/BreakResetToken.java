package SET3;

import Encoding.EncodingFormat;
import Encoding.UnrecognizedEncodingException;
import PRNG.MersenneTwister.MT19937;

public class BreakResetToken {
  public static void main(String[] args) throws UnrecognizedEncodingException {
    String password = "AAAAAAAAAAAAAA";

    long initialTime = System.currentTimeMillis();
    ResetToken generator = new ResetToken(password);
    MT19937 mt = new MT19937();
    long finalTime = System.currentTimeMillis();

    String token = generator.getToken();

    // find known output value\
    byte[] blocks = EncodingFormat.convertToBytes(token, "HEX");
    long x;
    int randomLength = (blocks.length - 14);
    int startPos = randomLength + randomLength % 4;
    int statePos = startPos / 4;
    int bitmask = (1 << 8) - 1;
    long PRNGNumber = 0;

    // get the tempered value
    for (int i = 0; i < 4; i++) {
      x = (blocks[startPos + i] ^ ((byte) 'A')) & bitmask;
      x = x << (8 * (3 - i));
      PRNGNumber += x;
    }

    long output = 0;
    for (long i = initialTime; i <= finalTime; i++) {
      mt.reset((int) i);
      for (int j = 0; j <= statePos; j++) {
        output = mt.nextInt();
      }

      if (output == PRNGNumber) {
        System.out.println("PRNG used time as seed");
        System.out.println("Seed in 32 bit: " + i);
        return;
      }
    }
    System.out.println("PRNG not used time as seed");
  }
}
