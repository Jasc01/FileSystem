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
			if(_directoryTree.get(i).getName().equals(pNewDirectory.getName())) {
				alreadyIs = true;
			}
		}
		if(!alreadyIs) {
			_directoryTree.add(pNewDirectory);
			return true;
		}
		return false;
	}
	
	public String getName() {
		return _name;
	}
	
}
