package cz.zcu.kiv.kc.plugin.showImage;

import java.io.File;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import cz.zcu.kiv.kc.plugin.AbstractPlugin;

public class ShowImagePlugin extends AbstractPlugin {


	public ShowImagePlugin()
	{
		super();
		UIManager.put("ClassLoader", getClass().getClassLoader());
	}
	
	@Override
	public void executeAction(List<File> selectedFiles, String destinationPath,
			String sourcePath) {
		if (selectedFiles != null) {
			for (File file : selectedFiles) {
				JPanel imagePanel = new ImageViewer(file);
				JFrame frame = new JFrame(file.getAbsolutePath());
				frame.add(imagePanel);
				frame.pack();
				frame.setVisible(true);
			}
		}

	}

	@Override
	public String getName() {
		return "Show image";
	}

}
