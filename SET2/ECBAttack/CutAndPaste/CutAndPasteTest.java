package SET2.ECBAttack.CutAndPaste;

public class CutAndPasteTest {
  public static void main(String[] args) {
    // String email = "Aditya@gmail.com&role=user";
    // System.out.println(Parser.profileForEncoded(email));
    // String sent = Parser.encryptProfile(email);
    // System.out.println(sent);
    // User p = Parser.decryptProfile(sent);
    // System.out.println(p.toString());
    Attack atck = new Attack();
    String x = atck.getAdmin();
    User y = User.parse(User.receive(x));
    System.out.println(y.toString());
  }
}


