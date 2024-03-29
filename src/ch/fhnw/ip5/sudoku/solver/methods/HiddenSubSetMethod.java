package ch.fhnw.ip5.sudoku.solver.methods;

import java.util.HashSet;
import java.util.Set;
import ch.fhnw.ip5.sudoku.solver.SolveMethod;
import ch.fhnw.ip5.sudoku.sudoku.Board;
import ch.fhnw.ip5.sudoku.sudoku.Cell;
import ch.fhnw.ip5.sudoku.sudoku.Container;

/**
 * implemtation of the Hidden Subset solving method
 */
public class HiddenSubSetMethod implements SolveMethod {

	private byte size;
	private byte subsetSize = 2;
	
	/**
	 * constructor
	 * default subset size is 2
	 */
	public HiddenSubSetMethod() {}
	
	/**
	 * constructor
	 * 
	 * @param subsetSize the size of the subset to search for
	 */
	public HiddenSubSetMethod(byte subsetSize) {
		this.subsetSize = subsetSize;
	}

	@Override
	public boolean apply(Board b) {

		size = b.SIZE;

		Container[] rows = b.getRows();
		Container[] columns = b.getColumns();
		Container[] boxes = b.getBoxes();

		for (byte i = 0; i < size; i++) {
			if (checkContainer(rows[i], size, subsetSize)) {
				return true;
			}
			if (checkContainer(columns[i], size, subsetSize)) {
				return true;
			}
			if (checkContainer(boxes[i], size, subsetSize)) {
				return true;
			}
		}

		return false;
	}

	private boolean checkContainer(Container container, byte size, byte subsetsize) {

		HashSet<Cell>[] pencilSets = new HashSet[size];

		for (byte i = 0; i < pencilSets.length; i++) {

			pencilSets[i] = new HashSet<>();

			for (byte k = 0; k < size; k++) {
				if (container.getCell(k).isPossible((byte) (i + 1))) {
					pencilSets[i].add(container.getCell(k));
				}
			}
		}

		boolean[] subsetvalues = new boolean[size];

		return checkRecursive(pencilSets, subsetsize, 0, 1, new HashSet<Cell>(), subsetvalues);

	}

	private boolean checkRecursive(Set<Cell>[] pencilsets, byte subsetsize, int pos, int count, HashSet<Cell> solset, boolean[] subsetvalues) {

		if (pos >= size || count > subsetsize)
			return false;

		for (int k = pos; k < size; k++) {
			if (!pencilsets[k].isEmpty()) {

				HashSet<Cell> tempSet = (HashSet<Cell>) solset.clone();
				tempSet.addAll(pencilsets[k]);
				subsetvalues[k] = true;

				if (tempSet.size() == subsetsize && count == subsetsize) {
					// subset was found

					boolean somethingchanged = false;

					for (Cell c : tempSet) {
						for (int i = 0; i < subsetvalues.length; i++) {
							if (!subsetvalues[i] && c.isPossible((byte) (i + 1))) {
								c.setImpossible((byte) (i + 1));
								somethingchanged = true;
							}
						}
					}

					return somethingchanged;

				} else if (tempSet.size() <= subsetsize) {

					if (checkRecursive(pencilsets, subsetsize, k + 1, count + 1, tempSet, subsetvalues))
						return true;

				}

				subsetvalues[k] = false;

			}

		}

		return false;
	}
}
