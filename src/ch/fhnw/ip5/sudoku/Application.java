package ch.fhnw.ip5.sudoku;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ch.fhnw.ip5.sudoku.gui.SudokuGUI;
import ch.fhnw.ip5.sudoku.reader.SudokuReader;
import ch.fhnw.ip5.sudoku.solver.Backtrack;
import ch.fhnw.ip5.sudoku.solver.SolveMethod;
import ch.fhnw.ip5.sudoku.solver.methods.BlockLineInteractionMethod;
import ch.fhnw.ip5.sudoku.solver.methods.HiddenSingleMethod;
import ch.fhnw.ip5.sudoku.solver.methods.HiddenSubSetMethod;
import ch.fhnw.ip5.sudoku.solver.methods.NakedSingleMethod;
import ch.fhnw.ip5.sudoku.solver.methods.NakedSubSetMethod;
import ch.fhnw.ip5.sudoku.solver.methods.XWingMethod;
import ch.fhnw.ip5.sudoku.sudoku.Board;

public class Application {

	public static void main(String[] args) {

		// System.out.println("Sudoku has been solved. Believe me!");

		String sourceFolder = "C:\\Users\\Simon\\OneDrive\\IP5-Sudoku\\Raetsel AG Sudoku\\all_parsed\\S1";
		// String sourceFolder =
		// "C:\\Users\\Simon\\OneDrive\\IP5-Sudoku\\Raetsel AG
		// Sudoku\\parsed\\06010006900_SUD_Datafiles_evil_parsed";
		// String sourceFolder ="C:\\Programming\\IP-5_sudoku\\res\\parsed";
		// String sourceFolder =
		// "C:\\Users\\Matth\\OneDrive\\IP5-Sudoku\\Raetsel AG
		// Sudoku\\parsed\\06010006800_SUD_Datafiles_exotic_parsed";
		//String sourceFolder = "C:\\Users\\Matth\\OneDrive\\IP5-Sudoku\\Raetsel AG Sudoku\\parsed\\06010052700_Archive_very hard_expert_parsed";

		ArrayList<Board> list = new ArrayList<>();

		File node = new File(sourceFolder);
		String[] subNodes = node.list();

		try {

			for (String fileName : subNodes) {
				list.addAll(SudokuReader.readFromFilename(sourceFolder + "\\" + fileName));
			}

			SolveMethod m1 = new NakedSingleMethod();
			SolveMethod m2 = new HiddenSingleMethod();

			SolveMethod m3_Size2 = new NakedSubSetMethod((byte) 2);
			SolveMethod m3_Size3 = new NakedSubSetMethod((byte) 3);
			SolveMethod m3_Size4 = new NakedSubSetMethod((byte) 4);
			SolveMethod m3_Size5 = new NakedSubSetMethod((byte) 5);
			SolveMethod m3_Size6 = new NakedSubSetMethod((byte) 6);
			SolveMethod m3_Size7 = new NakedSubSetMethod((byte) 7);
			SolveMethod m3_Size8 = new NakedSubSetMethod((byte) 8);
			SolveMethod m3_Size9 = new NakedSubSetMethod((byte) 9);
			SolveMethod m4_Size2 = new HiddenSubSetMethod((byte) 2);
			SolveMethod m4_Size3 = new HiddenSubSetMethod((byte) 3);
			SolveMethod m4_Size4 = new HiddenSubSetMethod((byte) 4);
			SolveMethod m4_Size5 = new HiddenSubSetMethod((byte) 5);
			SolveMethod m4_Size6 = new HiddenSubSetMethod((byte) 6);
			SolveMethod m4_Size7 = new HiddenSubSetMethod((byte) 7);
			SolveMethod m4_Size8 = new HiddenSubSetMethod((byte) 8);
			SolveMethod m4_Size9 = new HiddenSubSetMethod((byte) 9);

			SolveMethod m5 = new BlockLineInteractionMethod();
			
			SolveMethod m6 = new XWingMethod();

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
				int m3_Size2counter = 0;
				int m4_Size2counter = 0;
				int m5counter = 0;
				int m3_Size3counter = 0;
				int m4_Size3counter = 0;
				int m3_Size4counter = 0;
				int m4_Size4counter = 0;
				int m3_Size5counter = 0;
				int m4_Size5counter = 0;
				int m3_Size6counter = 0;
				int m4_Size6counter = 0;
				int m3_Size7counter = 0;
				int m4_Size7counter = 0;
				int m3_Size8counter = 0;
				int m4_Size8counter = 0;
				int m3_Size9counter = 0;
				int m4_Size9counter = 0;
				int m6counter = 0;
				boolean wasBacktracked = false;

				List<Board> steps = new ArrayList<Board>();
				steps.add(new Board(b));

				while (solving) {
					if (m1.apply(b)) {
						m1counter++;
						steps.add(new Board(b));
					} else if (m2.apply(b)) {
						m2counter++;
						steps.add(new Board(b));
					} else if (m3_Size2.apply(b)) {
						m3_Size2counter++;
					} else if (m4_Size2.apply(b)) {
						m4_Size2counter++;
					} else if (m5.apply(b)) {
						m5counter++;
					} else if (m3_Size3.apply(b)) {
						m3_Size3counter++;
					} else if (m4_Size3.apply(b)) {
						m4_Size3counter++;
					} else if (m3_Size4.apply(b)) {
						m3_Size4counter++;
					} else if (m4_Size4.apply(b)) {
						m4_Size4counter++;
					} else if (m3_Size5.apply(b)) {
						m3_Size5counter++;
					} else if (m4_Size5.apply(b)) {
						m4_Size5counter++;
					} else if (m3_Size6.apply(b)) {
						m3_Size6counter++;
					} else if (m4_Size6.apply(b)) {
						m4_Size6counter++;
					} else if (m3_Size7.apply(b)) {
						m3_Size7counter++;
					} else if (m4_Size7.apply(b)) {
						m4_Size7counter++;
					} else if (m3_Size8.apply(b)) {
						m3_Size8counter++;
					} else if (m4_Size8.apply(b)) {
						m4_Size8counter++;
					} else if (m3_Size9.apply(b)) {
						m3_Size9counter++;
					} else if (m4_Size9.apply(b)) {
						m4_Size9counter++;
					} else if (m6.apply(b)) {
						m6counter++;
					} else {
						solving = false;
					}
				}

				if (b.isSolvedCorrectly()) {
					numberOfSolvableWithGivenMethods++;
					lastSolved = steps.toArray(new Board[steps.size()]);
				} else {
					b.simplePrint();
					if (Backtrack.solve(b)) {
						steps.add(new Board(b));
						lastSolved = steps.toArray(new Board[steps.size()]);
						numberOfBacktrackedSudokus++;
						wasBacktracked = true;
					}
					
					if (!b.isSolvedCorrectly()) {
//						b.simplePrint();
					}
 				}

				System.out.println("Result board");
				b.simplePrint();
				System.out.println();
				System.out.println("Naked Single Counter  = " + m1counter);
				System.out.println("Hidden Single Counter = " + m2counter);
				System.out.println("Hidden Subset Size 2 Counter = " + m3_Size2counter);
				System.out.println("Naked Subset Size 2 Counter = " + m4_Size2counter);
				System.out.println("Block-Line Counter = " + m5counter);
				
				System.out.println("Hidden Subset Size 3 Counter = " + m3_Size3counter);
				System.out.println("Naked Subset Size 3 Counter = " + m4_Size3counter);
				System.out.println("Hidden Subset Size 4 Counter = " + m3_Size4counter);
				System.out.println("Naked Subset Size 4 Counter = " + m4_Size4counter);
				System.out.println("Hidden Subset Size 5 Counter = " + m3_Size5counter);
				System.out.println("Naked Subset Size 5 Counter = " + m4_Size5counter);
				System.out.println("Hidden Subset Size 6 Counter = " + m3_Size6counter);
				System.out.println("Naked Subset Size 6 Counter = " + m4_Size6counter);
				System.out.println("Hidden Subset Size 7 Counter = " + m3_Size7counter);
				System.out.println("Naked Subset Size 7 Counter = " + m4_Size7counter);
				System.out.println("Hidden Subset Size 8 Counter = " + m3_Size8counter);
				System.out.println("Naked Subset Size 8 Counter = " + m4_Size8counter);
				System.out.println("Hidden Subset Size 9 Counter = " + m3_Size9counter);
				System.out.println("Naked Subset Size 9 Counter = " + m4_Size9counter);
				System.out.println("XWing Counter = " + m6counter);
				
				System.out.println("was Backtracked = " + wasBacktracked);
				System.out.println("\n\n");

			}

			System.out.println("Total number of Sudoku = " + numberOfSudokus);
			System.out.println("Number of backtracked Sudokus = " + numberOfBacktrackedSudokus);
			System.out.println("Number of solvable Sudokus with given Methods = " + numberOfSolvableWithGivenMethods);

			if (lastSolved != null) {
				new SudokuGUI(lastSolved, lastSolved.length - 1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
