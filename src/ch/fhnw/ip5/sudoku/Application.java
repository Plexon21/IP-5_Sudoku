package ch.fhnw.ip5.sudoku;

import java.util.ArrayList;

import ch.fhnw.ip5.sudoku.reader.SudokuReader;
import ch.fhnw.ip5.sudoku.solver.Updater;
import ch.fhnw.ip5.sudoku.solver.methods.NakedSingleMethod;
import ch.fhnw.ip5.sudoku.solver.methods.SolveMethod;
import ch.fhnw.ip5.sudoku.sudoku.Board;

public class Application {

	public static void main(String[] args) {

//		System.out.println("Sudoku has been solved. Believe me!");
		
		try {
			ArrayList<Board> list = SudokuReader.readFromFile("veryEasy.sudoku");
			
			for (Board b : list) {
				b.simplePrint();
				System.out.println(b.createBoardString());
				
//				b.cluesPrint();
				
				b.setupBoard();
				
//				b.cluesPrint();
				
				SolveMethod m = new NakedSingleMethod();
				
				while(m.solve(b)) {
					b.simplePrint();
				}
				
				b.cluesPrint();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		

	}

}
