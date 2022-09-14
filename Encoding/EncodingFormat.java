package Encoding;

public class EncodingFormat {
  public static byte[] convertToBytes(String text, String encoding) throws UnrecognizedEncodingException {
    byte[] block;
    switch (encoding) {
      case "ASCII":
        block = ASCII.ASCIIEncoder(text);
        break;
      case "BASE64":
        // padding length
        block = Base64.fromBase64ToAscii(text);
        break;
      case "HEX":
        block = Hex.fromHexToBytes(text);
        break;
      default:
        throw new UnrecognizedEncodingException("Not recoginized encoding format" + encoding);
    }
    return block;
  }

  public static String fromBytesToText(byte[] binaryData, String encoding) throws UnrecognizedEncodingException {
    String s;
    switch (encoding) {
      case "ASCII":
        s = ASCII.ASCIIDecoder(binaryData);
        break;
      case "BASE64":
        s = Base64.base64Encoder(binaryData);
        break;
      case "HEX":
        s = Hex.hexEncoder(binaryData);
        break;
      default:
        throw new UnrecognizedEncodingException("Not recoginized encoding format" + encoding);
    }
    return s;
  }  
}
