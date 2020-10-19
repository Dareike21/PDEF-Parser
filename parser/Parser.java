package parser;

import debug.*;
import tokenizer.*;
import exceptions.*;

import javax.naming.PartialResultException;

/*
This class defines a recursive descent parser for the language PDef-light with context free grammar as follows:

    Program     --> Block
    Block       --> lcbT StmtList rcbT
    StmtList    --> Stmt { commaT Stmt }
    Stmt        --> Declaration | Assignment | Block
    Declaration --> typeT identT
    Assignment  --> identT assignT identT
    
In this grammar the terminals lcbT, rcbT, commaT, identT, typeT, assignT represent tokens in the lexical description for PDef-light.

*/

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
	public void parseProgram() throws ParseException{
		debug.show(">>> Entering parseProgram");

		parseBlock();
		consume(Token.TokenType.EOF_T);

		debug.show("<<< Leaving parseProgram");
	}

	// Grammar Rule: Block --> lcbT StmtList rcbT
	private void parseBlock() throws ParseException{
		debug.show(">>> Entering parseBlock");

		consume(Token.TokenType.LCB_T);
		parseStmtList();
		consume(Token.TokenType.RCB_T);

		debug.show("<<< Leaving parseBlock");
	}

	// Grammar Rule: StmtList --> Stmt [ commaT Stmt ]
	private void parseStmtList() throws ParseException {
		debug.show(">>> Entering parseStmtList");

		parseStmt();
		if( currentToken.getType() == Token.TokenType.COMMA_T ) {
			consume(Token.TokenType.COMMA_T);
			parseStmtList();
		}

		debug.show("<<< Leaving parseStmtList");
	}

	// Grammar Rule: Stmt --> Assignment | Declaration | Block
	private void parseStmt() throws ParseException{
		debug.show(">>> Entering parseStmt");
		try {
			switch (currentToken.getType()) {
				case TYPE_T:
					parseDeclaration();
					break;
				case IDENT_T:
					parseAssignment();
					break;
				case LCB_T:
					parseBlock();
					break;
				default:
					throw new ParseException("Expected to see TYPE_T, IDENT_T, LCB_T", currentToken);
			}
			if(!canFollowStatement(currentToken))
			{
				throw new ParseException("Expected a comma, right brace or end of file token", currentToken);
			}
		}catch (ParseException exc)
		{
			exc.print();
			consume2StatementEnd();
		}
		debug.show("<<< Leaving parseStmt");
	}

	// Grammar Rule: Assignment --> identT assign identT
	private void parseAssignment() throws ParseException{
		debug.show(">>> Entering parseAssignment");
		consume(Token.TokenType.IDENT_T);
		consume(Token.TokenType.ASSIGN_T);
		parseExpression();
		debug.show("<<< Leaving parseAssignment");
	}

	// Grammar Rule: Declaration --> typeT identT
	private void parseDeclaration() throws ParseException{
		debug.show(">>> Entering parseDeclaration");
		consume(Token.TokenType.TYPE_T);
		consume(Token.TokenType.IDENT_T);
		debug.show("<<< Leaving parseDeclaration");
	}

	//Grammar Rule: Term --> Factor Term'
	private void parseExpression() throws ParseException {
		debug.show(">>>  Entering parseExpression");
		parseTerm();
		parseExpressionP();
		debug.show(">>>  Leaving parseExpression");
	}

	//Grammar Rule: (addT | subT) Exp'
	private void parseExpressionP() throws ParseException
	{
		debug.show(">>>  Entering parseExpression '");
		switch (currentToken.getType()) {
			case ADD_T:
				consume(Token.TokenType.ADD_T);
				break;
			case SUB_T:
				consume(Token.TokenType.SUB_T);
				break;
			default:
				throw new ParseException("Expected to see ADD_T or SUB_T", currentToken);
		}
		debug.show(">>>  Leaving parseExpression '");
	}

	//Grammar Rule: Term --> Factor Term'
	private void parseTerm() throws ParseException
	{
		debug.show(">>>  Entering parseTerm");
		parseFactor();
		parseTermP();
		debug.show(">>>  Leaving parseTerm");
	}

	//Grammar Rule: (mulT | divT | modT) Term'
	private void parseTermP() throws ParseException
	{
		debug.show(">>>  Entering parseTerm '");
		switch(currentToken.getType())
		{
			case MUL_T:
				consume(Token.TokenType.MUL_T);
				break;
			case DIV_T:
				consume(Token.TokenType.DIV_T);
				break;
			case MOD_T:
				consume(Token.TokenType.MOD_T);
				break;
		}
		debug.show(">>>  Leaving parseTerm '");
	}

	//Grammar Rule: intT | floatT | IdenT | LPT | LPT Exp RPT
	private void parseFactor() throws ParseException
	{
		debug.show(">>>  Entering parseFactor");
		switch(currentToken.getType())
		{
			case INT_T:
				consume(Token.TokenType.INT_T);
				break;
			case FLOAT_T:
				consume(Token.TokenType.FLOAT_T);
				break;
			case IDENT_T:
				consume(Token.TokenType.IDENT_T);
				break;
			case LP_T:
				consume(Token.TokenType.LP_T);
				parseExpression();
				consume(Token.TokenType.RP_T);
		}
		debug.show(">>>  Leaving parseFactor");
	}

	private void consume(Token.TokenType ttype) throws ParseException
	{
		if (currentToken.getType() != ttype)
		{
			String msg = "Expected to see token " + ttype;
			throw new ParseException(msg, currentToken);
		}
		currentToken = tokenStream.getNextToken();
	}

	private void consume2StatementEnd() {
		Token.TokenType type = currentToken.getType();
		while (!canFollowStatement(currentToken)) {
			currentToken = tokenStream.getNextToken();
			type = currentToken.getType();
		}
	}

	private boolean canFollowStatement(Token t)
	{
		Token.TokenType type = t.getType();
		return (type == Token.TokenType.COMMA_T || type == Token.TokenType.RCB_T || type == Token.TokenType.EOF_T);
	}
}