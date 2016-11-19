package main;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class FileSystem {
	
	String _virtualDiskName;
	
	int _secAmount;
	int _secSize;
	DirectoryTree _mainDirectory;
	String _currentDirectory;
	
	public FileSystem() {
		_mainDirectory = new DirectoryTree("ROOT");
		_currentDirectory = _mainDirectory.getName();
		_virtualDiskName = "Virtual Disc.txt";
	}
	
	public void create(int pSecAmount, int pSecSize) {
		try{
			_secSize = pSecSize;
			_secAmount = pSecAmount;
		    PrintWriter writer = new PrintWriter(_virtualDiskName, "UTF-8");
		    char[] bytes = new char[pSecSize];
		    Arrays.fill(bytes, ' ');
		    String stringFilled = new String(bytes);
		    for (int i = 0; i < pSecAmount; i++) {
		    	writer.println(stringFilled);	
			}
		    writer.close();
		} catch (Exception e) {
		   System.out.println("Error creating the file");
		}
	}
	
	public boolean createFile(String pName, String pExtension, String pContent) {
		//TODO Revisar Repetidos
		//TODO Revisar símbolos no permitidos
		//TODO Revisar tamaño
		//TODO Enlazar
		boolean fileCreated = false;
		
	    char[] bytes = new char[_secSize];
	    Arrays.fill(bytes, ' ');
	    String stringFilled = new String(bytes);
	    
		Path FILE_PATH = Paths.get(_virtualDiskName);
		ArrayList<String> fileContent;
		
		try {
			fileContent = new ArrayList<>(Files.readAllLines(FILE_PATH, StandardCharsets.UTF_8));
			if(pContent.length() <= _secSize) {
				for (int i = 0; i < fileContent.size(); i++) {
				    if (fileContent.get(i).equals(stringFilled)) {
				        fileContent.set(i, pContent);
				        fileCreated = true;
				        //TODO Si el file entró bien el disco, crearlo en el árbol (Hacer una función aparte para eso)
				        break;
				    }
				}
			} else {
				//TODO Enlazar archivo a otro segmento cuando no cabe
			}
			Files.write(FILE_PATH, fileContent, StandardCharsets.UTF_8);
		} catch (IOException e) {
			System.out.println("Error creating the file");
		}
		return fileCreated;
	}
	
	public String getCurrentDirectory() {
		return _currentDirectory;
	}
	
	//TODO Hacer el método que cambia el directorio y el que crea el directorio
}
