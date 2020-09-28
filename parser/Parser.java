package parser;

import debug.*;
import tokenizer.*;

public class Parser {

	// Class Invariant:
	// The data member currentToken is a
	// reference to the next token of interest.
	private Tokenizer tokenStream;
	private ParserDebug debug;
	private Token currentToken;

	// Pre: tokenStream has a value
	// Post: debug == new ParserDebug() AND
	// 		this.tokenStream == tokenStream AND
	// 		class invariant is true
	public Parser(Tokenizer tokenStream) {
		this.debug = new ParserDebug();
		this.tokenStream = tokenStream;

		// ensure class invariant is true for call to parseProgram
		currentToken = tokenStream.getNextToken();
	}

	// Grammar Rule: Program --> Block eotT
	public void parseProgram() {
		debug.show(">>> Entering parseProgram");

		parseBlock();
		consume(Token.TokenType.EOF_T);

		debug.show("<<< Leaving parseProgram");
	}

	// Grammar Rule: Block --> lcbT StmtList rcbT
	private void parseBlock() {
		debug.show(">>> Entering parseBlock");

		consume(Token.TokenType.LCB_T);
		parseStmtList();
		consume(Token.TokenType.RCB_T);

		debug.show("<<< Leaving parseBlock");
	}

	// Grammar Rule: StmtList --> Stmt [ commaT Stmt ]
	private void parseStmtList() {
		debug.show(">>> Entering parseStmtList");

		parseStmt();
		while( currentToken.getType() == Token.TokenType.COMMA_T ) {
			consume(Token.TokenType.COMMA_T);
			parseStmt();
		}

		debug.show("<<< Leaving parseStmtList");
	}

	// Grammar Rule: Stmt --> Assignment | Declaration | Block
	private void parseStmt() {
		debug.show(">>> Entering parseStmt");
		debug.show("<<< Leaving parseStmt");
	}

	// Grammar Rule: Assignment --> identT assign identT
	private void parseAssignment() {
		debug.show(">>> Entering parseAssignment");
		debug.show("<<< Leaving parseAssignment");
	}

	// Grammar Rule: Declaration --> typeT identT
	private void parseDeclaration() {
		debug.show(">>> Entering parseDeclaration");
		debug.show("<<< Leaving parseDeclaration");
	}

	private void error() {}

	private void consume(Token.TokenType ttype) {
		if (currentToken.getType() != ttype) {
			System.out.println("Expected to see token " + ttype +
				" but saw token " + currentToken.getType());
			System.exit(0);
		}
		currentToken = tokenStream.getNextToken();
	}

}