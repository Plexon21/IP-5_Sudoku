package ch.fhnw.ip5.sudoku.solver;

import java.util.Random;

import ch.fhnw.ip5.sudoku.network.NeuralNetworkHandler;
import ch.fhnw.ip5.sudoku.solver.methods.BlockLineInteractionMethod;
import ch.fhnw.ip5.sudoku.solver.methods.HiddenSingleMethod;
import ch.fhnw.ip5.sudoku.solver.methods.HiddenSubSetMethod;
import ch.fhnw.ip5.sudoku.solver.methods.NakedSingleMethod;
import ch.fhnw.ip5.sudoku.solver.methods.NakedSubSetMethod;
import ch.fhnw.ip5.sudoku.solver.methods.SolveMethod;
import ch.fhnw.ip5.sudoku.solver.methods.XWingMethod;
import ch.fhnw.ip5.sudoku.sudoku.Board;
import ch.fhnw.ip5.sudoku.sudoku.Difficulty;

public class Solver {
	
	private NeuralNetworkHandler nnh;
	
	public Solver()  {
		
		nnh = new NeuralNetworkHandler();
		nnh.trainNetwork("C:\\Users\\Simon\\OneDrive\\IP5-Sudoku\\Raetsel AG Sudoku\\old_parsed");
		
	}
	
	public static boolean solve(Board b, boolean withBacktracking) {
		
		SolveMethod[] methods = new SolveMethod[] { 
				new NakedSingleMethod(), new HiddenSingleMethod(),
				new NakedSubSetMethod((byte) 2), new HiddenSubSetMethod((byte) 2),		 
				new BlockLineInteractionMethod(),
				new NakedSubSetMethod((byte) 3), new HiddenSubSetMethod((byte) 3),
				new NakedSubSetMethod((byte) 4), new HiddenSubSetMethod((byte) 4),	
				new XWingMethod()};
		
		b.setupBoard();

		boolean solving = true;

		int solveMethod = 0;
		
		while (solving) {
			if (solveMethod >= methods.length) {
				solving = false;
				solveMethod = 0;
			}
			else if (methods[solveMethod].solve(b)) {
				solveMethod = 0;
			} else
				solveMethod++;
		}
		
		if (b.isSolvedCorrectly()) {
			return true;
		} else {
			if (withBacktracking) {
				if (Backtrack.solve(b)) {
					return true;
				}
			}
			return false;
		}
	}
	
	public Difficulty getDifficulty(Board b) {
		//THIS IS JUST TO TEST THE GENERATOR
		//TODO Implement
//		Random rng = new Random();
//		Difficulty[] diffs = Difficulty.values();
//		
//		return diffs[rng.nextInt(diffs.length)];
		
		int diff = nnh.predictBoard(new Board(b));
		
		return Difficulty.values()[diff];
	}

}
