package main;

import java.util.ArrayList;
import java.util.Scanner;

public class MainGUI {
	FileSystem _fileSystem;
	Scanner _input;
	
	public MainGUI() {
		_fileSystem = new FileSystem();
		_input = new Scanner(System.in);
	}
	
	public void startConsole() {
		boolean exit = false;
		String userInput;
		System.out.println("File System - Write 'create /i /i' to start the file system");
		while(!exit) {
			userInput = _input.nextLine();
			String[] inputArray = userInput.split("\\s+");
			if(inputArray[0].toLowerCase().equals("create") && inputArray.length == 3) {
				try {
					int secAmount = Integer.parseInt(inputArray[1]);
					int secSize = Integer.parseInt(inputArray[2]);
					_fileSystem.create(secAmount, secSize);
					exit = true;	
				} catch(NumberFormatException e) {
					System.err.println("Ivalid parameter");
				}
			} else {
				System.err.println("Command not found");
			}
		}
	}
	
	public String[] separeCommand (String pInput){
		String[] inputArray = pInput.split("\\|+");
		for (int i = 0; i < inputArray.length; i++){
			//System.out.println(inputArray[i]);
		}
		return inputArray;
	}
		
	public void help (String[] pInputArray) {
	  if(pInputArray.length == 1){
	    System.out.println("\n\nHELP:");
        System.out.println("file /name /extension /content  \t | Creates new file in the current directory");
        System.out.println("makedir /name  \t\t\t\t | Creates new directory inside the current one");
        System.out.println("cdir /path \t\t\t\t | Changes the current directory");
        System.out.println("listdir \t\t\t\t | Lists the contents of the directory");
        System.out.println("modfile /path \t\t\t\t | Modifies the contents of the file given the path");
        System.out.println("showpro /path \t\t\t\t | Displays the properties of the file in the given path");
        System.out.println("show /path \t\t\t\t | Displays the file in the given path");
        System.out.println("copyrv /rpath /vpath \t\t\t | Copies a real file into the virtual path given");
        System.out.println("copyvr /vpath /rpath \t\t\t | Copies a virtual file into the real path given");
        System.out.println("copyvv /vpath /vpath \t\t\t | Copies a virtual file into the virtual path given");
        System.out.println("move /(filename|directory) (/newName)? \t | Moves file to destination");
        System.out.println("remove /(filename+|directory) \t\t | Removes the specified file");
        System.out.println("find /(filename|directory) \t\t | Searches for a file with said name");
        System.out.println("tree \t\t\t\t\t | Displays the directory tree");
        System.out.println("exit \t\t\t\t\t | Ends the program\n\n");
	  } else {
	    System.err.println("Invalid parameters");
	  }
	}
	public void file (String[] pInputArray) {
		if(pInputArray.length == 4) {
			if(_fileSystem.createFile(pInputArray[1], pInputArray[2], pInputArray[3], false)) { //file nombre extensión contenido
				System.out.println("File created");
			} else { 
			  System.err.println("File already exists, do you want to overwrite it? (y/n)");
              String newInput = _input.nextLine();
              if(newInput.toLowerCase().equals("y")){
                _fileSystem.createFile(pInputArray[1],pInputArray[2], pInputArray[3], true);
                System.out.println("File created");
              } else {
                System.err.println("Error creating file");
              }
			}
		} else {
		    System.err.println("Invalid parameter(s)");
		}
	}
	public void makedir (String[] pInputArray){
		if(pInputArray.length == 2) {
			if(_fileSystem.createDirectory(pInputArray[1], false)) { //makedir nombre
				System.out.println("Directory created");
			} else {
				System.err.println("Directory already exists, do you want to overwrite it? (y/n)");
				String newInput = _input.nextLine();
				if(newInput.toLowerCase().equals("y")){
				  _fileSystem.createDirectory(pInputArray[1], true);
				  System.out.println("Directory created");
				} else {
				  System.err.println("Error creating directory");
				}
			}
		} else {
		    System.err.println("Invalid parameter(s)");
		}
	}
	public void cdir (String[] pInputArray){
		if(pInputArray.length == 2) {
			if(_fileSystem.changeDirectory(pInputArray[1])) { //cdir path (absoluto o relativo)
				System.out.println("Directory changed");
			} else {
				System.err.println("Error changing directory - Probably doesn't exist");
			}
		} else {
		    System.err.println("Invalid parameter(s)");
		}
	
	}
	public void listdir (String[] pInputArray) {
		if(pInputArray.length == 1) {
			_fileSystem.listCurrentDirectory(); //listdir
		} else {
		    System.err.println("Invalid parameter(s)");
		}
	}
	public void modFile(String[] pInputArray) {
		if(pInputArray.length == 2) {
			if(_fileSystem.showFile(pInputArray[1])) {
				System.out.println("Showing the current file content above\nWrite new content:");
				String newContent = _input.nextLine();
				if(_fileSystem.modFile(pInputArray[1], newContent)) {
					System.out.println("File changed");
				} else {
					System.err.println("Error modifying file");
				}
			} else {
				System.err.println("Error modifying file - Probably doesn't exist");
			}
		} else {
		    System.err.println("Invalid parameter(s)");
		}
	}
	public void showpro (String[] pInputArray) {
		if(pInputArray.length == 2) {
			if(_fileSystem.showProperties(pInputArray[1])) { //cdir path (absoluto o relativo)
			} else {
				System.err.println("Error showing file properties - Probably doesn't exist");
			}
		} else {
		    System.err.println("Invalid parameter(s)");
		}
	}
	public void show (String[] pInputArray) {
		if(pInputArray.length == 2) {
			if(_fileSystem.showFile(pInputArray[1])) { //show filename 
			} else {
				System.err.println("Error showing file - Probably doesn't exist");
			}
		} else {
		    System.err.println("Invalid parameter(s)");
		}
		
	}
	public void copyrv (String[] pInputArray){
		if(pInputArray.length == 3) {
			if(_fileSystem.copyRealToVirtual(pInputArray[1], pInputArray[2])) { //copyrv realpath virtualpath 
			    System.out.println("File/directory copied");
			} else {
				System.err.println("Error copying file - invalid path or internal error");
			}
		} else {
		    System.err.println("Invalid parameter(s)");
		}
	}
	public void copyvr (String[] pInputArray){
		if(pInputArray.length == 3) { 
			if(_fileSystem.copyVirtualToReal(pInputArray[1], pInputArray[2])) { //copyvr virtualpath realpath 
			  System.out.println("File/directory copied");
			} else {
				System.err.println("Error copying file - invalid path or internal error");
			}
		} else {
		    System.err.println("Invalid parameter(s)");
		}
	}
	public void copyvv (String[] pInputArray){
		if(pInputArray.length == 3) { 
			if(_fileSystem.copyVirtualToVirtual(pInputArray[1], pInputArray[2])) { //copyrv realpath virtualpath 
			    System.out.println("File/directory copied");
			} else {
				System.err.println("Error copying file - invalid path or internal error");
			}
		} else {
		  System.err.println("Invalid parameter(s)");
		}
	}
	public void move(String[] pInputArray){
	  if (pInputArray.length >= 3) {
        String newName = "";
        if(pInputArray.length == 4){
          newName = pInputArray[3];
        }
	    String[] name = pInputArray[1].split("\\.");
	    if (name.length == 1) {//Move directory
	      if (_fileSystem.moveDirectory(pInputArray[1], pInputArray[2], newName)) {
	        System.out.println("Directory moved");
	      } else {
	        System.err.println("Error moving directory");
	      }
	    } else {//Move file
	      //System.out.println(newName);
	      if (_fileSystem.moveFile(pInputArray[1], pInputArray[2], newName)) {
	        System.out.println("File moved");
	      } else {
	        System.err.println("Error moving file");
	      }
	    }
	  } else {
	    System.err.println("Invalid parameter(s)");
	  }
	}
	public void remove(String[] pInputArray){
	  if(pInputArray.length >= 2){
	    String[] name = pInputArray[1].split("\\.");
	      if(name.length == 1){//Remove directory
	        if (_fileSystem.deleteDirectory(pInputArray[1])) {
	          System.out.println("Directory removed");
	        } else {
	          System.err.println("Error removing directory");
	        }
	      } else {//Remove file(s)
	        ArrayList<String> files = new ArrayList<>();
	        for(int i = 1; i < pInputArray.length; i++){
	          files.add(pInputArray[i]);
	        }
	        if (_fileSystem.deleteFiles(files)) {
	          System.out.println("File(s) removed");
	        } else {
	          System.err.println("Error removing file(s)");
	        }
	      }
	  } else {
	    System.err.println("Invalid parameter(s)");
	  }
	}
	public void find(String[] pInputArray) {
		if (pInputArray.length == 2) {
			_fileSystem.findFileOrDirectory(pInputArray[1]);;
		} else {
			System.out.println("Unknown command");
		}
	}
	public void tree (String[] pInputArray) {
		if (pInputArray.length == 1) {
			_fileSystem.treeFileSystem();
		} else {
		    System.err.println("Invalid parameter(s)");
		}
	}
	
