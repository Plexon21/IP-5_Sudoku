package ch.fhnw.ip5.sudoku.generator;

import ch.fhnw.ip5.sudoku.sudoku.Board;
import ch.fhnw.ip5.sudoku.sudoku.Cell;

public class Flipper {
	
	public static Cell[] getFlippedCellsOrtho(Board b, Cell c) {
		
		Cell[] cells = new Cell[4];
		
		double middle = (double)b.SIZE/2 - 0.5;
		
		double wDist = c.getWpos() - middle;
		double hDist = c.getHpos() - middle;
		
		cells[0] = b.getCellAt((byte)(middle + wDist), (byte)(middle + hDist));
		cells[1] = b.getCellAt((byte)(middle + wDist), (byte)(middle - hDist));
		cells[2] = b.getCellAt((byte)(middle - wDist), (byte)(middle + hDist));
		cells[3] = b.getCellAt((byte)(middle - wDist), (byte)(middle - hDist));
		
		return cells;
		
	}
	
	public static Cell[] getFlippedCellsDia(Board b, Cell c) {
		
		Cell[] cells = new Cell[4];
		
		double middle = (double)b.SIZE/2 - 0.5;
		
		double wDist = c.getWpos() - middle;
		double hDist = c.getHpos() - middle;
		
		cells[0] = b.getCellAt((byte)(middle + hDist), (byte)(middle + wDist));
		cells[1] = b.getCellAt((byte)(middle + hDist), (byte)(middle - wDist));
		cells[2] = b.getCellAt((byte)(middle - hDist), (byte)(middle + wDist));
		cells[3] = b.getCellAt((byte)(middle - hDist), (byte)(middle - wDist));	
		
		return cells;
		
	}
}
