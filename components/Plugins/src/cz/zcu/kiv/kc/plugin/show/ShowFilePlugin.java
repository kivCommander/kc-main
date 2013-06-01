package cz.zcu.kiv.kc.plugin.show;

import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
//import java.nio.file.InvalidPathException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextPane;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import cz.zcu.kiv.kc.interfaces.IViewPlugin;
import cz.zcu.kiv.kc.plugin.AbstractPlugin;
import cz.zcu.kiv.kc.plugin.I18N;

/**
 * Show file plug-in. Detects file type and shows either image viewer, text viewer or hexadecimal viewer.
 * @author Michal
 *
 */
public class ShowFilePlugin extends AbstractPlugin implements IViewPlugin, PropertyChangeListener {

	private LoadBinnaryDataTask worker = null;

	private JDialog progressDialog;
	private JProgressBar pb = new JProgressBar();
	private JLabel jl = new JLabel(I18N.getText("loading"));

	private Action exitAction = new AbstractAction()
	{
		private static final long serialVersionUID = 5409574734727190161L;

		{
			putValue(NAME, I18N.getText("cancelTitle"));
		}
	
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (!ShowFilePlugin.this.worker.isDone())
			{
				int res = JOptionPane.showConfirmDialog(
					ShowFilePlugin.this.mainWindow,
					I18N.getText("cancelCurrentOperation"),
					I18N.getText("cancelCurrentOperationTitle"),
					JOptionPane.YES_NO_OPTION
				);
				if (res == JOptionPane.NO_OPTION)
				{
					return;
				}
				else
				{
					System.out.println("canceled: " + ShowFilePlugin.this.worker.cancel(true));
				}
			}
			ShowFilePlugin.this.progressDialog.dispose();
			ShowFilePlugin.this.progressDialog = null;
		}
	};
	
	public ShowFilePlugin()
	{
		super();
		UIManager.put("ClassLoader", getClass().getClassLoader());
	}
	
	@Override
	public void executeAction(
		final List<File> selectedFiles,
		String destinationPath,
		String sourcePath)
	{
		if (selectedFiles.isEmpty())
		{
			JOptionPane.showMessageDialog(
				this.mainWindow,
				I18N.getText("showFileNoFileSelected"),
				I18N.getText("showFileSelectionError"),
				JOptionPane.ERROR_MESSAGE
			);
			return;
		}
		
		File fileToShow = selectedFiles.get(0);
		
		if (!fileToShow.canRead())
		{
			showUnableToRead();
			return;
		}
		if (!fileToShow.isFile())
		{
			JOptionPane.showMessageDialog(
				ShowFilePlugin.this.mainWindow,
				I18N.getText("showFileSelectedItemNotFile"),
				I18N.getText("showFileSelectionError"),
				JOptionPane.ERROR_MESSAGE
			);
			return;
		}

		String fileType = null;
		try
		{
			fileType = FileTypeDetector.probeFile(fileToShow.toPath());
		}
		catch (IOException ignore)
		{
			ShowFilePlugin.this.showUnableToRead();
			return;
		}
		
		this.jl.setText(I18N.getText("loading", fileToShow.getName()));
		this.jl.setPreferredSize(new Dimension(240, -1));
		this.pb.setValue(0);
		this.pb.setStringPainted(true);

		JPanel panel = new JPanel(new GridLayout(3, 1));
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panel.add(this.jl);
		panel.add(this.pb);
		panel.add(new JButton(this.exitAction));

		progressDialog = new JDialog(ShowFilePlugin.this.mainWindow, ModalityType.APPLICATION_MODAL);
		this.progressDialog.add(panel);
		this.progressDialog.pack();
		this.progressDialog.setLocationRelativeTo(ShowFilePlugin.this.mainWindow);
		this.progressDialog.setResizable(false);
		this.progressDialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.progressDialog.addWindowListener(
			new WindowAdapter()
			{
				@Override
				 public void windowClosing(java.awt.event.WindowEvent arg0)
				{
					ShowFilePlugin.this.exitAction.actionPerformed(null);
				}
			}
		);
		
		if (fileType != null && fileType.startsWith("image/"))
		{
			this.showImage(fileToShow);
		}
		else if (fileType != null && fileType.startsWith("text/"))
		{
			this.showText(fileToShow);
		}
		else
		{
			// load content
			this.worker = new LoadBinnaryDataTask(fileToShow);
			this.worker.addPropertyChangeListener(this);
			this.worker.execute();
			this.progressDialog.setVisible(true);
		}
		
		return;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		
		if (evt.getPropertyName().equals("loaded")) {
			try 
			{
				this.show(this.worker.get());
			}
			catch (InterruptedException | ExecutionException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.exitAction.actionPerformed(null);
		}
		else if (evt.getPropertyName().equals("progress"))
		{
			this.pb.setValue((int) evt.getNewValue());
		}
	
	}
	 
	private void showUnsupportedType() {
		JOptionPane.showMessageDialog(
			this.mainWindow,
			I18N.getText("showFileUnsupportedFormat"),
			I18N.getText("showFileReadError"),
			JOptionPane.ERROR_MESSAGE
		);
	}
	
	private void showUnableToRead() {
		JOptionPane.showMessageDialog(
			this.mainWindow,
			I18N.getText("showFileUnableRead"),
			I18N.getText("showFileReadError"),
			JOptionPane.ERROR_MESSAGE
		);
	}
	
	private void showUnableToRead(IOException ex) {
		JOptionPane.showMessageDialog(
			this.mainWindow,
			I18N.getText("showFileUnableRead") + "\n" + ex.getMessage(),
			I18N.getText("showFileReadError"),
			JOptionPane.ERROR_MESSAGE
		);
	}
	/**
	 * Open text viewer's dialog window with content of file.
	 * @param fileToShow
	 * @throws IOException 
	 */
	private void showText(File fileToShow)
	{
		try
		(Scanner sc = new Scanner(fileToShow.toPath(), "UTF-8");)
		{
			StringBuilder sb = new StringBuilder();

			while (sc.hasNextLine())
			{
				sb.append(sc.nextLine());
				sb.append("\n");
			}
			if (sb.length() > 0) sb.deleteCharAt(sb.length()-1);
						
			this.show(sb.toString());
		}
		catch (IOException e)
		{
			e.printStackTrace();
			ShowFilePlugin.this.showUnableToRead(e);
		}
	}

	/**
	 * Open image viewer's dialog window with image
	 * @param fileToShow
	 */
	private void showImage(File fileToShow)
	{		
		try 
		{
			Image image = ImageIO.read(fileToShow);
			if (image == null)
			{
				ShowFilePlugin.this.showUnsupportedType();
				return;
			}
			image = this.rescaleToWindow(image);
			new ViewerDialog(ShowFilePlugin.this.mainWindow, ModalityType.MODELESS, new JLabel(new ImageIcon(image)));		
		}
		catch (IOException e)
		{
			ShowFilePlugin.this.showUnableToRead(e);
		}

	}
	
	/**
	 * Opens text viewer with "content" text.
	 * @param content
	 */
	private void show(String content)
	{
    	int winWidth = this.mainWindow.getWidth() - (this.mainWindow.getInsets().left + this.mainWindow.getInsets().right);
		int winHeight = this.mainWindow.getHeight() - (this.mainWindow.getInsets().top + this.mainWindow.getInsets().bottom);
		
		JTextPane tp = new JTextPane();
		tp.setFont(Font.getFont(Font.MONOSPACED));
		tp.setEditable(false);
		tp.setPreferredSize(new Dimension(winWidth, winHeight));
		tp.setText(content);
		tp.setCaretPosition(0);
		new ViewerDialog(this.mainWindow, ModalityType.MODELESS, tp);
	}

	/**
	 * Creates new image instance rescaled to main window dimensions, keeps aspect ratio
	 * @param image to scale
	 * @return scaled image
	 */
	private Image rescaleToWindow(Image image)
	{
		int imgWidth = image.getWidth(null);
		int imgHeight = image.getHeight(null);
		
		int winWidth = ShowFilePlugin.this.mainWindow.getWidth() - (ShowFilePlugin.this.mainWindow.getInsets().left + ShowFilePlugin.this.mainWindow.getInsets().right);
		int winHeight = ShowFilePlugin.this.mainWindow.getHeight() - (ShowFilePlugin.this.mainWindow.getInsets().top + ShowFilePlugin.this.mainWindow.getInsets().bottom);
		
		// limit img dimensions by dimensions of main windows
		if (imgWidth > winWidth)
		{
			imgWidth = winWidth;
		}
		if (imgHeight > winHeight)
		{
			imgHeight = winHeight;
		}
		
		// release one dimension by img's orientation
		if (imgWidth > imgHeight)
		{
			imgWidth = -1;
		}
		else
		{
			imgHeight = -1;
		}
		
		image = image.getScaledInstance(imgWidth, imgHeight, java.awt.Image.SCALE_SMOOTH);
		return image;
	}

	@Override
	public String getName() {
		return "show";
	}
	
	private class LoadBinnaryDataTask extends SwingWorker<String, Void>
	{
		private File fileToShow = null;
		
		public LoadBinnaryDataTask(File fileToShow)
		{
			this.fileToShow = fileToShow;
		}

		/**
		 * Loads content of file and constructs hexadecimal representation.
		 */
		@Override
		protected String doInBackground() throws Exception {
			StringBuilder sb = new StringBuilder();
			StringBuilder tmpString = new StringBuilder();
			sb.append(String.format("%08X: ", 0));
	    	try
	    	(
	    		FileInputStream fin = new FileInputStream(this.fileToShow);
	            FileChannel finCh = fin.getChannel();
	    	)
	    	{
	    		ByteBuffer buff = ByteBuffer.allocate(65536);
	            int i = 1;
	            int pos = 0;
                int progress = 0;
                float chunk = ((float) fileToShow.length() / 1000);
                int descreteProgress = 0;
                int read = 0;
	            while ((read = finCh.read(buff)) != -1 || buff.position() > 0)
	            {
	            	buff.flip();
	            	
	            	progress += read;
                    if (progress - descreteProgress > chunk)
                    {
                    	descreteProgress = progress - (int)(progress % chunk);
                    	this.setProgress((int) (((float) descreteProgress)/fileToShow.length() * 100));
                    }
                    
	            	while(buff.hasRemaining())
	            	{
	            		byte b = buff.get();
	            		sb.append(String.format("%02X ", b));
	            		tmpString.append(String.format("%c", b >= 0x20 && b < 127 ? (char) b : '.' ));
	            		
	            		pos++;
	            		i++;
	            		if (i == 17)
	            		{
	            			sb.append(String.format("|%s%n%08X: ", tmpString.toString(), pos));
	            			tmpString.setLength(0);
	            			i = 1;
	            		}
	            		else if (i == 9)
	            		{
	            			sb.append("| ");
	            		}
	            	}            	
	                buff.compact();
	            }
	            
	            this.firePropertyChange("loaded", false, true);
	    	}
	    	
	    	return sb.toString();
		}
	}


}