	public void readCommand() {
		boolean exit = false;
		String userInput;
		System.out.println("Welcome to the File System");
		
		while(!exit) {
			System.out.println("Current Directory: " + _fileSystem.getCurrentDirectory());
			userInput = _input.nextLine();
			String[] inputArray = separeCommand(userInput);
			switch(inputArray[0].toLowerCase()) {
				
				case "help": help(inputArray); break;
				case "file": file(inputArray); break;
				case "makedir": makedir(inputArray); break;
				case "cdir": cdir(inputArray); break;
				case "listdir": listdir(inputArray); break;
				case "showpro": showpro(inputArray); break;
				case "show": show(inputArray); break;
				case "copyrv": copyrv(inputArray); break;
				case "copyvr": copyvr(inputArray); break;
				case "copyvv": copyvv(inputArray); break;
				case "move": move(inputArray); break;
				case "remove": remove(inputArray); break;
				
				case "modfile": modFile(inputArray); break;
				case "find": find(inputArray); break;
				case "tree": tree(inputArray); break;
				
				case "exit":
					if(inputArray.length == 1) {
						exit = true;
					} else {
						System.out.println("Invalid parameter");
					}
					break;
				case "sd":
					System.out.println(_fileSystem.getCurrentDirectory());
				default:
					System.out.println("Command not found");
					break;
			}
		}
	}
}
