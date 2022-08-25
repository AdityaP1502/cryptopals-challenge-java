package SET1.AES.ECB;

public class InvalidBlockSizeException extends Exception {
  InvalidBlockSizeException(String message) {
    super(message);
  }
}
