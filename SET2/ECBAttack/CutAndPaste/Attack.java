package SET2.ECBAttack.CutAndPaste;

public class Attack {
  private int blockSize;
  public Attack() {
    getCipherBlockSize();
  }

  public void getCipherBlockSize() {
    char x = 'A';
    String gibbs;
    String input = "";
    int prevLength = 0;
    // append x until the length of the ciphertext change
    // the amount of change = block used 
    do {
      input += x;
      User profile = new User(input);
      gibbs = profile.send();

      if (prevLength == 0) {
        prevLength = gibbs.length();
      }
    }
    while (prevLength == gibbs.length());
    
    blockSize = (gibbs.length() - prevLength) / 2; 
  }

  public String getAdmin() {
    // profile encoding
    // email=xxx@gmail.com&uid=10&role=xxx
    // (email=xxx...)(adminzzz...)(yyy)@gmail.com&uid=10&role=
    // z is the pad length
    // above length must be divisible by blockSize
    // append random yyy to pad the text
    String goToAdminCipherText;
    String adminCipherText;
    User adminUser;

    String admin = Roles.ADMIN.toString();
    int padLength = blockSize - admin.length();
    for (int i = 0; i < padLength; i++) {
      admin += (char) padLength;
    }
    // build email
    String email = "";
    for (int i = 0; i < blockSize - 6; i++) {
      email += "X";
    }
    email += admin;
    int remainder = 23 % blockSize;
    for (int i = 0; i < blockSize - remainder; i++) {
      email += "X";
    }
    email += "@gmail.com";
    // admin ciphertext at the second block
    adminUser = new User(email);
    goToAdminCipherText = adminUser.send();
    adminCipherText = goToAdminCipherText.substring(blockSize * 2, blockSize * 4);
    goToAdminCipherText = goToAdminCipherText.substring(0, goToAdminCipherText.length() - (blockSize * 2)) + adminCipherText;
    return goToAdminCipherText;

  }
}
