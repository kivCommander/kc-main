package cz.zcu.kiv.kc.plugin.delete;

import java.io.File;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import cz.zcu.kiv.kc.interfaces.IDeletePlugin;
import cz.zcu.kiv.kc.plugin.AbstractPlugin;
import cz.zcu.kiv.kc.plugin.I18N;

/**
 * Delete selected items plug-in
 * @author Michal
 *
 */
public class DeleteFilePlugin extends AbstractPlugin implements IDeletePlugin {

	public DeleteFilePlugin()
	{
		super();
		UIManager.put("ClassLoader", getClass().getClassLoader());
	}
	
	@Override
	public void executeAction(List<File> selectedFiles, String destinationPath,
			String sourcePath) {
		for (File file : selectedFiles) {
			if (file != null) {
				if (file.isDirectory() && file.listFiles().length > 0) {
					int option = JOptionPane
							.showConfirmDialog(
									null,
									I18N.getText("detleteQuestion", file.getName()),
									I18N.getText("detleteQuestionTitle"),
									JOptionPane.YES_NO_OPTION);
					if (option == JOptionPane.OK_OPTION) {
						// its ok, delete it
					} else {
						continue;
					}
				}
				deleteFile(file);
			}
		}
	}

	private void deleteFile(File file) {
		if (file.isDirectory() && file.listFiles().length > 0) {
			for (File childFile : file.listFiles()) {
				deleteFile(childFile);
			}
		}
		file.delete();
		sendEvent(file.getParent());
	}

	@Override
	public String getName() {
		return "delete";
	}

}
