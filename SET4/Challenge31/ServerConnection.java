package SET4.Challenge31;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerConnection {
  private HttpURLConnection con;
  public ServerConnection(String spec, String method) {
    try {
      URL url = new URL(spec);
      con = (HttpURLConnection) url.openConnection();
      con.setRequestMethod(method);
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(-1);
    }
  }

  public int readResponseCode() throws IOException {
    return con.getResponseCode();
  }

  public String readStream(Reader stream) throws IOException {
    BufferedReader in = new BufferedReader(stream);
    String inputLine;
    StringBuffer content = new StringBuffer();
    while((inputLine = in.readLine()) != null) {
      content.append(inputLine);
    }
    in.close();
    return content.toString();
  }

  public void closeConnection() {
    con.disconnect();
  }

  public String readResponse() throws IOException {
    int status = con.getResponseCode();
    Reader streamReader = null;
    if (status != 200) {
      streamReader = new InputStreamReader(con.getErrorStream());
    } else {
      streamReader = new InputStreamReader(con.getInputStream());
    }

    return readStream(streamReader);
  }
}
