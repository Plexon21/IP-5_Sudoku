package ch.fhnw.ip5.sudoku;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import ch.fhnw.ip5.sudoku.reader.SudokuParser;
import ch.fhnw.ip5.sudoku.reader.SudokuReader;
import ch.fhnw.ip5.sudoku.solver.methods.HiddenSingleMethod;
import ch.fhnw.ip5.sudoku.solver.methods.NakedSingleMethod;
import ch.fhnw.ip5.sudoku.solver.methods.SolveMethod;
import ch.fhnw.ip5.sudoku.sudoku.Board;

public class Statistics {
	
	public static void generateStatisticsFile(String sourceFolder) {
		String targetFile = sourceFolder + "\\" + "Statistics.csv";
		
		File node = new File(sourceFolder);
		String[] subNodes = node.list();
		for (String fileName : subNodes) {
			parse(new File(node, fileName), new File(targetFile));
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
			
			int numberOfSolvableWithGivenMethods = 0;
			int numberOfSudokus = list.size();
			
			for (Board b : list) {
				
				b.setupBoard();
				
				boolean solving = true;
				int m1counter = 0;
				int m2counter = 0;
				
				while(solving) {
					if (m1.solve(b)) {
						m1counter++;
					} else if (m2.solve(b)) {
						m2counter++;
					} else {
						solving = false;
					}
				}
				
				if (b.isFilled()) {
					numberOfSolvableWithGivenMethods++;
				}
								
				FileWriter pw = new FileWriter(target, true);
				
				pw.write(source + "," + (b.isFilled() ? "1," : "0,") + m1counter + "," + m2counter + "," + b.GIVENCOUNT + "\n");
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
