package ch.fhnw.ip5.sudoku.solver.methods;

import ch.fhnw.ip5.sudoku.solver.SolveMethod;
import ch.fhnw.ip5.sudoku.solver.Updater;
import ch.fhnw.ip5.sudoku.sudoku.Board;
import ch.fhnw.ip5.sudoku.sudoku.UsedMethod;

/**
 * implemtation of the Hidden Singles solving method
 */
public class HiddenSingleMethod implements SolveMethod {

	public boolean apply(Board b) {
		
		for (byte i = 0; i < b.SIZE; i++) {
			
			for (byte value = 1; value <= b.SIZE; value++) {
				
				byte countRow = 0;
				byte posRow = 0;
				byte countColumn = 0;
				byte posColumn = 0;
				byte countBox = 0;
				byte posBox = 0;
				
				for (byte j = 0; j < b.SIZE; j++) {
					
					if (b.getRows()[i].getCells()[j].isPossible(value)) { 
						countRow++;
						posRow = j;
					}
					if (b.getColumns()[i].getCells()[j].isPossible(value)) {
						countColumn++;
						posColumn = j;
					}
					if (b.getBoxes()[i].getCells()[j].isPossible(value)) {
						countBox++;
						posBox = j;
					}
				}
				
				if (countRow == 1) {
					Updater.updateBoard(b, i, posRow, value, UsedMethod.HIDDENSINGLE);
					return true;
				}
				if (countColumn == 1) {
					Updater.updateBoard(b, posColumn, i, value, UsedMethod.HIDDENSINGLE);
					return true;
				}
				if (countBox == 1) {
					Updater.updateBoard(b, b.getBoxes()[i].getCells()[posBox], value, UsedMethod.HIDDENSINGLE);
					return true;
				}
				
			}
		}
		
		return false;
		
	}
	
	/**
	 * check if a cell can be set with the H Single method<br>
	 * if yes the cell will be set but the pencilmarks will not be updated
	 * 
	 * @param b the board
	 * @return the value of the cell that was set 
	 */
	public int check(Board b) {
		
		for (byte i = 0; i < b.SIZE; i++) {
			
			for (byte value = 1; value <= b.SIZE; value++) {
				
				byte countRow = 0;
				byte posRow = 0;
				byte countColumn = 0;
				byte posColumn = 0;
				byte countBox = 0;
				byte posBox = 0;
				
				for (byte j = 0; j < b.SIZE; j++) {
					
					if (b.getRows()[i].getCells()[j].isPossible(value)) { 
						countRow++;
						posRow = j;
					}
					if (b.getColumns()[i].getCells()[j].isPossible(value)) {
						countColumn++;
						posColumn = j;
					}
					if (b.getBoxes()[i].getCells()[j].isPossible(value)) {
						countBox++;
						posBox = j;
					}
				}
				
				if (countRow == 1) {
					b.getRows()[i].getCell(posRow).setImpossible(value);
					return value;
				}
				if (countColumn == 1) {
					b.getColumns()[i].getCell(posColumn).setImpossible(value);
					return value;
				}
				if (countBox == 1) {
					b.getBoxes()[i].getCell(posBox).setImpossible(value);
					return value;
				}
			}
		}		
		return 0;		
	}
}
