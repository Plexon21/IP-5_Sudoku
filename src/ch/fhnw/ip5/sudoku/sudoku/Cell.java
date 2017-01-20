package ch.fhnw.ip5.sudoku.sudoku;

import java.util.Arrays;

/**
 * Cell class that represents one little square in a sudoku
 */
public class Cell {

	/**
	 * pencilmarks of this cell
	 */
	private CellState[] values;
	
	/**
	 * the method with which the cell was set
	 */
	private UsedMethod solvedWith;
	
	/**
	 * the final set value of the cell
	 * Is 0 if not yet set
	 */
	private byte finalValue;
	
	/**
	 * number of possible pencilmarks
	 */
	private byte possibleValuesCount;

	/**
	 * height position of the cell in the sudoku
	 */
	private byte hpos;
	
	/**
	 * width position of the cell in the sudoku
	 */
	private byte wpos;

	/**
	 * constructor for an empty cell
	 * 
	 * @param size size of the sudoku
	 * @param hpos height position of the cell in the sudoku
	 * @param wpos width position of the cell in the sudoku
	 */
	public Cell(byte size, byte hpos, byte wpos) {
		this.values = new CellState[size];
		Arrays.fill(values, CellState.POSSIBLE);
		this.possibleValuesCount = size;
		this.hpos = hpos;
		this.wpos = wpos;

		finalValue = 0;
	}

	/**
	 * constructor for a cell with a set value
	 * 
	 * @param size size of the sudoku
	 * @param hpos height position of the cell in the sudoku
	 * @param wpos width position of the cell in the sudoku
	 * @param value value of the cell
	 */
	public Cell(byte size, byte hpos, byte wpos, byte value) {

		this.values = new CellState[size];
		this.solvedWith = UsedMethod.GIVEN;
		Arrays.fill(values, CellState.IMPOSSIBLE);
		this.values[value - 1] = CellState.CERTAIN;
		this.possibleValuesCount = 0;
		
		this.hpos = hpos;
		this.wpos = wpos;

		finalValue = value;
	}
	
	/**
	 * copy constructor
	 * 
	 * @param c the cell to copy
	 */
	public Cell(Cell c) {
		this.values = new CellState[c.values.length];
		for (int i = 0; i < this.values.length; i++) {
			this.values[i] = c.values[i];
		}
		this.finalValue = c.finalValue;
		this.possibleValuesCount = c.possibleValuesCount;
		this.solvedWith = c.solvedWith;
		
		this.hpos = c.hpos;
		this.wpos = c.wpos;
	}
	
	/**
	 * check to see if two cells have the same possible pencilmarks
	 * 
	 * @param c the cell to check
	 * @return true if both cells have the same possible pencilmarks
	 */
	public boolean samePossibilities(Cell c) {
		for (int i = 0; i < values.length; i++) {
			if (this.values[i] == CellState.POSSIBLE && c.values[i] != CellState.POSSIBLE ||
				this.values[i] != CellState.POSSIBLE && c.values[i] == CellState.POSSIBLE) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * check if a value is possible
	 * 
	 * @param value value to check
	 * @return true if the given value is possible
	 */
	public boolean isPossible(byte value) {
		return values[value - 1] == CellState.POSSIBLE;
	}

	/**
	 * set a value to impossible
	 * 
	 * @param value the value to set
	 */
	public void setImpossible(byte value) {
		if (values[value - 1] != CellState.IMPOSSIBLE) {
			values[value - 1] = CellState.IMPOSSIBLE;
			possibleValuesCount--;
		}
	}

	/**
	 * set the value of a cell
	 * pencilmark of the value will be set to CERTAIN
	 * all other pencilmarks will be set to IMPOSSIBLE
	 * 
	 * @param value the value that will be set
	 */
	public void setValue(byte value) {
		Arrays.fill(values, CellState.IMPOSSIBLE);
		values[value - 1] = CellState.CERTAIN;
		possibleValuesCount = 0;
		this.finalValue = value;
	}
	
	/**
	 * setup possibilies for the cell
	 */
	public void setup() {
		if (finalValue == 0) {
			Arrays.fill(values, CellState.POSSIBLE);
			possibleValuesCount = (byte) values.length;
		} else {
			Arrays.fill(values, CellState.IMPOSSIBLE);
			values[finalValue - 1] = CellState.CERTAIN;
			possibleValuesCount = 0;
		}
	}
	
	/**
	 * sets all pencilmarks to possible and sets the finalValue to 0 
	 */
	public void removeValue() {
		Arrays.fill(values, CellState.POSSIBLE);
		this.possibleValuesCount = (byte)values.length;
		finalValue = 0;
	}

	public UsedMethod getSolveMethod() {
		return this.solvedWith;
	}

	public void setSolveMethod(UsedMethod solvedWith) {
		this.solvedWith = solvedWith;
	}

	public byte getValue() {
		return this.finalValue;
	}

	public byte getPossibleValuesCount() {
		return possibleValuesCount;
	}

	public byte getHpos() {
		return this.hpos;
	}

	public byte getWpos() {
		return this.wpos;
	}
}
