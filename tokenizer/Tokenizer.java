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
  private int lineNumber;
  private int horizontalPosition;

  private static final char eofChar = (char)0; // indicates end of file
  private enum StateName { START_S, IDENT_S, INT_S, ZERO_S, DOT_S, FLOAT_S, ZERO_ERROR_S, DONE_S}; // FSM

  // Constructor
  public Tokenizer (BufferedReader in, boolean echo) {
    this.inFile = in;
    this.echo = echo;
    this.debug = new TokenizerDebug();
    this.lineNumber = 1;
    this.horizontalPosition = 0;
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
      int startHorizontalPos = 0;
      int startLineNumber = 0;

      while (state != StateName.DONE_S) {
          char ch = getChar();
          switch (state) {
          case START_S:
              debug.show("\t>>> Entering state -- START_S: ", ch);
              startHorizontalPos = horizontalPosition;
              startLineNumber = lineNumber;
              if (ch == ' ') {
                  state = StateName.START_S;
              }
              else if (ch == eofChar) {
                  type  = Token.TokenType.EOF_T;
                  state = StateName.DONE_S;
              }
              else if (Character.isLetter(ch)) {
                name += ch;
                state = StateName.IDENT_S;
              }
              else if (ch == '0') {
                name += ch;
                state = StateName.ZERO_S;
                type = Token.TokenType.INT_T;
              }
              else if (Character.isDigit(ch)) {
                name += ch;
                state = StateName.INT_S;
              }
              else {
                  type = char2Token(ch);
                  name += ch;
                  state = StateName.DONE_S;
              }
              debug.show("\t<<< Leaving state -- START_S: ", ch);
              break;
          case ZERO_ERROR_S:
            debug.show("\t<<< Entering state -- ZERO_ERROR_S: ", ch);  
            type = Token.TokenType.ERROR_T;
            if (Character.isDigit(ch)) {
              name += ch;
              state = StateName.ZERO_ERROR_S;
              
            }
            else {
              putBackChar(ch);
              state = StateName.DONE_S;
            }
            debug.show("\t<<< Leaving state -- ZERO_ERROR_S: ", ch);    
            break;        
          case ZERO_S:
            debug.show("\t<<< Entering state -- ZERO_S: ", ch);            
            if (Character.isDigit(ch)) {
              name += ch;
              state = StateName.ZERO_ERROR_S;
              type = Token.TokenType.ERROR_T;
            }
            else if (ch == '.') {
              name += ch;
              state = StateName.DOT_S;
            }
            else {
              putBackChar(ch);
              state = StateName.DONE_S;
            }
            debug.show("\t<<< Leaving state -- ZERO_S: ", ch);
            break;
          case INT_S:
            debug.show("\t>>> Entering state -- INT_S: ", ch);
            if (Character.isDigit(ch)) {
              name += ch;
              state = StateName.INT_S;
            }
            else if (ch == '.') {
              name += ch;
              state = StateName.DOT_S;
            }
            else {
              putBackChar(ch);
              type = Token.TokenType.INT_T;
              state = StateName.DONE_S;
            }
            debug.show("\t<<< Leaving state -- INT_S: ", ch);
            break;
          case IDENT_S:
              debug.show("\t>>> Entering state -- ID_S: ", ch);
              if (Character.isLetter(ch)) {
                name += ch;
                  state = StateName.IDENT_S;
              }
              else {
                putBackChar(ch);
                type = string2Token(name);
                state = StateName.DONE_S;
              }
              debug.show("\t<<< Leaving state -- ID_S: ", ch);
              break;
          case DOT_S:
            debug.show("\t<<< Entering state -- DOT_S: ", ch);
            if (Character.isDigit(ch)) {
                name += ch;
                type = Token.TokenType.FLOAT_T;
                //if (type != Token.TokenType.ERROR_T)
                //  type = Token.TokenType.FLOAT_T;
            }
            else {
              putBackChar(ch);
              //type = Token.TokenType.ERROR_T; // added
              state = StateName.DONE_S;
            }
            debug.show("\t<<< Leaving state -- DOT_S: ", ch);
            break;
          case DONE_S: // Should never get here!  For completeness.
              debug.show("\t>>> Entering state -- DONE_S: ", ch);
              debug.show("\t<<< Leaving state -- DONE_S: ", ch);
              break;
          }

      }

      Token token = new Token(type, name);
      token.setLineNumber(startLineNumber);
      token.setHorizontalPosition(startHorizontalPos);

      debug.show("<<< Leaving getNextToken");
      return token;
  }

  private Token.TokenType string2Token(String str) {
    if (str.equals("int") || str.equals("float"))
      return Token.TokenType.TYPE_T;
    else
      return Token.TokenType.IDENT_T;
  }

  private Token.TokenType char2Token(char ch) {
    if (ch == '{') return Token.TokenType.LCB_T;
    else if (ch == '}') return Token.TokenType.RCB_T;
    else if (ch == '(') return Token.TokenType.LP_T;
    else if (ch == ')') return Token.TokenType.RP_T;
    else if (ch == '=') return Token.TokenType.ASSIGN_T;
    else if (ch == ',') return Token.TokenType.COMMA_T;
    else if (ch == '+') return Token.TokenType.ADD_T;
    else if (ch == '-') return Token.TokenType.SUB_T;
    else if (ch == '*') return Token.TokenType.MUL_T;
    else if (ch == '%') return Token.TokenType.MOD_T;
    else if (ch == '/') return Token.TokenType.DIV_T;
    else return Token.TokenType.ERROR_T;
  }
  // Helpers -- private methods

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

    horizontalPosition++;

    if (v == -1) ch = eofChar;
    else {
      ch = (char)v;
      if (echo) System.out.print(ch);
      if (ch == '\n') {
        ++lineNumber;
        horizontalPosition = 0;
      }
      if (ch == '\n' || ch == '\t') ch = ' ';
    }

    return ch;
  }

  // Pre:  inFile has a value
  // Post: inFile is the original inFile with ch added as its first character
  private void putBackChar(char ch) {
     debug.show(">>> Entering putBackChar");

     if (horizontalPosition > 0) {
       horizontalPosition--;
     } else {
       horizontalPosition = 0;
     }
     if (ch == '\n') {
       --lineNumber;
     }
     try { if (ch != eofChar) { inFile.reset(); }   }
     catch(IOException e)     { System.exit(0); }

     debug.show("<<< Leaving putBackChar");
  }
}
