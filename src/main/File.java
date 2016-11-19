package main;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


//TODO Falta crear el método para modificar el archivo, en el cual también hay que modificar la variable _midificationDate
public class File {
	String _name;
	String _extension;
	String _content;
	String _creationDate;
	String _modificationDate;
	int _size;
	
	public File(String pName, String pExtension, String pContent) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		
		_name = pName;
		_content = pContent;
		_extension = pExtension;
		_size = pContent.length();
		_creationDate = dateFormat.format(date);
		_modificationDate = dateFormat.format(date);
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
	
}
