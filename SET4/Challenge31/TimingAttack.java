package SET4.Challenge31;

import java.io.IOException;

import Encoding.Hex;

public class TimingAttack {
  public static String file = "passwd.txt";
  public static long processTime = -1;
  public static long offsetTime = 0;
  public static final long WINDOW = 10;
	public static final int MAX_COUNT = 5;
	public static final int RETRY_COUNT = 3;

  public static void init() throws IOException {
    // find byte process tiem
    long maxTime = 0;
    long sumMaxTime = 0;
		long sumTime = 0;
    long startTime, endTime, f;
    String signature = "0".repeat(38);
    byte x = 0;

    for (int count = 0; count < MAX_COUNT; count++) {
      for (int i = 0; i < 255; i++) {
        System.out.println(Hex.hexEncoder(x));
        signature = Hex.hexEncoder(x) + signature;
        // time request
        startTime = System.currentTimeMillis();
        sendRequest(signature);
        endTime = System.currentTimeMillis();
        // get process time
        f = endTime - startTime;
  
        System.out.println("Time: " + f);
				sumTime += f;
        maxTime = Math.max(maxTime, f);
  
        x++;
      }
	
			offsetTime = Math.max(offsetTime, (sumTime - maxTime) / 254);
      sumMaxTime += maxTime;
      maxTime = 0;
			sumTime = 0;
    }
		
		System.out.println(sumMaxTime);
		processTime = sumMaxTime / MAX_COUNT;
		System.out.println("Offset time is: " + offsetTime);
    System.out.println("Process time is: " + processTime);
  }

  public static String requestQuery(String signature) {
    String fileString = "file="+ file;
    String signatureString = "signature=" + signature;
    return fileString + "&" + signatureString;
  }
  
  public static String sendRequest(String signature) throws IOException {
    // send request to server
    String spec = "http://localhost:5000/test?" + requestQuery(signature);
    ServerConnection connection = new ServerConnection(spec, "GET");
    String response = connection.readResponse();
    //connection.closeConnection();
    return response;
  }

  public static boolean isTimeEqual(long actual) {
	  System.out.println(actual);
    // min(actual) = processTime - (offsetTime + WINDOW);
    if (actual > processTime - (offsetTime + WINDOW)) {
      return true;
    }

    return false;
  }
	
	public static boolean retry(String signature, int bytesID) throws IOException {
		int successCount = 0; 
		long f, c, startTime, endTime;
		for (int i = 0; i < RETRY_COUNT; i++) {
			startTime = System.currentTimeMillis();
      sendRequest(signature);
      endTime = System.currentTimeMillis();
      // get process time
      f = endTime - startTime;
			c = (processTime - offsetTime) * (bytesID - 1);
			if (isTimeEqual(f - c)) {
				successCount += 1;
			}
		}
		return successCount == RETRY_COUNT;
	}
	
  public static String attack(String signature, int bytesID) throws IOException {
    String g;
    long startTime, endTime, f, c;
    byte x = 0;
    for (int i = 0; i < 255; i++) {
      g = signature + Hex.hexEncoder(x);
      g += "0".repeat(40 - g.length()); // Padding with gibbersih
      System.out.println("Sent signature: " + g);
      // request time
      startTime = System.currentTimeMillis();
      sendRequest(g);
      endTime = System.currentTimeMillis();
      // get process time
      f = endTime - startTime;
			System.out.println("Elapsed time: " + f);
			c = (processTime - offsetTime) * (bytesID - 1);
			System.out.println("Offset time: " + c);
			if (isTimeEqual(f - c)) {
        // could be a false answer
				System.out.println("Potential Answer. Trying retrying for " + RETRY_COUNT + " Times");
				if (retry(g, bytesID)) return Hex.hexEncoder(x);
				System.out.println("False Answer");
      }
      x++;
    }

    return "";
  }

  public static void main(String[] args) throws IOException {
    // ATTACk
    // HMAC using SHA-1
    // Therefore there are 20 bytes to check = 40 Hex character
    /* if false then time is small, else there are delay */
		System.out.println("INIT");
		init();
		System.out.println("INIT Finished");
		System.out.println("ATTACK MODE");
		String signature = "";
	
		if (args.length == 1) {
			signature = args[0];
			System.out.println("Rebuilding from: " + signature);
		}
    
    String f;
    for (int i = (signature.length() / 2) + 1; i <= 20; i++) {
			System.out.println("BytesID: " + i);
			f = attack(signature, i);
			
			if (f == "") {
				System.out.println("Error: Can't find bytes");
				if (signature.length() > 0) System.out.println("Try to run from: " + signature.substring(0, signature.length() - 2));
				System.exit(-1);
			}
			
			System.out.println("Correct bytes: " + f);
			signature += f;
		}
		
		System.out.println("Signature found: " + signature);
		System.out.println("Sending request with valid signature!");
		f = sendRequest(signature);
		System.out.println(f);
	}
}
