package ch.fhnw.ip5.sudoku.sudoku;

import java.util.Arrays;

public class Cell {

	private CellState[] values;
	private UsedMethod solvedWith;
	private byte finalValue;
	private byte possibleValuesCount;

	private byte hpos;
	private byte wpos;

	public Cell(byte size, byte hpos, byte wpos) {
		values = new CellState[size];
		Arrays.fill(values, CellState.POSSIBLE);
		possibleValuesCount = size;
		this.hpos = hpos;
		this.wpos = wpos;

		finalValue = 0;
	}

	public Cell(byte size, byte hpos, byte wpos, byte value) {
		values = new CellState[size];
		this.solvedWith = UsedMethod.GIVEN;
		Arrays.fill(values, CellState.IMPOSSIBLE);
		values[value - 1] = CellState.CERTAIN;
		possibleValuesCount = 0;
		this.hpos = hpos;
		this.wpos = wpos;

		finalValue = value;
	}

	public boolean isPossible(byte value) {
		return values[value - 1] == CellState.POSSIBLE;
	}

	public void setImpossible(byte value) {
		if (values[value - 1] != CellState.IMPOSSIBLE) {
			values[value - 1] = CellState.IMPOSSIBLE;
			possibleValuesCount--;
		}
	}

	public void setValue(byte value) {
		Arrays.fill(values, CellState.IMPOSSIBLE);
		values[value - 1] = CellState.CERTAIN;
		possibleValuesCount = 0;
		this.finalValue = value;
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
