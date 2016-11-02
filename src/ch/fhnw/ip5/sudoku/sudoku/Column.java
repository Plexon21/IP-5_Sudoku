package ch.fhnw.ip5.sudoku.sudoku;

public class Column {
	
	private Cell[] cells;
	
	public Column(byte height) {
		cells = new Cell[height];
	}
	
	public void setCell(Cell cell, byte pos) {
		cells[pos] = cell;
	}
	
	public Cell[] getCells() { return cells; }

}
