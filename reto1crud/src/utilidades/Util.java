package utilidades;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Util {
private static Scanner sc = new Scanner(System.in);
// --------Métodos leer argumentos-----------------

	// ----------String---------------
	public static String introducirCadena() {
		String cadena = null;
		boolean fallo;
		InputStreamReader flujoEntrada = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(flujoEntrada);
		do {
			fallo = false;
			try {
				cadena = reader.readLine();
			} catch (IOException e) {
				System.out.println("Error en la entrada de datos.");
				fallo = true;
			}
		} while (fallo);
		return cadena;
	}
        
        public static String leerString(String mensaje) {
        System.out.print(mensaje + " ");
        return sc.nextLine();
    }

	// ----------Int---------------
	public static int leerInt() {
		int num = 0;
		boolean fallo;

		do {
			fallo = false;

			try {
				num = Integer.parseInt(introducirCadena());
			} catch (NumberFormatException e) {
				System.out.println("El valor no es numérico. Intentelo de nuevo: ");
				fallo = true;
			}

		} while (fallo);

		return num;
	}

	public static int leerInt(int min, int max) {
		int num = min;
		boolean fallo;

		do {
			fallo = false;
			try {
				num = Integer.parseInt(introducirCadena());
			} catch (NumberFormatException e) {
				System.out.println("El valor no es numérico. Intentelo de nuevo: ");
				fallo = true;
			}
			if (num < min || num > max) {
				System.out.println("Valor fuera de rango. Introduzca un valor entre " + min + " y " + max);
				fallo = true;
			}

		} while (fallo);

		return num;
	}
	public static int leerInt(String message) {
		int num = 0;
		boolean fallo;
		System.out.println(message);
		do {
			fallo = false;

			try {
				num = Integer.parseInt(introducirCadena());
			} catch (NumberFormatException e) {
				System.out.println("El valor no es numérico. Intentelo de nuevo: ");
				fallo = true;
			}

		} while (fallo);

		return num;
	}

	public static int leerInt(String message, int min, int max) {
		int num = 0;
		boolean error;
		System.out.println(message);
		do {
			error = false;
			try {
				num = Integer.parseInt(introducirCadena());

			} catch (NumberFormatException e) {
				System.out.println("Valor no num rico. Introduce de nuevo:");
				error = true;
				num = min;
			}
			if (num < min || num > max) {
				System.out.println("N  fuera de rango, introduce n  entre " + min + " y " + max + ": ");
				error = true;
			}
		} while (error);
		return num;
	}

	// ----------Char---------------
	public static char leerChar() {
		char letra = ' ';
		String cadena;
		boolean error;

		do {
			error = false;
			cadena = introducirCadena();
			if (cadena.length() != 1) {
				System.out.println("Error. Introduzca un único carácter: ");
				error = true;
			}

		} while (error);
		letra = cadena.charAt(0);
		return letra;
	}

	public static char leerChar(char opt1, char opt2) {
		char letra = ' ';
		String cadena;
		boolean error;

		do {
			error = false;
			cadena = introducirCadena();
			if (cadena.length() != 1) {
				System.out.println("Error. Introduzca un único carácter: ");
				error = true;
			} else {
				letra = cadena.charAt(0);
				letra = Character.toUpperCase(letra);
				if (letra != opt1 && letra != opt2) {
					System.out.println("Error, la opción es incorrecta");
					error = true;
				}
			}

		} while (error);
		return letra;
	}
	public static char leerChar(char opt1, char opt2, char opt3) {
		char letra = ' ';
		String cadena;
		boolean error;

		do {
			error = false;
			cadena = introducirCadena();
			if (cadena.length() != 1) {
				System.out.println("Error. Introduzca un único carácter: ");
				error = true;
			} else {
				letra = cadena.charAt(0);
				letra = Character.toUpperCase(letra);
				if (letra != opt1 && letra != opt2 && letra != opt3) {
					System.out.println("Error, la opción es incorrecta");
					error = true;
				}
			}

		} while (error);
		letra = cadena.charAt(0);
		return letra;
	}
	public static char leerArrayChar(char chars[]) {
		int i;
		boolean error = false;
		String letra;
		char aux = 0;

		do {
			error = false;
			letra = introducirCadena();

			if (letra.length() != 1) {
				System.err.println("Error; introduce solo un carácter:");
				error = true;
			} else {
				aux = letra.charAt(0);
				for (i = 0; i < chars.length; i++) {
					if (Character.toUpperCase(chars[i]) == Character.toUpperCase(aux)) {
						i=chars.length;
					}
				}
				if (i == chars.length) {
					error = true;
					System.err.println("Error; la opción introducida no es válida. Inténtalo de nuevo:");
				}
			}
		} while (error);

		return aux;
	}
	// ----------Boolean-------------

	public static boolean esBoolean() {
		String respu;
		do {
			respu = introducirCadena().toLowerCase();
		} while (!respu.equals("0") && !respu.equals("1") && !respu.equals("si") && !respu.equals("no")
				&& !respu.equals("s") && !respu.equals("n") && !respu.equals("true") && !respu.equals("false"));

		if (respu.equals("1") || respu.equals("si") || respu.equals("s") || respu.equals("true")) {
			return true;
		} else {
			return false;
		}
	}

	// ----------Float---------------

	public static float leerFloat() {
		float num = 0;
		boolean error;
		do {
			error = false;
			try {
				num = Float.parseFloat(introducirCadena());
			} catch (NumberFormatException e) {
				System.out.println("Valor no num rico. Introduce de nuevo:");
				error = true;
			}
		} while (error);
		return num;
	}

	public static float leerFloat(float min, float max) {
		float num = 0;
		boolean error;
		do {
			error = false;
			try {
				num = Float.parseFloat(introducirCadena());

			} catch (NumberFormatException e) {
				System.out.println("Valor no num rico. Introduce de nuevo:");
				error = true;
				num = min;
			}
			if (num < min || num > max) {
				System.out.println("N  fuera de rango, introduce n  entre " + min + " y " + max + ": ");
				error = true;
			}
		} while (error);
		return num;
	}

	public static float leerFloat(String message) {
		float num = 0;
		boolean error;
		System.out.println(message);
		do {
			error = false;
			try {
				num = Float.parseFloat(introducirCadena());
			} catch (NumberFormatException e) {
				System.out.println("Valor no num rico. Introduce de nuevo:");
				error = true;
			}
		} while (error);
		return num;
	}

	public static float leerFloat(String message, float min, float max) {
		float num = 0;
		boolean error;
		System.out.println(message);
		do {
			error = false;
			try {
				num = Float.parseFloat(introducirCadena());

			} catch (NumberFormatException e) {
				System.out.println("Valor no num rico. Introduce de nuevo:");
				error = true;
				num = min;
			}
			if (num < min || num > max) {
				System.out.println("N  fuera de rango, introduce n  entre " + min + " y " + max + ": ");
				error = true;
			}
		} while (error);
		return num;
	}

	// --------Métodos para fechas-----------------

	public static String fechaToString(LocalDate fecha) {
		DateTimeFormatter formateador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		String auxFecha;
		auxFecha = fecha.format(formateador);
		return auxFecha;
	}

	public static LocalDate leerFechaDMA() {
		boolean error;
		LocalDate date = null;
		String dateString;
		DateTimeFormatter formateador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		do {
			error = false;
			dateString = introducirCadena();
			try {
				date = LocalDate.parse(dateString, formateador);
			} catch (DateTimeParseException e) {
				System.out.println("Error, introduce una fecha en formato dd/mm/aaaa ");
				error = true;
			}
		} while (error);
		return date;
	}

	public static LocalDate leerFechaAMD() {
		boolean error;
		LocalDate date = null;
		String dateString;
		DateTimeFormatter formateador = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		do {
			error = false;
			dateString = introducirCadena();
			try {
				date = LocalDate.parse(dateString, formateador);
			} catch (DateTimeParseException e) {
				System.out.println("Error, uuuu/hh/ee ");
				error = true;
			}
		} while (error);
		return date;
	}
	// ----------------------------------------------------

	//Devuelve el n�mero de objetos de un fichero
		 public static int calculoFichero(File fich){
		 	int cont=0;
		 	if (fich.exists()){
			 	FileInputStream fis=null;
			 	ObjectInputStream ois=null;
			 	try{
			 		fis=new FileInputStream(fich);
			 		ois=new ObjectInputStream(fis);
		
			 		Object aux=ois.readObject();
		
			 		while (aux!=null){
			 			cont++;
			 			aux=ois.readObject();
			 		}
			 		
		
			 	}catch(EOFException e1){
					//System.out.println("Has acabado de leer, tienes "+cont+" objetos");
					
			 	}catch (Exception e2){
					 e2.printStackTrace();
			 	}
			 	
			 	
			 	try {
					ois.close();
					fis.close();
				} catch (IOException e) {
					System.out.println("Error al cerrar los flujos");
					
				}
		 	}
		 	return cont;
		 }

}