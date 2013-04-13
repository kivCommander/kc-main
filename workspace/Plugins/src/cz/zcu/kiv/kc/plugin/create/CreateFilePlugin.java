package cz.zcu.kiv.kc.plugin.create;

import java.io.File;
import java.io.IOException;
import java.util.List;

import cz.zcu.kiv.kc.plugin.AbstractPlugin;

public class CreateFilePlugin extends AbstractPlugin  {

	@Override
	public void executeAction(List<File> selectedFiles, String destinationPath,
			String sourcePath) {
		try {
			new File(destinationPath + File.separator + System.nanoTime()).createNewFile();
			sendEvent(destinationPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String getName() {
		return "Create";
	}

}
