package ch.fhnw.ip5.sudoku.generator;

import java.util.Random;

import ch.fhnw.ip5.sudoku.sudoku.Board;
import ch.fhnw.ip5.sudoku.sudoku.Cell;

public class Permutation {
	
	private static Random rng = new Random();

	public static void permutateBoard(Board b) {
		
		if (rng.nextDouble() < 0.5) transponse(b);
		
		rearrangeNumbers(b);
		
		
		
	}
	
	private static void transponse(Board b) {
		
		for (byte i = 1; i < b.SIZE; i++) {
			for (byte j = 0; j < i; j++) {
				Cell c1 = b.getCellAt(i, j);
				Cell c2 = b.getCellAt(j, i);
				
				byte c1Value = c1.getValue();
				byte c2Value = c2.getValue();
				
				if (c1Value == 0) {
					c2.removeValue();
				} else {
					c2.setValue(c1Value);
				}
				
				if (c2Value == 0) {
					c1.removeValue();
				} else {
					c1.setValue(c2Value);
				}
			}
		}
	}
	
	private static void rearrangeNumbers(Board b) {
		
		byte[] newPos = new byte[b.SIZE];
		
		for (byte i = 0; i < b.SIZE; i++) {
			newPos[i] = i;
		}
	    
		shuffleArray(newPos);
		
		for (byte i = 0; i < b.SIZE; i++) {
			for (byte j = 0; j < b.SIZE; j++) {
				
				Cell c = b.getCellAt(i, j);
				
				if (newPos[c.getValue()] == 0) {
					c.removeValue();
				} else {
					c.setValue(newPos[c.getValue()]);
				}
			}
		}
	}
	
	static void shuffleArray(byte[] array) {
		
		for (int i = array.length - 1; i > 0; i--) {
			int index = rng.nextInt(i + 1);
			
			byte a = array[index];
			array[index] = array[i];
			array[i] = a;
		}
	}
	
	
}
