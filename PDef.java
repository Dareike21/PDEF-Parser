import java.io.*;
import tokenizer.*;
import debug.*;
import parser.*;

/*
Driver which tests the Token and Tokenizer classes. The method main implements
command line entry of both an input file name and a string of (character) arguments,
ach of which signals a particular debugging capability:
    char     capability
    'e'      echo to the screen the input data as it is read
    't'      turn on debugging code in the Tokenizer object

If the first argument is not a valid input file name, then the program will
terminate with an error message indicating the problem.

The output from main will be a sequence of lines, each containing:
- the name of a token (from Token.TokenType) along with the string value of the token,
- the line number where it occurred, and
- the position on the line where its first character appeared.

@author Jerud Mead (2009)
*/
public class PDef {

    public static void main(String[] args) {

        // input character stream
        BufferedReader in = null;

        // 'true' if input is echoed (i.e., 'e' appears as command line argument)
        boolean echo = false;

        // numer of command line arguments (doesn't include command name)
        int numArgs = args.length;

        if (numArgs < 1) {
            // There must be a file name!
            System.out.println("Not enough arguments!\n");
            System.exit(0);
        }
        else  {
            // args[0] is the data file name
            try {
               in = new BufferedReader(new FileReader(args[0]));
               if (numArgs > 1)  // args[1] holds debug flags
                  for (int i = 0; i != args[1].length(); i++) {
                      switch (args[1].charAt(i)) {
                        case 'e': echo = true; break;
                        case 't': Debug.registerFlag ('t'); break;
                        case 'p': Debug.registerFlag ('p'); break;
                      }
                      // ignore invalid flag names
                  }
            }
            catch (FileNotFoundException e) {
               System.out.printf("Could not open file `%s'\n", args[0]);
               System.exit(0);
            }
        }

        Tokenizer tokenStream = new Tokenizer(in, echo);
        Parser parse = new Parser(tokenStream);
        
        parse.parseProgram();
        System.out.println("Program parsed!");
    }
}
