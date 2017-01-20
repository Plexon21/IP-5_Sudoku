package ch.fhnw.ip5.sudoku.solver;

import ch.fhnw.ip5.sudoku.sudoku.Board;
import ch.fhnw.ip5.sudoku.sudoku.Cell;
import ch.fhnw.ip5.sudoku.sudoku.UsedMethod;

/**
 * class that provides methods to update the pencilmarks if a value is set
 */
public class Updater {

	/**
	 * update the pencilmarks of the board
	 * 
	 * @param b the board
	 * @param hpos height position of the cell
	 * @param wpos width position of the cell
	 * @param value value that is set
	 * @param solvedWith the Method that was used
	 */
	public static void updateBoard(Board b, byte hpos, byte wpos, byte value, UsedMethod solvedWith) {

		//update row and column
		for (byte i = 0; i < b.SIZE; i++) {
			
			//row
			if (b.getCellAt(hpos, i).isPossible(value)) {
				b.getCellAt(hpos, i).setImpossible(value);		
			}
			
			//column
			if (b.getCellAt(i, wpos).isPossible(value)) {
				b.getCellAt(i, wpos).setImpossible(value);
			}
		}
		
		//update box
		byte hBoxstart = (byte) (hpos / b.BOXHEIGHT*b.BOXHEIGHT);
		byte wBoxstart = (byte) (wpos / b.BOXWIDTH*b.BOXWIDTH);
		
		for (byte i = hBoxstart; i < hBoxstart + b.BOXHEIGHT; i++) {
			for (byte j = wBoxstart; j < wBoxstart + b.BOXWIDTH; j++) {
				if (b.getCellAt(i, j).isPossible(value)) {
					b.getCellAt(i, j).setImpossible(value);
				}
			}
		}
		
		//update cell
		Cell c = b.getCellAt(hpos, wpos);
		c.setSolveMethod(solvedWith);
		c.setValue(value);
	}
	

	/**
	 * update the pencilmarks of the board
	 * 
	 * @param b the board
	 * @param c the cell that is updated
	 * @param value value that is set
	 * @param solvedWith the Method that was used
	 */
	public static void updateBoard(Board b, Cell c, byte value, UsedMethod solvedWith) {
		
		//update row and column
		for (byte i = 0; i < b.SIZE; i++) {
			
			//row
			if (b.getCellAt(c.getHpos(), i).isPossible(value)) {
				b.getCellAt(c.getHpos(), i).setImpossible(value);		
			}
			
			//column
			if (b.getCellAt(i, c.getWpos()).isPossible(value)) {
				b.getCellAt(i, c.getWpos()).setImpossible(value);
			}
		}
		
		//update box
		byte hBoxstart = (byte) (c.getHpos() / b.BOXHEIGHT*b.BOXHEIGHT);
		byte wBoxstart = (byte) (c.getWpos() / b.BOXWIDTH*b.BOXWIDTH);
		
		for (byte i = hBoxstart; i < hBoxstart + b.BOXHEIGHT; i++) {
			for (byte j = wBoxstart; j < wBoxstart + b.BOXWIDTH; j++) {
				if (b.getCellAt(i, j).isPossible(value)) {
					b.getCellAt(i, j).setImpossible(value);
				}
			}
		}
		
		//update cell
		c.setSolveMethod(solvedWith);
		c.setValue(value);
	}
}
