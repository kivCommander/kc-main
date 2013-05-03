package cz.zcu.kiv.kc.plugin.show;

import java.awt.Dialog.ModalityType;
import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import cz.zcu.kiv.kc.plugin.AbstractPlugin;

public class ShowFilePlugin extends AbstractPlugin {

	private JLabel imageLabel;
	
	@Override
	public void executeAction(
		List<File> selectedFiles,
		String destinationPath,
		String sourcePath)
	{
		if (selectedFiles.isEmpty())
		{
			throw new IllegalArgumentException("Empty selected files list.");
		}
		File fileToShow = selectedFiles.get(0);
		if (!fileToShow.canRead())
		{
			throw new IllegalArgumentException("Can't read file.");
		}
		if (!fileToShow.isFile())
		{
			throw new IllegalArgumentException("Selected item is not file.");
		}
		
		String fileType = null;
		try { fileType = Files.probeContentType(fileToShow.toPath()); }
		catch (InvalidPathException | IOException e) { }
		System.out.println(fileType);
		if (fileType != null && fileType.startsWith("image/"))
		{
			this.showImage(fileToShow);
		}
		else if (fileType != null && fileType.startsWith("text/"))
		{
			// TODO show text files
		}
		else
		{
			throw new IllegalArgumentException("Unknow file format.");
		}
	}

	private void showImage(File fileToShow)
	{
		ImageInputStream is = null;
		Image image = null;
		try
		{
			is = new FileImageInputStream(fileToShow);
			image = ImageIO.read(is);
		}
		catch (IOException e) { throw new IllegalStateException("Unable to open selected file. (" + e.getMessage() + ")"); }
		finally
		{
			try { is.close(); } catch (IOException e) { }
		}
		
		int width = this.mainWindow.getWidth() - (this.mainWindow.getInsets().left + this.mainWindow.getInsets().right);
		image = image.getScaledInstance(width, -1, java.awt.Image.SCALE_SMOOTH);
		new ViewerDialog(this.mainWindow, ModalityType.MODELESS, new JLabel(new ImageIcon(image)));		
	}

	@Override
	public String getName() {
		return "Show";
	}

}
