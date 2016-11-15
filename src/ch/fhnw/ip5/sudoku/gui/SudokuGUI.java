package ch.fhnw.ip5.sudoku.gui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import ch.fhnw.ip5.sudoku.sudoku.Board;

public class SudokuGUI {
	JFrame frame;
	Board[] boards;

	public SudokuGUI() {
		initFrame();
	}

	public SudokuGUI(Board b) {
		this.boards = new Board[]{b};
		initFrame();
		frame.add(showSudoku(boards[0]), BorderLayout.CENTER);
	}

	/*public SudokuGUI(Board[] boards) {
		this.boards = boards;
		initFrame();
		frame.add(showSudoku(boards[0]), BorderLayout.CENTER);
		JPanel buttons = new JPanel();
		Button prev = new Button("Previous Step");		
		Button next = new Button("Next Step");
		buttons.add(prev);
		buttons.add(next);
		frame.add(buttons, BorderLayout.SOUTH);

	}*/

	public void initFrame() {
		frame = new JFrame("Sudoku");
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		frame.setVisible(true);
	}

	public JPanel showSudoku(Board b) {
		JPanel grid = new JPanel();
		grid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		grid.setLayout(new GridLayout(b.HEIGHT / b.BOXHEIGHT, b.WIDTH / b.BOXWIDTH));

		for (byte i = 0; i < b.HEIGHT; i++) {

			JPanel box = new JPanel();
			box.setBorder(BorderFactory.createLineBorder(Color.black, 2));
			box.setLayout(new GridLayout(b.BOXHEIGHT, b.BOXWIDTH));

			for (byte j = 0; j < b.WIDTH; j++) {
				String value = String.valueOf(b.getCellAt(i, j).getValue());
				final JTextField field = new JTextField(value);
				field.setHorizontalAlignment(SwingConstants.CENTER);
				field.setBorder(BorderFactory.createLineBorder(Color.black));
				field.setEditable(false);
				field.setFont(new Font("Arial", 0, 24));
				box.add(field);
			}
			grid.add(box);
		}

		return grid;
	}
}
