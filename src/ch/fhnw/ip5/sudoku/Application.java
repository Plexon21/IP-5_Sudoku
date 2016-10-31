package ch.fhnw.ip5.sudoku;

import java.util.ArrayList;

import ch.fhnw.ip5.sudoku.reader.SudokuReader;
import ch.fhnw.ip5.sudoku.sudoku.Board;

public class Application {

	public static void main(String[] args) {

//		System.out.println("Sudoku has been solved. Believe me!");
		
		try {
			ArrayList<Board> list = SudokuReader.readFromFile("someSudokus.sudoku");
			
			for (Board b : list) {
				b.simplePrint();
				System.out.println(b.createBoardString());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		

	}

}
