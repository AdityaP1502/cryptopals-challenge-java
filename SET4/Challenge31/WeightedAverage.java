package SET4.Challenge31;

public class WeightedAverage {
  // Average to clean noise in timing
  private static int range = 10;
  private static int[] bin;
  private static long[] sum_bin;
  private static double[] weights;

  private static void setWeights(long min, long max, int n) {
    long range = (long) Math.pow((max - min), 2) + 1;
    weights = new double[n];
    long sum = 0;
    // set weights arr
    for (int i = 0; i < n; i++) {
      if (bin[i] == 0) continue;
      weights[i] = range - (long) Math.pow((max - bin[i]), 2);
      sum += weights[i];
    }
    // normalize weights
    for (int i = 0; i < n; i++) {
      weights[i] /= sum;;
    }
    
  }

  private static long findMax() {
    long max = 0;
    for (int i = 0; i < bin.length; i++) {
      max = Math.max(bin[i], max);
    }

    return max;
  }

  private static long findMin() {
    long min = bin[0];
    for (int i = 0; i < bin.length; i++) {
      if (bin[i] > 0 && min > bin[i]) {
        min = bin[i];
      }
    }
    return min;
  }

  public static long cleanNoise(long[] timings, long maxTimings) {
    // remove noise from timing
    int n = (int) Math.ceilDiv(maxTimings, range) + 1;
  
    bin = new int[n];
    sum_bin = new long[n];

    long max, min;
    long average = 0;
    int idx;

    // set bin
    for (long timing : timings) {
      idx = (int) Math.floorDiv(timing, range);
      bin[idx] += 1;
      sum_bin[idx] += timing;
    }
    
    // find max and min
    max = findMax();
    min = findMin();

    // set weights
    setWeights(min, max, n);

    // calculate average
    for (int i = 0; i < n; i++) {
      if (bin[i] == 0) continue;
      average += (sum_bin[i] / bin[i]) * weights[i];
    }
    return average;
  }
}
