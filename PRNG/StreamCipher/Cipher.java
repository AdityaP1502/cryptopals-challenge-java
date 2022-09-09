package PRNG.StreamCipher;

import Encoding.EncodingFormat;
import Encoding.UnrecognizedEncodingException;
import PRNG.MersenneTwister.CrackTheSeed;
import SET1.XOR.XOR;

public class Cipher {
  private int seed;
  private byte[] blocks;
  private String processedMessage;
  private final KeystreamGenerator KEYSTREAM_GENERATOR;

  public Cipher(int seed, String text, String encoding) throws UnrecognizedEncodingException {
    this.seed = seed;
    this.blocks = EncodingFormat.convertToBytes(text, encoding);
    this.KEYSTREAM_GENERATOR = new KeystreamGenerator(seed);
    this.processedMessage = "";
  }

  public String XORWithKeystream(byte[] x, byte[] y, int start) {
    return XOR.XORCombination(x, y, start);
  }

  public void process() {
    // encrypt or decrypt using PRNG to generate keystream
    // work byte per byte
    byte[] keystreamBytes;
    int n = Math.ceilDiv(blocks.length, 16);
    int start = 0;

    // RESET STATE
    processedMessage = "";
    KEYSTREAM_GENERATOR.reset(seed);

    for (int i = 0; i < n; i++) {
      keystreamBytes = KEYSTREAM_GENERATOR.nextKeystream();
      processedMessage += XORWithKeystream(blocks, keystreamBytes, start);
      start += 16;
    }
  }

  public String encrypt() {
    process();
    return processedMessage;
  }

  public String decrypt() {
    process();
    return processedMessage;
  }

  public int getSeed() {
    return seed;
  }

  public void reset(int seed) {
    // change into a new seed
    this.seed = seed;
    processedMessage = "";
    KEYSTREAM_GENERATOR.reset(seed);
  }

  public void setText(String text, String encoding) throws UnrecognizedEncodingException {
    this.blocks = EncodingFormat.convertToBytes(text, encoding);
  }

  public int breakSeed() throws UnrecognizedEncodingException {
    long x;
    // length ciphertext = length of plaintext
    // if append 14 X, then the first x at index, length - 14
    // 32 bit number from PRNG for XOR with 4 character
    int randomLength = (blocks.length - 14);
    int startPos = randomLength + randomLength % 4;
    int bitmask = (1 << 8) - 1;
    long PRNGNumber = 0;

    // get the tempered value
    for (int i = 0; i < 4; i++) {
      x = (blocks[startPos + i] ^ ((byte) 'A')) & bitmask;
      x = x << (8 * (3 - i));
      PRNGNumber += x;
    }

    return CrackTheSeed.crackTheSeed(PRNGNumber, (startPos / 4));
  }
}
