package ch.fhnw.ip5.sudoku.solver.methods;

import java.util.ArrayList;
import java.util.List;

import ch.fhnw.ip5.sudoku.solver.SolveMethod;
import ch.fhnw.ip5.sudoku.sudoku.Board;
import ch.fhnw.ip5.sudoku.sudoku.Cell;
import ch.fhnw.ip5.sudoku.sudoku.Container;

/**
 * implemtation of the Naked Subset solving method
 */
public class NakedSubSetMethod implements SolveMethod{
	
	private byte subsetSize = 2;

	/**
	 * constructor
	 * default subset size is 2
	 */
	public NakedSubSetMethod(){}
	
	/**
	 * constructor
	 * 
	 * @param subsetSize the size of the subset to search for
	 */
	public NakedSubSetMethod(byte subsetSize){
		this.subsetSize = subsetSize;
	}
	
	@Override
	public boolean apply(Board b) {
	
		return solveForSubsetsize(b, subsetSize);
		
	}
	
	private boolean solveForSubsetsize(Board b, byte subsetsize) {

		Container[] rows = b.getRows();
		Container[] columns = b.getColumns();
		Container[] boxes = b.getBoxes();
		
		for (byte i = 0; i < b.SIZE; i++) {
			if (checkContainer(rows[i], subsetsize)) { return true; }
			if (checkContainer(columns[i], subsetsize)) { return true; }
			if (checkContainer(boxes[i], subsetsize)) { return true; }
		}
		
		return false;
	}
	
	private boolean checkContainer(Container c, byte subsetsize) {
		
		List<Cell> possibleSubsetCells = new ArrayList<>();
		
		for (byte i = 0; i < c.getCells().length; i++) {
			if (c.getCell(i).getPossibleValuesCount() == subsetsize) {
				//a cell with this subsetsize was found
				possibleSubsetCells.add(c.getCell(i));
			}
		}
		
		if (possibleSubsetCells.size() == subsetsize) {
			//subsetlist could be exact subset, check if possible values are the same
			if (isSubset(possibleSubsetCells)) {
				
				Cell firstCell = possibleSubsetCells.get(0);
				boolean somethingChanged = false;
				
				for (Cell cell : c.getCells()) {
					if (!firstCell.samePossibilities(cell)) {
						for (byte k = 1; k <= c.getCells().length; k++) {
							if (firstCell.isPossible(k) && cell.isPossible(k)) {
								cell.setImpossible(k);
								somethingChanged = true;
							}
						}
					}
				}
				
				return somethingChanged;
				
			} else {
				return false;
			}
			
		} else if (possibleSubsetCells.size() > subsetsize) {
			//subsetlist could contain exact subset; check if subset is included
			
			while(possibleSubsetCells.size() >= subsetsize) {
				
				Cell firstCell = possibleSubsetCells.get(0);
				byte counter = 1;
				
				for (byte i = 1; i < possibleSubsetCells.size(); i++) {
					if (firstCell.samePossibilities(possibleSubsetCells.get(i))) { counter++; }
				}
				
				if (counter == subsetsize) {
					//subset contains firstCell
					boolean somethingChanged = false;
					
					for (Cell cell : c.getCells()) {
						if (!firstCell.samePossibilities(cell)) {
							for (byte k = 1; k <= c.getCells().length; k++) {
								if (firstCell.isPossible(k) && cell.isPossible(k)) {
									cell.setImpossible(k);
									somethingChanged = true;
								}
							}
						}
					}
					
					return somethingChanged;
					
					
				} else {
					//remove firstCell since it can't be part of the subset
					possibleSubsetCells.remove(0);
				}
				
			}
			
			return false;
			
		} else {
			//subsetlist too small for complete subset
			return false;	
		}	
	}
	
	private boolean isSubset(List<Cell> subsetlist) {
		
		Cell firstCell = subsetlist.get(0);
		
		for (byte i = 1; i < subsetlist.size(); i++) {
			if (!firstCell.samePossibilities(subsetlist.get(i))) { return false; }
		}
			
		return true;
	}
}
