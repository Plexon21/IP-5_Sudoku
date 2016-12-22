package ch.fhnw.ip5.sudoku.solver;

import ch.fhnw.ip5.sudoku.solver.methods.HiddenSingleMethod;
import ch.fhnw.ip5.sudoku.solver.methods.NakedSingleMethod;
import ch.fhnw.ip5.sudoku.sudoku.Board;
import ch.fhnw.ip5.sudoku.sudoku.Cell;
import ch.fhnw.ip5.sudoku.sudoku.UsedMethod;

public class Counter {
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
