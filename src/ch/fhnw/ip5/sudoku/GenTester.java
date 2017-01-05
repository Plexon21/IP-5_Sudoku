package ch.fhnw.ip5.sudoku;

import ch.fhnw.ip5.sudoku.generator.Generator;
import ch.fhnw.ip5.sudoku.reader.SudokuReader;
import ch.fhnw.ip5.sudoku.solver.Backtrack;
import ch.fhnw.ip5.sudoku.solver.Solver;
import ch.fhnw.ip5.sudoku.sudoku.Board;
import ch.fhnw.ip5.sudoku.sudoku.Difficulty;

public class GenTester {
	
	public static void main(String[] args) {
		
		Board b = SudokuReader.parseLine("9 9 3 3 000000240030100000000000060200056700080000000000040000000300001607000000400800000");

		b.simplePrint();
		
		Difficulty diff = Difficulty.MEDIUM;
		
		Solver solver = new Solver();
		
		System.out.println("Solver trained");
		
		Board populiert = Generator.generateBoard(solver, b, diff);
		
		System.out.println("Difficulty searched for : " + diff);
		
		while(populiert == null) {
			b.simplePrint();
			populiert = Generator.generateBoard(solver, b, diff);
		}
		
		System.out.println(solver.getDifficulty(b));
		b.simplePrint();
//		System.out.println("Found no Solution");
		
	}
	
}
