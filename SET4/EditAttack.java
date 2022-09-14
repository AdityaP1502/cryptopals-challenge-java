package SET4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import AES.InvalidBlockSizeException;
import AES.CTR.CTR;
import Encoding.ASCII;
import Encoding.Hex;
import Encoding.UnrecognizedEncodingException;
import SET1.XOR.XOR;

public class EditAttack {
  public static String readFiles() throws FileNotFoundException {
    File file = new File("SET4/file.txt");
    Scanner sc = new Scanner(file);
    String message = "";

    while (sc.hasNextLine()) {
      message += sc.nextLine();
    }

    sc.close();
    return message;
  }
  public static void main(String[] args) {
    String encryptedMessage = null;
    CTR ctr = null;

    try {
      ctr = new CTR();
      String message = readFiles();
      ctr.setMessage(message, "ASCII");
      encryptedMessage = ctr.decrypt();
      // edit the ciphertext
      int length = encryptedMessage.length();
      // craft string with byte value 0
      String attackString = "";
      for (int i = 0; i < length; i++) {
        attackString += "0";
      }

      // edit the ciphertext, overwrite all of the string
      // P XOR 0 = P
      // use edit to get the keystream
      String keystream = ctr.edit(0, attackString);
      // from keystream get original message
      byte[] x = Hex.fromHexToBytes(encryptedMessage);
      byte[] y = Hex.fromHexToBytes(keystream);
      String decrypedMessage = XOR.XORCombination(x, y, 0);
      decrypedMessage = ASCII.ASCIIDecoder(Hex.fromHexToBytes(decrypedMessage));
      System.out.println(decrypedMessage);

    } catch (UnrecognizedEncodingException | FileNotFoundException | InvalidBlockSizeException e) {
      e.printStackTrace();
      System.exit(-1);
    }

    


  }
}
