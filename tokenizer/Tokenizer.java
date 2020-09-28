package tokenizer;

import java.io.BufferedReader;
import java.io.IOException;

import debug.*;

/*
  Tokenizer extracts token values from the input stream, where the tokens
  are defined by the following regular expression:

  Regular Expression    Token.TokenType    Terminating character
  ,                     COMMA_T            any character
  =                     ASSIGN_T           any character
  {                     LCB_T              any character
  }                     RCB_T              any character
  int | float           TYPE_T             ',={}' or whitespace
  [a-zA-Z]+             IDENT_T            ',={}' or whitespace

  @author Jerud Mead (2009)
*/
public class Tokenizer {

  private BufferedReader inFile; // input stream
  private boolean echo = false;  // whether input characters are echoed to stdout
  private TokenizerDebug debug;  // controls display of debug info

  private static final char eofChar = (char)0; // indicates end of file
  private enum StateName { START_S, IDENT_S, DONE_S}; // FSM

  // Constructor
  public Tokenizer (BufferedReader in, boolean echo) {
    this.inFile = in;
    this.echo = echo;
    this.debug = new TokenizerDebug();
  }

  // Interface -- public methods

  // Pre:  inFile has a value
  // Post: inFile has initial blanks removed as well as the characters
  //       of the next token on inFile.  The tokens are determined by the
  //       finite state machine for the following regular expression
  //       Regular Expression
  //           ,
  //           =
  //           {
  //           }
  //       int | float
  //        [a-zA-Z]+
  public Token getNextToken() {
      debug.show(">>> Entering getNextToken");
      StateName state = StateName.START_S;
      Token.TokenType type = Token.TokenType.ERROR_T;
      String name  = "";

      while (state != StateName.DONE_S) {
          char ch = getChar();
          switch (state) {
          case START_S:
              debug.show("\t>>> Entering state -- START_S: ", ch);
              if (ch == ' ') {
                  state = StateName.START_S;
              }
              else if (ch == eofChar) {
                  type  = Token.TokenType.EOF_T;
                  state = StateName.DONE_S;
              }
              // @todo Activity 31 & 32
              // @todo Activity 35 part (2)
              else {
                  type = Token.TokenType.IDENT_T;
                  name += ch;
                  state = StateName.DONE_S;
              }
              debug.show("\t<<< Leaving state -- START_S: ", ch);
              break;
          case IDENT_S:
              debug.show("\t>>> Entering state -- ID_S: ", ch);
              // @todo: Activity 33 & 34
              debug.show("\t<<< Leaving state -- ID_S: ", ch);
              break;
          case DONE_S: // Should never get here!  For completeness.
              debug.show("\t>>> Entering state -- DONE_S: ", ch);
              debug.show("\t<<< Leaving state -- DONE_S: ", ch);
              break;
          }

      }

      Token token = new Token(type, name);

      debug.show("<<< Leaving getNextToken");
      return token;
  }

  // Helpers -- private methods

  // @todo Activity 32: create function char2Token

  // @todo Activity 34: create function string2Token

  // Pre:  ch is the character at the head of inFile
  // Post: inFile is original inFile with ch removed AND
  //       return ch -- Except
  //       if inFile.eof is true return tab character
  //       if ch is tab or eol return blank character
  private char getChar() {
  	char ch;
  	int v = 0;

  	try { inFile.mark(1); v = inFile.read(); }
  	catch (IOException e) {
  		System.out.println("Problem reading open input file!");
  		System.exit(0);
  	}

  	if (v == -1) {
      ch = eofChar;
    }
  	else {
  		ch = (char)v;
  		if (echo) System.out.print(ch);
  		if (ch == '\n' || ch == '\t') ch = ' ';
  	}

  	return ch;
  }

  // Pre:  inFile has a value
  // Post: inFile is the original inFile with ch added as its first character
  private void putBackChar(char ch) {
     debug.show(">>> Entering putBackChar");

     try { if (ch != eofChar) { inFile.reset(); }   }
     catch(IOException e)     { System.exit(0); }

     debug.show("<<< Leaving putBackChar");
  }
}
