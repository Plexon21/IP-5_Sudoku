package ch.fhnw.ip5.sudoku.sudoku;

public class Row {
	
	private Cell[] cells;
	
	public Row(byte width) {
		cells = new Cell[width];
	}
	
	public void setCell(Cell cell, byte pos) {
		cells[pos] = cell;
	}

}
