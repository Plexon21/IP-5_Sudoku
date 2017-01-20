package ch.fhnw.ip5.sudoku;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import ch.fhnw.ip5.sudoku.generator.Generator;
import ch.fhnw.ip5.sudoku.reader.SudokuReader;
import ch.fhnw.ip5.sudoku.sudoku.Board;

/**
 * class to Generate a number of Sudokus
 */
public class SudokuGenerator {
	
	public static void main(String[] args) {
		
		Random rng = new Random();
		Scanner scanner = new Scanner(System.in);
		ArrayList<Board> seventeens = new ArrayList<>();
		
		System.out.println("Reading from File");
		
		try {
			seventeens = SudokuReader.readFromFilename("res/17er_Korpus/17er_sudokus.sudoku");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Starting Solver training");
		
		System.out.println();
		System.out.println("Solver trained");
		
		int numberOfSudoku = 0;
		boolean reading = true;
		
		System.out.println("How many Sudokus would you like to generate?");
		
		do {
			reading = true;
			
			try {
				numberOfSudoku = Integer.parseInt(scanner.nextLine());
				reading = false;
			} catch (Exception e){
				System.out.println("Please enter a valid positive Integer");
			}
		} while (reading || numberOfSudoku <= 0);
		
		ArrayList<Board> generatedSudoku = new ArrayList<>();
		
		System.out.println("Generating " + numberOfSudoku + " Sudokus.");
		
		while (numberOfSudoku > 0) {
			
			int ran = rng.nextInt(seventeens.size());
			
			Board ori = seventeens.get(ran);
			
//			Board gen = Generator.generateBoardWithRandomCells(ori);
//			Board gen = Generator.generateBoardWithRandomFlippedCells(ori);
			Board gen = Generator.generateBoardRemoving(ori);
			
			if (gen != null) {
				generatedSudoku.add(new Board(gen));
				if (generatedSudoku.size() % 100 == 0) System.out.println("... " + generatedSudoku.size() + " Sudokus generated.");
				numberOfSudoku--;
			}
		}
		
		System.out.println("Amount of Sudoku generated: " + generatedSudoku.size());

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
