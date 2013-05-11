package cz.zcu.kiv.kc.plugin.createDir;

import java.io.File;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import cz.zcu.kiv.kc.interfaces.ICreateDirPlugin;
import cz.zcu.kiv.kc.plugin.AbstractPlugin;
import cz.zcu.kiv.kc.plugin.I18N;

public class CreateDirPlugin extends AbstractPlugin implements ICreateDirPlugin {

	public CreateDirPlugin() {
		super();
		UIManager.put("ClassLoader", getClass().getClassLoader());
	}

	@Override
	public void executeAction(List<File> selectedFiles, String destinationPath,
			String sourcePath) {
		String name = askForName(I18N.getText("newDirName"));
		if (name == null) {
			JOptionPane.showMessageDialog(this.mainWindow,
					I18N.getText("newDirOpCanceled"),
					I18N.getText("newDirOpCanceledTitle"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (name.trim().isEmpty()) {
			JOptionPane.showMessageDialog(this.mainWindow, 
					I18N.getText("newDirWrongInput"),
					I18N.getText("newDirWrongInputTitle"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		new File(sourcePath + File.separator + name).mkdir();
		sendEvent(sourcePath);
	}

	@Override
	public String getName() {
		return "newDir";
	}

}
