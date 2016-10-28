package ch.fhnw.ip5.sudoku.solver.methods;

import ch.fhnw.ip5.sudoku.sudoku.Board;

public interface SolveMethod {
	public boolean solve(Board b);
	public int getDifficultyValue();
}
