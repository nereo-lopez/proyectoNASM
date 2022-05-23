package Compiler;

import java.io.IOException;

public class Compilador {
	
	private static char lookahead;	//Lookahead se usará principalmente para detectar los operadores aritmeticos//

	public static void main(String[] args) throws IOException {		//clase main//
		//Aquí, se define el codigo de salida.//
		lookahead = (char) System.in.read();
		try {
			System.out.println("global _start");
			System.out.println("_start:");
			expresiones();
			printEspacio("mov eax, 1");
			printEspacio("mov ebx, 0");
			printEspacio("int 0x80");
			printEspacio(" ");
			printEspacio("mov eax, 1");
			printEspacio("int 0x80");
			
		} catch (ParseException e) {
			System.err.println(e.getMessage());	//Salidas de error//
		}
	}
	
	//Expresiones para diferentes tipos de operaciones ariméticas//
	private static void expresiones() throws IOException, ParseException {
		
		terminoInicial();

		if(lookahead == '+') {
			
			printEspacio("mov ebx, eax");
			signo('+');
			terminoInicial();
			printEspacio("add ebx, eax");
		} else if (lookahead == '-') {
			
			printEspacio("mov ebx, eax");
			signo('-');
			terminoInicial();
			printEspacio("sub ebx, eax");
			
		} else if (lookahead == '*'){
			
			signo('*');
			division();
			printEspacio("mul ecx");
			printEspacio("mov ebx, eax");
		} else if (lookahead == '/'){
			
			signo('/');
			division();
			printEspacio("div ebx");
			printEspacio("mov ebx, eax");
			
		} else {
			throw new ParseException("Se esperaba +,-,* o /");
		}
	}
	

	//Optimizamos codigo para las sumas y restas//
	private static void terminoInicial() throws IOException, ParseException {
		
		printEspacio("mov eax, " + getNum());
	}
	
	//Optimizamos codigo para las sumas y restas//
	private static void multiplicacion() throws IOException, ParseException {
		
		printEspacio("mov ecx, " + getNum());
	}
	
	
	//Aqui se define el código para devolver una base para dividir//
	private static void division() throws IOException, ParseException {
		
		printEspacio("mov bl, " + getNum());
	}


	//La funcion printEspacio, lanza el código ya identado para solo ser copiado y ejecutado en lenguaje ensamblador//
	private static void printEspacio(String s) {

		System.out.println("    " + s);
		
	}

	//Codigo de error en caso de no tener una operacion aritmética//
	private static void signo(char c) throws IOException, ParseException {
		
		if(lookahead == c) {
			
			lookahead = (char) System.in.read();
		} else {
			
			throw new ParseException("Se esperaba: " + c);
		}
		
	}

	//Codigo de error en caso de no tener operandos enteros//
	private static char getNum() throws IOException, ParseException {
		
		if(!Character.isDigit(lookahead)) {
			
			throw new ParseException("Se esperaba un entero");
		}
		
		char result = lookahead;
		lookahead = (char) System.in.read();
		return result;

	}

	//Errores de excepciones//
	static class ParseException extends Exception {
	
		public ParseException(String reason) {
		
			super(reason);
		}
	}
}
