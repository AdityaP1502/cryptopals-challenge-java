package PRNG;

public class RNGTest {
  public static void main(String[] args) {
    long x;
    MT19937 prng = new MT19937();
    
    for (int i = 0; i < 10; i++) {
      x = prng.nextInt();
      System.out.println(x);
    }
    
  }
}
