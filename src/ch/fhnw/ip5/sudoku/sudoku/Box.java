package ch.fhnw.ip5.sudoku.sudoku;

public class Box {
	
	private Cell[] cells;
	
	public Box(byte size) {
		cells = new Cell[size];
	}
	
	public void setCell(Cell cell, byte pos) {
		cells[pos] = cell;
	}
	
	public Cell[] getCells() { return cells; }
	
}
