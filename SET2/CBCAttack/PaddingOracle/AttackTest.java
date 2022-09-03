package SET2.CBCAttack.PaddingOracle;

import Encoding.ASCII;
import Encoding.Hex;
import Encoding.UnrecognizedEncodingException;

public class AttackTest {
  public static void main(String[] args) throws UnrecognizedEncodingException {
    PaddingOracle oracle = new PaddingOracle();
    String[] f = oracle.send();
    String message = f[0];
    String IV = f[1];

    int blockLength = message.length() / 32;
    String[] blockMessage = new String[blockLength + 1];
    blockMessage[0] = IV;

    int start = 0;
    int end = 32;

    for (int i = 1; i < blockLength + 1; i++) {
      blockMessage[i] = message.substring(start, end);
      start += 32;
      end += 32;
    }

    PaddingAttack atck = new PaddingAttack(blockMessage, IV, oracle);
    String originalMessage = atck.attack();
    String originalaMessageASCII = ASCII.ASCIIDecoder(Hex.fromHexToAscii(originalMessage));
    System.out.println(originalaMessageASCII);
  }
  
}
