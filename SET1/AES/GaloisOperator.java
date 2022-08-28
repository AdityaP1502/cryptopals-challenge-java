package SET1.AES;

import static java.lang.Math.pow;

public class GaloisOperator {
  public static byte add(byte a, byte b) {
    // addition is just XOR in GF(2^8)
    return (byte) (a ^ b);
  }
  public static int bitLength(int a) {
    // get the length of binary string of a
    int k = 0;
    while (pow(2, k) <= a) {
      // find smallest k such that 2 ^ k > a
      k++;
    }
    return k;
  }
  public static int polyMultiply(byte a, byte b) {
    // multiplicatin of polynomial
    // b is elemnt from mixColumns transformation
    // b is either 1, 2, 3 (for encryption) and 9, 11, 13, 14 (for decryption)
    int res = 0;
    int f;
    int bitmask = 255; // all 8 bit set to 1
    switch (a) {
      case 1:
        res = b & bitmask;
        break;
      case 2:
        // conversion for byte with first bit set to 1
        // to int make unneeded leading 1
        f = b & bitmask;
        res = (f << 1);
        break;
      case 3:
        f = b & bitmask;
        res = ((f << 1) ^ f);
        break;
      case 9:
        f = b & bitmask;
        res = ((f << 3) ^ f);
        break;
      case 11:
        f = b & bitmask;
        res = (f << 2);
        res = (res ^ f);
        res = ((res << 1) ^ f);
        break;
      case 13:
        f = b & bitmask;
        res = ((f << 1) ^ f);
        res = (res << 2);
        res = (res ^ f);
        break;
      case 14:
        f = b & bitmask;
        res = ((f << 1) ^ f);
        res = (res << 1) ^ f;
        res = (res << 1);
        break;
    }
    return res;
  }

  public static byte multiplicationModulo(int a) {
    int magicNumber = 283; // 100011011

    if (a < 256) {
      // can be reprsented in GF(2^8) -> has length < 9
      return (byte) a;
    }

    int m = 9; // irreducible bit length
    int n = bitLength(a); // a bit length (min length is 9)
    int bitmask = 1 << (n - 1); //  to check whether bit at some pos is 0
    magicNumber = magicNumber << (n - 9); // promote magic number to have the same bit pos as a

    for (int i = 0; i <= (n - m); i++) {
      if ((a & bitmask) != 0) {
        // only do xor when bit at that pos isn't 0
        a = a ^ magicNumber;
      }
      magicNumber = magicNumber >> 1; // move 1 position to the right
      bitmask = bitmask >> 1;
    }

    // a is guarenteed to be in GF(2^8) -> only use 8 bit
    return (byte) a;
  }
}
