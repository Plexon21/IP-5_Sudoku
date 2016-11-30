package ch.fhnw.ip5.sudoku;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ch.fhnw.ip5.sudoku.gui.SudokuGUI;
import ch.fhnw.ip5.sudoku.reader.SudokuReader;
import ch.fhnw.ip5.sudoku.solver.methods.HiddenSingleMethod;
import ch.fhnw.ip5.sudoku.solver.methods.NakedSingleMethod;
import ch.fhnw.ip5.sudoku.solver.methods.NakedSubSetMethod;
import ch.fhnw.ip5.sudoku.solver.methods.SolveMethod;
import ch.fhnw.ip5.sudoku.sudoku.Board;

public class SolveAll {

	public static void main(String[] args) {

//		System.out.println("Sudoku has been solved. Believe me!");
		
		//"C:\\Programming\\IP-5_sudoku\\res\\parsed"
		//"C:\\Users\\Simon\\OneDrive\\IP5-Sudoku\\Raetsel AG Sudoku\\06010054800_Archive_veryeasy_parsed"
		//String sourceFolder = "C:\\Programming\\IP-5_sudoku\\res\\parsed";
		String sourceFolder ="C:\\Programming\\IP-5_sudoku\\res\\parsed";
		
		ArrayList<Board> list = new ArrayList<>();
		ArrayList<Board> solveableSudokus = new ArrayList<>();
		
		File node = new File(sourceFolder);
		String[] subNodes = node.list();
		
		try {
			
			for (String fileName : subNodes) {
				list.addAll(SudokuReader.readFromFilename(sourceFolder + "\\" + fileName));
			}
			
			SolveMethod m1 = new NakedSingleMethod();
			SolveMethod m2 = new HiddenSingleMethod();
			SolveMethod m3 = new NakedSubSetMethod();
			
			int numberOfSolvableWithGivenMethods = 0;
			int numberOfSudokus = list.size();
						
			for (Board b : list) {
				b.setupBoard();

				Board givenBoard = new Board(b);
				
				boolean solving = true;
				int m1counter = 0;
				int m2counter = 0;
				int m3counter = 0;
				
				while(solving) {
					if (m1.solve(b)) {
						m1counter++;
					} else if (m2.solve(b)) {
						m2counter++;
					} else if (m3.solve(b)) {
						m3counter++;
					} else {
						solving = false;
					}
				}				
				
				if (b.isFilled()) {
					numberOfSolvableWithGivenMethods++;
					solveableSudokus.add(new Board(givenBoard));
				}

			}
			
			System.out.println("Total number of Sudoku = " + numberOfSudokus);
			System.out.println("Number of solvable Sudokus with given Methods = " + numberOfSolvableWithGivenMethods);
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		

	}

}
