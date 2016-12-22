package ch.fhnw.ip5.sudoku.sudoku;

import java.util.UUID;

import ch.fhnw.ip5.sudoku.solver.Updater;

public class Board {
	public final UUID id = UUID.randomUUID();	
	public final byte SIZE;	
	
	public final byte BOXHEIGHT;
	public final byte BOXWIDTH;
	
	public final byte GIVENCOUNT;
	
	private Cell[][] cells;
	private Container[] rows;
	private Container[] columns;
	private Container[] boxes;
	/*
	 * Idea: Box nach einem Array füllen in dem die Box-nummer relativ zur einlese-nummer stehen
	 * 
	 * eg:
	 * 	1 2 2 2
	 *  1 3 3 2
	 *  1 3 3 4
	 *  1 4 4 4
	 * 
	 */
	
	public Board(byte size, byte boxheight, byte boxwidth, byte[] values) {
		this.SIZE = size;
		this.BOXHEIGHT = boxheight;
		this.BOXWIDTH = boxwidth;
		
		assert values.length == this.SIZE * this.SIZE; //TODO: replace with proper condition checking
		
		cells = new Cell[this.SIZE][this.SIZE];
		
		rows = new Container[this.SIZE];
		columns = new Container[this.SIZE];
		boxes = new Container[this.SIZE];
		
		for (byte i = 0; i < this.SIZE; i++) {
			rows[i] = new Container(this.SIZE);
			columns[i] = new Container(this.SIZE);
			boxes[i] = new Container(this.SIZE);
		}
		byte givenCount = 0;
		
		// (1)
		for (byte i = 0; i < values.length; i++) {
			
			byte hpos = (byte) (i/this.SIZE);
			byte wpos = (byte) (i%this.SIZE);
			
			Cell c;
			
			if (values[i] == 0) {
				c = new Cell(this.SIZE, hpos, wpos);
			} else {
				c = new Cell(this.SIZE, hpos, wpos, values[i]);
				givenCount++;
			}
			
			cells[hpos][wpos] = c;
			rows[hpos].setCell(c, wpos);
			columns[wpos].setCell(c, hpos);
			
			byte hBoxstart = (byte) (hpos / BOXHEIGHT);
			byte wBoxstart = (byte) (wpos / BOXWIDTH);
			
			byte hBoxPos = (byte) (hpos % BOXHEIGHT);
			byte wBoxPos = (byte) (wpos % BOXWIDTH);
			
			boxes[hBoxstart*BOXWIDTH + wBoxstart].setCell(c, (byte)(hBoxPos*BOXWIDTH + wBoxPos));
		}
		this.GIVENCOUNT = givenCount;
	}
	
	public Board(Board b) {
		this.SIZE = b.SIZE;
		this.BOXHEIGHT = b.BOXHEIGHT;
		this.BOXWIDTH = b.BOXWIDTH;
		this.GIVENCOUNT = b.GIVENCOUNT;
		
		cells = new Cell[this.SIZE][this.SIZE];
		
		rows = new Container[this.SIZE];
		columns = new Container[this.SIZE];
		boxes = new Container[this.SIZE];
		
		for (byte i = 0; i < this.SIZE; i++) {
			rows[i] = new Container(this.SIZE);
			columns[i] = new Container(this.SIZE);
			boxes[i] = new Container(this.SIZE);
		}
		
		for (byte hpos = 0; hpos < this.SIZE; hpos++) {
			for (byte wpos = 0; wpos < this.SIZE; wpos++) {
				
				Cell c = new Cell(b.getCellAt(hpos, wpos));
				
				cells[hpos][wpos] = c;
				rows[hpos].setCell(c, wpos);
				columns[wpos].setCell(c, hpos);
				
				byte hBoxstart = (byte) (hpos / BOXHEIGHT);
				byte wBoxstart = (byte) (wpos / BOXWIDTH);
				
				byte hBoxPos = (byte) (hpos % BOXHEIGHT);
				byte wBoxPos = (byte) (wpos % BOXWIDTH);
				
				boxes[hBoxstart*BOXWIDTH + wBoxstart].setCell(c, (byte)(hBoxPos*BOXWIDTH + wBoxPos));

			}
		}
	}
	
