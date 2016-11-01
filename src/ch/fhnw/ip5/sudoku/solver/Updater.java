package ch.fhnw.ip5.sudoku.solver;

import ch.fhnw.ip5.sudoku.sudoku.Board;

public class Updater {

	public static void updateBoard(Board b, byte hpos, byte wpos, byte value) {

		for (byte i = 0; i < b.BOXWIDTH; i++) {
			b.getCellAt(hpos, i).setImpossible(value);
		}
		
		for (byte i = 0; i < b.BOXHEIGHT; i++) {
			b.getCellAt(i, wpos).setImpossible(value);
		}
		
		byte hBoxstart = (byte) (hpos / b.BOXHEIGHT*b.BOXHEIGHT);
		byte wBoxstart = (byte) (wpos / b.BOXWIDTH*b.BOXWIDTH);
		
		for (byte i = hBoxstart; i < hBoxstart + b.BOXHEIGHT; i++) {
			for (byte j = wBoxstart; j < wBoxstart + b.BOXWIDTH; j++) {
				b.getCellAt(i, j).setImpossible(value);
			}
		}
			
		b.getCellAt(hpos, wpos).setValue(value);
	}	
}
