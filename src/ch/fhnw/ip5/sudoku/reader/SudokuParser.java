package ch.fhnw.ip5.sudoku.reader;

import javax.xml.parsers.SAXParser;

import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

public class SudokuParser {

	SAXParser parser = new SAXParser() {
		
		@Override
		public void setProperty(String name, Object value) throws SAXNotRecognizedException, SAXNotSupportedException {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public boolean isValidating() {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public boolean isNamespaceAware() {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public XMLReader getXMLReader() throws SAXException {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public Parser getParser() throws SAXException {
			// TODO Auto-generated method stub
			return null;
		}
	};
}
