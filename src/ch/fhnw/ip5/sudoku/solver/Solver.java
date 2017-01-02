package ch.fhnw.ip5.sudoku.solver;

import java.util.Random;

import ch.fhnw.ip5.sudoku.sudoku.Board;
import ch.fhnw.ip5.sudoku.sudoku.Difficulty;

public class Solver {
	
	public static Difficulty getDifficulty(Board b) {
		//THIS IS JUST TO TEST THE GENERATOR
		//TODO Implement
		Random rng = new Random();
		Difficulty[] diffs = Difficulty.values();
		
		return diffs[rng.nextInt(diffs.length)];
	}

}
