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
	
	public void readCommand() {
		boolean exit = false;
		String userInput;
		System.out.println("Welcome to the File System");
		while(!exit) {
			System.out.println("Current Directory: " + _fileSystem.getCurrentDirectory());
			userInput = _input.nextLine();
			String[] inputArray = userInput.split("\\s+");
			switch(inputArray[0].toLowerCase()) {
				case "file":
					if(inputArray.length == 4) {
						if(_fileSystem.createFile(inputArray[1], inputArray[2], inputArray[3])) { //file nombre extensión contenido
							System.out.println("File created");
							//_fileSystem.printDirectoryFiles();
						} else {
							System.out.println("Error creating file");
						}
					} else {
						System.out.println("Invalid parameter");
					}
					break;
				case "makedir":
					if(inputArray.length == 2) {
						if(_fileSystem.createDirectory(inputArray[1])) { //makedir nombre
							System.out.println("Directory created");
							// _fileSystem.printDirectories();
						} else {
							System.out.println("Error creating directory");
						}
					} else {
						System.out.println("Invalid parameter");
					}
					break;
				case "cdir":
					if(inputArray.length == 2) {
						if(_fileSystem.changeDirectory(inputArray[1])) { //cdir path (absoluto o relativo)
							System.out.println("Directory changed");
							// _fileSystem.printDirectories();
						} else {
							System.out.println("Error changing directory - Probably not exist");
						}
					} else {
						System.out.println("Invalid parameter");
					}
					break;
				case "listdir":
					if(inputArray.length == 1) {
						_fileSystem.listCurrentDirectory(); //listdir
					} else {
						System.out.println("Invalid parameter");
					}
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
