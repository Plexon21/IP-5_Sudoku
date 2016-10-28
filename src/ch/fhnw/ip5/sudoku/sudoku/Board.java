package ch.fhnw.ip5.sudoku.sudoku;

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
		
		for (int i = 0; i < values.length; i++) {
			cells[i/9][i%9] = values[i] == 0 ? new Cell() : new Cell(values[i]);
		}
	}
}
