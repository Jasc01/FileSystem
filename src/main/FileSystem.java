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
import java.io.File;

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
	
	public boolean moveDirectory(String pDirectory, String pDestination, String pNewName){
	  DirectoryTree origin = searchDirectory(_currentDirectory);
	  DirectoryTree destination = searchDirectory(toAbsolute(_currentDirectory,pDestination.toLowerCase()));
	  if(destination == null || origin == null){
        return false;
      }
	  for(DirectoryTree d : origin.getDirectoryList()){
	    if(d.getName().toLowerCase().equals(pDirectory.toLowerCase())){
	      DirectoryTree dt;
	      if(pNewName.equals("")){
	        dt = new DirectoryTree(d.getName(), d.getDirectoryList(), d.getFileList());
          } else {
            dt = new DirectoryTree(pNewName, d.getDirectoryList(), d.getFileList());
          }
	      destination.addDirectory(dt, false);
	      origin.removeDirectory(d, false);
  	      return true;
	    }
	  }
	  return false;
	}
	
	public boolean moveFile(String pFile, String pDestination, String pNewName){
	  DirectoryTree origin = searchDirectory(_currentDirectory);
	  DirectoryTree destination = searchDirectory(toAbsolute(_currentDirectory,pDestination.toLowerCase()));
	  if(destination == null || origin == null){
	    return false;
	  }
	  for(MyFile f : origin.getFileList()){
	    if(pFile.toLowerCase().equals(f.get_name().toLowerCase() + "." + f.get_extension().toLowerCase())){
	      MyFile nf;
	      if(pNewName.equals("")){
	        nf = new MyFile(f.get_name(), f.get_extension(), f.get_content(), f.get_fileLines(), f.get_creationDate());
	      } else {
	        nf = new MyFile(pNewName, f.get_extension(), f.get_content(), f.get_fileLines(), f.get_creationDate());
	      }
	      destination.addFile(nf, false);
	      origin.removeFile(f);
	      return true;
	    }
	  }
	  return false;
	}

	public boolean deleteFiles(String pDirectory){
      String originalDir = _currentDirectory;
      _currentDirectory = pDirectory.toLowerCase();
      MyFile f;
      DirectoryTree directory = searchDirectory(pDirectory);
      if(directory == null){
        return false;
      }
      while(directory.getFileList().size() > 0){
        f = directory.getFileList().get(0);
        deleteFile(f.get_name().toLowerCase(), f.get_extension().toLowerCase());
      }
      _currentDirectory = originalDir;
      return true;
    }

    private void recursiveDelete(DirectoryTree pDirectory, String pathSoFar){
	  DirectoryTree dt;
	  for(int i = 0; i < pDirectory.getDirectoryList().size(); i++){
	    dt = pDirectory.getDirectoryList().get(i);
	    recursiveDelete(dt, pathSoFar + "/" + dt.getName().toLowerCase());
	  }
	  deleteFiles(pathSoFar.toLowerCase());
	}
	
	public boolean deleteDirectory(String pName){
	  DirectoryTree directory = searchDirectory(_currentDirectory);
	  if(directory == null){
        return false;
      }
	  for(int i = 0; i < directory.getDirectoryList().size(); i++){
	    DirectoryTree dt = directory.getDirectoryList().get(i);
	    if(dt.getName().toLowerCase().equals(pName.toLowerCase())){
	      recursiveDelete(dt, _currentDirectory + "/" + dt.getName().toLowerCase());
	      directory.removeDirectory(dt, true);
	      return true;
	    }
	  }
	  return false;
	}
	
	public boolean deleteFiles(ArrayList<String> pFiles){
	  boolean filesDeleted = false;
	  int counter = 0;
	  ArrayList<MyFile> files = new ArrayList<>();
	  DirectoryTree directory;
	  directory = searchDirectory(_currentDirectory);
	  Path FILE_PATH = Paths.get(_virtualDiskName);
      ArrayList<String> fileContent;
      try{
        fileContent = new ArrayList<>(Files.readAllLines(FILE_PATH, StandardCharsets.UTF_8));
        
        for(int i = 0; i < directory.getFileList().size(); i++){
          MyFile f = directory.getFileList().get(i);
          String name = f.get_name() + "." + f.get_extension();
          String nameL = f.get_name().toLowerCase() + "." + f.get_extension().toLowerCase();
          if(pFiles.contains(name) || pFiles.contains(nameL)){
            files.add(f);
            if(deleteFile(f.get_name().toLowerCase(), f.get_extension().toLowerCase())){
              counter++;
              i--;
            }
          }
        }
        if(counter == pFiles.size()){
          filesDeleted = true;
        }
        if(!filesDeleted){
          System.out.println("Error deleting files, reverting...");
          for(MyFile f : files){
            directory.addFile(f, true);
          }
          Files.write(FILE_PATH, fileContent, StandardCharsets.UTF_8);
        }
      }catch(Exception e){
        System.out.println("Error");
      }
	  return filesDeleted;
	}

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
	    if(directory == null){
	      return false;
	    }
	    for(int i = 0; i < directory.getFileList().size(); i++){
	      MyFile file = directory.getFileList().get(i);
	      if(file.get_name().toLowerCase().equals(pName.toLowerCase()) &&
	          file.get_extension().toLowerCase().equals(pExtension.toLowerCase())){
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
		MyFile newFile = new MyFile(pName, pExtension, pContent, pFileLines);
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
		if(pOverwrite){
		  deleteDirectory(newDirectory.getName().toLowerCase());
		}
		return searchDirectory(_currentDirectory).addDirectory(newDirectory, pOverwrite);
	}
	
	private String relativeToAbsolute (String pPathSoFar, String pCurrent) { //Convierte un path relativo en uno absoluto
      //El pPathSoFar siempre es absoluto. 
    
      String[] relativeArray = pCurrent.split("\\/");
      
      for (int i = 0; i < relativeArray.length; i++){
          
          if (relativeArray[i].equals("..")){
              String[] splitPath = pPathSoFar.split("\\/");
              pPathSoFar = ""; 
              
              if (!pPathSoFar.equals(_mainDirectory.getName())) {
                  for (int j = 0; j<splitPath.length-1; j++){ //-1 para que retroceda en el path 
                      if (pPathSoFar.equals("")){
                        pPathSoFar += splitPath[j];
                      } else{
                        pPathSoFar += "/" + splitPath[j];
                      }   
                  }
              }
          }
          else {
              if (!relativeArray[i].equals(".")){ 
                  pPathSoFar += "/" + relativeArray[i];
              }
          }
          
      }
      return pPathSoFar;
    }
	
	private String nes (String path) {
		String [] strip = path.split("\\/");
		path = "";
		for (int i = 0; i < strip.length; i++){
			if (path.equals("")) { path += strip[i];}
			else {path += "/" + strip[i];}
		}
		//System.out.println(path);
		return path;
	}
	
	private String toAbsolute (String pCurrentDirectory, String pNewPath) {
	    String[] dividedPath = pNewPath.split("\\/");
        if(dividedPath[0].toLowerCase().equals(_rootName.toLowerCase())) {
            return nes(relativeToAbsolute(pNewPath,""));
        } else {
            return nes(relativeToAbsolute(_currentDirectory,pNewPath));
        }
	}
	
	public boolean changeDirectory(String pNewPath) { 
	    String pathToSearch = toAbsolute(_currentDirectory, pNewPath);
		if(searchDirectory(pathToSearch) != null) {
			_currentDirectory = pathToSearch.toLowerCase();
			return true;
		}
		return false;
	}
	
	public void listCurrentDirectory() {
		printDirectories();
		printDirectoryFiles();
	}
	
	private void printDirectoryFiles() {
		DirectoryTree directoryToShow = searchDirectory(_currentDirectory);
		for(MyFile file : directoryToShow.getFileList()) {
			System.out.println(file.get_name() + "." + file.get_extension() + " - FILE");
		}
	}

	private void printDirectories() {
		DirectoryTree directoryToShow = searchDirectory(_currentDirectory);
		for(DirectoryTree directory : directoryToShow.getDirectoryList()) {
			System.out.println(directory.getName() + " - DIRECTORY");
		}
	}
	
	public boolean modFile(String pFileName, String pNewContent) {
		//TODO modificar un archivo que esté en el directorio actual
	    DirectoryTree directory = searchDirectory(_currentDirectory);
	    for(MyFile f : directory.getFileList()){
	      if(pFileName.equals(f.get_name().toLowerCase() + "." + f.get_extension().toLowerCase())){
	        MyFile nf = new MyFile(f.get_name(), f.get_extension(), f.get_content(), f.get_fileLines(), f.get_creationDate());
	        if(createFile(nf.get_name(), nf.get_extension(), pNewContent, true)){
	          directory.addFile(nf, true);
	          return true;
	        } else{
	          return false;
	        }
            
	      }
	    }/*
		String[] nameArray = getFixedFileName(pFileName);
		MyFile fileTemp = searchFile(nameArray[0] + "." +  nameArray[1]);
		if(fileTemp != null) {
			if(createFile(nameArray[0], nameArray[1], pNewContent, true)) {
				return true;
			} else {
				return false;
			}
		}*/
		return false;
	}
	
	public boolean showProperties(String pFileName) {
		String[] nameArray = getFixedFileName(pFileName);
		if(nameArray != null) {
			MyFile file = searchFile(nameArray[0] + "." + nameArray[1]);
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
			MyFile file = searchFile(nameArray[0] + "." + nameArray[1]);
			if(file != null) {
				System.out.println("FILE CONTENT: \n" + file.get_content());
				return true;
			}
		}
		return false;
	}
	
	private MyFile searchFile(String pFileName) {
		DirectoryTree currentDirectory = searchDirectory(_currentDirectory);
		for(MyFile file : currentDirectory.getFileList()) {
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
	
//Copy
	private boolean fileOrFolder (String path){
		return path.contains("."); 
	}
//Real to virtual
	public boolean copyRealToVirtual (String pRealPath, String pVirtualDestination) { //Copies a real file into the virtual file in the current directory
		if (fileOrFolder(pRealPath)) {
			return copyrvfile(pRealPath, pVirtualDestination);
		}
		else {
			System.out.println("Copying directory. How dare you.");
			String previous = _currentDirectory;
			boolean ret = false;
			if (changeDirectory(pVirtualDestination)) {
				Path filep = Paths.get(pRealPath);
				java.io.File curDir  = filep.toFile();
				createDirectory(curDir.getName(), false);
				changeDirectory(curDir.getName());;
				ret = copyallfilesrv(pRealPath);
				changeDirectory(previous);
			}
			return ret;
		}
	}
	
	private boolean copyallfilesrv(String pRealPath){
		Path filep = Paths.get(pRealPath);
		
		java.io.File curDir  = filep.toFile();
		java.io.File[] filesList = curDir.listFiles();
	    for(java.io.File f : filesList){
	    	if(f.isDirectory()){
	    		String previous = _currentDirectory;
	    		createDirectory(f.getName(), false);
	    		changeDirectory(f.getName());
	    		copyallfilesrv(f.getAbsolutePath());
	    		changeDirectory(previous);
	    	}
	    		
	        if(f.isFile()){
	        	copyrvfile(f.getAbsolutePath(),_currentDirectory);
	        }
	    }
	    return true;
	}
	
	private boolean copyrvfile (String pRealPath, String pVirtualDestination) {
		String originalPlace = _currentDirectory;
	    String finalDestination = toAbsolute(_currentDirectory, pVirtualDestination);
	  
	    Path filep = Paths.get(pRealPath);
		try {
			java.io.File file  = filep.toFile();
			String filename = file.getName();
			
			String[] splitfilename = filename.split("\\.");
			String allcontents ="";
			String[] finalfilename = {"", ""};
			for (int i = 0; i < splitfilename.length-1; i++) { finalfilename[0] += splitfilename[i]; }
			finalfilename[1] = splitfilename[splitfilename.length-1];
			
			List<String> contents = Files.readAllLines(filep);
			for (int i = 0; i < contents.size(); i++) { allcontents += contents.get(i); }
			
			//System.out.println(finalfilename[0]+ " " + finalfilename[1] +" " +  allcontents);
			if (changeDirectory(finalDestination)) {
			    if (createFile(finalfilename[0], finalfilename[1], allcontents, false)){
			        changeDirectory(originalPlace);
			        return true;
			    }
			}
			return false;
			
		} catch (IOException e) {
			//e.printStackTrace(); //Red flash error
			System.out.println("Invalid real path");
			return false;
		}
	}

	
	
	
	
	//Virtual to real
	public boolean copyVirtualToReal (String pVirtualPath, String pRealPath) { //Copies a real file into the virtual file in the current directory
		if (fileOrFolder(pVirtualPath)) {
			return copyvrfile(pVirtualPath, pRealPath);
		}
		else {
			System.out.println("Copying directory. How dare you.");
			String previous = _currentDirectory;
			boolean ret = false;
			if (changeDirectory(pVirtualPath)) { //Me ubico en el lugar que empiezo a copiar
				DirectoryTree cur = searchDirectory(_currentDirectory);
				
				String folderName = pRealPath+"\\"+cur.getName();
				File folder = new File( folderName );
				folder.mkdirs();
				 
				ret = copyallfilesvr(folderName);
				
				//No need to close it again.
				
				changeDirectory(previous);
				
			}
			return ret;
		}
	}
	private boolean copyallfilesvr(String pRealPath) {
		//Ya estoy en el lugar que quiero copiar
		
		DirectoryTree cur = searchDirectory(_currentDirectory);
		for (int i = 0; i < cur.getDirectoryList().size(); i++) {
			String folderName = pRealPath+"\\"+cur.getDirectoryList().get(i).getName();
			File folder = new File( folderName );
			folder.mkdirs();
			
			String previous = _currentDirectory;
			changeDirectory(_currentDirectory+"/"+cur.getDirectoryList().get(i).getName());
			copyallfilesvr(folderName);
			changeDirectory(previous);
			
		}
		for (int i = 0; i < cur.getFileList().size(); i++) {
			try {
				if (cur.getFileList().get(i).get_name().equals(_mainDirectory.getName())) {
					System.out.println("Can't copy main directory! ");
				}
				else {
					Files.createFile(Paths.get(pRealPath+"\\"+cur.getFileList().get(i).get_name()));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				return false;
			}
		}
	    return true;
	}
	
	private boolean copyvrfile(String pVirtualPath, String pRealPath){
		String originPath = toAbsolute(_currentDirectory,pVirtualPath.toLowerCase());
	    
	      String[] splitOPath = originPath.split("\\/");
	      String dirPath = ""; 
	      String filename = splitOPath[splitOPath.length-1];
	      for (int i = 0; i < splitOPath.length-1; i++ ) {
	        dirPath += splitOPath[i]  + "/";
	      }
	    
	      return copyvrAux (dirPath,filename,pRealPath);
	}
	
	private boolean copyvrAux (String pDirectoryPath, String pFileToCopy, String pRealPath){

      DirectoryTree originFolder = searchDirectory(pDirectoryPath);
      
      if (originFolder == null) {return false;}
      
      for(MyFile f : originFolder.getFileList()){
          if(pFileToCopy.toLowerCase().equals(f.get_name().toLowerCase() + "." + f.get_extension().toLowerCase())){
              try{
                  PrintWriter writer = new PrintWriter(pRealPath+"/"+f.get_name()+"."+f.get_extension(), "UTF-8");
                  writer.println(f.get_content());
                  writer.close();
                  return true;
              } catch (Exception e) {
                  return false;
              }
          } 
      }
      return false;
	}
//Virtual to virtual
	public boolean copyVirtualToVirtual (String pVirtualOrigin, String pVirtualDestination) { //Copies a real file into the virtual file in the current directory
		if (fileOrFolder(pVirtualOrigin)) {
			return copyvvfile(pVirtualOrigin, pVirtualDestination);
		}
		else {
			return copyvvDir(pVirtualOrigin, pVirtualDestination);
		}
	}
	
	private boolean copyvvDir(String pVirtualOrigin, String pVirtualDestination) {
		String originPath = toAbsolute(_currentDirectory,pVirtualOrigin.toLowerCase());
        String destinyPath = toAbsolute(_currentDirectory,pVirtualDestination.toLowerCase());
        
        String[] splitOPathOrigin = originPath.split("\\/");
        String[] splitOPathDestiny = destinyPath.split("\\/");
        String dirPathOrigin = "";
        String dirPathDestiny = "";
		for (int i = 0; i < splitOPathOrigin.length; i++ ) { dirPathOrigin += splitOPathOrigin[i] + "/"; }
		for (int i = 0; i < splitOPathDestiny.length; i++ ) { dirPathDestiny += splitOPathDestiny[i] + "/"; }
		DirectoryTree directoryOrigin = searchDirectory(dirPathOrigin);
		DirectoryTree directoryDestiny = searchDirectory(dirPathDestiny);
		if(directoryOrigin != null && directoryDestiny != null) {
			//Copiar Directorios
			String originalDir = _currentDirectory;
			changeDirectory(dirPathDestiny);
			createDirectory(directoryOrigin.getName(), false);
			changeDirectory(originalDir);
			for(int i = 0; i < directoryOrigin.getDirectoryList().size(); i++) {
				if(copyvvDir(dirPathOrigin + directoryOrigin.getDirectoryList().get(i).getName(), dirPathDestiny + directoryOrigin.getName())) { }
				else {
					System.out.println("Error while copying directory");
				}
			}
			for(int i = 0; i < directoryOrigin.getFileList().size(); i++) {
				if(copyvvfile(dirPathOrigin + directoryOrigin.getFileList().get(i).get_name() + "." + directoryOrigin.getFileList().get(i).get_extension(), dirPathDestiny + directoryOrigin.getName())) { }
				else {
					System.out.println("Error while copying file");
				}
			}
			return true;
		}
		return false;
	}
	
	private boolean copyvvfile(String pVirtualOrigin, String pVirtualDestination){
		String originPath = toAbsolute(_currentDirectory,pVirtualOrigin.toLowerCase());
        String destinyPath = toAbsolute(_currentDirectory,pVirtualDestination.toLowerCase());
      
        String[] splitOPath = originPath.split("\\/");
		String dirPath = ""; 
		String filename = splitOPath[splitOPath.length-1];
		for (int i = 0; i < splitOPath.length-1; i++ ) {
		  dirPath += splitOPath[i] + "/";
		}
		
		//System.out.println(dirPath);
		//System.out.println(filename);
		
	    return copyvvAux (dirPath,filename,destinyPath);
	}
	
	private boolean copyvvAux (String pDirectoryPath, String pFileToCopy, String pDestinationPath) {

		  
	      DirectoryTree originFolder = searchDirectory(pDirectoryPath);
	      
	      if (originFolder == null) {return false;}
	      
	      for(MyFile f : originFolder.getFileList()){
	          if(pFileToCopy.toLowerCase().equals(f.get_name().toLowerCase() + "." + f.get_extension().toLowerCase())){
	              if (changeDirectory(pDestinationPath)) {
	                  createFile(f.get_name(), f.get_extension(), f.get_content(), false);
	                  changeDirectory(pDirectoryPath);
	                  return true;
	              }
	          } 
	      }
	      return false;
		}
	
//Tree
	public void treeFileSystem () {
		_mainDirectory.treeFileSystem(0);
	}
	
	public void findFileOrDirectory(String pName) {
		String[] nameFixed = getFixedFileName(pName);
		if(nameFixed != null) {
			if(nameFixed[0].equals("*")) {
				_mainDirectory.findFileOrDirectory(nameFixed[1], _mainDirectory.getName(), false, true);
			} else {
				_mainDirectory.findFileOrDirectory(nameFixed[0] + "." + nameFixed[1], _mainDirectory.getName(), false, false);
			}
		} else {
			_mainDirectory.findFileOrDirectory(pName, _mainDirectory.getName(), true, false);
		}
	}
}
