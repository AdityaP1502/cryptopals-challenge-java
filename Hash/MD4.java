package Hash;
import Encoding.ASCII;
import Encoding.Hex;

public class MD4 {
  private final int INITIALIZE_VAR0 = 0x67452301;
  private final int INITIALIZE_VAR1 = 0xEFCDAB89;
  private final int INITIALIZE_VAR2 = 0x98BADCFE;
  private final int INITIALIZE_VAR3 = 0x10325476;
  private final int[][] K_VALUES = {
    {
      0, 1, 2, 3, 
      4, 5, 6, 7, 
      8, 9, 10, 11, 
      12, 13, 14, 15,
    }, 
    {
      0, 4, 8, 12, 
      1, 5, 9, 13, 
      2, 6, 10, 14,
      3, 7, 11, 15
    }, 
    {
      0, 8, 4, 12, 
      2, 10, 6, 14, 
      1, 9, 5, 13, 
      3, 11, 7, 15,
    }
  };
  
  private final int[][] S_VALUES = { { 3, 7, 11, 19 }, { 3, 5, 9, 13 }, { 3, 9, 11, 15} };
  private long length;
  private byte[][] chunks;
  private int[] state = new int[4];

  public MD4(String message, String encoding) {
    if (encoding == "ASCII") message = ASCII.asciiToHex(message);
    length = message.length() * 4;
    createChunks(preprocess(message));
  }

  public MD4(String message, String encoding, int length) {
    this.length = length;
    if (encoding == "ASCII") message = ASCII.asciiToHex(message);
    createChunks(preprocess(message));
  }

  public static String convertToLittleEndian(String hex) {
    String hexLittleEndian = "";
    for (int i = hex.length() - 1; i >= 1; i = i - 2) {
      hexLittleEndian += hex.charAt(i - 1);
      hexLittleEndian += hex.charAt(i);
    }

    return hexLittleEndian;
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
    
    // add message length as 64 bit little - endian number
    String lengthHexBigEndian = Hex.hexEncoder(this.length); // in big - endian

    // reverse big - endian into little - endian
    

    message = message + convertToLittleEndian(lengthHexBigEndian);
    System.out.println(message);
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
    // use little - endian 
    int n = 80;
    int bitmask = 0xFF;
    int[] blocks = new int[n];
    int temp;
    int count = 0;
    for (int i = 0; i < chunk.length; i += 4) {
      // concate 4 bytes into one word
      temp = ((chunk[i + 3] & bitmask) << 24) + ((chunk[i + 2] & bitmask) << 16) + ((chunk[i + 1] & bitmask) << 8)  + (chunk[i] & bitmask);
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
  }

  public static int lr(int word, int count) {
    int bitmask = (1 << (count)) - 1;
    return (word << count) + ((word >> (32 - count)) & bitmask);
  }

  private int F(int x, int y, int z) {
    return (x & y) | ((~x) & z);
  }

  private int G(int x, int y, int z) {
    return (x & y) | (x & z) | (y & z);
  }

  private int H(int x, int y, int z) {
    return x ^ y ^ z;
  }

  private void roundOneOperation(int[] state, int a, int b, int c, int d, int[] word, int k, int s) {
    state[a] = state[a] + F(state[b], state[c], state[d]) + word[k];
    state[a] = lr(state[a], s);
  }

  private void roundTwoOperation(int state[], int a, int b, int c, int d, int[] word, int k, int s) {
    state[a] = state[a] + G(state[b], state[c], state[d]) + word[k] + 0x5A827999;
    state[a] = lr(state[a], s);
  }

  private void roundThreeOperation(int[] state, int a, int b, int c, int d, int[] word, int k, int s) {
    state[a] = state[a] + H(state[b], state[c], state[d]) + word[k] + 0x6ED9EBA1;
    state[a] = lr(state[a], s);
  }

  private void roundProcess(int[] state, int[] chunk, int[] ss, int[] ks, int round) {
    // Do 16 operation
    int[] index = {0, 1, 2, 3};
    int a, b, c, d, k, s;
    int start = 0;

    for (int i = 0; i < 16; i++) {
      a = index[start % 4];
      b = index[(start + 1) % 4];
      c = index[(start + 2) % 4];
      d = index[(start + 3) % 4];
      k = ks[i];
      s = ss[i % 4];

      switch (round) {
        case 1:
          roundOneOperation(state, a, b, c, d, chunk, k, s);
          break;
        case 2:
          roundTwoOperation(state, a, b, c, d, chunk, k, s);
          break;
        case 3: 
          roundThreeOperation(state, a, b, c, d, chunk, k, s);
          break;
      }

      start--; // update start
      start = (start % 4);
      start = start < 0 ? start + 4 : start;
    }
  }

  private void round(int[] state, int[] chunk, int round) {
    roundProcess(state, chunk, S_VALUES[round], K_VALUES[round], round + 1);
  }


  private void process() {
    for (int i = 0; i < chunks.length; i++) {
      int[] temp = state.clone();
      // break 512 bit into 16 32-bit word
      int[] words = breakChunks(chunks[i]);
      // Do operation
      for (int j = 0; j < 3; j++) {
        round(temp, words, j);
      }
      // increment register
      state[0] += temp[0];
      state[1] += temp[1];
      state[2] += temp[2];
      state[3] += temp[3];
    } 
  }

  public String digest() {
    String digest = "";
    String temp = "";

    initState();
    process();

    for (int i = 0; i < 4; i++) {
      temp = Hex.hexEncoder(state[i]);
      temp = convertToLittleEndian(temp);
      digest += temp;
    }

    return digest;
  }

  private int[] forgeState(String hash) {
    String f;
    byte[] g;

    int[] state = new int[4];
    int bitmask = 0xFF;
    int start = 0;

    for (int i = 0; i < 4; i++) {
      f = hash.substring(start, start + 8);
      f = convertToLittleEndian(f);
      g = Hex.fromHexToBytes(f);
      for (int j = 3; j >= 0; j--) {
        state[i] += (g[j] & bitmask) << (8 * (3 - j));
      }
      start = start + 8;
    }

    return state;
  }

  public void setState(int[] state) {
    this.state = state;
  }

  public static String forge(String newMessage, String hash, int messageLength) {
    String temp;
    MD4 md4 = new MD4(newMessage, "ASCII", messageLength);
    int[] forgeState = md4.forgeState(hash);

    md4.setState(forgeState);
    md4.process();

    String digest = "";
    for (int i = 0; i < 4; i++) {
      temp = Hex.hexEncoder(md4.state[i]);
      temp = md4.convertToLittleEndian(temp);
      digest += temp;
    }

    return digest;
  }
}
