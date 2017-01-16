package ch.fhnw.ip5.sudoku.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.builder.Diff;

import ch.fhnw.ip5.sudoku.solver.Backtrack;
import ch.fhnw.ip5.sudoku.solver.Solver;
import ch.fhnw.ip5.sudoku.sudoku.Board;
import ch.fhnw.ip5.sudoku.sudoku.Cell;
import ch.fhnw.ip5.sudoku.sudoku.Difficulty;

public class Generator {
	
	public static int minValue = 20;
	public static int maxValue = 36;
	private static Random rng = new Random(System.nanoTime());
	
	public static Board generateBoard(Board ori) {
		
		Board b;
		List<Cell> possibleAddings;
		int amount;
		Board sol;
			
		b = new Board(ori);
		
		Permutation.permutateBoard(b);
		
		possibleAddings = new ArrayList<Cell>();
		
		for (byte i = 0; i < b.SIZE; i++) {
			for (byte j = 0; j < b.SIZE; j++) {
				Cell temp = b.getCellAt(i, j);
				if (temp.getValue() != 0) {
					
					
					
//					for (Cell c : Flipper.getFlippedCellsDia(b, temp)) {
//						if (c.getValue() == 0) {
//							possibleAddings.add(c);
//						}
//					}
//					for (Cell c : Flipper.getFlippedCellsOrtho(b, temp)) {
//						if (c.getValue() == 0) {
//							possibleAddings.add(c);
//						}
//					}
				} else {
					possibleAddings.add(temp);
				}
			}
		}
		
		Collections.shuffle(possibleAddings, rng);
		
		amount = rng.nextInt(maxValue + 1 - minValue) + minValue - b.GIVENCOUNT;
		
		sol = new Board(b);
		
		if (!Solver.solve(sol, false)) return null;
		
//		sol.simplePrint();
//		System.out.println(sol.isSolvedCorrectly());
		
		for (int i = 0; i < amount; i++) {
			Cell c = possibleAddings.get(i);
			c.setValue(sol.getCellAt(c.getHpos(), c.getWpos()).getValue());
		}
		
		
		b.setupBoard();
		
		return b;
		
	}
	
	public static Board generateBoardRemoving(Board ori) {
		
		Board b = new Board(ori);
		Permutation.permutateBoard(b);
		Solver.solve(b, true);
		
		List<Cell> cells = new ArrayList<>();
		
		for (byte i = 0; i < b.SIZE; i++) {
			for (byte j = 0; j < b.SIZE; j++) {
				cells.add(b.getCellAt(i, j));
			}
		}
		
		int amount = rng.nextInt(maxValue+1 - minValue) + minValue;
		int toRemove = 81 - amount;
		
		while(toRemove > 0) {
			
			Cell f = cells.get(rng.nextInt(cells.size()));
			
			byte x = f.getHpos();
			byte y = f.getWpos();
				
			if (x == 4 && y == 4) {
				
				f.removeValue();
				cells.remove(f);
				toRemove--;
				
			} else {
				
				byte distx = (byte)(x - 4);
				byte disty = (byte)(y - 4);
				
				Cell g = b.getCellAt((byte)(4 - distx), (byte)(4 - disty));
				
				f.removeValue();
				g.removeValue();
				toRemove -= 2;
				
				cells.remove(f);
				cells.remove(g);
			}
			
		}
		
		if (!Solver.solve(new Board(b), false)) return null;
		
		b.setupBoard();		
		
		return b;
		
	}
	

}
