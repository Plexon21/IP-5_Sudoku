package ch.fhnw.ip5.sudoku.solver;

import ch.fhnw.ip5.sudoku.solver.methods.HiddenSingleMethod;
import ch.fhnw.ip5.sudoku.solver.methods.NakedSingleMethod;
import ch.fhnw.ip5.sudoku.sudoku.Board;

public class StartPos {
	public static int[] check(Board b) {
		int pos[] = new int[b.SIZE];
		boolean noMoreStartPos = false;
		Board tmp = new Board(b);
		
		NakedSingleMethod ns = new NakedSingleMethod();
		HiddenSingleMethod hs = new HiddenSingleMethod();
		
		while (!noMoreStartPos) {
			int newStart;
			if ((newStart = ns.check(tmp))!=0) {
				pos[newStart-1]++;
			} else if ((newStart = hs.check(tmp))!=0) {
				pos[newStart-1]++;
			} else {
				noMoreStartPos = true;
			}
		}
		return pos;
	}
}
