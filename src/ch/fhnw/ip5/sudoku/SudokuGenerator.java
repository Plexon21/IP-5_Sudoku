package ch.fhnw.ip5.sudoku;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import ch.fhnw.ip5.sudoku.generator.Generator;
import ch.fhnw.ip5.sudoku.reader.SudokuReader;
import ch.fhnw.ip5.sudoku.solver.Solver;
import ch.fhnw.ip5.sudoku.sudoku.Board;
import ch.fhnw.ip5.sudoku.sudoku.Difficulty;

/**
 * class to Generate a number of Sudokus
 */
public class SudokuGenerator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Random rng = new Random();
		Scanner scanner = new Scanner(System.in);
		ArrayList<Board> seventeens = new ArrayList<>();
		
		//read amount of Sudokus to generate
		int numberOfSudoku = 0;
		boolean reading = true;

		System.out.println("How many Sudokus would you like to generate?");

		do {
			reading = true;

			try {
				numberOfSudoku = Integer.parseInt(scanner.nextLine());
				reading = false;
			} catch (Exception e) {
				System.out.println("Please enter a valid positive Integer");
			}
			if (numberOfSudoku <= 0) {
				System.out.println("Please enter a positive Number");
				reading = true;
			}
		} while (reading);
		
		
		//read minimum and maximum of clues on the Sudokus
		int minClues = 0;
		int maxClues = 0;

		System.out.println("What is the minimum amount of clues the Sudokus should have?");

		do {
			reading = true;

			try {
				minClues = Integer.parseInt(scanner.nextLine());
				reading = false;
			} catch (Exception e) {
				System.out.println("Please enter a valid positive Integer");
			}
			if (minClues < 17 || minClues > 81) {
				System.out.println("Please enter a number that is between 17 and 81 (both excluding)");
				reading = true;
			}
		} while (reading);
		
		System.out.println("What is the maximum amount of clues the Sudokus should have?");

		do {
			reading = true;

			try {
				maxClues = Integer.parseInt(scanner.nextLine());
				reading = false;
			} catch (Exception e) {
				System.out.println("Please enter a valid positive Integer");
			}
			if (maxClues < minClues || maxClues > 81) {
				System.out.println("Please enter a number that is between your minimum amount and 81 (both excluding)");
				reading = true;
			}
		} while (reading);


		System.out.println("Reading from File");

		try {
			seventeens = SudokuReader.readFromFilename("res/17er_Korpus/17er_sudokus.sudoku");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Starting Solver training");

		Solver solver = new Solver();
		System.out.println();
		System.out.println("Solver trained");

		ArrayList<Board> generatedSudoku = new ArrayList<>();

		System.out.println("Generating " + numberOfSudoku + " Sudokus.");
		
		Generator generator = new Generator(minClues, maxClues);

		while (numberOfSudoku > 0) {

			int ran = rng.nextInt(seventeens.size());

			Board ori = seventeens.get(ran);

			Board gen = generator.generateBoardWithRandomCells(ori);
//			Board gen = generator.generateBoardWithRandomFlippedCells(ori);
//			Board gen = generator.generateBoardRemoving(ori);

			if (gen != null) {
				generatedSudoku.add(new Board(gen));
				if (generatedSudoku.size() % 100 == 0)
					System.out.println("... " + generatedSudoku.size() + " Sudokus generated.");
				numberOfSudoku--;
			}
		}

		System.out.println("Amount of Sudoku generated: " + generatedSudoku.size());

		System.out.println("Classifying generated sudokus...");
		int[] diffs = { 0, 0, 0, 0, 0, 0, 0 };
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
			bw = new BufferedWriter(new FileWriter("genned/genned" + generatedSudoku.size() + ".sudoku"));

			for (Board b : generatedSudoku) {
				bw.write(b.createBoardString() + '\n');
			}

			bw.flush();
			bw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
