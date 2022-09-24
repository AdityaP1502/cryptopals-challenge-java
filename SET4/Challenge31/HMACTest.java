package SET4.Challenge31;
import java.io.IOException;
public class HMACTest {
  public static String requestQuery(String file, String signature) {
    String fileString = "file="+file;
    String signatureString = "signature=" + signature;
    return fileString + "&" + signatureString;
  }

  public static void main(String[] args) {
    try {
      String file = "passwd.txt";
      String signature = HMAC.sign(file);
      String spec = "http://localhost:5000/test?" + requestQuery(file, signature);
      ServerConnection connection = new ServerConnection(spec, "GET");
      String response = connection.readResponse();
      connection.closeConnection();
      System.out.println(response);
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(-1);
    }
  }    
}
