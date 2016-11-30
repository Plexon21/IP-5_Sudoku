package ch.fhnw.ip5.sudoku;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import ch.fhnw.ip5.sudoku.gui.SudokuGUI;
import ch.fhnw.ip5.sudoku.reader.SudokuReader;
import ch.fhnw.ip5.sudoku.solver.Backtrack;
import ch.fhnw.ip5.sudoku.solver.methods.HiddenSingleMethod;
import ch.fhnw.ip5.sudoku.solver.methods.HiddenSubSetMethod;
import ch.fhnw.ip5.sudoku.solver.methods.NakedSingleMethod;
import ch.fhnw.ip5.sudoku.solver.methods.NakedSubSetMethod;
import ch.fhnw.ip5.sudoku.solver.methods.SolveMethod;
import ch.fhnw.ip5.sudoku.sudoku.Board;

public class SolveAll {

	public static void main(String[] args) {

		// System.out.println("Sudoku has been solved. Believe me!");

		// "C:\\Programming\\IP-5_sudoku\\res\\parsed"
		// "C:\\Users\\Simon\\OneDrive\\IP5-Sudoku\\Raetsel AG
		// Sudoku\\06010054800_Archive_veryeasy_parsed"
		// String sourceFolder = "C:\\Programming\\IP-5_sudoku\\res\\parsed";
		String sourceFolder = "C:\\Programming\\IP-5_sudoku\\res\\parsed";

		ArrayList<Board> list = new ArrayList<>();
		ArrayList<Board> solvedSudokus = new ArrayList<>();

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

			int numberOfSolvableWithGivenMethods = 0;
			int numberOfBacktrackedSudokus = 0;
			int numberOfSudokus = list.size();

			for (Board b : list) {
				b.setupBoard();

				boolean solving = true;
				int m1counter = 0;
				int m2counter = 0;
				int m3counter = 0;
				int m4counter = 0;
				boolean wasBacktracked = false;

				while (solving) {
					if (m1.solve(b)) {
						m1counter++;
					} else if (m2.solve(b)) {
						m2counter++;
					} else if (m3.solve(b)) {
						m3counter++;
					} else if (m4.solve(b)) {
						m4counter++;
					} else {
						solving = false;
					}
				}

				if (b.isSolvedCorrectly()) {
					numberOfSolvableWithGivenMethods++;
					solvedSudokus.add(new Board(b));
				} else {
					Backtrack.solve(b);
					solvedSudokus.add(new Board(b));
					numberOfSolvableWithGivenMethods++;
					numberOfBacktrackedSudokus++;
					wasBacktracked = true;
				}

			}
			String target = sourceFolder + "\\" + "17er_solved";
			try (FileWriter writer = new FileWriter(target)) {
				for (Board b : solvedSudokus) {
					String sudokuString = b.createBoardString();
					writer.write(sudokuString + "\n");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
