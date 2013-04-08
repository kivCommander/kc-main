package cz.zcu.kiv.kc.shell;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.AbstractListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class FileListModel extends AbstractListModel<File> {

	private static final long serialVersionUID = 1616095720949603826L;

	private String dirPath = "/";
	private File[] files = new File[0];

	@Override
	public File getElementAt(int arg0) {
		return files[arg0];
	}

	@Override
	public int getSize() {
		return files.length;
	}

	public void refresh() {
		File dir = new File(dirPath);
		File[] childFiles = dir.listFiles();
		int childFilesLength = childFiles == null ? 0 : childFiles.length;
		String parentFile = dir.getParent();
		if (parentFile != null) {
			files = new File[childFilesLength + 1];
			files[0] = new FirstFile(parentFile);
		} else {
			files = new File[childFilesLength];
		}
		if (childFiles != null) {
			Arrays.sort(childFiles, new Comparator<File>() {
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
			});
			if (parentFile != null) {
				for (int i = 1; i < childFilesLength + 1; i++) {
					files[i] = childFiles[i - 1];
				}
			} else {
				for (int i = 0; i < childFilesLength; i++) {
					files[i] = childFiles[i];
				}
			}
		}
		ListDataEvent event = new ListDataEvent(this,
				ListDataEvent.CONTENTS_CHANGED, 0, files.length);
		for (ListDataListener listener : listenerList
				.getListeners(ListDataListener.class)) {
			listener.contentsChanged(event);
		}
	}

	public void setDirPath(String dirPath) {
		this.dirPath = dirPath;
		refresh();
	}

}
