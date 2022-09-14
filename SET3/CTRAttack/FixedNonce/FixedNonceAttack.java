package SET3.CTRAttack.FixedNonce;

import java.util.ArrayList;
import java.util.List;

import AES.InvalidBlockSizeException;
import AES.CTR.CTR;
import Encoding.ASCII;
import Encoding.Hex;
import Encoding.UnrecognizedEncodingException;
import SET1.XOR.XOR;
import SET1.XOR.Decoder.SingleByteDecoder.SingleByteDecoder;

public class FixedNonceAttack {
  public SingleByteDecoder decoder;
  private String[] message = {
    "SSBoYXZlIG1ldCB0aGVtIGF0IGNsb3NlIG9mIGRheQ",
    "Q29taW5nIHdpdGggdml2aWQgZmFjZXM",
    "RnJvbSBjb3VudGVyIG9yIGRlc2sgYW1vbmcgZ3JleQ",
    "RWlnaHRlZW50aC1jZW50dXJ5IGhvdXNlcy4",
    "SSBoYXZlIHBhc3NlZCB3aXRoIGEgbm9kIG9mIHRoZSBoZWFk",
    "T3IgcG9saXRlIG1lYW5pbmdsZXNzIHdvcmRzLA",
    "T3IgaGF2ZSBsaW5nZXJlZCBhd2hpbGUgYW5kIHNhaWQ",
    "UG9saXRlIG1lYW5pbmdsZXNzIHdvcmRzLA",
    "QW5kIHRob3VnaHQgYmVmb3JlIEkgaGFkIGRvbmU",
    "T2YgYSBtb2NraW5nIHRhbGUgb3IgYSBnaWJl",
    "VG8gcGxlYXNlIGEgY29tcGFuaW9u",
    "QXJvdW5kIHRoZSBmaXJlIGF0IHRoZSBjbHViLA",
    "QmVpbmcgY2VydGFpbiB0aGF0IHRoZXkgYW5kIEk",
    "QnV0IGxpdmVkIHdoZXJlIG1vdGxleSBpcyB3b3JuOg",
    "QWxsIGNoYW5nZWQsIGNoYW5nZWQgdXR0ZXJseTo",
    "QSB0ZXJyaWJsZSBiZWF1dHkgaXMgYm9ybi4",
    "VGhhdCB3b21hbidzIGRheXMgd2VyZSBzcGVudA",
    "SW4gaWdub3JhbnQgZ29vZCB3aWxsLA",
    "SGVyIG5pZ2h0cyBpbiBhcmd1bWVudA",
    "VW50aWwgaGVyIHZvaWNlIGdyZXcgc2hyaWxsLg",
    "V2hhdCB2b2ljZSBtb3JlIHN3ZWV0IHRoYW4gaGVycw",
    "V2hlbiB5b3VuZyBhbmQgYmVhdXRpZnVsLA",
    "U2hlIHJvZGUgdG8gaGFycmllcnM/",
    "VGhpcyBtYW4gaGFkIGtlcHQgYSBzY2hvb2w",
    "QW5kIHJvZGUgb3VyIHdpbmdlZCBob3JzZS4",
    "VGhpcyBvdGhlciBoaXMgaGVscGVyIGFuZCBmcmllbmQ",
    "V2FzIGNvbWluZyBpbnRvIGhpcyBmb3JjZTs",
    "SGUgbWlnaHQgaGF2ZSB3b24gZmFtZSBpbiB0aGUgZW5kLA",
    "U28gc2Vuc2l0aXZlIGhpcyBuYXR1cmUgc2VlbWVkLA",
    "U28gZGFyaW5nIGFuZCBzd2VldCBoaXMgdGhvdWdodC4",
    "VGhpcyBvdGhlciBtYW4gSSBoYWQgZHJlYW1lZA",
    "QSBkcnVua2VuLCB2YWluLWdsb3Jpb3VzIGxvdXQu",
    "SGUgaGFkIGRvbmUgbW9zdCBiaXR0ZXIgd3Jvbmc",
    "VG8gc29tZSB3aG8gYXJlIG5lYXIgbXkgaGVhcnQs",
    "WWV0IEkgbnVtYmVyIGhpbSBpbiB0aGUgc29uZzs",
    "SGUsIHRvbywgaGFzIHJlc2lnbmVkIGhpcyBwYXJ0",
    "SW4gdGhlIGNhc3VhbCBjb21lZHk7",
    "SGUsIHRvbywgaGFzIGJlZW4gY2hhbmdlZCBpbiBoaXMgdHVybiw",
    "VHJhbnNmb3JtZWQgdXR0ZXJseTo",
    "QSB0ZXJyaWJsZSBiZWF1dHkgaXMgYm9ybi4"
  };
  String[] ciphertexts = new String[message.length];
  int minCiphertextLength = -1;
  int threshold = -20;

  public FixedNonceAttack() {
    decoder = new SingleByteDecoder();

    String nonce = "0000000000000000";
    try {
      CTR ctr = new CTR(nonce);

      for (int i = 0; i < ciphertexts.length; i++) {
        ctr.setMessage(message[i], "BASE64");
        ciphertexts[i] = ctr.encrypt();
        if (minCiphertextLength == -1 || minCiphertextLength > ciphertexts[i].length()) {
          minCiphertextLength = ciphertexts[i].length();
        }
      }

    } catch (UnrecognizedEncodingException | InvalidBlockSizeException e) {
      e.printStackTrace();
      System.exit(-1);
    }
  } 

  public void findByte(int pos) {
    // brute force keystream
    // take the first byte from all ciphertext
    String combinedBytes = "";
    for (int i = 0; i < ciphertexts.length; i++) {
      // ciphertext in hex
      //2 hex char = 1 byte
      combinedBytes += ciphertexts[i].substring(pos * 2, (pos + 1) * 2);
    }

    decoder.decrypt(combinedBytes);
  }

  public String getKeyStream() {
    List<Byte> keystream = new ArrayList<>();
    for (int i = 0; i < minCiphertextLength / 2; i++){
      findByte(i);
      if (decoder.getMaxScore() < threshold) break;
      keystream.add(decoder.getKey());
    }

    byte[] f = new byte[keystream.size()];
    int i = 0;

    for (byte x : keystream) {
      f[i] = x;
      i++;
    }

    return Hex.hexEncoder(f);
  }

  public void attack() {
    byte[] x;
    String keystream = getKeyStream();
    String originalMessage = "";
    byte[] y = Hex.fromHexToBytes(keystream);
    for (int i = 0; i < keystream.length(); i++) {
      x = Hex.fromHexToBytes(ciphertexts[i].substring(0, keystream.length()));
      originalMessage = XOR.XORCombination(x, y, 0);
      originalMessage = ASCII.ASCIIDecoder(Hex.fromHexToBytes(originalMessage));
      System.out.println(originalMessage);
    }
  }
}
