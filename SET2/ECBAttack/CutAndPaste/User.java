package SET2.ECBAttack.CutAndPaste;

import AES.AESKey;
import AES.ECB.ECB;
import Encoding.ASCII;
import Encoding.Hex;
import Encoding.UnrecognizedEncodingException;
import SET2.EncryptionOracle.EncryptionOracle;

public class User {
  private String email;
  private int uid;
  private String role;
  private static String key;

  public User(String email) {
    this.email = Parser.sanitizeInput(email);
    uid = 10;
    role = Roles.USER.toString();
    try {
      key = AESKey.generateRandomKey("HEX");
    } catch (UnrecognizedEncodingException e) {
      e.printStackTrace();
      System.exit(-1);
    }
  }
  
  public User(String email, int uid, String role) {
    this.email = email;
    this.uid = uid;
    this.role = role;
  }
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getRole() {
    return role;
  }

  public int getUid() {
    return uid;
  }

  public String toString() {
    String encodedUser = "";
    encodedUser += "email=" + email + "&";
    encodedUser += "uid=" + uid + "&";
    encodedUser += "role=" + role;
    return encodedUser;
  }

  public String send() {
    String encryptedMessage = "";
    System.out.println(this);
    encryptedMessage = EncryptionOracle.encryptECB(toString(), "ASCII", key);
    return encryptedMessage;
  }

  public static String receive(String encryptedMessage) {
    String keyInAscii = ASCII.ASCIIDecoder(Hex.fromHexToAscii(key));
    ECB ecb = new ECB(encryptedMessage, keyInAscii, "HEX", 1);
    byte[] temp = Hex.fromHexToAscii(ecb.decrypt());
    return ASCII.ASCIIDecoder(temp);
  }

  public static User parse(String encodedProfile) {
    String[] strings = encodedProfile.split("&", -1);
    String email = strings[0].split("=", 2)[1];
    int uid = Integer.parseInt(strings[1].split("=", 2)[1]);
    String role = strings[2].split("=", 2)[1];
    return new User(email, uid, role);
  }

  public static String createNewUser(String email) {
    User newUser = new User(email);
    return newUser.send();
  }
}
