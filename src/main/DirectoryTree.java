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
	
	public void removeDirectory(DirectoryTree pDirectory){
	  DirectoryTree dt;
	  for(int i = 0; i < _directoryTree.size(); i++){
	     dt = _directoryTree.get(i);
	     if(dt.getName().equals(pDirectory.getName())){
	       //Borrar archivos
	       while(dt.getFileList().size() > 0){
	         dt.removeFile(dt.getFileList().get(0));
	       }
	       //Borrar directorios recursivamente
	       while(dt.getDirectoryList().size() > 0){
	         dt.removeDirectory(dt.getDirectoryList().get(0));
	       }
	       
	       _directoryTree.remove(i);
	       break;
	     }
	  }
	}
	
	public void removeFile(File pFile){
	  File f;
	  for(int i = 0; i < _files.size(); i++){
	    f = _files.get(i);
	    if(f.get_name().equals(pFile.get_name()) && f.get_extension().equals(pFile.get_extension())){
	      _files.remove(i);
	      break;
	    }
	  }
	}
	
	public boolean addDirectory(DirectoryTree pNewDirectory, boolean pOverwrite) {
		boolean alreadyIs = false;
		for(int i = 0; i < _directoryTree.size(); i++) {
			if(_directoryTree.get(i).getName().toLowerCase().equals(pNewDirectory.getName().toLowerCase())) {
				alreadyIs = true;
			}
		}
		if (!alreadyIs) {
			_directoryTree.add(pNewDirectory);
			return true;
		} else {
		  if (pOverwrite){
		    removeDirectory(pNewDirectory);
		    _directoryTree.add(pNewDirectory);
		    return true;
		  } else {
		    return false;
		  }
		}
	}
	
	public boolean addFile(File pNewFile, boolean pOverwrite) {
		String mergedName = pNewFile.get_name() + "." + pNewFile.get_extension();
		boolean alreadyIs = false;
		for(File file : _files) {
			String tempString = file.get_name() + "." + file.get_extension();
			if(mergedName.equals(tempString)) {
				alreadyIs = true;
			}
		}
		if (!alreadyIs && checkName(pNewFile.get_name()) && checkName(pNewFile.get_extension())) {
			_files.add(pNewFile);
			return true;
		} else {
		  if (pOverwrite){
		    removeFile(pNewFile);
		    _files.add(pNewFile);
		    return true;
		  } else{
		    return false;
		  }
		}
	}
	
	private boolean checkName(String inputString) {
		String[] forbiddenItems = {"/", "\\", ":", "*", "?", "|", "<", ">", "\"", "."};
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
	
	
	private void nTabs(int n){
		for(int i =0; i < n; i++) { System.out.print("\t");	 }
	}
	
	public void treeFileSystem (int depth) {
		for (int i = 0; i < _directoryTree.size(); i++){
			nTabs (depth);
			System.out.println(_name);
			_directoryTree.get(i).treeFileSystem(depth+1);
		}
		for (int i = 0; i < _files.size(); i++){
			nTabs (depth);
			System.out.println(_files.get(i).get_name()+"."+_files.get(i).get_extension());
			
		}	
	}
}
