package SET2.ECBAttack.CutAndPaste;

public class Parser {
  public static String quoteMetaChar(String input, char meteChar) {
    int x;
    int startIdx = 0;
    String quoteMeta;
    while ((x = input.indexOf(meteChar, startIdx))!= -1) {
      // quote
      // & -> '&'
      quoteMeta = "\'" + meteChar + "\'";
      input = input.substring(0, x) + quoteMeta + input.substring(x + 1, input.length());
      startIdx = x + 2;
    }

    return input;
  }
  public static String sanitizeInput(String input) {
    // remove & and =
    char[] meteChars = {'&', '=', ';'};

    for (char x : meteChars) {
      input = quoteMetaChar(input, x);
    }

    return input;
  }
}
