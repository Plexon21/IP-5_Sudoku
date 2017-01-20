package ch.fhnw.ip5.sudoku.helper;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;

/**
 * Helper class to add a string in front of every line in a file and save as a new file
 */
public class AddStringAtStart {

	private static BufferedReader br;
	private static BufferedWriter bw;
	
	public static void main(String[] args) {
		
		Scanner scan = new Scanner(System.in);
		
		String filename = scan.nextLine();
		String adder = scan.nextLine();
		
		scan.close();
		
		try {
			addText(filename, adder);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * add a string at the start of every line in a file
	 * 
	 * @param filename the name of the file to change
	 * @param adder the string to add at the start of every line
	 * @throws Exception if the filereader/writer throw exceptions
	 */
	public static void addText(String filename, String adder) throws Exception {
		br = new BufferedReader(new FileReader(filename));
		bw = new BufferedWriter(new FileWriter(filename + "_ADDED"));

		String line = br.readLine();

		while (line != null) {
			bw.write(adder + line + '\n');
			line = br.readLine();
		}
	}
	
}
