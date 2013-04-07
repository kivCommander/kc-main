package cz.zcu.kiv.kc.plugin.delete;

import java.io.File;
import java.util.List;

public class DeleteFilePlugin implements IDeleteFilePlugin {

	@Override
	public void executeAction(List<File> selectedFiles, String destinationPath,
			String sourcePath) {
		for (File file : selectedFiles) {
			file.delete(); 
		}
	}

	@Override
	public String getId() {
		return "delete";
	}

	@Override
	public String getName() {
		return "Delete";
	}

}
