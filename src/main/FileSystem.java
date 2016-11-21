package main;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	
	
	//Falta probar
	public boolean deleteDirectory(String pName){
	  boolean directoryDeleted = false;
	  DirectoryTree directory = searchDirectory(_currentDirectory);
	  for(int i = 0; i < directory.getDirectoryList().size(); i++){
	    DirectoryTree dt = directory.getDirectoryList().get(i);
	    if(dt.getName().equals(pName)){
	      directory.removeDirectory(dt);
	      directoryDeleted = true;
	      break;
	    }
	  }
	  return directoryDeleted;
	}
	
	//Falta probar
	public boolean deleteFiles(ArrayList<String> pFiles){
	  boolean filesDeleted = false;
	  ArrayList<File> files = new ArrayList<>();
	  DirectoryTree directory = searchDirectory(_currentDirectory);
	  
	  Path FILE_PATH = Paths.get(_virtualDiskName);
      ArrayList<String> fileContent;
      
      try{
        fileContent = new ArrayList<>(Files.readAllLines(FILE_PATH, StandardCharsets.UTF_8));
        
        for(int i = 0; i < directory.getFileList().size(); i++){
          File f = directory.getFileList().get(i);
          String name = f.get_name() + "." + f.get_extension();
          if(pFiles.contains(name)){
            files.add(f);
            deleteFile(f.get_name(), f.get_extension());
          }
        }
        if(!filesDeleted){
          System.out.println("Error deleting files, reverting...");
          for(File f : files){
            directory.addFile(f, true);
          }
          Files.write(FILE_PATH, fileContent, StandardCharsets.UTF_8);
        }
      }catch(Exception e){
        System.out.println("Error");
      }
	  return filesDeleted;
	}
	
	//Probado
	public boolean deleteFile(String pName, String pExtension){
	  boolean fileDeleted = false;
	  
	  char[] bytes = new char[_secSize];
	  Arrays.fill(bytes, ' ');
	  String emptyString = new String(bytes);
	  
	  Path FILE_PATH = Paths.get(_virtualDiskName);
	  ArrayList<String> fileContent;
	  try{
	    fileContent = new ArrayList<>(Files.readAllLines(FILE_PATH, StandardCharsets.UTF_8));
	    DirectoryTree directory = searchDirectory(_currentDirectory);
	    for(int i = 0; i < directory.getFileList().size(); i++){
	      File file = directory.getFileList().get(i);
	      if(file.get_name().equals(pName) && file.get_extension().equals(pExtension)){
	        ArrayList<Integer> pos = file.get_fileLines();
	        for(Integer j : pos){
	          fileContent.set(j, emptyString);
	          directory.removeFile(file);
	        }
	        fileDeleted = true;
	        break;
	      }
	    }
	    Files.write(FILE_PATH, fileContent, StandardCharsets.UTF_8);
	  } catch (Exception e){
	    System.out.println("Error deleting file");
	  }
	  return fileDeleted;
	}
	
	public boolean createFile(String pName, String pExtension, String pContent, boolean pOverwrite) {
		boolean fileCreated = false;
		
	    char[] bytes = new char[_secSize];
	    Arrays.fill(bytes, ' ');
	    String stringFilled = new String(bytes);
	    
		Path FILE_PATH = Paths.get(_virtualDiskName);
		ArrayList<String> fileContent;
		
		if(pOverwrite){
	      deleteFile(pName, pExtension);
	    }
		
		try {
			fileContent = new ArrayList<>(Files.readAllLines(FILE_PATH, StandardCharsets.UTF_8));
			if(pContent.length() <= _secSize) {
				for (int i = 0; i < fileContent.size(); i++) {
				    if (fileContent.get(i).equals(stringFilled)) {
				    	ArrayList<Integer> lines = new ArrayList<>();
				    	lines.add(i);
				    	if(addFileToDirectory(pName, pExtension, pContent, lines, pOverwrite)) {
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
			    	if(addFileToDirectory(pName, pExtension, pContent, memoryLeft, pOverwrite)) {
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
	
	private boolean addFileToDirectory(String pName, String pExtension, String pContent, ArrayList<Integer> pFileLines, boolean pOverwrite) {
		File newFile = new File(pName, pExtension, pContent, pFileLines);
		return searchDirectory(_currentDirectory).addFile(newFile, pOverwrite);
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
	
	public boolean createDirectory(String pName, boolean pOverwrite) {
		DirectoryTree newDirectory = new DirectoryTree(pName);
		return searchDirectory(_currentDirectory).addDirectory(newDirectory, pOverwrite);
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

	public boolean copyRealToVirtual (String pRealPath) { //Copies a real file into the virtual file in the current directory
		Path filep = Paths.get(pRealPath);
		try {
			java.io.File file  = filep.toFile();
			String filename = file.getName();
			
			String[] splitfilename = filename.split("\\.");
			String allcontents ="";
			
			List<String> contents = Files.readAllLines(filep);
			for (int i = 0; i < contents.size(); i++) { allcontents += contents.get(i); }
			
			return createFile(splitfilename[0], splitfilename[1], allcontents, false);
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
