package SET3;

import java.util.Random;

import Encoding.UnrecognizedEncodingException;
import PRNG.MersenneTwister.MT19937;
import PRNG.StreamCipher.Cipher;

public class ResetToken {
  private String token;
  public ResetToken(String password) throws UnrecognizedEncodingException {
    Random rnd = new Random();
    token = "";
    for (int i = 0; i < 20 + rnd.nextInt(80);i++) {
      token += (char) (rnd.nextInt(256));
    }

    String message = token + password;
    Cipher stream = new Cipher((int) System.currentTimeMillis(), message, "ASCII");
    token = stream.encrypt();
  }

  public String getToken() {
    return token;
  }
}
