package PRNG.StreamCipher;

import Encoding.Hex;

public class GenerateTest {
  public static void main(String[] args) {
    int seed = 32001;
    KeystreamGenerator keyGenerator = new KeystreamGenerator(seed);
    for (int i = 0; i < 1000; i++) {
      byte[] keystreamBytes = keyGenerator.nextKeystream();
      String keystream = Hex.hexEncoder(keystreamBytes);
      System.out.println(keystream);
    }
  }
}
