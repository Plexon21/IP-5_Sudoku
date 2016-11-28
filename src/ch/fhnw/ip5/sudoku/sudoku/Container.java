package ch.fhnw.ip5.sudoku.sudoku;

public class Container {
	
	private Cell[] cells;
	
	public Container(byte height) {
		cells = new Cell[height];
	}
	
	public void setCell(Cell cell, byte pos) {
		cells[pos] = cell;
	}
	
	public Cell[] getCells() { return cells; }
	
	public Cell getCell(byte pos) { return cells[pos]; }

}
