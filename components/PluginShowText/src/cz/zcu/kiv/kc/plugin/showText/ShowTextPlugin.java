package cz.zcu.kiv.kc.plugin.showText;

import java.io.File;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import cz.zcu.kiv.kc.plugin.AbstractPlugin;

/**
 * Alternative text viewer plug-in
 * @author Michal
 *
 */
public class ShowTextPlugin extends AbstractPlugin {

	public ShowTextPlugin()
	{
		super();
		UIManager.put("ClassLoader", getClass().getClassLoader());
	}
	
	@Override
	public void executeAction(List<File> selectedFiles, String destinationPath,
			String sourcePath) {
		if (selectedFiles != null) {
			for (File file : selectedFiles) {
				JPanel imagePanel = new TextViewer(file);
				JFrame frame = new JFrame(file.getAbsolutePath());
				frame.add(imagePanel);
				frame.pack();
				frame.setVisible(true);
			}
		}
	}

	@Override
	public String getName() {
		return "showText";
	}

}
