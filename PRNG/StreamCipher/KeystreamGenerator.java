package PRNG.StreamCipher;
import PRNG.MersenneTwister.MT19937;

public class KeystreamGenerator {
  // Generate Keystream using MT19937
  private final MT19937 generator;
  private byte[] randomBytes;
  private int count;

  public KeystreamGenerator(int seed) {
    generator = new MT19937(seed);
    randomBytes = new byte[4];
  }

  public void setRandom() {
    long temp = generator.nextInt();
    System.out.println("32 bit is: " + temp);
    long bitmask = 255L;
    // divide 32 bits temp into 4, 8 bit data
    for (int i = 3; i >= 0; i--) {
      randomBytes[i] = (byte) ((temp & bitmask) >> ((3 - i) * 8));
      bitmask = bitmask << 8;
    }
  }

  public byte nextByte() {
    if (count == 0) {
      // set randomBytes
      setRandom();
    }

    byte output = randomBytes[count];

    count++; // update count
    if (count == 4) count = 0; // reset the state

    return output;
  }
}
