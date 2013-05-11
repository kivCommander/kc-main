package cz.zcu.kiv.kc.plugin.createDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import cz.zcu.kiv.kc.interfaces.ICreateDirPlugin;
import cz.zcu.kiv.kc.plugin.AbstractPlugin;

public class CreateDirPlugin extends AbstractPlugin implements ICreateDirPlugin {

	public CreateDirPlugin()
	{
		super();
		UIManager.put("ClassLoader", getClass().getClassLoader());
	}
	@Override
	public void executeAction(List<File> selectedFiles, String destinationPath,
			String sourcePath) {
		String name = askForName("New directory name:");
		if (name == null)
		{
			JOptionPane.showMessageDialog(
				this.mainWindow,
				"Operace byla zru�ena u�ivatelem.",
				"Operace zru�ena.",
				JOptionPane.ERROR_MESSAGE
			);
			return;
		}
		if (name.trim().isEmpty())
		{
			JOptionPane.showMessageDialog(
				this.mainWindow,
				"Neplatn� zad�n�.",
				"Chyba.",
				JOptionPane.ERROR_MESSAGE
			);
			return;
		}
		
		File newDir = new File(sourcePath + File.separator + name);
		try {
			Files.createDirectory(newDir.toPath());
		}
		catch (FileAlreadyExistsException e)
		{
			JOptionPane.showMessageDialog(
				this.mainWindow,
				"Ji� existuje soubor/adres�� se zadan�m jm�nem.",
				"Chyba",
				JOptionPane.ERROR_MESSAGE
			);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		//System.out.println(new File(sourcePath + File.separator + name).mkdir());
		sendEvent(sourcePath);		
	}

	@Override
	public String getName() {
		return "New directory";
	}

}
