package PRNG.MersenneTwister;

public class ClonePRNG {

  public static MT19937 clonePRNG(MT19937 generator) {
    long output;
    int n = 624;
    int[] generatorState = new int[n];
    for (int i = 0; i < n; i++) {
      output = generator.nextInt();
      generatorState[i] = Untemper.reverse(output);
    }

    return MT19937.clone(generatorState);
  }

  public static void Test(MT19937 original, MT19937 clone) {
    long x, y;
    for (int i = 0; i < 624; i++) {
      x = original.nextInt();
      y = clone.nextInt();
      System.out.println("Original Generator: " + x);
      System.out.println("cloned Generator: " + y);
      System.out.println("is true: " + (x == y));
    } 
  }
}
