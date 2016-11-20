package main;

import java.util.ArrayList;

public class DirectoryTree {
	
	String _name;
	
	ArrayList<DirectoryTree> _directoryTree;
	ArrayList<File> _files;
	
	public DirectoryTree(String pName) {
		_name = pName;
		_directoryTree = new ArrayList<>();
		_files = new ArrayList<>();
	}
	
	public boolean addDirectory(DirectoryTree pNewDirectory) {
		boolean alreadyIs = false;
		for(int i = 0; i < _directoryTree.size(); i++) {
			if(_directoryTree.get(i).getName().toLowerCase().equals(pNewDirectory.getName().toLowerCase())) {
				alreadyIs = true;
			}
		}
		if(!alreadyIs) {
			_directoryTree.add(pNewDirectory);
			return true;
		}
		return false;
	}
	
	public boolean addFile(File pNewFile) {
		String mergedName = pNewFile.get_name() + "." + pNewFile.get_extension();
		boolean alreadyIs = false;
		for(File file : _files) {
			String tempString = file.get_name() + "." + file.get_extension();
			if(mergedName.equals(tempString)) {
				alreadyIs = true;
			}
		}
		if(!alreadyIs && checkName(pNewFile.get_name()) && checkName(pNewFile.get_extension())) {
			_files.add(pNewFile);
			return true;
		}
		return false;
	}
	
	private boolean checkName(String inputString) {
		String[] forbiddenItems = {"/", "\\", ":", "*", "?", "|", "<", ">", "\""};
	    for(int i =0; i < forbiddenItems.length; i++) {
	        if(inputString.contains(forbiddenItems[i])) {
	            return false;
	        }
	    }
	    return true;
	}
	
	public String getName() {
		return _name;
	}
	
	public ArrayList<DirectoryTree> getDirectoryList() {
		return _directoryTree;
	}
	
	public ArrayList<File> getFileList() {
		return _files;
	}
}
