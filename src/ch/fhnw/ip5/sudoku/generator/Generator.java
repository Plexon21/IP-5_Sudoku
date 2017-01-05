package ch.fhnw.ip5.sudoku.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import ch.fhnw.ip5.sudoku.solver.Backtrack;
import ch.fhnw.ip5.sudoku.solver.Solver;
import ch.fhnw.ip5.sudoku.sudoku.Board;
import ch.fhnw.ip5.sudoku.sudoku.Cell;
import ch.fhnw.ip5.sudoku.sudoku.Difficulty;

public class Generator {
	
	public static int[] minValues = { 26, 25, 25, 24, 23, 22, 21};
	public static int[] maxValues = { 31, 30, 29, 28, 27, 26, 25};
	
	public static Board generateBoard(Solver solver, Board ori, Difficulty difficulty) {
		
		Board b = new Board(ori);
		
		System.out.println("Difficulty: " + solver.getDifficulty(b));
		b.simplePrint();
		
		Permutation.permutateBoard(b);
		
		System.out.println("Difficulty: " + solver.getDifficulty(b));
		b.simplePrint();
		
		Board sol = new Board(b);
		
		Solver.solve(sol);
		
		System.out.println("Solution");
		sol.simplePrint();
		
		if (solver.getDifficulty(b).ordinal() < difficulty.ordinal()) return null;
		
		List<Cell> setCells = new ArrayList<Cell>();
		
		for (byte i = 0; i < b.SIZE; i++) {
			for (byte j = 0; j < b.SIZE; j++) {
				Cell temp = b.getCellAt(i, j);
				if (temp.getValue() != 0) {
					setCells.add(temp);
				}
			}
		}
		
		Collections.shuffle(setCells, new Random(System.nanoTime()));
		
		
		if (addNumbers(solver, b, sol, difficulty, setCells)) {
			b.setupBoard();
			return b;
		} else {
			return null;
		}
		
	}
	
	private static boolean recursiveAdd(Solver solver, Board b, Board sol, Difficulty difficulty, List<Cell> toAdd, int pos, int counter) {
		
		if (17 + counter < minValues[difficulty.ordinal()]) {
			
			Cell x = toAdd.get(pos);
			
			x.setValue(sol.getCellAt(x.getHpos(), x.getWpos()).getValue());
			
			return recursiveAdd(solver, b, sol, difficulty, toAdd, pos+1, counter+1);
		} else {
			
			if (17 + counter > maxValues[difficulty.ordinal()]) return false;
			
			System.out.println("pos:" + pos + " counter:" + counter);
			
			for (int i = pos; i < toAdd.size(); i++) {
				
				Cell x = toAdd.get(i);
				
				x.setValue(sol.getCellAt(x.getHpos(), x.getWpos()).getValue());
				
				int diff = solver.getDifficulty(b).ordinal();
				
				if (diff > difficulty.ordinal()) {
					if (recursiveAdd(solver, b, sol, difficulty, toAdd, i+1, counter+1)) {
						return true;
					}
				} else if (diff == difficulty.ordinal()) {
					return true;
				}
				
				x.removeValue();
				
			}
			
		}
		
		return false;
	}
	
	private static boolean addNumbers(Solver solver, Board b, Board sol, Difficulty difficulty, List<Cell> setCells) {
		
		if (setCells.size() > maxValues[difficulty.ordinal()]) return false;
		
		b.simplePrint();
		
		List<Cell> toAdd = new ArrayList<>();
		
		for (Cell c : setCells) {
			for (Cell x : Flipper.getFlippedCellsDia(b, c)) {
				if (x.getValue() == 0 && !toAdd.contains(x)) toAdd.add(x);
			}
		}
		
		for (Cell c : setCells) {
			for (Cell x : Flipper.getFlippedCellsOrtho(b, c)) {
				if (x.getValue() == 0 && !toAdd.contains(x)) toAdd.add(x);
			}
		}
		
		return recursiveAdd(solver, b, sol, difficulty, toAdd, 0, 0);
		
		
		
		/*
		
		
		for (Cell c : setCells) {
			
			for (Cell x : Flipper.getFlippedCellsDia(b, c)) {
				
				if (x.getValue() == 0 && !checkedCells.contains(x)) {
					//not yet set
					
					x.setValue(sol.getCellAt(x.getHpos(), x.getWpos()).getValue());
					
					int diff = solver.getDifficulty(b).ordinal();
					
					if (diff > difficulty.ordinal()) {
						newList.add(x);
						if (recursiveAdd(solver, b, sol, difficulty, newList)) {
							return true;
						}
						newList.remove(x);
					} else if (diff == difficulty.ordinal()) {
						return true;
					}
					
					x.removeValue();
					checkedCells.add(x);
					
				}
				
			}
			for (Cell x : Flipper.getFlippedCellsOrtho(b, c)) {

				if (x.getValue() == 0 && !checkedCells.contains(x)) {
					//not yet set
					
					x.setValue(sol.getCellAt(x.getHpos(), x.getWpos()).getValue());
					
					int diff = solver.getDifficulty(b).ordinal();
					
					if (diff > difficulty.ordinal()) {
						newList.add(x);
						if (recursiveAdd(solver, b, sol, difficulty, newList)) {
							return true;
						}
						newList.remove(x);
					} else if (diff == difficulty.ordinal()) {
						return true;
					}
					
					x.removeValue();
					checkedCells.add(x);
					
				}
				
			}
		}
		
		return false;
		
		*/
		
	}

}
