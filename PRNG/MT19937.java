package PRNG;

public class MT19937 {
  private int seed;
  // constant
  public final long bitmask = 0xFFFFFFFFL;
  public final int w = 32;
  public final int n = 624;
  public final int m = 397;
  public final int r = 31;
  public final int a = 0x9908B0DF;
  public final int u = 11;
  public final int d = 0xFFFFFFFF;
  public final int s = 7;
  public final int b = 0x9D2C5680;
  public final int t = 15;
  public final int c = 0xEFC60000;
  public final int l = 18;
  public final int f = 1812433253;
  
  public final int LOWER_MASK = 0x7fffffff;
  public final int UPPER_MASK = 0x80000000;
  public final int DEFAULT_SEED = 5489;

  private int index = n + 1;
  private int[] MT = new int[n];


  public MT19937() {
    this.seed = DEFAULT_SEED;
    index = n + 1;
    seedMT();
  }

  public MT19937(int seed) {
    this.seed = seed;
    index = n + 1;
    seedMT();
  }
  
  private void seedMT() {
    // initialize generator from a seed
    long g;
    long longF, longMT;
    
    index = n;
    MT[0] = seed;
    for (int i = 1; i < n; i++) {
      longF = f;
      longMT = MT[i - 1] & bitmask; // get w lowest bits
      g = (longF * (longMT ^ (longMT >> (w - 2)))) + i;
      MT[i] = (int) g;
    }
  }

  private void twist() {

    for (int i = 0; i < n - 1; i++) {
      long x = ((MT[i] & UPPER_MASK) + (MT[(i + 1) % n] & LOWER_MASK)) & bitmask;
      long longA = a & bitmask;

      int xA = x % 2 == 1 ? (int) ((x >> 1) ^ longA) : (int) (x >> 1);  

      MT[i] = MT[(i + m) % n] ^ xA;
    }    

    index = 0;
  }
  private long extractNumber() {
    if (index == n) {
      twist();
    }

    long y = MT[index] & bitmask;
    long longD = d & bitmask;
    long longB = b & bitmask;
    long longC = c & bitmask;

    y = y ^ ((y >> u) & longD);
    y = y ^ ((y << s) & longB);
    y = y ^ ((y << t) & longC);
    y = y ^ (y >> l);
    index++;

    return y & bitmask;
  }

  public long nextInt() {
    // use long to simulate unsigned int
    // will return number between 0 and (2 ** 32 - 1)
    return extractNumber();
  }
}