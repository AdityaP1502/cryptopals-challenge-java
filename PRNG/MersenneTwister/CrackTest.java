package PRNG.MersenneTwister;

public class CrackTest {
  public static void main(String[] args) {
    try {
      int seed = CrackTheSeed.findSeedTime();
      System.out.println(seed);
    } catch (InterruptedException e) {
      e.printStackTrace();
      System.exit(-1);
    }
  }
  
}
