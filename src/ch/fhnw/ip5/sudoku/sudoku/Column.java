package ch.fhnw.ip5.sudoku.sudoku;

public class Column {
	
	private Cell[] cells;
	
	public Column(int height) {
		cells = new Cell[height];
	}
	
	public void setCell(Cell cell, int pos) {
		cells[pos] = cell;
	}

}
