package SET2.ECBAttack.CutAndPaste;

public class Parser {
  public static User profileFor(String email) {
    // remove & and =
    int x;
    while ((x = email.indexOf("&"))!= -1) {
      // remove & at x
      email = email.substring(0, x) + email.substring(x + 1, email.length() - 1);
    }

    while ((x = email.indexOf("="))!= -1) {
      // remove & at x
      email = email.substring(0, x) + email.substring(x + 1, email.length() - 1);
    }

    return new User(email);
  }
}
