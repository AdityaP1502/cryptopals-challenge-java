package Hash;

import Encoding.ASCII;
import Encoding.Hex;

public class SHA1 {
  private final int INITIALIZE_VAR0 = 0x67452301;
  private final int INITIALIZE_VAR1 = 0xEFCDAB89;
  private final int INITIALIZE_VAR2 = 0x98BADCFE;
  private final int INITIALIZE_VAR3 = 0x10325476;
  private final int INITIALIZE_VAR4 = 0xC3D2E1F0;
  private long length;
  private byte[][] chunks;

  public SHA1(String message) {
    // message is in ASCII
    message = ASCII.asciiToHex(message);
    createChunks(preprocess(message));
  }

  private String preprocess(String message) {
    // message in hex
    int length = message.length() * 4; // local length
    this.length = length;

    if (length % 8 == 0) {
      message += "80";
      length += 8;
    }

    int n = length <= 448 ? (448 - length) : 448 + (512 - length);
    n /= 4; // each hex character consist of 4 bit 

    // append 0 until message % 512 = 448
    for (int i = 0; i < n; i++) {
      message += '0';
    }
    
    // add message length as 64 bit big - endian number
    String lengthHex = Hex.hexEncoder(this.length);
    message = message + lengthHex;

    this.length = message.length();
    return message;
  }

  private void createChunks(String hex) {
    // divide bits into 512 bit chunks
    // 512 bit = 64 byte
    int n = hex.length() / 128;
    byte[][] chunks = new byte[n][64];
    byte[] bytes = Hex.fromHexToBytes(hex);

    for (int i = 0; i < bytes.length; i++) {
      chunks[i / 64][i % 64] = bytes[i];
    }

    this.chunks = chunks;
  }

  private int[] breakChunks(byte[] chunk) {
    // chunks consist of 64 bytes
    // break chunks into 32 bit word -> 4 bytes
    int n = 80;
    int bitmask = 0xFF;
    int[] blocks = new int[n];
    int temp;
    int count = 0;
    for (int i = 0; i < chunk.length; i += 4) {
      // concate 4 bytes into one word
      temp = ((chunk[i] & bitmask) << 24) + ((chunk[i + 1] & bitmask) << 16) + ((chunk[i + 2] & bitmask) << 8)  + (chunk[i + 3] & bitmask);
      blocks[count] = temp;
      count++;
    }

    return blocks;
  }

  public static int lr(int word, int count) {
    int bitmask = (1 << (count)) - 1;
    return (word << count) + ((word >> (32 - count)) & bitmask);
  }

  private void fill(int[] blocks) {
    int f;
    for (int i = 16; i < 80; i++) {
      f = blocks[i - 3] ^ blocks[i - 8] ^ blocks[i - 14] ^ blocks[i - 16];
      // do left rotate by 1
      f = lr(f, 1);
      blocks[i] = f;
    }
  }

  private int[] mainLoopOp(int a, int b, int c, int d, int i) {
    int[] res = new int[2];

    if (i >= 0 && i <= 19) {
      res[0] = (b & c) | ((~b) & d);
      res[1] = 0x5A827999;
    } else if (i >= 20 && i <= 39) {
      res[0] = b ^ c ^ d;
      res[1] = 0x6ED9EBA1;
    } else if (i >= 40 && i <= 59) {
      res[0] = (b & c) | (b & d) | (c & d);
      res[1] = 0x8F1BBCDC;
    } else if (i >= 60 && i <= 79) {
      res[0] = b ^ c ^ d;
      res[1] = 0xCA62C1D6;
    }

    return res;
  }

  private int[] process() {
    long bitmask = 0xFFFFFFFFL;
    long temp;
    int h0 = INITIALIZE_VAR0;
    int h1 = INITIALIZE_VAR1;
    int h2 = INITIALIZE_VAR2;
    int h3 = INITIALIZE_VAR3;
    int h4 = INITIALIZE_VAR4;

    for (int i = 0; i < chunks.length; i++) {
      /* preprocess block */
      // break chunks into 16, 32 bit word
      int[] words = breakChunks(chunks[i]);

      // fill empty slot in words
      fill(words);

      /* Init hash value */
      int a = INITIALIZE_VAR0;
      int b = INITIALIZE_VAR1;
      int c = INITIALIZE_VAR2;
      int d = INITIALIZE_VAR3;
      int e = INITIALIZE_VAR4;

      /* Main Loop */
      for (int j = 0; j < 80; j++) {
        int[] res = mainLoopOp(a, b, c, d, j);
        
        temp = (lr(a, 5) + res[0] + e + res[1] + words[j]) & bitmask;
        // update hash value
        e = d;
        d = c;
        c = lr(b, 30);
        b = a;
        a = (int) temp;
      }

      /* Add chunk hash into result */
      h0 += a;
      h1 += b;
      h2 += c;
      h3 += d;
      h4 += e;
    }
    // produce final hash value
    // final hash value is 160 bit
    // represent hash value as array of integer
    int[] hash = {h0, h1, h2, h3, h4};
    return hash;
  }

  public String digest() {
    String digest = "";
    int[] hash = process();
    for (int i = 0; i < 5; i++) {
      digest += Hex.hexEncoder(hash[i]);
    }
    return digest;
  }
}
