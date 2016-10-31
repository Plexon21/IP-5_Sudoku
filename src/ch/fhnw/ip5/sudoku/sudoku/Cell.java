package ch.fhnw.ip5.sudoku.sudoku;

import java.util.Arrays;

public class Cell {
	
	private CellState[] values;
	private byte finalValue;
	
	public Cell() {
		values = new CellState[9];
		Arrays.fill(values, CellState.POSSIBLE);
		
		finalValue = 0;
	}
	
	public Cell(byte value) {
		values = new CellState[9];
		Arrays.fill(values, CellState.IMPOSSIBLE);
		values[value-1] = CellState.CERTAIN;
		
		finalValue = value;
	}
	
	public boolean isPossible(byte value) {
		return values[value-1] == CellState.POSSIBLE;
	}
	
	public void setValue(byte value) {
		Arrays.fill(values, CellState.IMPOSSIBLE);
		values[value-1] = CellState.CERTAIN;
		this.finalValue = value;
	}
	
	public byte getValue() {
		return this.finalValue;
	}
}
