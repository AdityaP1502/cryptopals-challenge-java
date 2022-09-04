package AES.CTR;

import java.util.Random;

import AES.AESCipher;
import AES.InvalidBlockSizeException;
import Encoding.EncodingFormat;
import Encoding.Hex;
import Encoding.UnrecognizedEncodingException;
import SET1.XOR.XOR;

public class CTR {
  private final String KEY;
  private String nonce;
  private String processedMessage;
  private byte[] blocks;

  public CTR(String text, String encoding, String key, String nonce) throws UnrecognizedEncodingException {
    this.KEY = key;
    this.blocks = EncodingFormat.convertToBytes(text, encoding);
    this.nonce = nonce;
  }

  public CTR(String text, String encoding, String key) throws UnrecognizedEncodingException {
    this.KEY = key;
    this.blocks = EncodingFormat.convertToBytes(text, encoding);
    this.nonce = generateRandomNonce();
  }

  public String generateRandomNonce() {
    byte[] x = new byte[8];
    Random rnd = new Random();

    for (int i = 0; i < 8; i++) {
      rnd.nextBytes(x);
    }

    return Hex.hexEncoder(x);
  }

  public String XORWithKeystream(byte[] x, byte[] y, int start) {
    return XOR.XORCombination(x, y, start);
  }
  
  public void process() throws InvalidBlockSizeException, UnrecognizedEncodingException {
    byte[] keystreamBytes;
    int n = Math.ceilDiv(blocks.length, 16);
    int start = 0;
    byte[] AESInput = new byte[16];
    byte[] f = Hex.hexDecoder(nonce);
    String keystream;
    processedMessage = "";

    for (int i = 0; i < 8; i++) {
      AESInput[i] = f[1];
    }

    for (int i = 0; i < n; i++) {
      keystream = AESCipher.encrypt(AESInput, KEY).replace(" ", "");
      keystreamBytes = EncodingFormat.convertToBytes(keystream, "HEX");
      processedMessage += XORWithKeystream(blocks, keystreamBytes, start);
      AESInput[8]++;
      start += 16;
    }
  }

  public String encrypt() throws InvalidBlockSizeException, UnrecognizedEncodingException {
    process();
    return processedMessage;
  }

  public String decrypt() throws InvalidBlockSizeException, UnrecognizedEncodingException {
    process();
    return processedMessage;
  }

  public void setMessage(String message, String encoding) throws UnrecognizedEncodingException {
    blocks = EncodingFormat.convertToBytes(message, encoding);
  }

  public void setNonce(String nonce) {
    this.nonce = nonce;
  }
}
