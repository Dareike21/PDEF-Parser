package tokenizer;

/*
  Token class defines the structure of a Token object for production
  by the tokenizer and use by the parser.

  @author Jerud Mead (2009)
*/
public class Token {

  public enum TokenType {
    IDENT_T,  // identifier
    TYPE_T,   // type
    ERROR_T,  // error
    ASSIGN_T, // assignment operator =
    RCB_T,    // right curly bracket }
    LCB_T,    // left curly bracket {
    COMMA_T,  // comma ,
    EOF_T     // end of token
  };
  private TokenType type;
  private String name;

  // Constructor
  public Token(TokenType t, String s) {
    type = t;
    name = s;
  }

  // Interface -- public methods
  public TokenType getType() { return type; }
  public String getName() { return name; }
  public String toString() {
    return type.toString() + "( " + name + " )";
  }

}
