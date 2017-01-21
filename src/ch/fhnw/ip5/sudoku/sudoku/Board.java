package ch.fhnw.ip5.sudoku.sudoku;

import java.util.UUID;
import ch.fhnw.ip5.sudoku.solver.Updater;

/**
 * Board class that represents the whole Sudoku
 */
public class Board {
	
	/**
	 * ID
	 */
	public final UUID id = UUID.randomUUID();
	
	/**
	 * size of the Sudoku
	 */
	public final byte SIZE;	
	
	/**
	 * height of a box of the sudoku
	 */
	public final byte BOXHEIGHT;
	/**
	 * width of a box of the sudoku
	 */
	public final byte BOXWIDTH;
	
	/**
	 * number of values set at the creation of the sudoku
	 */
	public byte GIVENCOUNT;
	
	/**
	 * 2D array of the cells in the sudoku
	 */
	private Cell[][] cells;
	
	/**
	 * array of the rows of the sudoku
	 */
	private Container[] rows;
	/**
	 * array of the columns of the sudoku
	 */
	private Container[] columns;
	/**
	 * array of the boxes of the sudoku
	 */
	private Container[] boxes;

	/**
	 * constructor
	 * 
	 * @param size the size of the sudoku
	 * @param boxheight the height of a box of the sudoku
	 * @param boxwidth the width of a box of the sudoku
	 * @param values the values of the cell of the sudoku / 0 is interpreted as not set
	 */
	public Board(byte size, byte boxheight, byte boxwidth, byte[] values) {
		this.SIZE = size;
		this.BOXHEIGHT = boxheight;
		this.BOXWIDTH = boxwidth;
		
		if (values.length != this.SIZE * this.SIZE) throw new IllegalArgumentException("values.length is not equal to size squared");
		
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
	
	/**
	 * copy constructor
	 * 
	 * @param b the board to copy
	 */
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
	
	/**
	 * constructor to create an empty board
	 * 
	 * @param size the size of the board
	 * @param boxheight the height of a box of the sudoku
	 * @param boxwidth the width of a box of the sudoku
	 */
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
	
	/**
	 * updates the pencilmarks of all cells according to the given cells
	 * also updates the givencount
	 */
	public void setupBoard() {
		
		//remove all changes to the pencilmarks
		for (byte i = 0; i < SIZE; i++) {
			for (byte j = 0; j < SIZE; j++) {
				cells[i][j].setup();
			}
		}
		
		byte newGivenCount = 0;
		
		//reassign the pencilmarks according to the given cells on the board
		for (byte i = 0; i < SIZE; i++) {
			for (byte j = 0; j < SIZE; j++) {
				if (cells[i][j].getValue() != 0) {
					Updater.updateBoard(this, i, j, cells[i][j].getValue(), UsedMethod.GIVEN);
					newGivenCount++;
				}
			}
		}
		
		this.GIVENCOUNT = newGivenCount;
		
	}
	
	/**
	 * creates the string that is used to save the board
	 * 
	 * @return the created String
	 */
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
	
	
	/**
	 * check to see if every cell of the board has a set value.
	 * does not check if the board is solved correctly!
	 * 
	 * @return true if every cell of the board has a set value
	 */
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
	
	/**
	 * check to see if the sudoku is solved correctly
	 * 
	 * @return true if the sudoku is solved correctly
	 */
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
	
	/**
	 * prints the board to the console
	 */
	public void simplePrint() {
		System.out.println("cells:");
		
		for (int i = 0; i < this.SIZE; i++) {
			for (int j = 0; j < this.SIZE; j++) {
				System.out.print((j % this.BOXWIDTH == 0 ? "|" : " ") + (cells[i][j].getValue() == 0 ? " " : cells[i][j].getValue()) + (j == this.SIZE-1 ? "|" : ""));				
			}
			System.out.println((i+1) % this.BOXHEIGHT == 0 ? "\n" : "");
		}
	}
	
	/**
	 * prints the pencilmarks of the sudoku to the console
	 */
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
