package ch.fhnw.ip5.sudoku;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import ch.fhnw.ip5.sudoku.reader.SudokuParser;
import ch.fhnw.ip5.sudoku.reader.SudokuReader;
import ch.fhnw.ip5.sudoku.solver.Backtrack;
import ch.fhnw.ip5.sudoku.solver.Counter;
import ch.fhnw.ip5.sudoku.solver.methods.BlockLineInteractionMethod;
import ch.fhnw.ip5.sudoku.solver.methods.HiddenSingleMethod;
import ch.fhnw.ip5.sudoku.solver.methods.HiddenSubSetMethod;
import ch.fhnw.ip5.sudoku.solver.methods.NakedSingleMethod;
import ch.fhnw.ip5.sudoku.solver.methods.NakedSubSetMethod;
import ch.fhnw.ip5.sudoku.solver.methods.SolveMethod;
import ch.fhnw.ip5.sudoku.solver.methods.XWingMethod;
import ch.fhnw.ip5.sudoku.sudoku.Board;

public class Statistics_Subsets_Split {

	public static void generateStatisticsFile(String sourceFolder) {
		String targetFile = sourceFolder + "\\" + "Statistics.csv";

		File target = new File(targetFile);

		try (FileWriter pw = new FileWriter(target, true)) {
			/*pw.write(
					"Filename,Difficulty,wasSolved,NakedSingles,HiddenSingles,NakedSubsets_Size2,HiddenSubsets_Size2,BlockLine-Interactions,NakedSubsets_Size3,HiddenSubsets_Size3,NakedSubsets_Size4,HiddenSubsets_Size4,NakedSubsets_Size5,HiddenSubsets_Size5,NakedSubsets_Size6,HiddenSubsets_Size6,NakedSubsets_Size7,HiddenSubsets_Size7,NakedSubsets_Size8,HiddenSubsets_Size8,NakedSubsets_Size9,HiddenSubsets_Size9,GivenCount,AnzStartPos1,AnzStartPos2,AnzStartPos3,AnzStartPos4,AnzStartPos5,AnzStartPos6,AnzStartPos7,AnzStartPos8,AnzStartPos9,wasBacktracked\n");
			*/
			pw.write(
					"wasSolved,NakedSingles,HiddenSingles,NakedSubsets_Size2,HiddenSubsets_Size2,BlockLine-Interactions,NakedSubsets_Size3,HiddenSubsets_Size3,NakedSubsets_Size4,HiddenSubsets_Size4,XWing,GivenCount,AnzStartPos1,AnzStartPos2,AnzStartPos3,AnzStartPos4,AnzStartPos5,AnzStartPos6,AnzStartPos7,AnzStartPos8,AnzStartPos9,AnzPossibilities,wasBacktracked,Difficulty\n");
			
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

			/*SolveMethod[] methods = new SolveMethod[] { 
					new NakedSingleMethod(), new HiddenSingleMethod(),
					new NakedSubSetMethod((byte) 2), new HiddenSubSetMethod((byte) 2),
					new NakedSubSetMethod((byte) 3), new HiddenSubSetMethod((byte) 3),
					new NakedSubSetMethod((byte) 4), new HiddenSubSetMethod((byte) 4),
					new NakedSubSetMethod((byte) 5), new HiddenSubSetMethod((byte) 5),
					new NakedSubSetMethod((byte) 6), new HiddenSubSetMethod((byte) 6),
					new NakedSubSetMethod((byte) 7), new HiddenSubSetMethod((byte) 7),
					new NakedSubSetMethod((byte) 8), new HiddenSubSetMethod((byte) 8),
					new NakedSubSetMethod((byte) 9), new HiddenSubSetMethod((byte) 9),					 
					new BlockLineInteractionMethod() };*/
			
			SolveMethod[] methods = new SolveMethod[] { 
					new NakedSingleMethod(), new HiddenSingleMethod(),
					new NakedSubSetMethod((byte) 2), new HiddenSubSetMethod((byte) 2),
					new BlockLineInteractionMethod(),
					new NakedSubSetMethod((byte) 3), new HiddenSubSetMethod((byte) 3),
					new NakedSubSetMethod((byte) 4), new HiddenSubSetMethod((byte) 4),			 
					new XWingMethod()  };

			int numberOfSolvableWithGivenMethods = 0;
			int numberOfSudokus = list.size();

			for (Board b : list) {

				b.setupBoard();

				int[] startPos = Counter.check(b);
				int total = 0;
				for (int i = 1; i <= b.SIZE; i++) {
					//System.out.print("Number " + i + ": " + startPos[i - 1] + ", ");
					total += startPos[i - 1];
				}
				
				int possibilities[] = Counter.countPossibilities(b);
				int totalPossibilities = 0;
				for(int i=1;i<=b.SIZE;i++){
					totalPossibilities += possibilities[i-1];
				}


				boolean solving = true;
				//int[] solveCounter = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
				int[] solveCounter = new int[] {  0, 0, 0, 0, 0, 0, 0, 0, 0,0 };

				/*
				 * int m1counter = 0; int m2counter = 0;
				 * 
				 * int m3_Size2counter = 0; int m4_Size2counter = 0; int
				 * m3_Size3counter = 0; int m4_Size3counter = 0; int
				 * m3_Size4counter = 0; int m4_Size4counter = 0; int
				 * m3_Size5counter = 0; int m4_Size5counter = 0; int
				 * m3_Size6counter = 0; int m4_Size6counter = 0; int
				 * m3_Size7counter = 0; int m4_Size7counter = 0; int
				 * m3_Size8counter = 0; int m4_Size8counter = 0; int
				 * m3_Size9counter = 0; int m4_Size9counter = 0;
				 * 
				 * int m5counter = 0;
				 */
				int wasBacktracked = 0;

				int solveMethod = 0;
				while (solving) {
					if (solveMethod >= methods.length) {
						solving = false;
						solveMethod = 0;
					}
					if (methods[solveMethod].solve(b)) {
						solveCounter[solveMethod]++;
						solveMethod = 0;
					} else
						solveMethod++;
					/*
					 * if (m1.solve(b)) { m1counter++; } else if (m2.solve(b)) {
					 * m2counter++; } else if (m3_Size2.solve(b)) {
					 * m3_Size2counter++; } else if (m4_Size2.solve(b)) {
					 * m4_Size2counter++; } else if (m5.solve(b)) { m5counter++;
					 * } else if (m3_Size3.solve(b)) { m3_Size3counter++; } else
					 * if (m4_Size3.solve(b)) { m4_Size3counter++; } else if
					 * (m3_Size4.solve(b)) { m3_Size4counter++; } else if
					 * (m4_Size4.solve(b)) { m4_Size4counter++; } else if
					 * (m3_Size5.solve(b)) { m3_Size5counter++; } else if
					 * (m4_Size5.solve(b)) { m4_Size5counter++; } else if
					 * (m3_Size6.solve(b)) { m3_Size6counter++; } else if
					 * (m4_Size6.solve(b)) { m4_Size6counter++; } else if
					 * (m3_Size7.solve(b)) { m3_Size7counter++; } else if
					 * (m4_Size7.solve(b)) { m4_Size7counter++; } else if
					 * (m3_Size8.solve(b)) { m3_Size8counter++; } else if
					 * (m4_Size8.solve(b)) { m4_Size8counter++; } else if
					 * (m3_Size9.solve(b)) { m3_Size9counter++; } else if
					 * (m4_Size9.solve(b)) { m4_Size9counter++; } else { solving
					 * = false; }
					 */
				}

				if (b.isSolvedCorrectly()) {
					numberOfSolvableWithGivenMethods++;
				} else {
					Backtrack.solve(b);
					numberOfSolvableWithGivenMethods++;
					wasBacktracked = 1;
				}
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

				/*pw.write(source + "," + difficulty + "," 
						+ (b.isFilled() ? "1," : "0,") 
						+ solveCounter[0] + ","	+ solveCounter[1] + "," 
						+ solveCounter[2] + "," + solveCounter[3] + "," 
						+ solveCounter[18] + ","
						+ solveCounter[4] + "," + solveCounter[5] + "," 
						+ solveCounter[6] + "," + solveCounter[7] + ","
						+ solveCounter[8] + "," + solveCounter[9] + "," 
						+ solveCounter[10] + "," + solveCounter[11]+ "," 
						+ solveCounter[12] + "," + solveCounter[13] + "," 
						+ solveCounter[14] + "," + solveCounter[15] + "," 
						+ solveCounter[16] + "," + solveCounter[17] + "," 
						+ b.GIVENCOUNT + ","
						+ startPos[0] + "," + startPos[1] + "," + startPos[2] + "," + startPos[3] + "," + startPos[4]+ ","
						+ startPos[5] + "," + startPos[6] + "," + startPos[7] + "," + startPos[8] + ","
						+ totalPossibilities + ","
						+ wasBacktracked + "\n");*/
				/*pw.write(source + "," + difficulty + "," 
						+ (b.isFilled() ? "1," : "0,") 
						+ solveCounter[0] + ","	+ solveCounter[1] + "," 
						+ solveCounter[2] + "," + solveCounter[3] + "," 
						+ solveCounter[8] + ","
						+ solveCounter[4] + "," + solveCounter[5] + "," 
						+ solveCounter[6] + "," + solveCounter[7] + ","
						+ b.GIVENCOUNT + ","
						+ startPos[0] + "," + startPos[1] + "," + startPos[2] + "," + startPos[3] + "," + startPos[4]+ ","
						+ startPos[5] + "," + startPos[6] + "," + startPos[7] + "," + startPos[8] + ","
						+ totalPossibilities + ","
						+ wasBacktracked + "\n");*/
				pw.write((b.isFilled() ? "1," : "0,") 
						+ solveCounter[0] + ","	+ solveCounter[1] + "," 
						+ solveCounter[2] + "," + solveCounter[3] + "," 
						+ solveCounter[4] + ","
						+ solveCounter[5] + "," + solveCounter[6] + "," 
						+ solveCounter[7] + "," + solveCounter[8] + ","
						+ solveCounter[9] + ","
						+ b.GIVENCOUNT + ","
						+ startPos[0] + "," + startPos[1] + "," + startPos[2] + "," + startPos[3] + "," + startPos[4]+ ","
						+ startPos[5] + "," + startPos[6] + "," + startPos[7] + "," + startPos[8] + ","
						+ totalPossibilities + ","
						+ wasBacktracked +","+ difficulty+ "\n");
				pw.flush();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		Statistics_Subsets_Split
				.generateStatisticsFile("C:\\Users\\Simon\\OneDrive\\IP5-Sudoku\\Raetsel AG Sudoku\\old_parsed");

	}

}
