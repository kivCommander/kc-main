package cz.zcu.kiv.kc.plugin.showText;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;

public class TextViewer extends JPanel {
	private static final long serialVersionUID = -3740944757153796505L;

	public TextViewer()
	{
		super();
		UIManager.put("ClassLoader", getClass().getClassLoader());
	}
	
	public TextViewer(File fileToShow) {
		try {
			Scanner scan = new Scanner(fileToShow);
			scan.useDelimiter("\\Z");
			if (scan.hasNext()) {
				String content = scan.next();
			add(new JTextArea(content));
			}
			scan.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
