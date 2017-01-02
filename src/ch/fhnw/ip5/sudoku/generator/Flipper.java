package ch.fhnw.ip5.sudoku.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import ch.fhnw.ip5.sudoku.sudoku.Board;
import ch.fhnw.ip5.sudoku.sudoku.Cell;

public class Flipper {
	
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
