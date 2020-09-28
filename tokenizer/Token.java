package tokenizer;

/*
  Token class defines the structure of a Token object for production
  by the tokenizer and use by the parser.

  @author Jerud Mead (2009)
*/
public class Token {

  public enum TokenType {
    IDENT_T,  // identifier [a-zA-Z]+
    TYPE_T,   // type
    ERROR_T,  // error
    ASSIGN_T, // assignment operator =
    RCB_T,    // right curly bracket }
    LCB_T,    // left curly bracket {
    COMMA_T,  // comma ,
    ADD_T,    // addition +
    SUB_T,    // subtraction -
    MUL_T,    // multiplication *
    DIV_T,    // division /
    MOD_T,    // modulo %
    INT_T,    // integer 0 | [1-9][0-9]*
    FLOAT_T,  // float (0 | [1-9][0-9]*).[0-9]+
    LP_T,     // (
    RP_T,     // )
    EOF_T     // end of token
  };
  private TokenType type;
  private String name;
  private int lineNumber;
  private int horizontalPos;

  // Constructor
  public Token(TokenType t, String s) {
    type = t;
    name = s;
    lineNumber = 1;
    horizontalPos = 0;
  }

  // Interface -- public methods
  public TokenType getType() { return type; }
  public String getName() { return name; }
  public void setLineNumber(int lo) { lineNumber = lo; }
  public int getLineNumber() { return lineNumber; }
  public void setHorizontalPosition(int hpos) { horizontalPos = hpos; }
  public int getHorizontalPosition() { return horizontalPos; }
  public String toString() {
    return type.toString() + "( " + name + " )";
    //return "line " + lineNumber + ", hcol " + horizontalPos + ", " + type.toString() + "( " + name + " )";
  }
}
