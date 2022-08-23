package SET1.Decoder;

public class Set1Test {
  public static void main(String[] args) {
    String s = "49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d";
    String expected = "SSdtIGtpbGxpbmcgeW91ciBicmFpbiBsaWtlIGEgcG9pc29ub3VzIG11c2hyb29t";
    String base64 = Hex.fromHexToBase64(s);
    System.out.println(base64);
    System.out.println(base64.equals(expected));
  }
  
}
