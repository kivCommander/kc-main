package cz.zcu.kiv.kc.plugin.move;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import cz.zcu.kiv.kc.plugin.AbstractPlugin;

public class MoveFilePlugin extends AbstractPlugin{

	@Override
	public void executeAction(List<File> selectedFiles, String destinationPath,
			String sourcePath) {
		for (File file : selectedFiles) {
			// TODO handle not empty dir
			try {
				FileOutputStream fileOutputStream = new FileOutputStream(destinationPath + File.separator + file.getName() );
				Files.copy(file.toPath(), fileOutputStream );
				fileOutputStream.close();
				file.delete();
				sendEvent(destinationPath);
				sendEvent(sourcePath);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public String getName() {
		return "Move";
	}

}
