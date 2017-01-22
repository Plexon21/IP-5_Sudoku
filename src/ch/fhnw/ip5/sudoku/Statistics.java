package ch.fhnw.ip5.sudoku;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import ch.fhnw.ip5.sudoku.reader.SudokuReader;
import ch.fhnw.ip5.sudoku.solver.Solver;
import ch.fhnw.ip5.sudoku.sudoku.Board;

/**
 * Class to create statistics.csv file for matlab usage
 *
 */
public class Statistics {

	public static void generateStatisticsFile(String sourceFolder) {
		String targetFile = sourceFolder + "\\" + "Statistics.csv";

		File target = new File(targetFile);

		try (FileWriter pw = new FileWriter(target, true)) {
			pw.write(
					"Difficulty,wasSolved,NakedSingles,HiddenSingles,NakedSubsets_Size2,HiddenSubsets_Size2,BlockLine-Interactions,NakedSubsets_Size3,HiddenSubsets_Size3,NakedSubsets_Size4,HiddenSubsets_Size4,XWing,GivenCount,AnzStartPos1,AnzStartPos2,AnzStartPos3,AnzStartPos4,AnzStartPos5,AnzStartPos6,AnzStartPos7,AnzStartPos8,AnzStartPos9,AnzPossibilities,wasBacktracked\n");

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
	
			for (Board b : list) {

				b.setupBoard();

				int difficulty = 0;
				if (source.toString().contains("veryeasy") || source.toString().contains("S1"))
					difficulty = 1;
				else if (source.toString().contains("easy") || source.toString().contains("S2"))
					difficulty = 2;
				else if (source.toString().contains("medium") || source.toString().contains("S3"))
					difficulty = 3;
				else if (source.toString().contains("S6"))
					difficulty = 5;
				else if (source.toString().contains("very hard_expert") || source.toString().contains("S7"))
					difficulty = 6;
				else if (source.toString().contains("hard") || source.toString().contains("S5"))
					difficulty = 4;
				else if (source.toString().contains("evil") || source.toString().contains("exotic"))
					difficulty = 7;

				FileWriter pw = new FileWriter(target, true);

				int[] results = Solver.solve(b, true);
				pw.write(difficulty + ", ");
				for (int i = 1; i < results.length - 1; i++) {
					pw.write(results[i] + ", ");
				}
				pw.write(results[results.length - 1] + "\n");
				pw.flush();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		Statistics.generateStatisticsFile("C:\\Programming\\IP-5_sudoku\\genned\\removing");

	}

}
