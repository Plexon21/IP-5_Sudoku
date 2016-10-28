package ch.fhnw.ip5.sudoku.sudoku;

import java.util.Arrays;

public class Cell {
	
	private CellState[] values;
	
	public Cell() {
		values = new CellState[9];
		Arrays.fill(values, CellState.POSSIBLE);
	}
	
	public Cell(byte value) {
		values = new CellState[9];
		Arrays.fill(values, CellState.IMPOSSIBLE);
		values[value] = CellState.CERTAIN;
	}
}
