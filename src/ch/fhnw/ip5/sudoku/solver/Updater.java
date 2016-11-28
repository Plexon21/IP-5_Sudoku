package ch.fhnw.ip5.sudoku.solver;

import ch.fhnw.ip5.sudoku.sudoku.Board;
import ch.fhnw.ip5.sudoku.sudoku.Cell;
import ch.fhnw.ip5.sudoku.sudoku.UsedMethod;

public class Updater {

	public static void updateBoard(Board b, byte hpos, byte wpos, byte value, UsedMethod solvedWith) {

		for (byte i = 0; i < b.WIDTH; i++) {
			if (b.getCellAt(hpos, i).isPossible(value)) {
				b.getCellAt(hpos, i).setImpossible(value);		
			}
		}
		
		
		for (byte i = 0; i < b.HEIGHT; i++) {
			if (b.getCellAt(i, wpos).isPossible(value)) {
				b.getCellAt(i, wpos).setImpossible(value);
			}
		}
		
		
		byte hBoxstart = (byte) (hpos / b.BOXHEIGHT*b.BOXHEIGHT);
		byte wBoxstart = (byte) (wpos / b.BOXWIDTH*b.BOXWIDTH);
		
		for (byte i = hBoxstart; i < hBoxstart + b.BOXHEIGHT; i++) {
			for (byte j = wBoxstart; j < wBoxstart + b.BOXWIDTH; j++) {
				if (b.getCellAt(i, j).isPossible(value)) {
					b.getCellAt(i, j).setImpossible(value);
				}
			}
		}
		Cell c = b.getCellAt(hpos, wpos);
		c.setSolveMethod(solvedWith);
		c.setValue(value);
	}
	public static void updateBoard(Board b, byte hpos, byte wpos, byte value) {

		for (byte i = 0; i < b.WIDTH; i++) {
			if (b.getCellAt(hpos, i).isPossible(value)) {
				b.getCellAt(hpos, i).setImpossible(value);		
			}
		}
		
		
		for (byte i = 0; i < b.HEIGHT; i++) {
			if (b.getCellAt(i, wpos).isPossible(value)) {
				b.getCellAt(i, wpos).setImpossible(value);
			}
		}
		
		
		byte hBoxstart = (byte) (hpos / b.BOXHEIGHT*b.BOXHEIGHT);
		byte wBoxstart = (byte) (wpos / b.BOXWIDTH*b.BOXWIDTH);
		
		for (byte i = hBoxstart; i < hBoxstart + b.BOXHEIGHT; i++) {
			for (byte j = wBoxstart; j < wBoxstart + b.BOXWIDTH; j++) {
				if (b.getCellAt(i, j).isPossible(value)) {
					b.getCellAt(i, j).setImpossible(value);
				}
			}
		}
		Cell c = b.getCellAt(hpos, wpos);
		c.setValue(value);
	}	
}
