package main;

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
					System.out.println("Ivalid parameter");
				}
			} else {
				System.out.println("Command not found");
			}
		}
	}
	
	public String[] separeCommand (String pInput){
		String[] inputArray = pInput.split("\\|+");
		for (int i = 0; i < inputArray.length; i++){
			System.out.println(inputArray[i]);
		}
		return inputArray;
	}
		
	public void help () {
		System.out.println("\n\nHELP:");
		System.out.println("file /name /extension /content  \t | Creates new file in the current directory");
		System.out.println("makedir /name  \t\t\t | Creates new directory inside the current one");
		System.out.println("cdir /path \t\t\t\t | Changes the current directory");
		System.out.println("listdir \t\t\t\t | Lists the contents of the directory");
		System.out.println("modfile /path \t\t\t\t | Modifies the contents of the file given the path");
		System.out.println("showpro /path \t\t\t\t | Displays the properties of the file in the given path");
		System.out.println("show /path \t\t\t\t | Displays the file in the given path");
		System.out.println("exit \t\t\t\t\t | Ends the program\n\n");
	}
	public void file (String[] pInputArray) {
		if(pInputArray.length == 4) {
			if(_fileSystem.createFile(pInputArray[1], pInputArray[2], pInputArray[3], false)) { //file nombre extensión contenido
				System.out.println("File created");
			} else { 
			  System.out.println("File already exists, do you want to overwrite it? (y/n)");
              String newInput = _input.nextLine();
              if(newInput.toLowerCase().equals("y")){
                _fileSystem.createFile(pInputArray[1],pInputArray[2], pInputArray[3], true);
                System.out.println("File created");
              } else {
                System.out.println("Error creating file");
              }
			}
		} else {
			System.out.println("Invalid parameter");
		}
	}
	public void makedir (String[] pInputArray){
		if(pInputArray.length == 2) {
			if(_fileSystem.createDirectory(pInputArray[1], false)) { //makedir nombre
				System.out.println("Directory created");
			} else {
				System.out.println("Directory already exists, do you want to overwrite it? (y/n)");
				String newInput = _input.nextLine();
				if(newInput.toLowerCase().equals("y")){
				  _fileSystem.createDirectory(pInputArray[1], true);
				  System.out.println("Directory created");
				} else {
				  System.out.println("Error creating directory");
				}
			}
		} else {
			System.out.println("Invalid parameter");
		}
	}
	public void cdir (String[] pInputArray){
		if(pInputArray.length == 2) {
			if(_fileSystem.changeDirectory(pInputArray[1])) { //cdir path (absoluto o relativo)
				System.out.println("Directory changed");
			} else {
				System.out.println("Error changing directory - Probably doesn't exist");
			}
		} else {
			System.out.println("Invalid parameter");
		}
	
	}
	public void listdir (String[] pInputArray) {
		if(pInputArray.length == 1) {
			_fileSystem.listCurrentDirectory(); //listdir
		} else {
			System.out.println("Invalid parameter");
		}
	}
	public void showpro (String[] pInputArray) {
		if(pInputArray.length == 2) {
			if(_fileSystem.showProperties(pInputArray[1])) { //cdir path (absoluto o relativo)
			} else {
				System.out.println("Error showing file properties - Probably doesn't exist");
			}
		} else {
			System.out.println("Invalid parameter");
		}
	}
	public void show (String[] pInputArray) {
		if(pInputArray.length == 2) {
			if(_fileSystem.showFile(pInputArray[1])) { //show filename 
			} else {
				System.out.println("Error showing file - Probably doesn't exist");
			}
		} else {
			System.out.println("Invalid parameter");
		}
		
	}
	public void copyrv (String[] pInputArray){
		if(pInputArray.length == 3) {
			if(_fileSystem.copyRealToVirtual(pInputArray[1], pInputArray[2])) { //copyrv realpath virtualpath 
			} else {
				System.out.println("Error copying file - invalid path or internal error");
			}
		} else {
			System.out.println("Invalid parameter");
		}
	}
	public void copyvr (String[] pInputArray){
		if(pInputArray.length == 3) { 
			if(_fileSystem.copyVirtualToReal(pInputArray[1], pInputArray[2])) { //copyvr virtualpath realpath  
			} else {
				System.out.println("Error copying file - invalid path or internal error");
			}
		} else {
			System.out.println("Invalid parameter");
		}
	}
	public void copyvv (String[] pInputArray){
		if(pInputArray.length == 3) { 
			if(_fileSystem.copyVirtualToVirtual(pInputArray[1], pInputArray[2])) { //copyrv realpath virtualpath 
			} else {
				System.out.println("Error copying file - invalid path or internal error");
			}
		} else {
			System.out.println("Invalid parameter");
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
				
				case "help": help(); break;
				case "file": file(inputArray); break;
				case "makedir": makedir(inputArray); break;
				case "cdir": cdir(inputArray); break;
				case "listdir": listdir(inputArray); break;
				case "showpro": showpro(inputArray); break;
				case "show": show(inputArray); break;
				case "copyrv": copyrv(inputArray); break;
				case "copyvr": copyvr(inputArray); break;
				case "copyvv": copyvv(inputArray); break;
				
				case "modfile": 
					// TODO Modificar un archivo que se encuentra en el directorio actual
					break;
				
				
				case "exit":
					if(inputArray.length == 1) {
						exit = true;
					} else {
						System.out.println("Invalid parameter");
					}
					break;
				
				default:
					System.out.println("Command not found");
					break;
			}
		}
	}
}
