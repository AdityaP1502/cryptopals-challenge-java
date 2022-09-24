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
  private int[] state = new int[5];

  
  public SHA1(String message, String encoding) {
    if (encoding == "ASCII") message = ASCII.asciiToHex(message);
    length = message.length() * 4;
    createChunks(preprocess(message));
  }

  public SHA1(String message) {
    length = message.length() * 4;
    createChunks(preprocess(message));
  }

  public SHA1(String message, String encoding, int length) {
    this.length = length;
    if (encoding == "ASCII") message = ASCII.asciiToHex(message);
    createChunks(preprocess(message));
  }

  public SHA1() {
    this.length = -1;
  }

  public void setMessage(String message, String encoding) {
    if (encoding == "ASCII") message = ASCII.asciiToHex(message);
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

  private void initState() {
    state[0] = INITIALIZE_VAR0;
    state[1] = INITIALIZE_VAR1;
    state[2] = INITIALIZE_VAR2;
    state[3] = INITIALIZE_VAR3;
    state[4] = INITIALIZE_VAR4;
  }

  private void process() {
    long bitmask = 0xFFFFFFFFL;
    long temp;
    int a, b, c, d, e; // init hash value

    for (int i = 0; i < chunks.length; i++) {
      /* preprocess block */
      // break chunks into 16, 32 bit word
      int[] words = breakChunks(chunks[i]);

      // fill empty slot in words
      fill(words);

      /* Init hash value */
      a = state[0];
      b = state[1];
      c = state[2];
      d = state[3];
      e = state[4];

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
      state[0] += a;
      state[1] += b;
      state[2] += c;
      state[3] += d;
      state[4] += e;
    }
  }

  private int[] forgeState(String hash) {
    String f;
    byte[] g;

    int[] state = new int[5];
    int bitmask = 0xFF;
    int start = 0;

    for (int i = 0; i < 5; i++) {
      f = hash.substring(start, start + 8);
      g = Hex.fromHexToBytes(f);
      for (int j = 3; j >= 0; j--) {
        state[i] += (g[j] & bitmask) << (8 * (3 - j));
      }
      start = start + 8;
    }

    return state;
  }

  public static String forge(String newMessage, String hash, int messageLength) {
    SHA1 sha = new SHA1(newMessage, "ASCII", messageLength);
    int[] forgeState = sha.forgeState(hash);

    sha.setState(forgeState);
    sha.process();

    String digest = "";
    for (int i = 0; i < 5; i++) {
      digest += Hex.hexEncoder(sha.state[i]);
    }

    return digest;
  }
  
  public String digest() {
    String digest = "";

    initState();
    process();

    for (int i = 0; i < 5; i++) {
      digest += Hex.hexEncoder(state[i]);
    }

    return digest;
  }

  public void setState(int[] state) {
    this.state = state;
  }
}
