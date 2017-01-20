package ch.fhnw.ip5.sudoku.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import ch.fhnw.ip5.sudoku.solver.Solver;
import ch.fhnw.ip5.sudoku.sudoku.Board;
import ch.fhnw.ip5.sudoku.sudoku.Cell;

/**
 * class to Generate new board on a given base board
 */
public class Generator {
	
	/**
	 * minimum number of given start cells
	 */
	public static int minValue = 20;
	
	/**
	 * maximum number of given start cells
	 */
	public static int maxValue = 36;
	
	/**
	 * Random used to generate random decisions
	 */
	private static Random rng = new Random(System.nanoTime());
	
	/**
	 * generates a new board on a given base board<br>
	 * cells to add are chosen randomly between all not yet set cells
	 * 
	 * @param ori the base board
	 * @return the newly generated board
	 */
	public static Board generateBoardWithRandomCells(Board ori) {
		
		//copy the given board
		Board b = new Board(ori);
		
		//list of cells that could be added to the board
		List<Cell> possibleAddings = new ArrayList<Cell>();
		
		//permutate the board
		Permutation.permutateBoard(b);
		
		//add all not set cells to the list
		for (byte i = 0; i < b.SIZE; i++) {
			for (byte j = 0; j < b.SIZE; j++) {
				Cell temp = b.getCellAt(i, j);
				if (temp.getValue() == 0) {
					possibleAddings.add(temp);
				}
			}
		}
		
		//shuffle the list
		Collections.shuffle(possibleAddings, rng);
		
		//number of cells that will be added
		int amount = rng.nextInt(maxValue + 1 - minValue) + minValue - b.GIVENCOUNT;
		
		//copy the permutated board
		Board sol = new Board(b);
		
		//solve the copied board
		//if not solvable return null
		if (Solver.solve(sol, false) == null) return null;
		
		//set cells according to the solution
		for (int i = 0; i < amount; i++) {
			Cell c = possibleAddings.get(i);
			c.setValue(sol.getCellAt(c.getHpos(), c.getWpos()).getValue());
		}
		
		b.setupBoard();
		
		return b;
		
	}
	
	/**
	 * generates a new board on a given base board<br>
	 * cells to add are chosen randomly between the flipped cells of the given cells
	 * 
	 * @param ori the base board
	 * @return the newly generated board
	 */
	public static Board generateBoardWithRandomFlippedCells(Board ori) {
		
		//copy the given board
		Board b = new Board(ori);
		
		//list of cells that could be added to the board
		List<Cell> possibleAddings = new ArrayList<Cell>();
		
		//permutate the board
		Permutation.permutateBoard(b);
		
		//add all not set flipped cells to the list
		for (byte i = 0; i < b.SIZE; i++) {
			for (byte j = 0; j < b.SIZE; j++) {
				Cell temp = b.getCellAt(i, j);
				if (temp.getValue() != 0) {
					
					for (Cell c : Flipper.getFlippedCellsDia(b, temp)) {
						if (c.getValue() == 0) {
							possibleAddings.add(c);
						}
					}
					for (Cell c : Flipper.getFlippedCellsOrtho(b, temp)) {
						if (c.getValue() == 0) {
							possibleAddings.add(c);
						}
					}
					
				}
			}
		}
		
		//shuffle the list
		Collections.shuffle(possibleAddings, rng);
		
		//number of cells that will be added
		int amount = rng.nextInt(maxValue + 1 - minValue) + minValue - b.GIVENCOUNT;
		
		//copy the permutated board
		Board sol = new Board(b);
		
		//solve the copied board
		//if not solvable return null
		if (Solver.solve(sol, false) == null) return null;
		
		//set cells according to the solution
		for (int i = 0; i < amount; i++) {
			Cell c = possibleAddings.get(i);
			c.setValue(sol.getCellAt(c.getHpos(), c.getWpos()).getValue());
		}
		
		b.setupBoard();
		
		return b;
		
	}
	
	/**
	 * generates a new board on a given base board<br>
	 * board is solved and then the cells are removed alone or (preferably) in pairs
	 * until the amount of remaining cells has reached a certain point
	 * 
	 * @param ori the base board
	 * @return the newly generated board
	 */
	public static Board generateBoardRemoving(Board ori) {
		
		//copy the given board
		Board b = new Board(ori);
		
		//permutate the board
		Permutation.permutateBoard(b);
		
		if (Solver.solve(b, true) == null) throw new IllegalStateException("Board was not solvable with backtrack");
		
		//list of set cells on the board
		List<Cell> cells = new ArrayList<>();
		
		for (byte i = 0; i < b.SIZE; i++) {
			for (byte j = 0; j < b.SIZE; j++) {
				cells.add(b.getCellAt(i, j));
			}
		}
		
		//amount of cells to have at the end
		int toRemove = (b.SIZE*b.SIZE) - (rng.nextInt(maxValue+1 - minValue) + minValue);
		
		while(toRemove > 0) {
			
			Cell cellToRemove = cells.get(rng.nextInt(cells.size()));
			
			byte x = cellToRemove.getHpos();
			byte y = cellToRemove.getWpos();
			byte middle = (byte) (b.SIZE/2);
			
			if (x == middle && y == middle) {
				//cell in the middle
				
				cellToRemove.removeValue();
				cells.remove(cellToRemove);
				toRemove--;
				
			} else {
				
				byte distx = (byte)(x - middle);
				byte disty = (byte)(y - middle);
				
				Cell secondCellToRemove = b.getCellAt((byte)(middle - distx), (byte)(middle - disty));
				
				if (rng.nextDouble() < 0.2) {
					//only remove the first cell
					//this is to not only remove pairs of cells
					
					cellToRemove.removeValue();
					toRemove -= 1;
					
				} else {
					
					cellToRemove.removeValue();
					secondCellToRemove.removeValue();
					toRemove -= 2;
					
				}
				
				cells.remove(cellToRemove);
				cells.remove(secondCellToRemove);
			}
			
		}
		
		//if not solvable without backtracking return null
		if (Solver.solve(new Board(b), false) == null) return null;
		
		b.setupBoard();		
		
		return b;
		
	}
	

}
