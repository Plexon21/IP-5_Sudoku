package ch.fhnw.ip5.sudoku;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ch.fhnw.ip5.sudoku.network.SudokuNetwork;
import ch.fhnw.ip5.sudoku.reader.SudokuReader;
import ch.fhnw.ip5.sudoku.sudoku.Board;

public class NN_Trying {
	public static void main(String[] args) {
		
		SudokuNetwork network = new SudokuNetwork(22, 50, 7, 80, 200, 0.2, 0.01);		
		network.initWithSudokus("C:\\Users\\Matth\\OneDrive\\IP5-Sudoku\\Raetsel AG Sudoku\\all_parsed");
//		SudokuNetwork network = (SudokuNetwork)SudokuNetwork.createFromFile("C:\\Programming\\IP-5_sudoku\\res\\network.txt");
//		network.setInputNodes(22);
//		network.setHiddenNodes(10);
//		network.setOutputNodes(7);
//		network.setTestSetPercentage(80);
//		network.setMaxIter(500);
//		network.setLearningRate(0.2);
//		network.setMaxError(0.01);
		
		List<Board> boards = new ArrayList<Board>();
		try {
			boards = SudokuReader.readFromFile(new File("C:\\Users\\Matth\\OneDrive\\IP5-Sudoku\\Raetsel AG Sudoku\\KTI_parsed\\S2\\SU_K9x9_S2_R_0020704.sudoku"));
		} catch (Exception e) {			
			e.printStackTrace();
		}
		
		System.out.println(network.getSudokuDifficulty(boards.get(0)));
	}	
}
