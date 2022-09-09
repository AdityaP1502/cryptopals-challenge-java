package PRNG.StreamCipher;
import PRNG.MersenneTwister.MT19937;

public class KeystreamGenerator {
  // Generate Keystream using MT19937
  private final MT19937 generator;
  private byte[] keystream;

  public KeystreamGenerator(int seed) {
    // seed is an 16 bit number
    generator = new MT19937(seed);
    keystream = new byte[16];
  }

  public void setRandom() {
    long temp, bitmask;
    // divide 32 bits temp into 4, 8 bit data
    for (int j = 0; j < 4; j++) {
      temp = generator.nextInt();
      bitmask = 255L;
      for (int i = 3; i >= 0; i--) {
        keystream[4*j + i] = (byte) ((temp & bitmask) >> ((3 - i) * 8));
        bitmask = bitmask << 8;
      }
    }
  }

  public byte[] nextKeystream() {
    setRandom();
    return keystream;
  }

  public void reset(int seed) {
    generator.reset(seed); // reset generator state
    keystream = new byte[16]; // clear keystream output
  }
}
