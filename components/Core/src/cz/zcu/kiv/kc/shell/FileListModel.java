package cz.zcu.kiv.kc.shell;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.AbstractListModel;
//import javax.swing.event.ListDataEvent;
//import javax.swing.event.ListDataListener;

/**
 * data model for directory panel
 * @author Michal
 *
 */
public class FileListModel extends AbstractListModel<File> {

	private static final long serialVersionUID = 1616095720949603826L;

	private String dirPath = "/";
	
	private File[] files = new File[0];

	private File parentDir = null;
		
	private File longestFile;
	
	private int maxLength = 0;
	
	public File getLongestName()
	{
		return this.longestFile;
	}
	
	private Comparator<File> filenameComparator = new Comparator<File>() {
		@Override
		public int compare(File arg0, File arg1) {
			int ret;
						
			if (arg0.isDirectory() && !arg1.isDirectory()) {
				ret = -1;
			} else if (!arg0.isDirectory() && arg1.isDirectory()) {
				ret = 1;
			} else if (arg0.isDirectory() && arg1.isDirectory()) {
				ret = arg0.getName()
						.compareToIgnoreCase(arg1.getName());
			} else {
				ret = arg0.getName()
						.compareToIgnoreCase(arg1.getName());
			}
			return ret;
		}
	};
	
	@Override
	public File getElementAt(int arg0) {
		if (this.parentDir != null && arg0 == 0)
		{
			return new FirstFile(this.parentDir.getAbsolutePath());
		}
		
		return arg0 > files.length ? null: files[arg0 - (this.parentDir != null ? 1 : 0)];
	}

	@Override
	public int getSize() {
		return files.length + (this.parentDir != null ? 1 : 0);
	}
	
	/**
	 * Refreshes model according to currently set directory.
	 */
	public void refresh() {
		this.maxLength = 0;
		int origFilesCount = this.files.length;
		File dir = new File(dirPath);
		this.parentDir = dir.getParentFile();
		this.files = dir.listFiles();
		if (this.files == null)
		{
			this.files = new File[0];
		}
		
		Arrays.sort(this.files, this.filenameComparator);

		// find file with the longest name -> in order to be able
		// to preset dimension in directorypanel's file list
		for (File arg0 : this.files)
		{
			if (maxLength < arg0.getName().length())
			{
				maxLength = arg0.getName().length();
				longestFile = arg0;
			}
		}
		
		this.fireIntervalRemoved(this, 0, origFilesCount);
		this.fireIntervalAdded(this, 0, this.files.length + (this.parentDir != null ? 1 : 0));
	}

	/**
	 * current directory setter
	 * @param dirPath
	 */
	public void setDirPath(String dirPath) {
		this.dirPath = dirPath;
		refresh();
	}
}
