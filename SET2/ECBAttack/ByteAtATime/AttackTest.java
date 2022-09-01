package SET2.ECBAttack.ByteAtATime;

public class AttackTest {
  public static void main(String[] args) {
    ByteAtATime decipher = new ByteAtATime();
    String x = decipher.attack();
    System.out.println("Message is: ");
    System.out.print(x);
  }
}
