package cz.zcu.kiv.kc.plugin;

import java.awt.Window;
import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;

public class TestPlugin implements Plugin {

	@Override
	public void executeAction(List<File> selectedFiles, String destinationPath, String sourcePath) {
		JOptionPane.showMessageDialog(null,
				"Selected items='" + Arrays.toString(selectedFiles.toArray())
						+ "', destinationPath='" + destinationPath + "'");
	}

	@Override
	public String getName() {
		return "Test action";
	}

	@Override
	public void setMainWindow(Window win) {
		// TODO Auto-generated method stub
		
	}

}
