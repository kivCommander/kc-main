package cz.zcu.kiv.kc.plugin;

import java.awt.Window;
import java.io.File;
import java.util.List;

/**
 * @author 2540p
 *
 */
public interface Plugin {
	
	/**
	 * Executes plug-in's functionality.
	 * Method should fire refresh event after it's execution.
	 * @param selectedFiles - list of files selected for processing
	 * @param destinationPath - target directory for storing the result
	 * @param sourcePath - source directory of selected files
	 */
	void executeAction(List<File> selectedFiles, String destinationPath, String sourcePath);
	
	/**
	 * Returns name of the plug-in.
	 * Probably will be replaced with more complex system for translations.
	 * @return name given by user
	 */
	String getName();
	
	/**
	 * Sets the application window for proper positioning of possible dialogs and windows.   
	 * @param win
	 */
	void setMainWindow(Window win);
}
