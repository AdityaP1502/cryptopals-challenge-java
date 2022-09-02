package AES;

public class InvalidBlockSizeException extends Exception {
  InvalidBlockSizeException(String message) {
    super(message);
  }
}
