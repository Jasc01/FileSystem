package main;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


//TODO Falta crear el método para modificar el archivo, en el cual también hay que modificar la variable _midificationDate
public class MyFile {
	String _name;
	String _extension;
	String _content;
	String _creationDate;
	String _modificationDate;
	int _size;
	
	ArrayList<Integer> _fileLines;
	
	public MyFile(String pName, String pExtension, String pContent, ArrayList<Integer> pFileLines) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		
		_name = pName;
		_content = pContent;
		_extension = pExtension;
		_size = pContent.length();
		_creationDate = dateFormat.format(date);
		_modificationDate = dateFormat.format(date);
		
		_fileLines = pFileLines;
	}
	public MyFile(String pName, String pExtension, String pContent, ArrayList<Integer> pFileLines, String pDate){
	    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
	    
	    _name = pName;
        _content = pContent;
        _extension = pExtension;
        _size = pContent.length();
        _creationDate = pDate;
        _modificationDate = dateFormat.format(date);
        
        _fileLines = pFileLines;
	}

	public String get_name() {
		return _name;
	}

	public String get_content() {
		return _content;
	}

	public int get_size() {
		return _size;
	}

	public String get_extension() {
		return _extension;
	}

	public String get_creationDate() {
		return _creationDate;
	}

	public String get_modificationDate() {
		return _modificationDate;
	}
	
	public void setModificationDate() {
	    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        _modificationDate = dateFormat.format(date);
	}

	public ArrayList<Integer> get_fileLines() {
		return _fileLines;
	}
}
