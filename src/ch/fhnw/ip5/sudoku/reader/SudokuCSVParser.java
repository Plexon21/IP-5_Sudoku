package ch.fhnw.ip5.sudoku.reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import ch.fhnw.ip5.sudoku.sudoku.Board;

/**
 * Class used to parse sudokus from "Datenpaket 2" to .sudoku format
 *
 */
public class SudokuCSVParser {

	/**
	 * values of the sudoku read
	 */
	public static char[] values;

	public static void main(String[] args) {
		Board b = parseAndReadSudoku("C://Users//Matth//Downloads//Sudoku//KTI//S1//SU_K9x9_S1_R_0020728.csv");
		b.simplePrint();
	}

	/**
	 * Parses the sudoku from the given fileName to .sudoku filestring
	 * @param fileName name of source file
	 * @return parsed sudoku filestring
	 */
	public static String parseSudoku(String fileName) {
		String result = null;
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String firstLine = br.readLine();
			if (firstLine == null) {
				System.err.println("file " + fileName + " is empty.");
				return result;
			}
			String[] infos = firstLine.split(";");
			if (infos.length < 6) {
				System.err.println("First line has too few arguments (<6)");
				return result;
			}
			int sudokuSize = Integer.valueOf(infos[3]);
			int boxHeight = Integer.valueOf(infos[4]);
			int boxWidth = Integer.valueOf(infos[5]);

			result = sudokuSize + " " + sudokuSize + " " + boxHeight + " " + boxWidth + " ";
			String line;
			// only read unsolved sudoku
			for (int i = 0; i < sudokuSize; i++) {
				line = br.readLine().trim();
				if (line == null) {
					System.err.println("Problem on sudoku line " + (i + 1));
					return result;
				}
				for (char c : line.toCharArray()) {
					int digit;
					if ('-' == c)
						digit = 0;
					else{
						digit = (int)c-64;
					}
					result = result + digit;
				}
			}
			return result;
		} catch (NumberFormatException e) {
			System.err.println("Problem casting arguments to integer.");
			return result;
		} catch (IOException e) {
			System.err.println("Problem reading file " + fileName);
			return result;
		}
	}

	/**
	 * Creates a Board object from a .csv sudoku file
	 * @param fileName name of file to parse
	 * @return Parsed sudoku as Board object
	 */
	public static Board parseAndReadSudoku(String fileName) {
		return SudokuReader.parseLine(parseSudoku(fileName));
	}
}
