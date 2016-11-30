package ch.fhnw.ip5.sudoku.solver;

import ch.fhnw.ip5.sudoku.sudoku.Board;
import ch.fhnw.ip5.sudoku.sudoku.Cell;
import ch.fhnw.ip5.sudoku.sudoku.UsedMethod;

public class Backtrack{
	
	private static byte size;
	private static byte boxheight;
	private static byte boxwidth;
	private static byte[][] board;
	
	public static boolean solve(Board b) {
		
		size = b.SIZE;
		boxheight = b.BOXHEIGHT;
		boxwidth = b.BOXWIDTH;
		
		board = new byte[size][size];
		
		for (byte i = 0; i < size; i++) {
			for (byte j = 0; j < size; j++) {
				board[i][j] = b.getCellAt(i, j).getValue();
			}
		}
		
		if (backtrack(0, 0)) {
			for (byte i = 0; i < size; i++) {
				for (byte j = 0; j < size; j++) {
					Cell temp = b.getCellAt(i, j);
					if (temp.getValue() == 0) {
						temp.setValue(board[i][j]);
						temp.setSolveMethod(UsedMethod.BACKTRACK);
					} else {
						if (temp.getValue() != board[i][j]) {
							throw new IllegalStateException("Backtracking did not return a valid solution.");
						}
					}
				}
			}
			
			return true;
		}
		
		return false;
	}
	
	private static boolean backtrack(int hpos, int wpos) {
		
		if (hpos >= size) return true;
		
		if (board[hpos][wpos] != 0) {
			if (wpos >= size-1) {
				if (backtrack(hpos+1, 0)) return true;
			} else {
				if (backtrack(hpos, wpos+1)) return true;
			}
		} else {
			for (byte value = 1; value <= size; value++) {
				
				if (isValid(hpos, wpos, value)) {
					board[hpos][wpos] = value;
					
					if (wpos >= size-1) {
						if (backtrack(hpos+1, 0)) return true;
					} else {
						if (backtrack(hpos, wpos+1)) return true;
					}
					
					board[hpos][wpos] = 0;
				}
			}
		}
		return false;
	}
	
	private static boolean isValid(int hpos, int wpos, int value) {
		
		for (int i = 0; i < size; i++) {
			if (board[hpos][i] == value || board[i][wpos] == value) {
				return false;
			}
		}
		
		int hBoxstart = (byte) (hpos / boxheight*boxheight);
		int wBoxstart = (byte) (wpos / boxwidth*boxwidth);
		
		for (int i = hBoxstart; i < hBoxstart + boxheight; i++) {
			for (int j = wBoxstart; j < wBoxstart + boxwidth; j++) {
				if (board[i][j] == value) {
					return false;
				}
			}
		}
		return true;
	}
}
