package exceptions;
/*
This class is the more abstract exception for the PDef system. 
This exception expects all necessary information to be transmitted by the argument to the constructor. 
That information is stored in the part of the PDefException state defined in the Java class Exception.
If more fine grained exception handling is to be done then appropriate new classes should be defined
as subclasses of this one.

*/
public class PDefException extends Exception {

	public PDefException(String msg) { super(msg); }
	public void print() {
		System.out.println(super.getMessage());
	}

}