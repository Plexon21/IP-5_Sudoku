package ch.fhnw.ip5.sudoku.solver;

import ch.fhnw.ip5.sudoku.network.NeuralNetworkHandler;
import ch.fhnw.ip5.sudoku.solver.methods.BlockLineInteractionMethod;
import ch.fhnw.ip5.sudoku.solver.methods.HiddenSingleMethod;
import ch.fhnw.ip5.sudoku.solver.methods.HiddenSubSetMethod;
import ch.fhnw.ip5.sudoku.solver.methods.NakedSingleMethod;
import ch.fhnw.ip5.sudoku.solver.methods.NakedSubSetMethod;
import ch.fhnw.ip5.sudoku.solver.methods.XWingMethod;
import ch.fhnw.ip5.sudoku.sudoku.Board;
import ch.fhnw.ip5.sudoku.sudoku.Difficulty;

/**
 * class that solves and creates a feature vector to analyze the data
 */
public class Solver {
	
	/**
	 * neural network
	 */
	private NeuralNetworkHandler nnh;
	
	/**
	 * constructor
	 * network is trained 
	 */
	public Solver()  {
		
		nnh = new NeuralNetworkHandler();
		nnh.trainNetwork("res/Datenpaket_1");
	}
	
	/**
	 * solve a given board with or without backtracking<br>
	 * returns the feature vector to use for analyzing<br>
	 * <br>
	 * Feature vector with the following structure<br>
	 * <br>
	 * - difficulty {not set = 0, else 1-7}<br>
	 * - is solved {yes = 1, no = 0}<br>
	 * <br>
	 * - amount of Naked Singles used<br>
	 * - amount of Hidden Singles used<br>
	 * - amount of Naked Subset size 2 used<br>
	 * - amount of Hidden Subset size 2 used<br>
	 * - amount of Block-Line Interactions used<br>
	 * - amount of Naked Subset size 3 used<br>
	 * - amount of Hidden Subset size 3 used<br>
	 * - amount of Naked Subset size 4 used<br>
	 * - amount of Hidden Subset size 4 used<br>
	 * - amount of X-Wing used<br>
	 * <br>
	 * - amount of given numbers at the start<br>
	 * <br>
	 * - amount of cells that can be set with Naked/Hidden Singles for the number 1<br>
	 * - amount of cells that can be set with Naked/Hidden Singles for the number 2<br>
	 * - amount of cells that can be set with Naked/Hidden Singles for the number 3<br>
	 * - amount of cells that can be set with Naked/Hidden Singles for the number 4<br>
	 * - amount of cells that can be set with Naked/Hidden Singles for the number 5<br>
	 * - amount of cells that can be set with Naked/Hidden Singles for the number 6<br>
	 * - amount of cells that can be set with Naked/Hidden Singles for the number 7<br>
	 * - amount of cells that can be set with Naked/Hidden Singles for the number 8<br>
	 * - amount of cells that can be set with Naked/Hidden Singles for the number 9<br>
	 * <br>
	 * - amount of pencilmarks before solving<br>
	 * <br>
	 * - is backtracked {yes = 1, no = 0}<br>
	 * 
	 * 
	 * @param b the board to solve
	 * @param withBacktracking whether 
	 * @return feature vector / null if not solvable
	 * 
	 */
	public static int[] solve(Board b, boolean withBacktracking) {
		
		SolveMethod[] methods = new SolveMethod[] { 
				new NakedSingleMethod(),
				new HiddenSingleMethod(),
				new NakedSubSetMethod((byte) 2),
				new HiddenSubSetMethod((byte) 2),		 
				new BlockLineInteractionMethod(),
				new NakedSubSetMethod((byte) 3),
				new HiddenSubSetMethod((byte) 3),
				new NakedSubSetMethod((byte) 4),
				new HiddenSubSetMethod((byte) 4),	
				new XWingMethod()
		};
		
		b.setupBoard();

		int[] featureVector = new int[24];
		int[] startPos = Counter.check(b);
		
		int sumPencilmarks = 0;
		
		for (byte i = 0; i < b.SIZE; i++) {
			for (byte j = 0; j < b.SIZE; j++) {
				sumPencilmarks += b.getCellAt(i, j).getPossibleValuesCount();
			}
		}
		
		featureVector[22] = sumPencilmarks;
		
		
		//solve the board
		boolean solving = true;
		int solveMethod = 0;
		
		while (solving) {
			if (solveMethod >= methods.length) {
				solving = false;
				solveMethod = 0;
			}
			else if (methods[solveMethod].apply(b)) {
				featureVector[solveMethod + 1]++;
				solveMethod = 0;
			} else
				solveMethod++;
		}
		
		//setup feature vector
		featureVector[0] = 0;
		featureVector[12] = b.GIVENCOUNT;

		for (int i = 0; i < startPos.length; i++) {
			featureVector[13+i] = startPos[i];
		}
		
		if (b.isSolvedCorrectly()) {
			
			featureVector[23] = 0;
			return featureVector;
			
		} else {
			if (withBacktracking) {
				if (Backtrack.solve(b)) {
					
					featureVector[23] = 1;
					return featureVector;
					
				}
			}
			return null;
		}
	}
	
	/**
	 * get the difficulty for a given board
	 * 
	 * @param b the board
	 * @return the difficulty of the board
	 */
	public Difficulty getDifficulty(Board b) {		
		int diff = nnh.predictBoard(new Board(b));
		return Difficulty.values()[diff];
	}

}
