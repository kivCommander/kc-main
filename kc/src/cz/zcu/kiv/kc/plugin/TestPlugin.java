package cz.zcu.kiv.kc.plugin;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;

public class TestPlugin implements Plugin {

	@Override
	public void executeAction(List<File> selectedFiles, String destinationPath) {
		System.out.println("Plugin executed: selectedFiles='"
				+ Arrays.toString(selectedFiles.toArray())
				+ "', destinationPath='" + destinationPath + "'");
		JOptionPane.showMessageDialog(null,
				"Selected items='" + Arrays.toString(selectedFiles.toArray())
						+ "', destinationPath='" + destinationPath + "'");
	}

	@Override
	public String getId() {
		return "test";
	}

	@Override
	public String getName() {
		return "Test action";
	}

}
