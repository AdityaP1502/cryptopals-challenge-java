package PRNG.MersenneTwister;

public class Untemper {
  // Untemper
  // temper step
  // state = y0
  // y1 = y0 ^ (lshr(y0, u) AND d) mode 1
  // y2 = y1 ^ (rshr(y1, s) AND b) mode 2
  // y3 = y2 ^ (rshr(y2, t) AND c) mode 2
  // y4 = y3 ^ (lshr(y3, l)) mode 3
  // output = y4

  // Constant
  public static final int u = 11;
  public static final int s = 7;
  public static final int t = 15;
  public static final int l = 18;
  public static final long d = 0xFFFFFFFFL;
  public static final long b = 0x9D2C5680L;
  public static final long c = 0xEFC60000L;
  
  private static long reverseMode3(long output) {
    // only reverse left shift
    /* For 32 bit integer, after left shift n times
     * bit at position 31 -> 31 - n would be 0
     * therefore
     * output_i = y3_i XOR (y3_(i + n)) if i < 32 - n
     * else y3_i XOR 0
     */
    long f, h;
    // long f, g, h;
    long y3 = 0;
    long mask = 0x80000000L; // bit set only at bit pos 31
    long mask2 = mask;
    
    // init (phase 1)
    for (int i = 31; i > 31 - l; i--) {
      f = (output & mask);
      y3 += f;
      mask = mask >> 1;
    }

    // phase 2
    // g = y3 >> l;
    for (int i = 31 - l; i >= 0; i--) {
      // bit_i = bit_(i + l)
      f = output & mask;
      h = ((y3 & mask2) >> l)  & mask;
      y3 += (f ^ h);
      mask = mask >> 1;
      mask2 = mask2 >> 1;
    }

    return y3;
  }

  public static long reverseMode2(long output, long bitmask, int shiftCount) {
    // reverse and and 
    /* for right shift n times, bit at 0 - n will have value 0
     * therefore the first n bit input would be the same as output
     * and for the rest, bitInput_i = bitOutput_i XOR (bitInput_(i - n) AND bitBitmask_i)
    */
    long f, h;
    long input = 0;

    long mask = 0x1L;
    long mask2 = 0x1L;

    for (int i = 0; i < shiftCount; i++) {
      f = (output & mask);
      input += f;
      mask = mask << 1;
    }

    for (int i = shiftCount; i < 32; i++) {
      f = output & mask;
      h = ((input & mask2) << shiftCount) & (bitmask & mask);
      input += (f ^ h);
      mask = mask << 1;
      mask2 = mask2 << 1;
    }

    return input;
  }

  public static long reverseMode1(long output, long bitmask, int shiftCount) {
    // reverse and and 
    /* for right shift n times, bit at 0 - n will have value 0
     * therefore the first n bit input would be the same as output
     * and for the rest, bitInput_i = bitOutput_i XOR (bitInput_(i - n) AND bitBitmask_i)
    */
    
    // if bit pos at 11 at higher isn't set then output >> 11 would be 0
    // therefore input = output
    long threshold = 0x7FFL;
    if (output <= threshold) return output;
    
    long f, h;
    long input = 0;
    long mask = 0x80000000L;
    long mask2 = 0x80000000L;

    // init phase
    for (int i = 31; i > 31 - shiftCount; i--) {
      f = (output & mask);
      input += f;
      mask = mask >> 1;
    }

    // phase 2
    for (int i = 31 - shiftCount; i >= 0; i--) {
      f = output & mask;
      h = ((input & mask2) >> shiftCount) & (bitmask & mask);
      input += (f ^ h);
      mask = mask >> 1;
      mask2 = mask2 >> 1;
    }

    return input;
  }
  public static int reverse(long output) {
    long y3 = reverseMode3(output); 
    long y2 = reverseMode2(y3, c, t); 
    long y1 = reverseMode2(y2, b, s); 
    long y0 = reverseMode1(y1, d, u);
    // testing
    long input = y0;
    input = input ^ ((input >> u) & d);
    input = input ^ ((input << s) & b);
    input = input ^ ((input << t) & c);
    input = input ^ (input >> l);

    // check
    System.out.println("Output is: " + output + "\nFrom input: " + y0 + ",output is: " + input);
    System.out.println("Equal: " + (input == output));
    return (int) y0;
  }

}
