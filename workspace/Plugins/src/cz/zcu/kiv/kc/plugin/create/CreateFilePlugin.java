package cz.zcu.kiv.kc.plugin.create;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.UIManager;

import cz.zcu.kiv.kc.plugin.AbstractPlugin;

public class CreateFilePlugin extends AbstractPlugin  {

	public CreateFilePlugin()
	{
		super();
		UIManager.put("ClassLoader", getClass().getClassLoader());
	}
		
	@Override
	public void executeAction(List<File> selectedFiles, String destinationPath,
			String sourcePath) {
		try {
			new File(sourcePath + File.separator + System.nanoTime()).createNewFile();
			sendEvent(sourcePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String getName() {
		return "create";
	}

}
