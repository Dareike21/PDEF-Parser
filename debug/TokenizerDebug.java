package debug;

/**
  Mechanism for displaying debugging information for the Tokenizer.
  The display of the information is controlled by the appearance of 't' in the
  string of commandline arguments for the application.

  @author Jerud Mead (2009)
*/
public class TokenizerDebug extends Debug {

    private int regPsn; // position of registered object

    public TokenizerDebug() {
       regPsn = registerObject('t');
    }

    public void show(String msg) {
        show(regPsn, msg);
    }

    public void show(String msg, char ch) {
        show(regPsn, msg + "('" + ch + "')");
    }
}
