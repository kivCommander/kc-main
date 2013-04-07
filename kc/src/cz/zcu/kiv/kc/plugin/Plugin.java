package cz.zcu.kiv.kc.plugin;

import java.io.File;
import java.util.List;

/**
 * @author 2540p
 *
 */
public interface Plugin {	
	void executeAction(List<File> selectedFiles, String destinationPath);	
	String getId();	
	String getName();
}
