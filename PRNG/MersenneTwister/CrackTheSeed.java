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

  public static int findSeedTime() throws InterruptedException {
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

  public static int crackTheSeed(long number, int statePos) {
    // given some state at some pos 
    // brute force the seed that give the same value
    // seed is a 16 bit numbers
    int seedMaxRange = (1 << 16) - 1;
    MT19937 generator;
    long output = 0;
    for (int i = 0; i <= seedMaxRange; i ++) {
      generator = new MT19937(i);
      // tap the generator for statePos amount
      for (int j = 0; j < (statePos + 1); j++) {
        output = generator.nextInt();
      }

      // output would be state at statePos
      if (output == number) {
        // found the state
        return i;
      }
    }

    // seed isn't 16 bit 
    return -1;
  }
}
