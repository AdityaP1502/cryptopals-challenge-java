package SET4;

import Encoding.ASCII;
import SET1.XOR.XOR;

public class CBCIVAttack {
  public static void main(String[] args) {
    // make server send message 
    WebApp app = new WebApp();
    String message = "http://ThisIsALinkHOHOHOHOHEHEHEHEHEEHEHEHEE.com";
    message = app.sendMessage(message);

    /* ATTACKER REALM */
    String inject = "00000000000000000000000000000000";
    String control = message.substring(0, 32);
    message = control + inject + control;

    // raise high compliance ascii error
    String response = app.getMessage(message);
    response = response.substring(7, 55);

    // get key
    byte[] x = ASCII.ASCIIEncoder(response.substring(0, 16));
    byte[] y = ASCII.ASCIIEncoder(response.substring(32, 48));
    String key = XOR.XORCombination(x, y, 0);
    System.out.println(key);
  }
}

