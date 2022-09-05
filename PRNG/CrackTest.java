package PRNG;

public class CrackTest {
  public static void main(String[] args) {
    try {
      int seed = CrackTheSeed.findSeed();
      System.out.println(seed);
    } catch (InterruptedException e) {
      e.printStackTrace();
      System.exit(-1);
    }
  }
  
}
