package ch.fhnw.ip5.sudoku.sudoku;

import ch.fhnw.ip5.sudoku.solver.Updater;

public class Board {
	
	public final byte HEIGHT;
	public final byte WIDTH;
	
	public final byte BOXHEIGHT;
	public final byte BOXWIDTH;
	
	private Cell[][] cells;
	private Row[] rows;
	private Column[] columns;
	private Box[] boxes;
	
	public Board(byte height, byte width, byte boxheight, byte boxwidth, byte[] values) {
		this.HEIGHT = height;
		this.WIDTH = width;
		this.BOXHEIGHT = boxheight;
		this.BOXWIDTH = boxwidth;
		
		assert values.length == this.HEIGHT * this.WIDTH; //TODO: replace with proper condition checking
		
		cells = new Cell[this.HEIGHT][this.WIDTH];
		rows = new Row[this.HEIGHT];
		columns = new Column[this.WIDTH];
		boxes = new Box[(this.HEIGHT/this.BOXHEIGHT) * (this.WIDTH/this.BOXWIDTH)];
		
		// (1)
		for (byte i = 0; i < values.length; i++) {
			cells[i/9][i%9] = values[i] == 0 ? new Cell() : new Cell(values[i]);
		}
		
		for (byte i = 0; i < this.HEIGHT; i++) {
			rows[i] = new Row(this.WIDTH);
		}
		
		for (byte i = 0; i < this.WIDTH; i++) {
			columns[i] = new Column(this.HEIGHT);
		}
		
		for (byte i = 0; i < this.HEIGHT; i++) {
			for (byte j = 0; j < this.WIDTH; j++) {
				
				//TODO: combine this with (1)
				
				Cell tempCell = cells[i][j];
				
				rows[i].setCell(tempCell, j);
				columns[j].setCell(tempCell, i);
			}
		}
	}
	
	public void setupBoard() {
		for (byte i = 0; i < HEIGHT; i++) {
			for (byte j = 0; j < WIDTH; j++) {
				if (cells[i][j].getValue() != 0) {
					Updater.updateBoard(this, i, j, cells[i][j].getValue());
				}
			}
		}
	}
	
	public String createBoardString() {
		String s = 
				+ this.HEIGHT + " "
				+ this.WIDTH + " "
				+ this.BOXHEIGHT + " "
				+ this.BOXWIDTH + " ";
		
		for (int i = 0; i < this.HEIGHT; i++) {
			for (int j = 0; j < this.WIDTH; j++) {
				s += cells[i][j].getValue();				
			}
		}
		
		return s;
	}
	
	public void simplePrint() {
		System.out.println("cells:");
		
		for (int i = 0; i < this.HEIGHT; i++) {
			for (int j = 0; j < this.WIDTH; j++) {
				System.out.print((j % this.BOXWIDTH == 0 ? "|" : " ") + (cells[i][j].getValue() == 0 ? " " : cells[i][j].getValue()) + (j == this.WIDTH-1 ? "|" : ""));				
			}
			System.out.println((i+1) % this.BOXHEIGHT == 0 ? "\n" : "");
		}
	}
	
	public void cluesPrint() {
		System.out.println("cells with clues");
		
		for (int i = 0; i < this.HEIGHT; i++) {
			for (int j = 0; j < this.WIDTH; j++) {
				Cell tempCell = cells[i][j];
				System.out.print("Cell #" + (i*this.WIDTH + j));
				
				for (byte x = 1; x <= this.WIDTH; x++) {
					System.out.print(tempCell.isPossible(x) ? " X" : "  ");
				}
				System.out.println();
			}

		}
	}
	
	public Cell[][] getCells() {
		return cells;
	}
	
	public Row[] getRows() {
		return rows;
	}
	
	public Column[] getColumns() {
		return columns;
	}
	
	public Box[] getBoxes() {
		return boxes;
	}
	
	public Cell getCellAt(byte hpos, byte wpos) {
		return cells[hpos][wpos];
	}
}
