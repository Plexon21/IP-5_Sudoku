package ch.fhnw.ip5.sudoku.solver.methods;

import ch.fhnw.ip5.sudoku.solver.Updater;
import ch.fhnw.ip5.sudoku.sudoku.Board;

public class HiddenSingleMethod implements SolveMethod {

	public boolean solve(Board b) {
		
		for (byte i = 0; i < b.WIDTH; i++) {
			
			for (byte x = 1; x <= b.WIDTH; x++) {
				
				byte countRow = 0;
				byte posRow = 0;
				byte countColumn = 0;
				byte posColumn = 0;
				byte countBox = 0;
				byte posBox = 0;
				
				for (byte j = 0; j < b.HEIGHT; j++) {
					
					if (b.getRows()[i].getCells()[j].isPossible(x)) { countRow++; posRow = j;}
					if (b.getColumns()[i].getCells()[j].isPossible(x)) {countColumn++; posColumn = j;}
					if (b.getBoxes()[i].getCells()[j].isPossible(x)) {countBox++; posBox = j;}
					
				}
				
				if (countRow == 1) {
					Updater.updateBoard(b, i, posRow, x);
					return true;
				}
				if (countColumn == 1) {
					Updater.updateBoard(b, posColumn, i, x);
					return true;
				}
				if (countBox == 1) {
					Updater.updateBoard(b, b.getBoxes()[i].getCells()[posBox].getHpos(), b.getBoxes()[i].getCells()[posBox].getWpos(), x);
					return true;
				}
				
			}
		}
		
		return false;
		
	}

	public int getDifficultyValue() {
		return 10;
	}

}
