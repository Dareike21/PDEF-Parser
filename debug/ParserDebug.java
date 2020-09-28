package debug;

/**
  Mechanism for displaying debugging information for the Parser.
  The display of the information is controlled by the appearance of 'p' in the
  string of commandline arguments for the application.

  @author Jerud Mead (2009)
*/

public class ParserDebug extends Debug {

	private int regPsn; // position of registered object

	public ParserDebug() {
	   regPsn = registerObject('p');
	}

	public void show(String msg) {
	    show(regPsn, msg);
	}

	public void show(String msg, char ch) {
	    show(regPsn, msg + "('" + ch + "')");
	}
}
