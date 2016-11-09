package ch.fhnw.ip5.sudoku.reader;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ch.fhnw.ip5.sudoku.sudoku.Board;

public class SudokuParser {

	public static char[] values;

	public static void main(String[] args) {
		Board b = parseAndReadSudoku("06010054100_000.31_32.sud");
		b.simplePrint();
	}

	public static String parseSudoku(String fileName) {
		try {
			DocumentBuilderFactory bf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = bf.newDocumentBuilder();
			Document doc = db.parse(new File(fileName));
			traverse(doc.getDocumentElement(), 0);
			int fieldSide = (int) Math.sqrt(values.length);
			int boxSide = (int) Math.sqrt(fieldSide);
			return fieldSide + " " + fieldSide + " " + boxSide + " " + boxSide + " " + String.valueOf(values);
		} catch (Exception e) {
			e.printStackTrace(System.err);
			return null;
		}
	}

	public static Board parseAndReadSudoku(String fileName) {
		return SudokuReader.parseLine(parseSudoku(fileName));
	}

	private static void traverse(Element e, int indent) {
		if (e.getNodeName().equalsIgnoreCase("cell")) {
			NamedNodeMap m = e.getAttributes();
			values[Integer.parseInt(m.getNamedItem("Index").getNodeValue())] = m.getNamedItem("Value").getNodeValue()
					.charAt(0);
		} else if (e.hasChildNodes()) {
			if (e.getNodeName().equalsIgnoreCase("data")) {
				NamedNodeMap m = e.getAttributes();
				int size = Integer.parseInt(m.getNamedItem("Total").getNodeValue());
				values = new char[size];
			}
			NodeList list = e.getChildNodes();
			for (int i = 0; i < list.getLength(); ++i) {
				if (list.item(i).getNodeType() == Node.ELEMENT_NODE)
					traverse((Element) list.item(i), indent + 1);
			}
		}
	}
}
