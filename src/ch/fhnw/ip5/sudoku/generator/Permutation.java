package ch.fhnw.ip5.sudoku.generator;

import java.util.Random;

import ch.fhnw.ip5.sudoku.sudoku.Board;
import ch.fhnw.ip5.sudoku.sudoku.Cell;

public class Permutation {
	
	private static Random rng = new Random();

	public static void permutateBoard(Board b) {
		
		if (rng.nextDouble() < 0.5) transponse(b);
		
		rearrangeNumbers(b);
		
		swapRowsColumnsAndBlocks(b);
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
				
				if (c.getValue() != 0) {
					
					c.setValue((byte)(newPos[c.getValue()-1]+1));
					
				}
			}
		}
	}
	
	private static void swapRowsColumnsAndBlocks(Board b) {
		
		byte[] pos = new byte[] {0,1,2};
		
		swapBlocksHorizontally(b, pos);
		swapBlocksVertically(b, pos);
		swapColumnsInBlock(b, pos, 0);
		swapColumnsInBlock(b, pos, 1);
		swapColumnsInBlock(b, pos, 2);
		swapRowsInBlock(b, pos, 0);
		swapRowsInBlock(b, pos, 1);
		swapRowsInBlock(b, pos, 2);
	}
	
	private static void swapBlocksHorizontally(Board b, byte[] pos) {
		
		shuffleArray(pos);
		
		for (byte i = 0; i < b.SIZE; i++) {
			for (byte j = 0; j < b.BOXWIDTH; j++) {
				
				byte[] v = new byte[] {
						b.getCellAt(i, j).getValue(),
						b.getCellAt(i, (byte)(j+b.BOXWIDTH)).getValue(),
						b.getCellAt(i, (byte)(j+2*b.BOXWIDTH)).getValue()
				};
				
				for (int k = 0; k < 3; k++) {
					
					Cell c = b.getCellAt(i, (byte)(j + pos[k] * b.BOXWIDTH));
					
					if (v[k] == 0) {
						c.removeValue();
					} else {
						c.setValue(v[k]);
					}
					
				}
			}
		}
	}
	
	private static void swapBlocksVertically(Board b, byte[] pos) {
		
		shuffleArray(pos);
		
		for (byte i = 0; i < b.BOXHEIGHT; i++) {
			for (byte j = 0; j < b.SIZE; j++) {
				
				byte[] v = new byte[] {
						b.getCellAt(i, j).getValue(),
						b.getCellAt((byte)(i+b.BOXHEIGHT), j).getValue(),
						b.getCellAt((byte)(i+2*b.BOXHEIGHT), j).getValue()
				};
				
				for (int k = 0; k < 3; k++) {
					
					Cell c = b.getCellAt((byte)(i + pos[k] * b.BOXHEIGHT), j);
					
					if (v[k] == 0) {
						c.removeValue();
					} else {
						c.setValue(v[k]);
					}
					
				}
			}
		}
	}
	
	private static void swapRowsInBlock(Board b, byte[] pos, int block) {
		
		shuffleArray(pos);
		
		for (byte i = 0; i < b.SIZE; i++) {
				
			byte[] v = new byte[] {
					b.getCellAt((byte)(block*b.BOXHEIGHT), i).getValue(),
					b.getCellAt((byte)(block*b.BOXHEIGHT+1), i).getValue(),
					b.getCellAt((byte)(block*b.BOXHEIGHT+2), i).getValue()
			};
			
			for (int k = 0; k < 3; k++) {
				
				Cell c = b.getCellAt((byte)(block * b.BOXHEIGHT + pos[k]), i);
				
				if (v[k] == 0) {
					c.removeValue();
				} else {
					c.setValue(v[k]);
				}
			}
		}
	}
	
	private static void swapColumnsInBlock(Board b, byte[] pos, int block) {
		
		shuffleArray(pos);
		
		for (byte i = 0; i < b.SIZE; i++) {
				
			byte[] v = new byte[] {
					b.getCellAt(i, (byte)(block*b.BOXWIDTH)).getValue(),
					b.getCellAt(i, (byte)(block*b.BOXWIDTH+1)).getValue(),
					b.getCellAt(i, (byte)(block*b.BOXWIDTH+2)).getValue()
			};
			
			for (int k = 0; k < 3; k++) {
				
				Cell c = b.getCellAt(i, (byte)(block * b.BOXWIDTH + pos[k]));
				
				if (v[k] == 0) {
					c.removeValue();
				} else {
					c.setValue(v[k]);
				}
			}
		}
	}
	
	private static void shuffleArray(byte[] array) {
		
		for (int i = array.length - 1; i > 0; i--) {
			int index = rng.nextInt(i + 1);
			
			byte a = array[index];
			array[index] = array[i];
			array[i] = a;
		}
	}
	
	
}
