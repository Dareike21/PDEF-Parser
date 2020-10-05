package exceptions;
import tokenizer.*;

public class ParseException extends PDefException
{
    Token token;

    public ParseException(String msg, Token t)
    {
        super(msg + ", but saw the token " + t);
        token = t;
    }

    public Token getToken()
    {
        return token;
    }
}
