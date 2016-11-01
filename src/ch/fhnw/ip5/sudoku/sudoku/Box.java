package ch.fhnw.ip5.sudoku.sudoku;

public class Box {
	
	private Cell[][] cells;
	
	public Box(byte height, byte width) {
		cells = new Cell[height][width];
	}
	
	public void setCell(Cell cell, byte hpos, byte wpos) {
		cells[hpos][wpos] = cell;
	}

}
