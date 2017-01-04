package ch.fhnw.ip5.sudoku;

import ch.fhnw.ip5.sudoku.reader.SudokuReader;
import ch.fhnw.ip5.sudoku.solver.Backtrack;
import ch.fhnw.ip5.sudoku.solver.methods.BlockLineInteractionMethod;
import ch.fhnw.ip5.sudoku.solver.methods.HiddenSubSetMethod;
import ch.fhnw.ip5.sudoku.solver.methods.SolveMethod;
import ch.fhnw.ip5.sudoku.solver.methods.XWingMethod;
import ch.fhnw.ip5.sudoku.sudoku.Board;

public class CheckSudoku {
	

	
	public static void main(String[] args) {
		
		Board b = new Board((byte)9,(byte)3,(byte)3);
		
		b.getCellAt((byte)0, (byte)2).setValue((byte)3);
		b.getCellAt((byte)0, (byte)5).setValue((byte)2);
		b.getCellAt((byte)0, (byte)6).setValue((byte)7);
		
		b.getCellAt((byte)3, (byte)0).setValue((byte)9);
		b.getCellAt((byte)4, (byte)3).setValue((byte)9);
		b.getCellAt((byte)5, (byte)7).setValue((byte)9);
		
		b.getCellAt((byte)6, (byte)4).setValue((byte)9);
		b.getCellAt((byte)7, (byte)2).setValue((byte)5);
		b.getCellAt((byte)7, (byte)5).setValue((byte)1);
		b.getCellAt((byte)7, (byte)6).setValue((byte)4);
		
		b.setupBoard();
		
		SolveMethod m = new XWingMethod();
		
		b.simplePrint();
		b.cluesPrint();
		
		if (m.solve(b)) {
			System.out.println("Worked");
		}
		
		b.simplePrint();
		b.cluesPrint();
		
		
		
//		for (int k = 0; k < b.SIZE; k++) {
//			
//			if (k != 8) {
//				
//				for (byte i = 0; i < b.SIZE; i++) {
//					for (byte j = 0; j < b.SIZE; j++) {
//						b.getCellAt(i, j).setImpossible((byte)(k+1));
//					}
//				}
//			}
//		}
		

		
//		b.getBoxes()[1].getCell((byte) 0).setValue((byte) 1);
//		b.getBoxes()[1].getCell((byte) 2).setValue((byte) 2);
//		b.getBoxes()[1].getCell((byte) 6).setValue((byte) 3);
//		b.getBoxes()[1].getCell((byte) 8).setValue((byte) 4);
//		b.getBoxes()[2].getCell((byte) 4).setValue((byte) 9);
//		
//		b.setupBoard();
//		
//		b.simplePrint();
//		b.cluesPrint();
//		
//		BlockLineInteractionMethod m5 = new BlockLineInteractionMethod();
//		
//		System.out.println(m5.solve(b));
//		
//		b.cluesPrint();
		
//		Board b = SudokuReader.parseLine("9 9 3 3 006900000004100000980006300150008900000000000003700068001500027000009600000004500");
////		
//		Backtrack.solve(b);
////		
//		b.simplePrint();
//		b.cluesPrint();
		
//		b.setupBoard();
//		System.out.println(b.createBoardString());
//		
//		boolean solving = true;
//		int m1counter = 0;
//		int m2counter = 0;
//		int m3counter = 0;
//		int m4counter = 0;				
//		
//		List<Board> steps = new ArrayList<Board>();
//		steps.add(new Board(b));
//		
//		while(solving) {
//			if (m1.solve(b)) {
//				m1counter++;
//				steps.add(new Board(b));	
//			} else if (m2.solve(b)) {
//				m2counter++;
//				steps.add(new Board(b));
//			} else if (m3.solve(b)) {
//				m3counter++;
//			} else if (m4.solve(b)) {
//				m4counter++;
//			} else {
//				solving = false;
//			}
//		}
//		
//		System.out.println("Result board");
//		b.simplePrint();
//		System.out.println();
//		System.out.println("Naked Single Counter  = " + m1counter);
//		System.out.println("Hidden Single Counter = " + m2counter);
//		System.out.println("Naked Subset Counter = " + m3counter);
//		System.out.println("Hidden Subset Counter = " + m4counter);
//		System.out.println("\n\n");
		
	}

}
