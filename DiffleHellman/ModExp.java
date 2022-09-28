package DiffleHellman;

import java.math.BigInteger;

public class ModExp {
  public static long modExp(long base, long power, long div) {
    // Using the fact that a * b = [a mod m * b mod m] mod m
    // Using exponentiation by squaring to get O(log n) running time

    long resMod = 1, resModA = base % div, b = power;

    while (b > 0) {
      if (b % 2 == 1) {
        // res = res * a
        resMod = (resMod * resModA) % div;
      }

      // a = a * a
      resModA = (resModA * resModA) % div;
      b  = b >> 1;
    }

    return resMod;
  }

  public static int modExp(int base, int power, int div) {
    // Using the fact that a * b = [a mod m * b mod m] mod m
    // Using exponentiation by squaring to get O(log n) running time

    int resModA = base % div, b = power, resMod = 1;

    while (b > 0) {
      if (b % 2 == 1) {
        // res = res * a
        resMod = (resMod * resModA) % div;
      }

      // a = a * a
      resModA = (resModA * resModA) % div;
      b  = b >> 1;
    }

    return resMod;
  }

  public static BigInteger modExp(BigInteger base, BigInteger power, BigInteger div) {
    // bignum constant
    BigInteger f = new BigInteger("0"), g = new BigInteger("2"), h = new BigInteger("1");

    BigInteger resModA = base.remainder(div);
    BigInteger b = power;
    BigInteger resMod = h;

   
    while (b.compareTo(f) >= 1) {
      if (b.remainder(g).equals(h)) {
        // res = res * a
        resMod = (resMod.multiply(resModA)).remainder(div);
      }

      // a = a * a
      resModA = resModA.multiply(resModA).remainder(div);
      b  = b.shiftRight(1);
    }

    return resMod;
  }
}
