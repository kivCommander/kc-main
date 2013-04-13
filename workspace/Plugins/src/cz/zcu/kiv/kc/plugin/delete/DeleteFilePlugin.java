package cz.zcu.kiv.kc.plugin.delete;

import java.io.File;
import java.util.List;

import cz.zcu.kiv.kc.plugin.AbstractPlugin;

public class DeleteFilePlugin extends AbstractPlugin {

	@Override
	public void executeAction(List<File> selectedFiles, String destinationPath,
			String sourcePath) {
		for (File file : selectedFiles) {
			// TODO handle not empty dir
			file.delete(); 
			sendEvent(destinationPath);
		}
	}

	@Override
	public String getName() {
		return "Delete";
	}

}
