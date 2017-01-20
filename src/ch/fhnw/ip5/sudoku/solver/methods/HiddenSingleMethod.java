package ch.fhnw.ip5.sudoku.solver.methods;

import ch.fhnw.ip5.sudoku.solver.SolveMethod;
import ch.fhnw.ip5.sudoku.solver.Updater;
import ch.fhnw.ip5.sudoku.sudoku.Board;
import ch.fhnw.ip5.sudoku.sudoku.UsedMethod;

//TODO javadoc
public class HiddenSingleMethod implements SolveMethod {

	public boolean apply(Board b) {
		
		for (byte i = 0; i < b.SIZE; i++) {
			
			for (byte x = 1; x <= b.SIZE; x++) {
				
				byte countRow = 0;
				byte posRow = 0;
				byte countColumn = 0;
				byte posColumn = 0;
				byte countBox = 0;
				byte posBox = 0;
				
				for (byte j = 0; j < b.SIZE; j++) {
					
					if (b.getRows()[i].getCells()[j].isPossible(x)) { 
						countRow++; posRow = j;}
					if (b.getColumns()[i].getCells()[j].isPossible(x)) {
						countColumn++; posColumn = j;}
					if (b.getBoxes()[i].getCells()[j].isPossible(x)) {
						countBox++; posBox = j;}
					
				}
				
				if (countRow == 1) {
					Updater.updateBoard(b, i, posRow, x, UsedMethod.HIDDENSINGLE);
					return true;
				}
				if (countColumn == 1) {
					Updater.updateBoard(b, posColumn, i, x, UsedMethod.HIDDENSINGLE);
					return true;
				}
				if (countBox == 1) {
					Updater.updateBoard(b, b.getBoxes()[i].getCells()[posBox], x, UsedMethod.HIDDENSINGLE);
					return true;
				}
				
			}
		}
		
		return false;
		
	}
public int check(Board b) {		
		for (byte i = 0; i < b.SIZE; i++) {			
			for (byte x = 1; x <= b.SIZE; x++) {				
				byte countRow = 0;
				byte posRow = 0;
				byte countColumn = 0;
				byte posColumn = 0;
				byte countBox = 0;
				byte posBox = 0;				
				for (byte j = 0; j < b.SIZE; j++) {					
					if (b.getRows()[i].getCells()[j].isPossible(x)) { 
						countRow++; posRow = j;}
					if (b.getColumns()[i].getCells()[j].isPossible(x)) {
						countColumn++; posColumn = j;}
					if (b.getBoxes()[i].getCells()[j].isPossible(x)) {
						countBox++; posBox = j;}					
				}				
				if (countRow == 1) {
					Updater.updateBoard(b, i, posRow, x, UsedMethod.HIDDENSINGLE);
					return x;
				}
				if (countColumn == 1) {
					Updater.updateBoard(b, posColumn, i, x, UsedMethod.HIDDENSINGLE);
					return x;
				}
				if (countBox == 1) {
					Updater.updateBoard(b, b.getBoxes()[i].getCells()[posBox], x, UsedMethod.HIDDENSINGLE);
					return x;
				}				
			}
		}		
		return 0;		
	}
}
