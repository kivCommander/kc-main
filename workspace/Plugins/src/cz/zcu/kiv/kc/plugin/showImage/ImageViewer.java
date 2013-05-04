package cz.zcu.kiv.kc.plugin.showImage;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImageViewer extends JPanel {
	private static final long serialVersionUID = 885202053851291681L;
	private BufferedImage image;

	public ImageViewer(File fileToShow) {
		try {
			image = ImageIO.read(fileToShow);
			if (image != null) {
				setPreferredSize(new Dimension(image.getWidth(),
						image.getHeight()));
			}
		} catch (IOException ex) {
			// handle exception...
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, null); // see javadoc for more info on the
										// parameters
	}

}
