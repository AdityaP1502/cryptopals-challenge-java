package SET2.CBCAttack.PaddingOracle;

import Encoding.UnrecognizedEncodingException;

public class OracleTest {
  public static void main(String[] args) {
    try {
      PaddingOracle oracle = new PaddingOracle();
      String[] x = oracle.send();

      String message = x[0];
      String IV = x[1];
      
      boolean isValidPad = oracle.receive(message, IV);
      System.out.println(isValidPad);

    } catch (UnrecognizedEncodingException e) {
      e.printStackTrace();
      System.exit(-1);
    }
    
  }
}
