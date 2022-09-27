package SET4.Challenge31;
import Encoding.Hex;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;


public class TimingAttackRobust {
  public static String signature = "";
  public static String file = "passwd.txt";
	public static final int MAX_COUNT = 10;
  public static long[] maxTimes = new long[20];
  public static long[] avgTimes = new long[20];
  public static int PICK = 10;
  public static long WINDOW = 2;

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

  public static Timing bruteForce(String signature, boolean verbose) throws IOException {
    Pair temp;
    String g;
    long cleanTiming, averageTime, startTime, endTime, f;

    long maxTiming = 0, sumTime = 0;
    byte x = 0;
    int padLength = Math.max(0, 18 - signature.length());
    String padding = "0".repeat(padLength);

    long[] timings = new long[MAX_COUNT];
    Pair[] cleanTimings = new Pair[256];
    sendRequest("0000000000000000000000000000000000000000");
    for (int i = 0; i < 256; i++) {
      g = signature + Hex.hexEncoder(x);
      g += padding;
      if (verbose) System.out.println("Sent signature: " + g);
      for (int j = 0; j < MAX_COUNT; j++) {
        // request time
        startTime = System.currentTimeMillis();
        sendRequest(g);
        endTime = System.currentTimeMillis();
        // get process time
        f = endTime - startTime;
        timings[j] = f;
        maxTiming = Math.max(f, maxTiming);
      }
      // remove noise from timing
      cleanTiming = WeightedAverage.cleanNoise(timings, maxTiming);

      sumTime += cleanTiming;
      temp = new Pair(i, cleanTiming);
      cleanTimings[i] = temp;
      x++;
    }

    averageTime = sumTime / 256;
    return new Timing(cleanTimings, averageTime);
  }

  public static Timing getBytes(Pair[] timings, long averageTime) throws IOException, ByteNotFoundException {
    String g, f;
    Timing res;
    // from potential bytes check if 
    Comparator<Pair> c = Collections.reverseOrder(Comparator.comparingLong((o) -> o.timing));
    // sort in reverse order
    Arrays.sort(timings, c);
    System.out.println("Top " + PICK + " bytes with the biggest timing");
    for (int i = 0; i < PICK; i++) {
      System.out.println("Bytes: " + timings[i].bytes + " ,timing: " + timings[i].timing);
    }
    
    // check the first N bytes
    System.out.println("Trying each bytes...");
    for (int i = 0; i < PICK; i++) {
      f = Hex.hexEncoder((byte) timings[i].bytes);
      g = signature + f;
      res = bruteForce(g, false);
      if (res.averageTime > averageTime + WINDOW) {
        System.out.println("Found correct bytes: " + f);
        // update signature
        signature += f;
        return res;
      }
      System.out.println("Last Average Time: " + averageTime + ", current Average Time: " + res.averageTime);
      System.out.println("Byte " + f +  ": false");
    }

    throw new ByteNotFoundException("Failed To Find a the correct bytes. Rebuild from " + signature);
  }

  public static void attack() throws IOException, ByteNotFoundException {
    System.out.println("INIT Timing with signature: " + signature);
    Timing f = bruteForce(signature, false); // init timing value
    System.out.println("Init Done!");
    System.out.println("Bruteforce signature initiated...");
    for (int i = (signature.length() / 2) + 1; i <= 20; i++ ) {
      f = getBytes(f.timing, f.averageTime);
      System.out.println("Finding next bytes...");
    }
  }

  
  public static void main(String[] args) throws IOException, ByteNotFoundException {
		System.out.println("ATTACK");
		String f;
	
		if (args.length == 1) {
			signature = args[0];
			System.out.println("Rebuilding from: " + signature);
		}
    
    attack();

		System.out.println("Signature found: " + signature);
		System.out.println("Sending request with valid signature!");
		f = sendRequest(signature);
		System.out.println(f);
  }
}