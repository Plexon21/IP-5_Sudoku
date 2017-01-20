package ch.fhnw.ip5.sudoku.solver.methods;

import ch.fhnw.ip5.sudoku.solver.SolveMethod;
import ch.fhnw.ip5.sudoku.solver.Updater;
import ch.fhnw.ip5.sudoku.sudoku.Board;
import ch.fhnw.ip5.sudoku.sudoku.Cell;
import ch.fhnw.ip5.sudoku.sudoku.UsedMethod;

/**
 * implemtation of the Naked Singles solving method
 */
public class NakedSingleMethod implements SolveMethod {

	public boolean apply(Board b) {
		for (byte i = 0; i < b.SIZE; i++) {
			for (byte j = 0; j < b.SIZE; j++) {
				Cell tempCell = b.getCellAt(i, j);
				if (tempCell.getPossibleValuesCount() == 1) {
					for (byte x = 1; x <= b.SIZE; x++) {
						if (tempCell.isPossible(x)) {
							Updater.updateBoard(b, i, j, x, UsedMethod.NAKEDSINGLE);
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * check if a cell can be set with the Naked Single method<br>
	 * if yes the cell will be set but the pencilmarks will not be updated
	 * 
	 * @param b
	 * @return
	 */
	public int check(Board b) {
		for (byte i = 0; i < b.SIZE; i++) {
			for (byte j = 0; j < b.SIZE; j++) {
				Cell tempCell = b.getCellAt(i, j);
				if (tempCell.getPossibleValuesCount() == 1) {
					for (byte x = 1; x <= b.SIZE; x++) {
						if (tempCell.isPossible(x)) {
							tempCell.setImpossible(x);
							return x;
						}
					}
				}
			}
		}
		return 0;
	}
}
