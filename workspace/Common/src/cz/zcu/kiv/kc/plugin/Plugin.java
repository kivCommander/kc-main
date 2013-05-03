package cz.zcu.kiv.kc.plugin;

import java.awt.Window;
import java.io.File;
import java.util.List;

/**
 * @author 2540p
 *
 */
public interface Plugin {	
	void executeAction(List<File> selectedFiles, String destinationPath, String sourcePath);
	String getName();
	void setMainWindow(Window win);
}
