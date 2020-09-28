package debug;

import java.util.*;

/**
  Debug is the top of a debugging hierarchy.

  When a command line argument is encountered (typically in the main method),
  the static method 'registerFlag' is called with the particular argument
  character as argument.  That character is added to 'commandLine'.

  When a debuggable class is instantiated, its constructor will instantiate
  its corresponding subclass of 'Debug' and call the method 'registerObject'
  and pass as argument its assigned command line character.
  'registerObject' will first assign a position p in 'flags' and initialize
  flags[p] to true if the argument character appears in 'commandLine' and
  false otherwise.  The value of 'p' is returned to the caller as the
  registration number.

  @author Jerud Mead (2009)
*/
public abstract class Debug {

   // a string of command line arguments, each specified by a known character
   private static String commandLine = "";

   // contains the position in 'flags' that can allocated next
   private static int flagNum = 0;

   // debug flag values for each command line flag
   private static ArrayList<Boolean> flags = new ArrayList<Boolean>();

   // Interface -- public methods

   public static void registerFlag(char ch) {
      commandLine += ch;
   }

   // Post: psn == (in)flagNum AND psn >= 0 AND flagNum == (in)flagNum+1
   //       flags.get(psn) == ( commandLine.indexOf(ch) != -1 ) AND
   //       return psn
   public int registerObject(char ch) {
       int psn = flagNum++;
       flags.add(psn,  commandLine.indexOf(ch) != -1 );
       return psn;
   }

   // Pre:  0 <= psn < FlagNum AND
   //       psn is a registration number sent from
   //       an instance of a subclass of Debug
   public void show(int psn, String msg) {
      if (flags.get(psn))
          System.out.println(msg);
   }
}
