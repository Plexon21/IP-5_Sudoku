package ch.fhnw.ip5.sudoku;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import ch.fhnw.ip5.sudoku.generator.Generator;
import ch.fhnw.ip5.sudoku.gui.SudokuGUI;
import ch.fhnw.ip5.sudoku.reader.SudokuReader;
import ch.fhnw.ip5.sudoku.solver.Solver;
import ch.fhnw.ip5.sudoku.sudoku.Board;
import ch.fhnw.ip5.sudoku.sudoku.Difficulty;

public class GenTester {
	
	public static void main(String[] args) {
		
		ArrayList<Board> seventeens = new ArrayList<>();
		
		Random rng = new Random();
		
		System.out.println("Reading from File");
		
		try {
			seventeens = SudokuReader.readFromFilename("C:\\Users\\Simon\\OneDrive\\IP5-Sudoku\\17-sudokus_Nullen_with_prefix.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Starting Solver training");
		
		Solver solver = new Solver();
		
		System.out.println();
		System.out.println("Solver trained");
		
		int numberOfSudoku = 100000;
		
		int[][] testarray = new int[9][9];
		
		ArrayList<Board> generatedSudoku = new ArrayList<>();
		
		System.out.println("Generating " + numberOfSudoku + " Sudokus.");
		
		for (int i = 0; i < numberOfSudoku; i++) {
			
			System.out.println(i);
			int ran = rng.nextInt(seventeens.size());
//			System.out.println(ran);
			
			Board ori = seventeens.get(ran);
			
			Board gen = Generator.generateBoard(ori);
			
			if (gen != null) {
				
//				gen.simplePrint();
				for (byte k = 0; k < gen.SIZE; k++) {
					for (byte j = 0; j < gen.SIZE; j++) {
						testarray[k][j] += gen.getCellAt(k, j).getValue();
					}
				}
				
				
				generatedSudoku.add(new Board(gen));
			}
			
		}
		
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				System.out.print((j % 3 == 0 ? "|" : " ") + (testarray[i][j] == 0 ? " " : testarray[i][j]) + (j == 9-1 ? "|" : ""));				
			}
			System.out.println((i+1) % 3 == 0 ? "\n" : "");
		}

		int[] diffs = {0, 0, 0, 0, 0, 0, 0};
		
		System.out.println("Amount of Sudoku generated: " + generatedSudoku.size());
		System.out.println();
		
		for (int i = 0; i < generatedSudoku.size(); i++) {
			Board b = generatedSudoku.get(i);
			Difficulty diff = solver.getDifficulty(b);
			diffs[diff.ordinal()]++;
		}
		
		for (int i = 0; i < diffs.length; i++) {
			System.out.println(Difficulty.values()[i] + ": " + diffs[i]);
		}
		

		BufferedWriter bw;
		try {
			bw = new BufferedWriter(new FileWriter("genned/genned.sudoku"));
			
			for (int i = 0; i < generatedSudoku.size(); i++) {
//				generatedSudoku.get(i).simplePrint();
//				System.out.println(generatedSudoku.get(i).createBoardString());
				bw.write(generatedSudoku.get(i).createBoardString() + '\n');
				bw.flush();
			}	
			bw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		} 
		
		


		
	}
	 
}
