package AES.CBC;

import AES.AESCipher;
import AES.InvalidBlockSizeException;
import AES.Words;
import Encoding.ASCII;
import Encoding.EncodingFormat;
import Encoding.Hex;
import Encoding.UnrecognizedEncodingException;

public class CBCTest2 {
  public static void main(String[] args) throws UnrecognizedEncodingException, InvalidBlockSizeException {
    String IV = "1f265dfcef538e67a3c71230ec4c72cb";
    String key = "bae9e8ddf212132396060a8ff1a4da1e";
    String message = "comment1=cooking";

    // convert to bytes
    byte[] ivBytes = EncodingFormat.convertToBytes(IV, "HEX");
    String keyInASCII = ASCII.ASCIIDecoder(Hex.fromHexToBytes(key));
    byte[] messageByte = EncodingFormat.convertToBytes(message, "ASCII");
    

    // XOR with iv
    Words.XOR(messageByte, ivBytes);
    System.out.println(Hex.hexEncoder(messageByte));
    String decryptedMessage = AESCipher.encrypt(messageByte, keyInASCII).replace(" ", "");
    System.out.println(decryptedMessage);

    // decrypt
    byte[] decryptedBlock = EncodingFormat.convertToBytes(decryptedMessage, "HEX");
    String beforeIV = AESCipher.decrypt(decryptedBlock, keyInASCII).replace(" ", "");
    System.out.println(beforeIV);
    String originalMessage;
    byte[] beforeIVBytes = EncodingFormat.convertToBytes(beforeIV, "HEX");
    Words.XOR(beforeIVBytes, ivBytes);
    originalMessage = ASCII.ASCIIDecoder(beforeIVBytes);
    System.out.println(originalMessage);

    CBC cbc = new CBC(message, keyInASCII, IV, "ASCII", 0);
    decryptedMessage = cbc.encrypt();
    System.out.println(decryptedMessage);
    cbc = new CBC(decryptedMessage, keyInASCII, IV, "HEX", 1);
    originalMessage = cbc.decrypt();
    originalMessage = ASCII.ASCIIDecoder(Hex.fromHexToBytes(originalMessage));
    System.out.println(originalMessage);
  }
  
}
