package ch.fhnw.ip5.sudoku.solver.methods;

import java.util.ArrayList;

import ch.fhnw.ip5.sudoku.solver.SolveMethod;
import ch.fhnw.ip5.sudoku.sudoku.Board;
import ch.fhnw.ip5.sudoku.sudoku.Cell;
import ch.fhnw.ip5.sudoku.sudoku.Container;

//TODO JAVADOC
public class BlockLineInteractionMethod implements SolveMethod{

	@Override
	public boolean apply(Board b) {
		
		for (Container c : b.getBoxes()) {
			
			ArrayList<Cell>[] pencilSets = new ArrayList[b.SIZE];
			
			for (byte i = 0 ; i < pencilSets.length; i++) {
				
				pencilSets[i] = new ArrayList<>();
				
				for (byte k = 0 ; k < b.SIZE; k++) {
					if (c.getCell(k).isPossible((byte) (i+1))) {
						pencilSets[i].add(c.getCell(k));
					}
				}
			}
			
			for (int i = 0; i < pencilSets.length; i++) {
				if (!pencilSets[i].isEmpty() && pencilSets[i].size() <= Math.max(b.BOXHEIGHT, b.BOXWIDTH)) {
					
					byte sameRowNumber = -1;
					byte sameColNumber = -1;
					
					for (Cell cell : pencilSets[i]) {
						if (sameRowNumber == -1 && sameColNumber == -1) {
							sameRowNumber = cell.getHpos();
							sameColNumber = cell.getWpos();
						} else {
							if (sameRowNumber != cell.getHpos()) {
								sameRowNumber = -2;
							}
							if (sameColNumber != cell.getWpos()) {
								sameColNumber = -2;
							}
						}
						
					}
					
					
					
					if (sameRowNumber >= 0) {
						// row is the same
						
						boolean somethingchanged = false;
						
						for (Cell cell : b.getRows()[sameRowNumber].getCells()) {
							if (!pencilSets[i].contains(cell) && cell.isPossible((byte)(i+1))) {
								somethingchanged = true;
								cell.setImpossible((byte)(i+1));
							}
						}
						
						return somethingchanged;
						
					} else if (sameColNumber >= 0) {
						// column is the same
						
						boolean somethingchanged = false;
						
						for (Cell cell : b.getColumns()[sameColNumber].getCells()) {
							if (!pencilSets[i].contains(cell) && cell.isPossible((byte)(i+1))) {
								somethingchanged = true;
								cell.setImpossible((byte)(i+1));
							}
						}
						
						return somethingchanged;
					}
					
				}
			}
			
		}
		
		return false;
	}
}
