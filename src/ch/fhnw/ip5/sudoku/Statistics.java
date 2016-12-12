package ch.fhnw.ip5.sudoku;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import ch.fhnw.ip5.sudoku.reader.SudokuParser;
import ch.fhnw.ip5.sudoku.reader.SudokuReader;
import ch.fhnw.ip5.sudoku.solver.Backtrack;
import ch.fhnw.ip5.sudoku.solver.methods.BlockLineInteractionMethod;
import ch.fhnw.ip5.sudoku.solver.methods.HiddenSingleMethod;
import ch.fhnw.ip5.sudoku.solver.methods.HiddenSubSetMethod;
import ch.fhnw.ip5.sudoku.solver.methods.NakedSingleMethod;
import ch.fhnw.ip5.sudoku.solver.methods.NakedSubSetMethod;
import ch.fhnw.ip5.sudoku.solver.methods.SolveMethod;
import ch.fhnw.ip5.sudoku.sudoku.Board;

public class Statistics {

	public static void generateStatisticsFile(String sourceFolder) {
		String targetFile = sourceFolder + "\\" + "Statistics.csv";

		File target = new File(targetFile);

		try (FileWriter pw = new FileWriter(target, true)) {
			pw.write(
					"Filename,Difficulty,wasSolved,NakedSingles,HiddenSingles,NakedSubsets,HiddenSubsets,GivenCount\n");
			pw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

		File node = new File(sourceFolder);
		String[] subNodes = node.list();
		for (String fileName : subNodes) {
			parse(new File(node, fileName), target);
		}
	}

	private static void parse(File source, File target) {
		if (source.isDirectory()) {
			String[] subNode = source.list();

			for (String fileName : subNode) {
				parse(new File(source, fileName), target);
			}
		} else {
			if (source.getName().substring(source.getName().length() - 6).equalsIgnoreCase("sudoku")) {
				solve(source, target);
			}
		}
	}

	private static void solve(File source, File target) {
		try {

			ArrayList<Board> list = new ArrayList<>();

			list.addAll(SudokuReader.readFromFile(source));

			SolveMethod m1 = new NakedSingleMethod();
			SolveMethod m2 = new HiddenSingleMethod();
			SolveMethod m3 = new NakedSubSetMethod();
			SolveMethod m4 = new HiddenSubSetMethod();
			SolveMethod m5 = new BlockLineInteractionMethod();

			int numberOfSolvableWithGivenMethods = 0;
			int numberOfSudokus = list.size();

			for (Board b : list) {

				b.setupBoard();

				boolean solving = true;
				int m1counter = 0;
				int m2counter = 0;
				int m3counter = 0;
				int m4counter = 0;
				int m5counter = 0;
				int wasBacktracked = 0;

				while (solving) {
					if (m1.solve(b)) {
						m1counter++;
					} else if (m2.solve(b)) {
						m2counter++;
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
				} else {
					Backtrack.solve(b);
					numberOfSolvableWithGivenMethods++;
					wasBacktracked = 1;
				}
				int difficulty = 0;
				if (source.toString().contains("veryeasy"))
					difficulty = 1;
				else if (source.toString().contains("easy"))
					difficulty = 2;
				else if (source.toString().contains("medium"))
					difficulty = 3;
				else if (source.toString().contains("very hard"))
					difficulty = 5;
				else if (source.toString().contains("hard"))
					difficulty = 4;
				else if (source.toString().contains("evil"))
					difficulty = 6;
				else if (source.toString().contains("exotic"))
					difficulty = 7;

				FileWriter pw = new FileWriter(target, true);

				pw.write(source + "," + difficulty + "," + (b.isFilled() ? "1," : "0,") + m1counter + "," + m2counter
						+ "," + m3counter + "," + m4counter + "," + m5counter + "," + b.GIVENCOUNT + "," + wasBacktracked + "\n");
				pw.flush();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		Statistics.generateStatisticsFile("C:\\Users\\Matth\\OneDrive\\IP5-Sudoku\\Raetsel AG Sudoku\\parsed");

	}

}
