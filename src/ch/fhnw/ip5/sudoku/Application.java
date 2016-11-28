package ch.fhnw.ip5.sudoku;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ch.fhnw.ip5.sudoku.gui.SudokuGUI;
import ch.fhnw.ip5.sudoku.reader.SudokuReader;
import ch.fhnw.ip5.sudoku.solver.methods.HiddenSingleMethod;
import ch.fhnw.ip5.sudoku.solver.methods.NakedSingleMethod;
import ch.fhnw.ip5.sudoku.solver.methods.SolveMethod;
import ch.fhnw.ip5.sudoku.sudoku.Board;

public class Application {

	public static void main(String[] args) {

//		System.out.println("Sudoku has been solved. Believe me!");
		
		//"C:\\Programming\\IP-5_sudoku\\res\\parsed"
		//"C:\\Users\\Simon\\OneDrive\\IP5-Sudoku\\Raetsel AG Sudoku\\06010054800_Archive_veryeasy_parsed"
		//String sourceFolder = "C:\\Programming\\IP-5_sudoku\\res\\parsed";
		String sourceFolder = "C:\\Users\\Simon\\OneDrive\\IP5-Sudoku\\Raetsel AG Sudoku\\parsed\\06010054800_Archive_veryeasy_parsed";
		
		ArrayList<Board> list = new ArrayList<>();
		
		File node = new File(sourceFolder);
		String[] subNodes = node.list();

		
		try {
			
			for (String fileName : subNodes) {
				list.addAll(SudokuReader.readFromFilename(sourceFolder + "\\" + fileName));
			}

			
			SolveMethod m1 = new NakedSingleMethod();
			SolveMethod m2 = new HiddenSingleMethod();
			
			int numberOfSolvableWithGivenMethods = 0;
			int numberOfSudokus = list.size();
			
			Board[] lastSolved = null;
			
			for (Board b : list) {
				
				b.setupBoard();
				
				boolean solving = true;
				int m1counter = 0;
				int m2counter = 0;
				List<Board> steps = new ArrayList<Board>();
				
				while(solving) {
					if (m1.solve(b)) {
						m1counter++;
					} else if (m2.solve(b)) {
						m2counter++;
					} else {
						solving = false;
					}
					steps.add(SudokuReader.parseLine(b.createBoardString()));
				}
				
				System.out.println("Result board");
				b.simplePrint();
				System.out.println();
				System.out.println("Naked Single Counter  = " + m1counter);
				System.out.println("Hidden Single Counter = " + m2counter);
				System.out.println("\n\n");
				
				if (b.isFilled()) {
					numberOfSolvableWithGivenMethods++;
					lastSolved = steps.toArray(new Board[steps.size()]);
				}

			}
			
			System.out.println("Total number of Sudoku = " + numberOfSudokus);
			System.out.println("Number of solvable Sudokus with given Methods = " + numberOfSolvableWithGivenMethods);
			
			
			SudokuGUI gui = new SudokuGUI(lastSolved,lastSolved.length-1);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		

	}

}
