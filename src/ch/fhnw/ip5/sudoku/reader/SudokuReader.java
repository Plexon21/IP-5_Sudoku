package ch.fhnw.ip5.sudoku.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import ch.fhnw.ip5.sudoku.sudoku.Board;

/**
 * Helper Class which reads Board from .sudoku file
 *
 */
public class SudokuReader {

	private static BufferedReader br;

	public static ArrayList<Board> readFromFilename(String filename) throws Exception {
		br = new BufferedReader(new FileReader(filename));

		ArrayList<Board> list = new ArrayList<Board>();

		String line = br.readLine();

		while (line != null) {
			list.add(parseLine(line));
			line = br.readLine();
		}
		return list;
	}

	/**
	 * read all sudokus from a given file
	 * 
	 * @param file
	 *            File containing sudokus (.sudoku format)
	 * @return List of Boards read from file
	 * @throws IOException
	 *             Reading exception
	 */
	public static ArrayList<Board> readFromFile(File file) throws IOException {
		br = new BufferedReader(new FileReader(file));

		ArrayList<Board> list = new ArrayList<Board>();

		String line = br.readLine();

		while (line != null) {

			list.add(parseLine(line));
			line = br.readLine();
		}
		return list;
	}

	/**
	 * Parse a single sudoku line to a board
	 * @param line Line to parse
	 * @return read board
	 */
	static Board parseLine(String line) {
		String[] fields = line.split(" +");

		byte size = (byte) Integer.parseInt(fields[0]);
		byte size2 = (byte) Integer.parseInt(fields[1]);
		byte boxheight = (byte) Integer.parseInt(fields[2]);
		byte boxwidth = (byte) Integer.parseInt(fields[3]);

		if (size != size2) {
			throw new IllegalStateException("Board is not squared");
		}

		byte[] values = new byte[size * size];

		for (int i = 0; i < values.length; i++) {
			values[i] = (byte) Integer.parseInt(fields[4].charAt(i) + "");
		}

		return new Board(size, boxheight, boxwidth, values);
	}

}
