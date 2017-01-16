package ch.fhnw.ip5.sudoku.gui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import ch.fhnw.ip5.sudoku.sudoku.Board;
import ch.fhnw.ip5.sudoku.sudoku.Cell;
import ch.fhnw.ip5.sudoku.sudoku.UsedMethod;

public class SudokuGUI extends JFrame implements ActionListener {
	private HashMap<UsedMethod, Color> colors;
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
		this.colors = new HashMap<UsedMethod,Color>();
		this.currStep = currStep;
		this.boards = boards;
		initColors();
		initFrame();
		sudokuPanel = showSudoku(boards[currStep]);
		add(sudokuPanel, BorderLayout.CENTER);

		JPanel buttons = new JPanel();
		Button start = new Button("Start");
		start.addActionListener(this);
		Button prev = new Button("Previous Step");
		prev.addActionListener(this);
		Button next = new Button("Next Step");
		next.addActionListener(this);
		Button end = new Button("End");
		end.addActionListener(this);
		buttons.add(start);
		buttons.add(prev);
		buttons.add(next);
		buttons.add(end);
		add(buttons, BorderLayout.SOUTH);

		setVisible(true);
	}

	private void initColors() {
		colors.put(UsedMethod.GIVEN, Color.WHITE);
		colors.put(UsedMethod.NAKEDSINGLE, Color.ORANGE);
		colors.put(UsedMethod.HIDDENSINGLE, Color.CYAN);
		colors.put(UsedMethod.BACKTRACK, Color.PINK);
	}

	public void initFrame() {
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);
	}

	public JPanel showSudoku(Board b) {
		JPanel grid = new JPanel();
		grid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		grid.setLayout(new GridLayout(b.SIZE / b.BOXHEIGHT, b.SIZE / b.BOXWIDTH));

		for (byte i = 0; i < b.SIZE; i++) {

			JPanel box = new JPanel();
			box.setBorder(BorderFactory.createLineBorder(Color.black, 2));
			box.setLayout(new GridLayout(b.BOXHEIGHT, b.BOXWIDTH));

			for (byte j = 0; j < b.SIZE; j++) {
				Cell c = b.getBoxes()[i].getCell(j);
				String value = String.valueOf(c.getValue());
				value = (value.equals("0")) ? "" : value;
				final JTextField field = new JTextField(value);
				field.setHorizontalAlignment(SwingConstants.CENTER);
				field.setBorder(BorderFactory.createLineBorder(Color.black));
				field.setEditable(false);
				int ord = c.getSolveMethod().ordinal();
				field.setBackground(colors.get(ord));
				field.setFont(new Font("Arial", 0, 24));
				field.setForeground(Color.BLACK);
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
		case "Start":
			currStep = 0;
			remove(sudokuPanel);
			sudokuPanel = showSudoku(boards[currStep]);
			add(sudokuPanel, BorderLayout.CENTER);
			break;
		case "Previous Step":
			if (currStep > 0) {
				currStep--;
				remove(sudokuPanel);
				sudokuPanel = showSudoku(boards[currStep]);
				add(sudokuPanel, BorderLayout.CENTER);
			} else
				currStep = 0;
			break;
		case "Next Step":
			if (currStep < boards.length - 1) {
				currStep++;
				remove(sudokuPanel);
				sudokuPanel = showSudoku(boards[currStep]);
				add(sudokuPanel, BorderLayout.CENTER);
			} else
				currStep = boards.length - 1;
			break;
		case "End":
			currStep = boards.length - 1;
			remove(sudokuPanel);
			sudokuPanel = showSudoku(boards[currStep]);
			add(sudokuPanel, BorderLayout.CENTER);
			break;
		}
		repaint();
		revalidate();
	}
}
