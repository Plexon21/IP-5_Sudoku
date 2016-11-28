package ch.fhnw.ip5.sudoku.gui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import ch.fhnw.ip5.sudoku.sudoku.Board;

public class SudokuGUI extends JFrame implements ActionListener {
	Board[] boards;
	int currStep = 0;
	JPanel sudokuPanel;

	public SudokuGUI(Board b) {
		this.boards = new Board[] { b };
		initFrame();
		add(showSudoku(boards[0]), BorderLayout.CENTER);
		setVisible(true);
	}

	public SudokuGUI(Board[] boards, int currStep) {
		this.currStep = currStep;
		this.boards = boards;
		initFrame();
		sudokuPanel = showSudoku(boards[currStep]);
		add(sudokuPanel, BorderLayout.CENTER);

		JPanel buttons = new JPanel();
		Button prev = new Button("Previous Step");
		prev.addActionListener(this);
		Button next = new Button("Next Step");
		next.addActionListener(this);
		buttons.add(prev);
		buttons.add(next);
		add(buttons, BorderLayout.SOUTH);

		setVisible(true);
	}

	public void initFrame() {
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);
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
				value = (value.equals("0")) ? "" : value;
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

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		switch (command) {
		case "Previous Step":
			currStep--;
			if (currStep > 0) {
				remove(sudokuPanel);
				// sudokuPanel = new JPanel();
				sudokuPanel = showSudoku(boards[currStep]);
				add(sudokuPanel, BorderLayout.CENTER);
			} else
				currStep = 0;
			break;
		case "Next Step":
			if (currStep < boards.length-1) {
				currStep++;
				remove(sudokuPanel);
				// sudokuPanel = new JPanel();
				sudokuPanel = showSudoku(boards[currStep]);
				add(sudokuPanel, BorderLayout.CENTER);
			} else
				currStep = boards.length - 1;
			break;
		}
		repaint();
		revalidate();

	}
}
