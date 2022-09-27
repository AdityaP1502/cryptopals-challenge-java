package Hash;

import Encoding.Hex;
import Encoding.ASCII;

public class SHA256 {
  // all variables are 32 bit unsigned integer 
  // addition is calculated mod 2**32 == Bitmask with 0xFFFFFFFF
  private final long BITMASK = 0xFFFFFFFFL;
  private final int INITIALIZE_VAR0 = 0x6a09e667;
  private final int INITIALIZE_VAR1 = 0xbb67ae85;
  private final int INITIALIZE_VAR2 = 0x3c6ef372;
  private final int INITIALIZE_VAR3 = 0xa54ff53a;
  private final int INITIALIZE_VAR4 = 0x510e527f;
  private final int INITIALIZE_VAR5 = 0x9b05688c;
  private final int INITIALIZE_VAR6 = 0x1f83d9ab;
  private final int INITIALIZE_VAR7 = 0x5be0cd19;
  private final int[] ROUND_CONSTANT = {
    0x428a2f98, 0x71374491, 0xb5c0fbcf, 0xe9b5dba5, 0x3956c25b, 0x59f111f1, 0x923f82a4, 0xab1c5ed5,
    0xd807aa98, 0x12835b01, 0x243185be, 0x550c7dc3, 0x72be5d74, 0x80deb1fe, 0x9bdc06a7, 0xc19bf174,
    0xe49b69c1, 0xefbe4786, 0x0fc19dc6, 0x240ca1cc, 0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc, 0x76f988da,
    0x983e5152, 0xa831c66d, 0xb00327c8, 0xbf597fc7, 0xc6e00bf3, 0xd5a79147, 0x06ca6351, 0x14292967,
    0x27b70a85, 0x2e1b2138, 0x4d2c6dfc, 0x53380d13, 0x650a7354, 0x766a0abb, 0x81c2c92e, 0x92722c85,
    0xa2bfe8a1, 0xa81a664b, 0xc24b8b70, 0xc76c51a3, 0xd192e819, 0xd6990624, 0xf40e3585, 0x106aa070,
    0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5, 0x391c0cb3, 0x4ed8aa4a, 0x5b9cca4f, 0x682e6ff3,
    0x748f82ee, 0x78a5636f, 0x84c87814, 0x8cc70208, 0x90befffa, 0xa4506ceb, 0xbef9a3f7, 0xc67178f2,
  };

  private long length;
  private byte[][] chunks;
  private int[] state = new int[8];

  public SHA256(String message, String encoding) {
    setMessage(message, encoding);
  }

  public SHA256(String message) {
    setMessage(message);
  }

  public SHA256() {
    this.length = -1;
  }

  public void setMessage(String message, String encoding) {
    if (encoding == "ASCII") message = ASCII.asciiToHex(message);
    setMessage(message);
  }

  public void setMessage(String message) {
    this.length = message.length() * 4;
    createChunks(preprocess(message));
  }

  private String preprocess(String message) {
    // message in hex
    int length = message.length() * 4;
    if (length % 8 == 0) {
      message += "80";
      length += 8;
    }

    int lenMod = length % 512;
    int n = lenMod <= 448 ? (448 - lenMod) : 448 + (512 - lenMod);
    n /= 4; // each hex character consist of 4 bit 

    // append 0 until message % 512 = 448
    for (int i = 0; i < n; i++) {
      message += '0';
    }
    
    // add message length as 64 bit big - endian number
    String lengthHex = Hex.hexEncoder(this.length);
    message = message + lengthHex;
    // System.out.println(message);
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
    int n = 64;
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

  private void initState() {
    state[0] = INITIALIZE_VAR0;
    state[1] = INITIALIZE_VAR1;
    state[2] = INITIALIZE_VAR2;
    state[3] = INITIALIZE_VAR3;
    state[4] = INITIALIZE_VAR4;
    state[5] = INITIALIZE_VAR5;
    state[6] = INITIALIZE_VAR6;
    state[7] = INITIALIZE_VAR7;
  }

  private int rr(int word, int count) {
    // RIGHT ROTATE
    int bitmask = (1 << count) - 1;
    // promote word into long and bitmask to remove leading 1 if temp is negative
    long temp = word & BITMASK; 

    int f = (int) (temp & bitmask) << (32 - count);
    int g = (int) (temp >> count);

    return f + g;
  }

  private void fill(int[] w) {
    int f, g;
    for (int i = 16; i < 64; i++) {
      f = rr(w[i - 15], 7) ^ rr(w[i - 15], 18) ^ (int) ((w[i - 15] & BITMASK) >> 3);
      g = rr(w[i - 2], 17) ^ rr(w[i - 2], 19) ^ (int) ((w[i - 2] & BITMASK) >> 10);
      w[i] = (int) ((w[i - 16] + f + w[i - 7] + g) & BITMASK);
    }
  }

  private void process() {
    int a, b, c, d, e, f, g, h; // hash current val placeholder
    int s0, s1, temp1, temp2, ch, maj; // compress temp var
    for (int i = 0; i < chunks.length; i++) {
      /* preprocess block */
      // break chunks into 16, 32 bit word
      // copy chunk into message schedule array that consist of 64, 32 bit word
      int[] w = breakChunks(chunks[i]);
      // fill w value from 16 to 63
      fill(w); 

      // init hash current value
      a = state[0];
      b = state[1];
      c = state[2];
      d = state[3];
      e = state[4];
      f = state[5];
      g = state[6];
      h = state[7];

      /* Main Loop */
      for (int j = 0; j < 64; j++) {
        // compress
        s1 = rr(e, 6) ^ rr(e, 11) ^ rr(e, 25);
        ch = (e & f) ^ ((~e) & g);
        temp1 = (int) ((h + s1 + ch + ROUND_CONSTANT[j] + w[j]) & BITMASK);
        s0 = rr(a, 2) ^ rr(a, 13) ^ rr(a, 22);
        maj = (a & b) ^ (a & c) ^ (b & c);
        temp2 = (int) ((s0 + maj) & BITMASK);

        // update curr hash value
        h = g;
        g = f;
        f = e;
        e = (int) ((d + temp1) & BITMASK);
        d = c;
        c = b;
        b = a;
        a = (int) ((temp1 + temp2) & BITMASK);
      }

      /* Add compress chunk into current hash value */
      state[0] += a;
      state[1] += b;
      state[2] += c;
      state[3] += d;
      state[4] += e;
      state[5] += f;
      state[6] += g;
      state[7] += h;
    }
  }

  public String digest() {
    String digest = "";

    initState();
    process();

    for (int i = 0; i < 8; i++) {
      digest += Hex.hexEncoder(state[i]);
    }

    return digest;
  }
}
