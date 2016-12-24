package ch.fhnw.ip5.sudoku.generator;

import java.util.ArrayList;
import java.util.List;

import ch.fhnw.ip5.sudoku.solver.Solver;
import ch.fhnw.ip5.sudoku.sudoku.Board;
import ch.fhnw.ip5.sudoku.sudoku.Cell;
import ch.fhnw.ip5.sudoku.sudoku.Difficulty;

public class Generator {
	
	public static Board generateBoard(Board ori, Board sol, Difficulty difficulty) {
		
		Board b = new Board(ori);
		
		if (Solver.getDifficulty(b).ordinal() < difficulty.ordinal()) return null;
		
		List<Cell> setCells = new ArrayList<Cell>();
		
		for (byte i = 0; i < b.SIZE; i++) {
			for (byte j = 0; j < b.SIZE; j++) {
				Cell temp = b.getCellAt(i, j);
				if (temp.getValue() != 0) {
					setCells.add(temp);
				}
			}
		}
		
		for (Cell c : setCells) {
			for (Cell x : Flipper.getFlippedCellsOrtho(b, c)) {
				 x.setValue(sol.getCellAt(x.getHpos(), x.getWpos()).getValue());
			}
			for (Cell x : Flipper.getFlippedCellsDia(b, c)) {
				 x.setValue(sol.getCellAt(x.getHpos(), x.getWpos()).getValue());
			}
		}
		
		b.setupBoard();
		
		return b;		
		
	}

}
