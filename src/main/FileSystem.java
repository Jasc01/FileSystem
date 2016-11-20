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
	String _rootName;
	
	int _secAmount;
	int _secSize;
	DirectoryTree _mainDirectory;
	String _currentDirectory;
	
	public FileSystem() {
		_rootName = "root:";
		_mainDirectory = new DirectoryTree(_rootName);
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
				    if (fileContent.get(i).equals(stringFilled) && addFileToDirectory(pName, pExtension, pContent)) {
				        fileContent.set(i, pContent);
				        fileCreated = true;
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
	
	private boolean addFileToDirectory(String pName, String pExtension, String pContent) {
		File newFile = new File(pName, pExtension, pContent);
		return searchDirectory(_currentDirectory).addFile(newFile);
	}
	
	public DirectoryTree searchDirectory(String pDirectoryToSearch) {
		String[] directoriesArray = pDirectoryToSearch.split("/");
		DirectoryTree directoryToReturn = null;
		DirectoryTree tempDirectory = _mainDirectory;
		boolean directoryNotFound = false;
		if(directoriesArray.length == 1) {
			directoryToReturn = _mainDirectory;	
		}
		for(int indexString = 1; indexString < directoriesArray.length; indexString++) { //Empieza en 1 porque si la ruta es ROOT:, entonces para que no entre al for
			directoryNotFound = true;
			for(int i = 0; i < tempDirectory.getDirectoryList().size(); i++) {
				if(tempDirectory.getDirectoryList().get(i).getName().toLowerCase().equals(directoriesArray[indexString].toLowerCase())) {
					if(indexString == directoriesArray.length - 1) {
						directoryToReturn = tempDirectory.getDirectoryList().get(i);
					}
					tempDirectory = tempDirectory.getDirectoryList().get(i);
					directoryNotFound = false;
				}
			}
			if(directoryNotFound) {
				break;
			}
		}
		return directoryToReturn;
	}
	
	public String getCurrentDirectory() {
		return _currentDirectory;
	}
	
	public boolean createDirectory(String pName) {
		DirectoryTree newDirectory = new DirectoryTree(pName);
		return searchDirectory(_currentDirectory).addDirectory(newDirectory);
	}
	
	public boolean changeDirectory(String pNewPath) { //TODO Validar cuando escribe nombre///////nombre2
		String[] dividedPath = pNewPath.split("/");
		if(dividedPath[0].toLowerCase().equals(_rootName.toLowerCase())) {
			//Buscar path absoluto
			if(searchDirectory(pNewPath) != null) {
				_currentDirectory = pNewPath.toLowerCase();
				return true;
			}
		} else {
			String pathToSearch = _currentDirectory + "/" + pNewPath;
			//Buscar path relativo
			if(searchDirectory(pathToSearch) != null) {
				_currentDirectory = pathToSearch.toLowerCase();
				return true;
			}
		}
		return false;
	}
	
	public void listCurrentDirectory() {
		printDirectories();
		printDirectoryFiles();
	}
	
	public void printDirectoryFiles() {
		DirectoryTree directoryToShow = searchDirectory(_currentDirectory);
		for(File file : directoryToShow.getFileList()) {
			System.out.println(file.get_name() + " - FILE");
		}
	}

	public void printDirectories() {
		DirectoryTree directoryToShow = searchDirectory(_currentDirectory);
		for(DirectoryTree directory : directoryToShow.getDirectoryList()) {
			System.out.println(directory.getName() + " - DIRECTORY");
		}
	}
	
}
