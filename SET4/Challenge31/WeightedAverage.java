package SET4.Challenge31;

public class WeightedAverage {
  // Average to clean noise in timing
  private static int range = 10;
  private static int[] bin;
  private static long[] avg_bin;
  private static double[] weights;

  private static long[] averageBinValue(int maxBin) {
    long maxVal = 0, minVal = -1, maxBinVal = 0;

    for (int i = 0; i < avg_bin.length; i++) {
      if (bin[i] == 0) continue;
      avg_bin[i] = avg_bin[i] / bin[i];
      maxVal = Math.max(avg_bin[i], maxVal);
      minVal = minVal < 0 ? avg_bin[i] : Math.min(avg_bin[i], minVal);
      if (bin[i] == maxBin) maxBinVal = avg_bin[i];
    }

    maxVal = Math.abs(maxVal - maxBinVal);
    minVal = Math.abs(minVal - maxBinVal);
    long[] val = {Math.max(maxVal, minVal), maxBinVal};
    return val;
  }

  private static long calculateDistanceSquared(long a, long b) {
    return (long) Math.pow(b - a, 2);
  }

  private static long calculateDistanceRoot(long a, long b) {
    return (long) Math.sqrt(Math.abs(a - b));
  }

  private static void setWeights(long min, long max, long maxDistance, long maxBinVal, int n) {
    long range = calculateDistanceSquared(max, min) + calculateDistanceRoot(maxBinVal, maxDistance) + 1;
    weights = new double[n];
    long sum = 0;
    // set weights arr
    for (int i = 0; i < n; i++) {
      if (bin[i] == 0) continue;
      weights[i] = range - (calculateDistanceSquared(max, bin[i]) + calculateDistanceRoot(maxBinVal, avg_bin[i]));
      sum += weights[i];
    }
    // normalize weights
    for (int i = 0; i < n; i++) {
      weights[i] /= sum;;
    }
    
  }

  private static int findMax() {
    int max = 0;
    for (int i = 0; i < bin.length; i++) {
      max = Math.max(bin[i], max);
    }

    return max;
  }

  private static int findMin() {
    int min = -1;
    for (int i = 0; i < bin.length; i++) {
      if (bin[i] > 0) min = min < 0 ? bin[i] : Math.min(bin[i], min);
    }
    return min;
  }

  public static long cleanNoise(long[] timings, long maxTimings) {
    // remove noise from timing
    long[] f;
    int n = (int) Math.ceilDiv(maxTimings, range) + 1;
  
    bin = new int[n];
    avg_bin = new long[n];

    int max, min;
    long maxDistance, maxBinVal;
    long average = 0;
    int idx;

    // set bin
    for (long timing : timings) {
      idx = (int) Math.floorDiv(timing, range);
      bin[idx] += 1;
      avg_bin[idx] += timing;
    }
    
    // find max and min bin
    max = findMax();
    min = findMin();

    // find bin average value and its extreme
    f = averageBinValue(max);
    maxDistance = f[0];
    maxBinVal = f[1];

    // set weights
    setWeights(min, max, maxDistance, maxBinVal, n);

    // calculate average
    for (int i = 0; i < n; i++) {
      if (bin[i] == 0) continue;
      average += (avg_bin[i]) * weights[i];
    }
    return average;
  }
}
