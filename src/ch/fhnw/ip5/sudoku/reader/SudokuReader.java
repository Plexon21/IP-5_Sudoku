package ch.fhnw.ip5.sudoku.reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import ch.fhnw.ip5.sudoku.sudoku.Board;

public class SudokuReader {
	
	private static BufferedReader br;
	
	public static ArrayList<Board> readFromFile(String filename) throws Exception {
		br = new BufferedReader(new FileReader(filename));
		
		ArrayList<Board> list = new ArrayList<Board>();
		
		String line = br.readLine();
		
		while(line != null) {
			String[] fields = line.split(" +");
			
			byte height = (byte) Integer.parseInt(fields[0]);
			byte width = (byte) Integer.parseInt(fields[1]);
			byte boxheight = (byte) Integer.parseInt(fields[2]);
			byte boxwidth = (byte) Integer.parseInt(fields[3]);
			
			byte[] values = new byte[height * width];
			
			for (int i = 0; i < values.length; i++) {
				values[i] = (byte) Integer.parseInt(fields[4].charAt(i) + "");
			}
			
			list.add(new Board(height, width, boxheight, boxwidth, values));
			
			line = br.readLine();
		}
		
		return list;
	}

}
