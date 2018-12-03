// ConcreteSyntax.java

// Implementation of the Scanner for JAY

// This code DOES NOT implement a scanner for JAY. You have to complete
// the code and also make sure it implements a scanner for JAY - not something
// else.

// SCANNER FOR JAY

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TokenStream {

	// CHECK THE WHOLE CODE
	// 229 lines
	
	// 3 instance vars

	//isEof (is end of file)
	private boolean isEof = false;

	private char nextChar = ' '; // next character in input stream

	private char followingChar = ' '; //the character that follows after nextChar

	private BufferedReader input;

	// This function was added to make the main.
	public boolean isEoFile() {
		return isEof;
	}

	// Pass a filename for the program text as a source for the TokenStream.
	public TokenStream(String fileName) {
		try {
			input = new BufferedReader(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + fileName);
			// System.exit(1); // Removed to allow ScannerDemo to continue
			// running after the input file is not found.
			isEof = true;
		}
	}
	//returns just discovered token
	public Token nextToken() { // Return next token type and value.
		// skipNewLine();
		Token t = new Token();
		t.setType("Other"); //error
		t.setValue("");

		// First check for whitespace and bypass it.
		skipWhiteSpace();
		// Then check for a comment, and bypass it
		// but remember that / is also a division operator.
		while (nextChar == '/') {
			// Changed if to while to avoid the 2nd line being printed when
			// there
			// are two comment lines in a row.
			nextChar = readChar();
			if (nextChar == '/') { // If / is followed by another /
				skipWhiteSpace();
				skipComments();
				// COMPLETED
				// look for <cr>, <lf>, <ff>
				skipNewLine();
				//sometimes a tab is considered 3 spaces so call sws one more time
				skipWhiteSpace();
			} else {
					// A slash followed by anything else must be an operator.
				// t.setValue("/");
				// t.setType("Division Operator");
				// return t; // THIS CODE IS MOVED FURTHER DOWN
				if (nextChar == 92) {
					t.setValue("/" + nextChar);
					nextChar = readChar();
				}else{
					t.setValue("/");
				}
				t.setType("Operator");
				return t;
			}
			// skipNewLine();
		}
		// Then check for an operator; recover 2-character operators
		// as well as 1-character ones.
		if (isOperator(nextChar)) {
			t.setType("Operator");
			t.setValue(t.getValue() + nextChar);
			
			//if its not a comment get the following char.
			if (nextChar != '/'){
				followingChar = readChar();
			}
			
			switch (nextChar) {
			case '<':
				if(followingChar == '='){
					t.setValue("<=");
					nextChar = readChar();
					return t;
				}else{
					t.setValue("<");
					nextChar = followingChar;
					return t;
				}

			case '>':
				
				if(followingChar == '='){
					t.setValue(">=");
					nextChar = readChar();
					return t;
				}else{
					t.setValue(">");
					nextChar = followingChar;
					return t;
				}

			case '=':
				// t.setValue(t.getValue() + nextChar);
				// look for <=, >=, !=, ==
				// TO BE COMPLETED TODO: I think its completed? Test it
				if(followingChar == '='){
					t.setValue("==");
					nextChar = readChar();
					return t;
				}else{
					t.setValue("=");
					nextChar = readChar();
					return t;
				}
				
			case '|': // look for the OR operator, \/
				// TO BE COMPLETED TODO: TEST - IT'S COMPLETED
				if(followingChar == '|'){
					t.setValue("||");
					nextChar = readChar();
					return t;
				}else{
					t.setValue("|");
					t.setType("Lexical Error");
					nextChar = readChar();
					return t;
				}
			case '&':
				if(followingChar == '&'){
					t.setValue("&&");
					nextChar = readChar();
					return t;
				}else{
					t.setValue("&");
					t.setType("Lexical Error");
					nextChar = readChar();
					return t;
				}
			case 92:
				if(followingChar == '/'){
					t.setValue(nextChar + "/");
					nextChar = readChar();
					return t;
				}
			default: // all other operators
				// check for double operators
				if (nextChar == followingChar) {
				// doubleOpArry[0] = nextChar;
				// doubleOpArry[1] = followingChar;
				// 	for(char i : doubleOpArry){
					// System.out.println("PL00P");
					// t.setType("Operator");
					t.setValue(t.getValue());
					// System.out.println(t.getValue()+" "+followingChar);
					return t;
					// }
				}else{
					nextChar = readChar();
					// followingChar = readChar();
					// System.out.println("ALL OTHER OPERATORS NEXT CHAR: "+ nextChar+" " +
					// followingChar);
					// skipNewLine();
					return t;
				}
			}
		}
		
		// Then check for a separator.
		if (isSeparator(nextChar)) {
			t.setType("Separator");
			t.setValue(t.getValue() + nextChar);
			nextChar = readChar();
			skipNewLine();
			return t;
		}

		// Then check for an identifier, keyword, or literal.
		if (isLetter(nextChar)) {
			// get an identifier
			t.setType("Identifier");
			while ((isLetter(nextChar) || isDigit(nextChar))) {
				t.setValue(t.getValue() + nextChar);
				nextChar = readChar();
			}
			// see if it is a boolean literal
			if(isBooleanLiteral(t.getValue()))
				t.setType("Boolean-Literal");
			// now see if this is a keyword
			if (isKeyword(t.getValue()))
				t.setType("Keyword");
			if (isEndOfToken(nextChar)) // If token is valid, returns.
				skipNewLine();
				skipWhiteSpace();
				return t;
		}
		// followingChar = readChar();
		if (isDigit(nextChar)) { // check for integers
			t.setType("Integer-Literal");
			while (isDigit(nextChar)) {
				t.setValue(t.getValue() + nextChar);
				nextChar = readChar();
			}
			// An Integer-Literal is to be only followed by a space,
			// an operator, or a separator.
			if (isEndOfToken(nextChar)) // If token is valid, returns.
				// skipNewLine();
				// skipWhiteSpace();
				return t;
		}

		if (isEof)
			return t;
		// Makes sure that the whole unknown token (Type: Other) is printed.
		while (!isEndOfToken(nextChar) && nextChar != 7) {
			if (nextChar == '!') {
				nextChar = readChar();
				if (nextChar == '=') { // looks for = after !
					nextChar = 7; // means next token is !=
					//System.out.println("====================================="+nextChar);
					break;
				} else
					t.setValue(t.getValue() + "!");
			} else {
				t.setValue(t.getValue() + nextChar);
				nextChar = readChar();
			}
		}

		if (nextChar == 7) {
			if (t.getValue().equals("")) { // Looks for a !=
				t.setType("Operator"); // operator. If token is
				t.setValue("!="); // empty, sets != as token,
				nextChar = readChar();
				// skipNewLine();
			}

		} else
			t.setType("Lexical Error"); // otherwise, unknown token.
		// skipNewLine();
		return t;
	}

	private char readChar() {
		int i = 0;
		if (isEof)
			return (char) 0;
		System.out.flush();
		try {
			i = input.read();
		} catch (IOException e) {
			System.exit(-1);
		}
		if (i == -1) {
			isEof = true;
			return (char) 0;
		}
		return (char) i;
	}

	private boolean isKeyword(String s) {
		// COMPLETED
		return(s.equals("boolean")  || s.equals("else")  || s.equals("if")  || s.equals("int")  || s.equals("main")  || s.equals("void")  || s.equals("while"));
	}

	private boolean isBooleanLiteral(String s){
		return(s.equals("true") || s.equals("false"));
	}

	private boolean isWhiteSpace(char c) {
		return (c == ' ' || c == '\t' || c == '\r' || c == '\n' || c == '\f');
	}

	private boolean isEndOfLine(char c) {
		return (c == '\r' || c == '\n' || c == '\f' || c == '\t');
		
	}

	private boolean isEndOfToken(char c) { // Is the value a seperate token?
		return (isWhiteSpace(nextChar) || isOperator(nextChar)
				|| isSeparator(nextChar) || isEndOfLine(c) || isEof);
	}

	private void skipWhiteSpace() {
		// check for whitespace, and bypass it
		while (!isEof && isWhiteSpace(nextChar)) {
			// System.out.println("WHITESPACE IS "+nextChar);
			nextChar = readChar();
		}
	}
	private void skipComments(){
		// System.out.println("SKIP COMMENT");
		while(!isEof && !isEndOfLine(nextChar)){
			// System.out.println(isEndOfLine(nextChar));
			nextChar = readChar();
			// System.out.print("skipping comment char");
		}
	}
	
	private void skipNewLine() {
		while (!isEof && isEndOfLine(nextChar)) {
			nextChar = readChar();
			// System.out.println("SKIPPING NewLine");
		}
	}
	private boolean isSeparator(char c) {
		// COMPLETED
		return (c == '(' || c == ')' || c == ';' || c == ',' || c == '{' || c == '}');
	}

	private boolean isOperator(char c) {
		// COMPLETED  
		return (c == '/' || c == '*' || c == '+' || c == '-' || c == '=' || 
				c == '<' || c == '&' || c == '|' || c == '>' || c == '!');
	}

	private boolean isLetter(char c) {
		return (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z');
	}

	private boolean isDigit(char c) {
		// COMPLETED
		return (c == '0' || c == '1' || c == '2' || c == '3' || 
				c == '4' || c == '5' || c == '6' || c == '7' || 
				c == '8' || c == '9');
	}

	public boolean isEndofFile() {
		return isEof;
	}
	
}
