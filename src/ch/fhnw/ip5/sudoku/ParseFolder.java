package ch.fhnw.ip5.sudoku;

import java.io.File;

import ch.fhnw.ip5.sudoku.reader.SudokuParser;

public class ParseFolder {
	public static String sourceFolder = "parse";
	public static String targetFolder = "res";
	public static void main(String[] args) {
		SudokuParser parser = new SudokuParser();
		if (args.length > 0) {
			sourceFolder = args[0];
		}
		if (args.length > 1) {
			targetFolder = args[1];
		}
		File node = new File(sourceFolder);
		parse(node, parser);
	}

	private static void parse(File node, SudokuParser parser) {
		if (node.isDirectory()) {
			String[] subNode = node.list();
			for (String fileName : subNode) {
				parse(new File(node, fileName), parser);
			}
		} else {
			SudokuParser.parseSudoku(node.getAbsolutePath());
		}

	}

	private static void write(String parseSudoku, File node) {
		File file = new File(targetFolder,)		
	}
}
