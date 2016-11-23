package main;

import java.util.ArrayList;

public class DirectoryTree {
	
	private String _name;
	private String _pathForFind;
	
	private ArrayList<DirectoryTree> _directoryTree;
	private ArrayList<File> _files;
	
	public DirectoryTree(String pName) {
		_name = pName;
		_directoryTree = new ArrayList<>();
		_files = new ArrayList<>();
		_pathForFind = "";
	}
	public DirectoryTree(String pName, ArrayList<DirectoryTree> pDirectories, ArrayList<File> pFiles){
	  _name = pName;
	  _directoryTree = pDirectories;
	  _files = pFiles;
	}
	
	public void removeDirectory(DirectoryTree pDirectory, boolean pPurge){
	  DirectoryTree dt;
	  for(int i = 0; i < _directoryTree.size(); i++){
	     dt = _directoryTree.get(i);
	     if(dt.getName().equals(pDirectory.getName())){
	       //Borrar archivos
	       while(dt.getFileList().size() > 0 && pPurge){
	         dt.removeFile(dt.getFileList().get(0));
	       }
	       //Borrar directorios recursivamente
	       while(dt.getDirectoryList().size() > 0 && pPurge){
	         dt.removeDirectory(dt.getDirectoryList().get(0), true);
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
	    if(f.get_name().toLowerCase().equals(pFile.get_name().toLowerCase()) && f.get_extension().toLowerCase().equals(pFile.get_extension().toLowerCase())){
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
		    removeDirectory(pNewDirectory, false);
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
			System.out.println(_directoryTree.get(i).getName());
			_directoryTree.get(i).treeFileSystem(depth+1);
		}
		for (int i = 0; i < _files.size(); i++){
			nTabs (depth);
			System.out.println(_files.get(i).get_name()+"."+_files.get(i).get_extension());
			
		}	
	}	
	public void findFileOrDirectory(String pName, String pCurrentName, boolean pKindOfSearch, boolean pSearchAll) {
		_pathForFind = pCurrentName;
		for (int i = 0; i < _directoryTree.size(); i++){
			if(pKindOfSearch) {
				if(_directoryTree.get(i).getName().toLowerCase().equals(pName.toLowerCase())) {
					System.out.println(_pathForFind + "/" + _directoryTree.get(i).getName());
				}
			}
			_directoryTree.get(i).findFileOrDirectory(pName, pCurrentName + "/" + _directoryTree.get(i).getName(), pKindOfSearch, pSearchAll);
		}
		if(!pKindOfSearch) {
			for (int i = 0; i < _files.size(); i++){
				if(!pSearchAll) {
					if((_files.get(i).get_name()+"."+_files.get(i).get_extension()).toLowerCase().equals(pName.toLowerCase())) {
						System.out.println(_pathForFind + "/" + _files.get(i).get_name()+"."+_files.get(i).get_extension());
					}	
				} else {
					if(_files.get(i).get_extension().toLowerCase().equals(pName.toLowerCase())) {
						System.out.println(_pathForFind + "/" + _files.get(i).get_name()+"."+_files.get(i).get_extension());
					}
				}
			}	
		}
	}
}
