package ch.fhnw.ip5.sudoku;

import ch.fhnw.ip5.sudoku.generator.Generator;
import ch.fhnw.ip5.sudoku.reader.SudokuReader;
import ch.fhnw.ip5.sudoku.solver.Backtrack;
import ch.fhnw.ip5.sudoku.sudoku.Board;
import ch.fhnw.ip5.sudoku.sudoku.Difficulty;

public class GenTester {
	
	public static void main(String[] args) {
		
		Board b = SudokuReader.parseLine("9 9 3 3 005000200090004030306000508010060800000982000000070020801000605060200070004000100");

		b.simplePrint();
		
		Board sol = new Board(b);
		
		Backtrack.solve(sol);
		
		sol.simplePrint();
		
		Board populiert = Generator.generateBoard(b, sol, Difficulty.MEDIUM);
		
		populiert.simplePrint();
		
	}
	
}
