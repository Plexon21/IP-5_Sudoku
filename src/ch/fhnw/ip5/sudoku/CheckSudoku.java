package ch.fhnw.ip5.sudoku;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import ch.fhnw.ip5.sudoku.reader.SudokuReader;
import ch.fhnw.ip5.sudoku.solver.methods.HiddenSingleMethod;
import ch.fhnw.ip5.sudoku.solver.methods.HiddenSubSetMethod;
import ch.fhnw.ip5.sudoku.solver.methods.NakedSingleMethod;
import ch.fhnw.ip5.sudoku.solver.methods.NakedSubSetMethod;
import ch.fhnw.ip5.sudoku.solver.methods.SolveMethod;
import ch.fhnw.ip5.sudoku.sudoku.Board;

public class CheckSudoku {
	

	
	public static void main(String[] args) {
		
//		Board b = new Board((byte)9,(byte)3,(byte)3);
//		
//		for (int i = 0; i < b.SIZE; i++) {
//			
//			if (i != 4) {
//				
//				for (byte j = 0; j < b.SIZE; j++) {
//					for (byte k = 0; k < b.SIZE; k++) {
//						b.getBoxes()[i].getCell(j).setImpossible((byte)(k+1));
//					}
//				}
//			}
//		}
//		
//		b.getBoxes()[4].getCell((byte) 0).setImpossible((byte)1);
//		b.getBoxes()[4].getCell((byte) 0).setImpossible((byte)4);
//		b.getBoxes()[4].getCell((byte) 0).setImpossible((byte)6);
//		b.getBoxes()[4].getCell((byte) 0).setImpossible((byte)7);
//		b.getBoxes()[4].getCell((byte) 0).setImpossible((byte)9);
//		
//		b.getBoxes()[4].getCell((byte) 1).setImpossible((byte)2);
//		b.getBoxes()[4].getCell((byte) 1).setImpossible((byte)3);
//		b.getBoxes()[4].getCell((byte) 1).setImpossible((byte)5);
//		b.getBoxes()[4].getCell((byte) 1).setImpossible((byte)6);
//		b.getBoxes()[4].getCell((byte) 1).setImpossible((byte)9);
//		
//		b.getBoxes()[4].getCell((byte) 2).setImpossible((byte)1);
//		b.getBoxes()[4].getCell((byte) 2).setImpossible((byte)2);
//		b.getBoxes()[4].getCell((byte) 2).setImpossible((byte)4);
//		b.getBoxes()[4].getCell((byte) 2).setImpossible((byte)5);
//		b.getBoxes()[4].getCell((byte) 2).setImpossible((byte)6);
//		b.getBoxes()[4].getCell((byte) 2).setImpossible((byte)7);
//		
//		b.getBoxes()[4].getCell((byte) 3).setImpossible((byte)1);
//		b.getBoxes()[4].getCell((byte) 3).setImpossible((byte)2);
//		b.getBoxes()[4].getCell((byte) 3).setImpossible((byte)4);
//		b.getBoxes()[4].getCell((byte) 3).setImpossible((byte)5);
//		
//		b.getBoxes()[4].getCell((byte) 4).setImpossible((byte)1);
//		b.getBoxes()[4].getCell((byte) 4).setImpossible((byte)4);
//		b.getBoxes()[4].getCell((byte) 4).setImpossible((byte)8);
//		
//		b.getBoxes()[4].getCell((byte) 5).setImpossible((byte)1);
//		b.getBoxes()[4].getCell((byte) 5).setImpossible((byte)3);
//		b.getBoxes()[4].getCell((byte) 5).setImpossible((byte)4);
//		b.getBoxes()[4].getCell((byte) 5).setImpossible((byte)8);
//		
//		b.getBoxes()[4].getCell((byte) 6).setImpossible((byte)1);
//		b.getBoxes()[4].getCell((byte) 6).setImpossible((byte)4);
//		b.getBoxes()[4].getCell((byte) 6).setImpossible((byte)5);
//		b.getBoxes()[4].getCell((byte) 6).setImpossible((byte)6);
//		b.getBoxes()[4].getCell((byte) 6).setImpossible((byte)7);
//		b.getBoxes()[4].getCell((byte) 6).setImpossible((byte)8);
//		
//		b.getBoxes()[4].getCell((byte) 7).setImpossible((byte)1);
//		b.getBoxes()[4].getCell((byte) 7).setImpossible((byte)4);
//		b.getBoxes()[4].getCell((byte) 7).setImpossible((byte)6);
//		b.getBoxes()[4].getCell((byte) 7).setImpossible((byte)8);
//		b.getBoxes()[4].getCell((byte) 7).setImpossible((byte)9);
//		
//		b.getBoxes()[4].getCell((byte) 8).setImpossible((byte)7);
//		b.getBoxes()[4].getCell((byte) 8).setImpossible((byte)9);
//		
//		b.cluesPrint();
		
		SolveMethod m1 = new NakedSingleMethod();
		SolveMethod m2 = new HiddenSingleMethod();
		SolveMethod m3 = new NakedSubSetMethod();
		SolveMethod m4 = new HiddenSubSetMethod();
		
		Board b = SudokuReader.parseLine("9 9 3 3 006049000000600500090070004100000070309000408080000005700020030003004000000590600");
		
		b.setupBoard();
		System.out.println(b.createBoardString());
		
		boolean solving = true;
		int m1counter = 0;
		int m2counter = 0;
		int m3counter = 0;
		int m4counter = 0;				
		
		List<Board> steps = new ArrayList<Board>();
		steps.add(new Board(b));
		
		while(solving) {
			if (m1.solve(b)) {
				m1counter++;
				steps.add(new Board(b));	
			} else if (m2.solve(b)) {
				m2counter++;
				steps.add(new Board(b));
			} else if (m3.solve(b)) {
				m3counter++;
			} else if (m4.solve(b)) {
				m4counter++;
			} else {
				solving = false;
			}
		}
		
		System.out.println("Result board");
		b.simplePrint();
		System.out.println();
		System.out.println("Naked Single Counter  = " + m1counter);
		System.out.println("Hidden Single Counter = " + m2counter);
		System.out.println("Naked Subset Counter = " + m3counter);
		System.out.println("Hidden Subset Counter = " + m4counter);
		System.out.println("\n\n");
		
	}

}
