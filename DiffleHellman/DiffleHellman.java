package DiffleHellman;
import java.math.BigInteger;

import Encoding.Hex;
import Hash.SHA256;
import PRNG.MersenneTwister.MT19937;

public class DiffleHellman {
  public static void main(String[] args) {
    String num = "ffffffffffffffffc90fdaa22168c234c4c6628b80dc1cd129024e088a67cc74020bbea63b139b22514a08798e3404ddef9519b3cd3a431b302b0a6df25f14374fe1356d6d51c245e485b576625e7ec6f44c42e9a637ed6b0bf5cb6f406b7edee386bfb5a899fa5ae9f24117c4b1fe649286651ece45b3dc2007cb8a163bf0598da48361c55d39a69163fa8fd24cf5f83655d23dca3ad961c62f356208552bb9ed529077096966d670c354e4abc9804f1746c08ca237327ffffffffffffffff";
    BigInteger p = new BigInteger(num, 16);
    BigInteger g = new BigInteger("2");

    MT19937 generator = new MT19937((int)System.currentTimeMillis());
    
    long a =  generator.nextInt();
    BigInteger bigA = new BigInteger(Long.toString(a)).remainder(p);

    long b = generator.nextInt();
    BigInteger bigB = new BigInteger(Long.toString(b)).remainder(p);

    // produce public key
    BigInteger A = ModExp.modExp(g, bigA, p);
    BigInteger B = ModExp.modExp(g, bigB, p);

    // create session key
    BigInteger s1 = ModExp.modExp(B, bigA, p);
    // System.out.println(s1.toString(10));
    BigInteger s2 = ModExp.modExp(A, bigB, p);
    // System.out.println(s2.toString(10));

    System.out.println(s1.equals(s2));
    
    String session = s1.toString(16);
    SHA256 sha2 = new SHA256(session);
    String hash = sha2.digest();
    
    // first 128 bit for key and the next 128 bit for hmac
    String key = hash.substring(0, hash.length() / 2);
    String keyHMAC = hash.substring(hash.length() / 2);
    // get key
    System.out.println(key);
    System.out.println(keyHMAC);
  }
}
