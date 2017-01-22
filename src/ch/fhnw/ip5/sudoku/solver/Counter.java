package ch.fhnw.ip5.sudoku.solver;

import ch.fhnw.ip5.sudoku.solver.methods.HiddenSingleMethod;
import ch.fhnw.ip5.sudoku.solver.methods.NakedSingleMethod;
import ch.fhnw.ip5.sudoku.sudoku.Board;
import ch.fhnw.ip5.sudoku.sudoku.Cell;
import ch.fhnw.ip5.sudoku.sudoku.UsedMethod;

/**
 * 
 * class to count possible starting pos and pencilmarks of board
 *
 */
public class Counter {
	/**
	 * Traverses the whole board using Hidden and Naked Single Methods
	 * Counts the positions, where it would be possible to set a number
	 * counts number of starting pos for every number
	 * @param b the board which needs to be checked for starting pos
	 * @return Array containing number of starting pos for every number
	 */
	public static int[] check(Board b) {
		int pos[] = new int[b.SIZE];
		boolean noMoreStartPos = false;
		Board tmp = new Board(b);

		NakedSingleMethod ns = new NakedSingleMethod();
		HiddenSingleMethod hs = new HiddenSingleMethod();

		while (!noMoreStartPos) {
			int newStart;
			if ((newStart = ns.check(tmp)) != 0) {
				pos[newStart - 1]++;
			} else if ((newStart = hs.check(tmp)) != 0) {
				pos[newStart - 1]++;
			} else {
				noMoreStartPos = true;
			}
		}
		return pos;
	}

/**
 * Count all Pencilmarks in the board
 * @param b the board on which the pencilmarks will be counted
 * @return number of pencilmarks
 */
	public static int[] countPossibilities(Board b) {
		Board tmp = new Board(b);
		int[] possibilities = new int[tmp.SIZE];
		for (byte i = 0; i < tmp.SIZE; i++) {
			for (byte j = 0; j < tmp.SIZE; j++) {
				Cell tempCell = tmp.getCellAt(i, j);
				for (byte k = 1; k <= tmp.SIZE; k++) {
					if (tempCell.isPossible(k)) {
						possibilities[k - 1]++;
					}
				}
			}
		}
		return possibilities;
	}
}
