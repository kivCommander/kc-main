package cz.zcu.kiv.kc.shell;

import java.io.File;

import javax.swing.AbstractListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class FileListModel extends AbstractListModel<File> {
	
	private static final long serialVersionUID = 1616095720949603826L;
	
	private String dirPath = "c:/Users";
	private File[] files = new File[0];

	@Override
	public File getElementAt(int arg0) {
		return files[arg0];
	}

	@Override
	public int getSize() {
		return files.length;
	}	
	
	public void refresh(){
		File dir = new File(dirPath);
		files = dir.listFiles();
		ListDataEvent event = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0 ,files.length);
		for(ListDataListener listener : listenerList.getListeners(ListDataListener.class)){
			listener.contentsChanged(event);
		}
	}

	public void setDirPath(String dirPath){
		this.dirPath = dirPath;
	}	

}
