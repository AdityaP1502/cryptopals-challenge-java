package PRNG.MersenneTwister;

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

  public static MT19937 clone(int MT[]) {
    MT19937 mt = new MT19937();
    // make index to n
    mt.index = mt.n;
    mt.MT = MT;
    return mt;
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
  private long temper(long input) {
    long longD = d & bitmask;
    long longB = b & bitmask;
    long longC = c & bitmask;

    // temper
    input = input ^ ((input >> u) & longD);
    input = input ^ ((input << s) & longB);
    input = input ^ ((input << t) & longC);
    input = input ^ (input >> l);

    return input;
  }

  private long extractNumber() {
    if (index == n) {
      twist();
    }

    long input = MT[index] & bitmask;
    long output = temper(input);
    index++;

    return output & bitmask;
  }

  public long nextInt() {
    // use long to simulate unsigned int
    // will return number between 0 and (2 ** 32 - 1)
    return extractNumber();
  }

  public void reset(int seed) {
    // change the seed and reset the state
    this.seed = seed;
    seedMT();
  }
}
