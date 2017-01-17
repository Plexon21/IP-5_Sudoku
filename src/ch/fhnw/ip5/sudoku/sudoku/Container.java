package ch.fhnw.ip5.sudoku.sudoku;

/**
 * Container class that holds a number of cells
 * generally represents a row, column or a box of the sudoku
 * 
 * @author Simon
 *
 */
public class Container {
	
	/**
	 * cells contained in this container
	 */
	private Cell[] cells;
	
	/**
	 * constructor
	 * 
	 * @param size the size of the container
	 */
	public Container(byte size) {
		cells = new Cell[size];
	}
	
	public void setCell(Cell cell, byte pos) {
		cells[pos] = cell;
	}
	
	public Cell[] getCells() { return cells; }
	
	public Cell getCell(byte pos) { return cells[pos]; }

}
