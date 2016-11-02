package ch.fhnw.ip5.sudoku.solver.methods;

import ch.fhnw.ip5.sudoku.solver.Updater;
import ch.fhnw.ip5.sudoku.sudoku.Board;
import ch.fhnw.ip5.sudoku.sudoku.Cell;

public class NakedSingleMethod implements SolveMethod{

	public boolean solve(Board b) {
		for (byte i = 0; i < b.HEIGHT; i++) {
			for (byte j = 0; j < b.WIDTH; j++) {
				Cell tempCell = b.getCellAt(i, j);
				if (tempCell.getPossibleValuesCount() == 1) {
					for (byte x = 1; x <= b.WIDTH;x++) {
						if (tempCell.isPossible(x)) {
							Updater.updateBoard(b, i, j, x);
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public int getDifficultyValue() {
		return 1;
	}

}
