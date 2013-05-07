package cz.zcu.kiv.kc.plugin.createDir;

import java.io.File;
import java.util.List;

import javax.swing.JOptionPane;

import cz.zcu.kiv.kc.plugin.AbstractPlugin;

public class CreateDirPlugin extends AbstractPlugin{

	@Override
	public void executeAction(List<File> selectedFiles, String destinationPath,
			String sourcePath) {
		String name = askForName("New directory name:");
		if (name == null)
		{
			JOptionPane.showMessageDialog(
				this.mainWindow,
				"Operace byla zrušena uživatelem.",
				"Operace zrušena.",
				JOptionPane.ERROR_MESSAGE
			);
			return;
		}
		if (name.trim().isEmpty())
		{
			JOptionPane.showMessageDialog(
				this.mainWindow,
				"Neplatné zadání.",
				"Chyba.",
				JOptionPane.ERROR_MESSAGE
			);
			return;
		}
		
		new File(sourcePath + File.separator + name).mkdir();
		sendEvent(sourcePath);		
	}

	@Override
	public String getName() {
		return "New directory";
	}

}
