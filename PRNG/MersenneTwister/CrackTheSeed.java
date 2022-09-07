package PRNG.MersenneTwister;

import java.util.Random;

public class CrackTheSeed {
  public static long RNGRoutine() throws InterruptedException {
    Random rnd = new Random();
    int sleepTime = 40 + rnd.nextInt(961);
    Thread.sleep(sleepTime * 1000);
    MT19937 mt = new MT19937((int) System.currentTimeMillis());
    sleepTime = 40 + rnd.nextInt(961);
    Thread.sleep(sleepTime * 1000);
    return mt.nextInt();
  }

  public static int findSeed() throws InterruptedException {
    long initialTime = System.currentTimeMillis();
    int seed = -1;
    MT19937 mt = new MT19937();
    long output = RNGRoutine();
    long finalTime = System.currentTimeMillis();
    for (long i = initialTime; i <= finalTime; i++) {
      seed = (int) i;
      mt.reset(seed);
      if (mt.nextInt() == output) {
        System.out.println("Found the seed");
        // found the seed
        break;
      }
    }

    return seed;

  }
}
