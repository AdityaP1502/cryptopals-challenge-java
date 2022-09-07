package PRNG.MersenneTwister;

public class CloneTest {
  public static void main(String[] args) {
    MT19937 someGenerator = new MT19937(12321);
    MT19937 cloneGenerator = ClonePRNG.clonePRNG(someGenerator);
    ClonePRNG.Test(someGenerator, cloneGenerator);

  }
}
