package ch.fhnw.ip5.sudoku.solver;

import ch.fhnw.ip5.sudoku.sudoku.Board;

/**
 * interface for the solving methods
 */
public interface SolveMethod {
	
	/**
	 * try to apply the solving method
	 *  
	 * @param b the board
	 * @return true if solving method could be applied, false otherwise
	 */
	public boolean apply(Board b);
}
