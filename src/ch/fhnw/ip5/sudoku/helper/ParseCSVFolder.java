package ch.fhnw.ip5.sudoku.helper;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import ch.fhnw.ip5.sudoku.reader.SudokuCSVParser;
import ch.fhnw.ip5.sudoku.reader.SudokuParser;

public class ParseCSVFolder {
	public static String sourceFolder = "C://Users//Matth//Downloads//Sudoku//KTI";
	public static String targetFolder = "C://Users//Matth//Downloads//Sudoku//KTI_parsed";

	public static void main(String[] args) {
		SudokuCSVParser parser = new SudokuCSVParser();
		if (args.length > 0) {
			sourceFolder = args[0];
		}
		if (args.length > 1) {
			targetFolder = args[1];
		}
		File node = new File(sourceFolder);
		String[] subNodes = node.list();
		for (String fileName : subNodes) {
			parse(new File(node, fileName), parser, new File(targetFolder));
		}
	}

	private static void parse(File source, SudokuCSVParser parser, File target) {
		if (source.isDirectory()) {
			File targetFolder = new File(target, source.getName());
			targetFolder.mkdirs();
			String[] subNode = source.list();

			for (String fileName : subNode) {
				parse(new File(source, fileName), parser, targetFolder);
			}
		} else {
			write(SudokuCSVParser.parseSudoku(source.getAbsolutePath()),
					new File(target, source.getName().substring(0, source.getName().length() - 3) + "sudoku"));
		}
	}

	private static void write(String sudokuString, File target) {
		target.getParentFile().mkdirs();
		try {
			target.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try (PrintWriter writer = new PrintWriter(target)) {

			writer.println(sudokuString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