	public Board(byte size, byte boxheight, byte boxwidth) {
		this.SIZE = size;
		this.BOXHEIGHT = boxheight;
		this.BOXWIDTH = boxwidth;
		this.GIVENCOUNT = 0;
		
		cells = new Cell[this.SIZE][this.SIZE];
		
		rows = new Container[this.SIZE];
		columns = new Container[this.SIZE];
		boxes = new Container[this.SIZE];
		
		for (byte i = 0; i < this.SIZE; i++) {
			rows[i] = new Container(this.SIZE);
			columns[i] = new Container(this.SIZE);
			boxes[i] = new Container(this.SIZE);
		}
		
		for (byte hpos = 0; hpos < this.SIZE; hpos++) {
			for (byte wpos = 0; wpos < this.SIZE; wpos++) {
				Cell c = new Cell(size, hpos, wpos);
				
				cells[hpos][wpos] = c;
				rows[hpos].setCell(c, wpos);
				columns[wpos].setCell(c, hpos);
				
				byte hBoxstart = (byte) (hpos / BOXHEIGHT);
				byte wBoxstart = (byte) (wpos / BOXWIDTH);
				
				byte hBoxPos = (byte) (hpos % BOXHEIGHT);
				byte wBoxPos = (byte) (wpos % BOXWIDTH);
				
				boxes[hBoxstart*BOXWIDTH + wBoxstart].setCell(c, (byte)(hBoxPos*BOXWIDTH + wBoxPos));
			}
		}
	}
	
	public void setupBoard() {
		for (byte i = 0; i < SIZE; i++) {
			for (byte j = 0; j < SIZE; j++) {
				if (cells[i][j].getValue() != 0) {
					Updater.updateBoard(this, i, j, cells[i][j].getValue(), UsedMethod.GIVEN);
				}
			}
		}
	}
	
	public String createBoardString() {
		String s = 
				+ this.SIZE + " "
				+ this.SIZE + " "
				+ this.BOXHEIGHT + " "
				+ this.BOXWIDTH + " ";
		
		for (int i = 0; i < this.SIZE; i++) {
			for (int j = 0; j < this.SIZE; j++) {
				s += cells[i][j].getValue();				
			}
		}
		
		return s;
	}
	
	public boolean isFilled() {
		
		for (int i = 0; i < this.SIZE; i++) {
			for (int j = 0; j < this.SIZE; j++) {
				if (cells[i][j].getValue() == 0) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	public boolean isSolvedCorrectly() {
		
		for (int i = 0; i < this.SIZE; i++) {
			boolean[] check = new boolean[3*this.SIZE];
			
			for (Cell c : rows[i].getCells()) {
				if (c.getValue() == 0) return false;
				
				check[c.getValue()-1] = true;
			}
			for (Cell c : columns[i].getCells()) {				
				check[this.SIZE + c.getValue()-1] = true;
			}
			for (Cell c : boxes[i].getCells()) {				
				check[2*this.SIZE + c.getValue()-1] = true;
			}
			
			for (boolean b : check) {
				if (!b) return false;
			}
		}
		
		return true;
		
	}
	
	public void simplePrint() {
		System.out.println("cells:");
		
		for (int i = 0; i < this.SIZE; i++) {
			for (int j = 0; j < this.SIZE; j++) {
				System.out.print((j % this.BOXWIDTH == 0 ? "|" : " ") + (cells[i][j].getValue() == 0 ? " " : cells[i][j].getValue()) + (j == this.SIZE-1 ? "|" : ""));				
			}
			System.out.println((i+1) % this.BOXHEIGHT == 0 ? "\n" : "");
		}
	}
	
	public void cluesPrint() {
		System.out.println("cells with clues");
		
		for (int i = 0; i < this.SIZE; i++) {
			for (int j = 0; j < this.SIZE; j++) {
				Cell tempCell = cells[i][j];
				System.out.print("Cell #" + i + " "+ j);
				
				for (byte x = 1; x <= this.SIZE; x++) {
					System.out.print(tempCell.isPossible(x) ? " X" : "  ");
				}
				System.out.println(" " + tempCell.getPossibleValuesCount());
			}
		}
	}
	
	public Cell[][] getCells() {
		return cells;
	}
	
	public Container[] getRows() {
		return rows;
	}
	
	public Container[] getColumns() {
		return columns;
	}
	
	public Container[] getBoxes() {
		return boxes;
	}
	
	public Cell getCellAt(byte hpos, byte wpos) {
		return cells[hpos][wpos];
	}
}
