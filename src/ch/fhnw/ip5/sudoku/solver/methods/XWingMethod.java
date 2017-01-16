package ch.fhnw.ip5.sudoku.solver.methods;

import ch.fhnw.ip5.sudoku.sudoku.Board;
import ch.fhnw.ip5.sudoku.sudoku.Cell;

public class XWingMethod implements SolveMethod {

	@Override
	public boolean solve(Board b) {
		
		for (byte i = 0; i < b.SIZE-1; i++) {
			
			for (byte v1 = 1; v1 < b.SIZE+1; v1++) {
					
				int count = 0;
				Cell[] cs = new Cell[2];
				
				for (byte k = 0; k < b.SIZE; k++) {
					
					Cell c = b.getCellAt(i, k);
					
					if (c.isPossible(v1)) {
						
						if (count < 2) {
							cs[count] = c;
						}
						
						count++;
					}
				}
				
				if (count == 2) {
					
					for (byte j = (byte)(i+1); j < b.SIZE; j++) {
						
						int count1 = 0;
						int count2 = 0;
						
						for (byte k = 0; k < b.SIZE; k++) {
							
							Cell u = b.getCellAt(j, k);
							
							if (u.isPossible(v1)) {
								
								count2++;
								
								if (cs[0].getWpos() == k || cs[1].getWpos() == k) count1++;

							}
							
						}
						
						if (count1 == 2 && count2 == 2) {
							
							//XWing found
							boolean somethingChanged = false;
							
							for (int t = 0; t < cs.length; t++) {
								for (Cell q : (b.getColumns()[cs[t].getWpos()]).getCells()) {
									
									if (q.getHpos() != i && q.getHpos() != j) {
										
										if (q.isPossible(v1)) {
											q.setImpossible(v1);
											somethingChanged = true;
										}
										
									}
									
								}
							}
							
							if (somethingChanged) {
								return true;
							}
							
						}
						
					}
					
				}
				
			}
			
			
		}
		
		return false;
	}

	@Override
	public int getDifficultyValue() {
		//TODO
		return 10000;
	}

}
