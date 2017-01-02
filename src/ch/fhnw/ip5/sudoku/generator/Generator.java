package ch.fhnw.ip5.sudoku.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
		
		recursiveAdd(b, sol, difficulty, setCells);
		
		b.setupBoard();
		
		return b;		
		
	}
	
	private static boolean recursiveAdd(Board b, Board sol, Difficulty difficulty, List<Cell> setCells) {
		
		Collections.shuffle(setCells, new Random(System.nanoTime()));
		List<Cell> newList = new ArrayList<>(setCells);
		
		for (Cell c : setCells) {
			for (Cell x : Flipper.getFlippedCellsDia(b, c)) {
				
				if (x.getValue() == 0) {
					//not yet set
					
					x.setValue(sol.getCellAt(x.getHpos(), x.getWpos()).getValue());
					
					int diff = Solver.getDifficulty(b).ordinal();
					
					if (diff > difficulty.ordinal()) {
						newList.add(x);
						if (recursiveAdd(b, sol, difficulty, newList)) {
							return true;
						}
						newList.remove(x);
					} else if (diff == difficulty.ordinal()) {
						return true;
					}
					
					x.removeValue();
					
				}
				
			}
			for (Cell x : Flipper.getFlippedCellsOrtho(b, c)) {

				if (x.getValue() == 0) {
					//not yet set
					
					x.setValue(sol.getCellAt(x.getHpos(), x.getWpos()).getValue());
					
					int diff = Solver.getDifficulty(b).ordinal();
					
					if (diff > difficulty.ordinal()) {
						newList.add(x);
						if (recursiveAdd(b, sol, difficulty, newList)) {
							return true;
						}
						newList.remove(x);
					} else if (diff == difficulty.ordinal()) {
						return true;
					}
					
					x.removeValue();
					
				}
				
			}
		}
		
		return false;
		
	}

}
