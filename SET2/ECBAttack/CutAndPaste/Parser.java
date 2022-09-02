package SET2.ECBAttack.CutAndPaste;

public class Parser {
  public static String sanitizeInput(String input) {
    // remove & and =
    int x;
    String quoteMeta;
    StringBuilder temp = new StringBuilder(input);
    while ((x = temp.indexOf("&"))!= -1) {
      // quote
      // & -> '&'
      temp.deleteCharAt(x);
      quoteMeta = "\'" + "&" + "\'";
      input = input.substring(0, x) + quoteMeta + input.substring(x + 1, input.length());
    }

    while ((x = temp.indexOf("="))!= -1) {
      temp.deleteCharAt(x);
      quoteMeta = "\'" + "=" + "\'";
      input = input.substring(0, x) + quoteMeta + input.substring(x + 1, input.length());
    }

    while ((x = temp.indexOf(";"))!= -1) {
      temp.deleteCharAt(x);
      quoteMeta = "\'" + ";" + "\'";
      input = input.substring(0, x) + quoteMeta + input.substring(x + 1, input.length());
    }

    return input;
  }
}
