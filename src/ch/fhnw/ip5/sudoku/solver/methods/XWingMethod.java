package ch.fhnw.ip5.sudoku.solver.methods;

import ch.fhnw.ip5.sudoku.solver.SolveMethod;
import ch.fhnw.ip5.sudoku.sudoku.Board;
import ch.fhnw.ip5.sudoku.sudoku.Cell;

/**
 * implemtation of the X-Wing solving method
 */
public class XWingMethod implements SolveMethod {

	@Override
	public boolean apply(Board b) {
		
		for (byte i = 0; i < b.SIZE-1; i++) {
			
			for (byte value = 1; value < b.SIZE+1; value++) {
					
				int numFirstLineCells = 0;
				Cell[] cells = new Cell[2];
				
				for (byte k = 0; k < b.SIZE; k++) {
					
					Cell c = b.getCellAt(i, k);
					
					if (c.isPossible(value)) {
						
						if (numFirstLineCells < 2) {
							cells[numFirstLineCells] = c;
						}
						
						numFirstLineCells++;
					}
				}
				
				if (numFirstLineCells == 2) {
					
					for (byte j = (byte)(i+1); j < b.SIZE; j++) {
						
						int verticalCheckCounter = 0;
						int numSecondLineCells = 0;
						
						for (byte k = 0; k < b.SIZE; k++) {
							
							Cell u = b.getCellAt(j, k);
							
							if (u.isPossible(value)) {
								
								numSecondLineCells++;
								
								if (cells[0].getWpos() == k || cells[1].getWpos() == k) verticalCheckCounter++;

							}
							
						}
						
						if (verticalCheckCounter == 2 && numSecondLineCells == 2) {
							
							//XWing found
							boolean somethingChanged = false;
							
							for (int t = 0; t < cells.length; t++) {
								for (Cell q : (b.getColumns()[cells[t].getWpos()]).getCells()) {
									
									if (q.getHpos() != i && q.getHpos() != j) {
										
										if (q.isPossible(value)) {
											q.setImpossible(value);
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
}
