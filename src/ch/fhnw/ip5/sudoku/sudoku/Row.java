package ch.fhnw.ip5.sudoku.sudoku;

public class Row {
	
	private Cell[] cells;
	
	public Row(int width) {
		cells = new Cell[width];
	}
	
	public void setCell(Cell cell, int pos) {
		cells[pos] = cell;
	}

}
