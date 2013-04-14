package cz.zcu.kiv.kc.plugin.createDir;

import java.io.File;
import java.util.List;

import cz.zcu.kiv.kc.plugin.AbstractPlugin;

public class CreateDirPlugin extends AbstractPlugin{

	@Override
	public void executeAction(List<File> selectedFiles, String destinationPath,
			String sourcePath) {
		new File(sourcePath + File.separator + System.nanoTime()).mkdir();
		sendEvent(sourcePath);
		
	}

	@Override
	public String getName() {
		return "New directory";
	}

}
