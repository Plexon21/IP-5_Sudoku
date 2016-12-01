package ch.fhnw.ip5.sudoku;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ch.fhnw.ip5.sudoku.gui.SudokuGUI;
import ch.fhnw.ip5.sudoku.reader.SudokuReader;
import ch.fhnw.ip5.sudoku.solver.Backtrack;
import ch.fhnw.ip5.sudoku.solver.methods.BlockLineInteractionMethod;
import ch.fhnw.ip5.sudoku.solver.methods.HiddenSingleMethod;
import ch.fhnw.ip5.sudoku.solver.methods.HiddenSubSetMethod;
import ch.fhnw.ip5.sudoku.solver.methods.NakedSingleMethod;
import ch.fhnw.ip5.sudoku.solver.methods.NakedSubSetMethod;
import ch.fhnw.ip5.sudoku.solver.methods.SolveMethod;
import ch.fhnw.ip5.sudoku.sudoku.Board;

public class Application {

	public static void main(String[] args) {

//		System.out.println("Sudoku has been solved. Believe me!");
		

		String sourceFolder = "C:\\Users\\Simon\\OneDrive\\IP5-Sudoku\\Raetsel AG Sudoku\\parsed\\06010052700_Archive_very hard_expert_parsed";
//		String sourceFolder = "C:\\Users\\Simon\\OneDrive\\IP5-Sudoku\\Raetsel AG Sudoku\\parsed\\06010006900_SUD_Datafiles_evil_parsed";
//		String sourceFolder ="C:\\Programming\\IP-5_sudoku\\res\\parsed";
//		String sourceFolder =  "C:\\Users\\Matth\\OneDrive\\IP5-Sudoku\\Raetsel AG Sudoku\\parsed\\06010006800_SUD_Datafiles_exotic_parsed";
//		String sourceFolder =  "C:\\Users\\Matth\\OneDrive\\IP5-Sudoku\\Raetsel AG Sudoku\\parsed\\06010052700_Archive_very hard_expert_parsed";
		
		ArrayList<Board> list = new ArrayList<>();
		
		File node = new File(sourceFolder);
		String[] subNodes = node.list();

		
		try {
			
			for (String fileName : subNodes) {
				list.addAll(SudokuReader.readFromFilename(sourceFolder + "\\" + fileName));
			}

			
			SolveMethod m1 = new NakedSingleMethod();
			SolveMethod m2 = new HiddenSingleMethod();
			SolveMethod m3 = new NakedSubSetMethod();
			SolveMethod m4 = new HiddenSubSetMethod();
			SolveMethod m5 = new BlockLineInteractionMethod();
			
			int numberOfSolvableWithGivenMethods = 0;
			int numberOfBacktrackedSudokus = 0;
			int numberOfSudokus = list.size();
			
			Board[] lastSolved = null;
			
			for (Board b : list) {
				
				b.setupBoard();
				System.out.println(b.createBoardString());
				
				boolean solving = true;
				int m1counter = 0;
				int m2counter = 0;
				int m3counter = 0;
				int m4counter = 0;
				int m5counter = 0;
				boolean wasBacktracked = false;
				
				List<Board> steps = new ArrayList<Board>();
				steps.add(new Board(b));
				
				while(solving) {
					if (m1.solve(b)) {
						m1counter++;
						steps.add(new Board(b));	
					} else if (m2.solve(b)) {
						m2counter++;
						steps.add(new Board(b));
					} else if (m3.solve(b)) {
						m3counter++;
					} else if (m4.solve(b)) {
						m4counter++;
					} else if (m5.solve(b)) {
						m5counter++;
					} else {
						solving = false;
					}
				}
				
				if (b.isSolvedCorrectly()) {
					numberOfSolvableWithGivenMethods++;
//					lastSolved = steps.toArray(new Board[steps.size()]);
				} else {
					Backtrack.solve(b);
					if (!b.isSolvedCorrectly()) {
						throw new IllegalStateException("SOMETHING IS WRONG IMPLEMENTED");
					}
					lastSolved = steps.toArray(new Board[steps.size()]);
					numberOfSolvableWithGivenMethods++;
					numberOfBacktrackedSudokus++;
					wasBacktracked = true;
				}
				
				System.out.println("Result board");
				b.simplePrint();
				System.out.println();
				System.out.println("Naked Single Counter  = " + m1counter);
				System.out.println("Hidden Single Counter = " + m2counter);
				System.out.println("Hidden Subset Counter = " + m3counter);
				System.out.println("Naked Subset Counter = " + m4counter);
				System.out.println("Block-Line Counter = " + m5counter);
				System.out.println("was Backtracked = " + wasBacktracked);
				System.out.println("\n\n");
				
			}
			
			System.out.println("Total number of Sudoku = " + numberOfSudokus);
			System.out.println("Number of backtracked Sudokus = " + numberOfBacktrackedSudokus);
			System.out.println("Number of solvable Sudokus with given Methods = " + numberOfSolvableWithGivenMethods);
			
			if (lastSolved != null) {
				new SudokuGUI(lastSolved,lastSolved.length-1);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		

	}

}
