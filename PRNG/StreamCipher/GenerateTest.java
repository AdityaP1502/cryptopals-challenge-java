package PRNG.StreamCipher;

public class GenerateTest {
  public static void main(String[] args) {
    int seed = 32001;
    KeystreamGenerator keyGenerator = new KeystreamGenerator(seed);
    for (int i = 0; i < 1000; i++) {
      System.out.println(keyGenerator.nextByte());
    }
  }
}
