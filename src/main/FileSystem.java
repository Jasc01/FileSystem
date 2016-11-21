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
				    	ArrayList<Integer> lines = new ArrayList<>();
				    	lines.add(i);
				    	if(addFileToDirectory(pName, pExtension, pContent, lines)) {
				    		fileContent.set(i, pContent);
					        fileCreated = true;
					        break;	
				    	}
				    }
				}
			} else { // Enlaza
				ArrayList<String> stringDivided = divideString(pContent, _secSize);
				ArrayList<Integer> memoryLeft = getMemoryLeftInFile(fileContent, stringFilled);
				if(stringDivided.size() <= memoryLeft.size()) {
					memoryLeft.subList(stringDivided.size(), memoryLeft.size()).clear();
			    	if(addFileToDirectory(pName, pExtension, pContent, memoryLeft)) {
			    		for(int i = 0 ; i < memoryLeft.size(); i++) {
				    		fileContent.set(memoryLeft.get(i), stringDivided.get(i));	
			    		}
				        fileCreated = true;
			    	}
				}
			}
			Files.write(FILE_PATH, fileContent, StandardCharsets.UTF_8);
		} catch (IOException e) {
			System.out.println("Error creating the file");
		}
		return fileCreated;
	}
	
	private ArrayList<Integer> getMemoryLeftInFile(ArrayList<String> pList, String pBlank) {
		ArrayList<Integer> memoryLeft = new ArrayList<>();
		for (int i = 0; i < pList.size(); i++) {
			if (pList.get(i).equals(pBlank)) {
				memoryLeft.add(i);
			}
		}
		return memoryLeft;
	}
	
	private ArrayList<String> divideString(String pString, int pSecSize) {
		ArrayList<String> toReturn = new ArrayList<>();
		while(pString.length() >= pSecSize) {
			toReturn.add(pString.substring(0, pSecSize));
			pString = pString.substring(pSecSize, pString.length());
		}
		if(!pString.equals(""))
			toReturn.add(pString);
		return toReturn;
	}
	
	private boolean addFileToDirectory(String pName, String pExtension, String pContent, ArrayList<Integer> pFileLines) {
		File newFile = new File(pName, pExtension, pContent, pFileLines);
		return searchDirectory(_currentDirectory).addFile(newFile, false);
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
		return searchDirectory(_currentDirectory).addDirectory(newDirectory, false);
	}
	
	public boolean changeDirectory(String pNewPath) { //NOTA: cuando escribe nombre///////nombre2 simplemente no lo permite
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
	
	private void printDirectoryFiles() {
		DirectoryTree directoryToShow = searchDirectory(_currentDirectory);
		for(File file : directoryToShow.getFileList()) {
			System.out.println(file.get_name() + "." + file.get_extension() + " - FILE");
		}
	}

	private void printDirectories() {
		DirectoryTree directoryToShow = searchDirectory(_currentDirectory);
		for(DirectoryTree directory : directoryToShow.getDirectoryList()) {
			System.out.println(directory.getName() + " - DIRECTORY");
		}
	}
	
	public void modFile(String pFileName, String pNewContent) {
		//TODO modificar un archivo que esté en el directorio actual
		// Buscar el File correspondiente
		// Buscar la línea (o líneas) en la que tiene la data
		// Borrar esas líneas
		// Volver a escribir en el archivo con first fit
		// Modificar (setFile) el File con lo nuevo
	}
	
	public boolean showProperties(String pFileName) {
		String[] nameArray = getFixedFileName(pFileName);
		if(nameArray != null) {
			File file = searchFile(nameArray[0] + "." + nameArray[1]);
			if(file != null) {
				System.out.println("FILE NAME: " + file.get_name());
				System.out.println("EXTENSION: " + file.get_extension());
				System.out.println("CREATION DATE: " + file.get_creationDate());
				System.out.println("MODIFICATION DATE: " + file.get_modificationDate());
				System.out.println("SIZE: " + file.get_size());
				return true;
			}
		}
		return false;
	}
	
	public boolean showFile(String pFileName) {
		String[] nameArray = getFixedFileName(pFileName);
		if(nameArray != null) {
			File file = searchFile(nameArray[0] + "." + nameArray[1]);
			if(file != null) {
				System.out.println("FILE CONTENT: \n" + file.get_content());
				return true;
			}
		}
		return false;
	}
	
	private File searchFile(String pFileName) {
		DirectoryTree currentDirectory = searchDirectory(_currentDirectory);
		for(File file : currentDirectory.getFileList()) {
			if((file.get_name().toLowerCase() + "." + file.get_extension().toLowerCase()).equals(pFileName.toLowerCase())) {
				return file;
			}
		}
		return null;
	}
	
	private String[] getFixedFileName(String pFileName) {
		String[] toReturn  = new String[2];
		String[] tempArray = pFileName.split("");
		int dotPos = -1;
		for (int i = 0; i < tempArray.length; i++) {
			if(tempArray[i].equals(".")) {
				dotPos = i;
			}
		}
		if(dotPos != -1) {
			String nameTemp = pFileName.substring(0, dotPos);
			String extentionTemp = pFileName.substring(dotPos + 1, pFileName.length());
			toReturn[0] = nameTemp;
			toReturn[1] = extentionTemp;
			return toReturn;
		}
		return null;
	}
	
}
