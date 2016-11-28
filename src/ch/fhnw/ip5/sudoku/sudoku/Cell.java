package ch.fhnw.ip5.sudoku.sudoku;

import java.util.Arrays;

public class Cell {
	
	private CellState[] values;
	private byte finalValue;
	private byte possibleValuesCount;
	
	private byte hpos;
	private byte wpos;
	
	public Cell(byte size, byte hpos, byte wpos) {
		this.values = new CellState[size];
		Arrays.fill(values, CellState.POSSIBLE);
		this.possibleValuesCount = size;
		this.hpos = hpos;
		this.wpos = wpos;
		
		finalValue = 0;
	}
	
	public Cell(byte size, byte hpos, byte wpos, byte value) {
		this.values = new CellState[size];
		Arrays.fill(values, CellState.IMPOSSIBLE);
		this.values[value-1] = CellState.CERTAIN;
		this.possibleValuesCount = 0;
		this.hpos = hpos;
		this.wpos = wpos;
		
		finalValue = value;
	}
	
	public Cell(Cell c) {
		this.values = new CellState[c.values.length];
		for (int i = 0; i < this.values.length; i++) {
			this.values[i] = c.values[i];
		}
		this.finalValue = c.finalValue;
		this.possibleValuesCount = c.possibleValuesCount;
		
		this.hpos = c.hpos;
		this.wpos = c.wpos;
	}
	
	public boolean isPossible(byte value) {
		return values[value-1] == CellState.POSSIBLE;
	}
	
	public void setImpossible(byte value) {
		if (values[value-1] != CellState.IMPOSSIBLE) {
			values[value-1] = CellState.IMPOSSIBLE;
			possibleValuesCount--;
		}
	}
	
	public void setValue(byte value) {
		Arrays.fill(values, CellState.IMPOSSIBLE);
		values[value-1] = CellState.CERTAIN;
		possibleValuesCount = 0;
		this.finalValue = value;
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
