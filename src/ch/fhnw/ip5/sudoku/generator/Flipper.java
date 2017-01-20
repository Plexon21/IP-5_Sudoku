package ch.fhnw.ip5.sudoku.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import ch.fhnw.ip5.sudoku.sudoku.Board;
import ch.fhnw.ip5.sudoku.sudoku.Cell;

/**
 * class to get the flipped cells of a given cell on a board
 */
public class Flipper {
	
	/**
	 * get the cells that are flipped on the horizontal and vertical axis to a given cell
	 * 
	 * @param b the board
	 * @param c the given cell
	 * @return The shuffled List of Cells
	 */
	public static List<Cell> getFlippedCellsOrtho(Board b, Cell c) {
		
		List<Cell> cells = new ArrayList<>();
		
		double middle = (double)b.SIZE/2 - 0.5;
		
		double wDist = c.getWpos() - middle;
		double hDist = c.getHpos() - middle;
		
		cells.add(b.getCellAt((byte)(middle + wDist), (byte)(middle + hDist)));
		cells.add(b.getCellAt((byte)(middle + wDist), (byte)(middle - hDist)));
		cells.add(b.getCellAt((byte)(middle - wDist), (byte)(middle + hDist)));
		cells.add(b.getCellAt((byte)(middle - wDist), (byte)(middle - hDist)));
		
		Collections.shuffle(cells, new Random(System.nanoTime()));
		
		return cells;
		
	}
	
	/**
	 * get the cells that represent the horizontal/vertical flipped cells that are flipped additional on a diagonal axis
	 * 
	 * @param b the board
	 * @param c the given cell
	 * @return The shuffled List of Cells
	 */
	public static List<Cell> getFlippedCellsDia(Board b, Cell c) {
		
		List<Cell> cells = new ArrayList<>();
		
		double middle = (double)b.SIZE/2 - 0.5;
		
		double wDist = c.getWpos() - middle;
		double hDist = c.getHpos() - middle;
		
		cells.add(b.getCellAt((byte)(middle + hDist), (byte)(middle + wDist)));
		cells.add(b.getCellAt((byte)(middle + hDist), (byte)(middle - wDist)));
		cells.add(b.getCellAt((byte)(middle - hDist), (byte)(middle + wDist)));
		cells.add(b.getCellAt((byte)(middle - hDist), (byte)(middle - wDist)));	
		
		Collections.shuffle(cells, new Random(System.nanoTime()));
		
		return cells;
		
	}
}
