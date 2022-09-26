package SET4.Challenge31;
import Encoding.Hex;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;


public class TimingAttackRobust {
  public static String file = "passwd.txt";
	public static final int MAX_COUNT = 10;
  public static final int MAX_RETRY = 5;
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

  public static boolean check(String signature, long lastAverageTime) throws IOException {
    String g;

    long maxTiming = 0;
    long sumTime = 0;

    long startTime, endTime, f;
    long cleanTiming, averageTime;
		String padding = "0".repeat(40 - (signature.length() + 2));
    byte x = 0;

    long[] timings = new long[MAX_RETRY];
    for (int i = 0; i < 256; i++) {
      for (int j = 0; j < MAX_RETRY; j++) {
        g = signature + Hex.hexEncoder(x);
        g += padding; // Padding with gibbersih
        // System.out.println("Sent signature: " + g);
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
      x++;
    }
		
		System.out.println(lastAverageTime);
    averageTime = sumTime / 256;
    System.out.println(averageTime);
    return averageTime > lastAverageTime + WINDOW;
  }

  public static String attack(String signature) throws IOException {
    // get potential bytes that has the highest timing
    String g;

    long maxTiming = 0;
    long sumTime = 0;

    long startTime, endTime, f;
    long cleanTiming, averageTime;

    long[] timings = new long[MAX_COUNT];
    Pair[] cleanTimings = new Pair[256];

    Pair temp;
    byte x = 0;
    String padding = "0".repeat(40 - (signature.length() + 2));

    sendRequest("0000000000000000000000000000000000000000");
    
    for (int i = 0; i < 256; i++) {
      g = signature + Hex.hexEncoder(x);
      g += padding;
      System.out.println("Sent signature: " + g);
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
    return getBytes(signature, cleanTimings, averageTime);
  }

  public static String getBytes(String signature, Pair[] timings, long averageTime) throws IOException {
    String g, f;
    // from potential bytes check if 
    Comparator<Pair> c = Collections.reverseOrder(Comparator.comparingLong((o) -> o.timing));
    // sort in reverse order
    Arrays.sort(timings, c);
    for (int i = 0; i < PICK; i++) {
      System.out.println("Bytes: " + timings[i].bytes + " ,timing: " + timings[i].timing);
    }
    
    // check the first N bytes
    for (int i = 0; i < PICK; i++) {
      f = Hex.hexEncoder((byte) timings[i].bytes);
      g = signature + f;
      if (check(g, averageTime)) {
        return f;
      }
    }

    return "";
  }
  public static void main(String[] args) throws IOException {
		System.out.println("ATTACK MODE");
		String signature = "";
	
		if (args.length == 1) {
			signature = args[0];
			System.out.println("Rebuilding from: " + signature);
		}
    
    String f;
    for (int i = (signature.length() / 2) + 1; i <= 20; i++) {
			System.out.println("BytesID: " + i);
			f = attack(signature);
      if (f.equals("")) {
        System.out.println("Failed!");
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