package cz.zcu.kiv.kc.plugin.copy;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Dialog.ModalityType;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;
import java.util.List;

import javax.jws.Oneway;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.SwingWorker.StateValue;
import javax.swing.text.html.parser.TagElement;

import cz.zcu.kiv.kc.interfaces.ICopyPlugin;
import cz.zcu.kiv.kc.plugin.AbstractPlugin;
import cz.zcu.kiv.kc.plugin.I18N;

public class CopyFilePlugin extends AbstractPlugin implements ICopyPlugin, PropertyChangeListener
{

	private CopyTask worker;
	private JDialog progressDialog = new JDialog(this.mainWindow, ModalityType.APPLICATION_MODAL);
	private JProgressBar pb = new JProgressBar();
	private JProgressBar fpb = new JProgressBar();
	private JLabel jl = new JLabel(I18N.getText("status"));
	private String destinationPath = "";
	private String sourcePath = "";
	
	private Action exitAction = new AbstractAction()
	{
		{
			putValue(NAME, I18N.getText("cancelTitle"));
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!CopyFilePlugin.this.worker.isDone())
			{
				int res = JOptionPane.showConfirmDialog(
					CopyFilePlugin.this.mainWindow,
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
					System.out.println("canceled: " + CopyFilePlugin.this.worker.cancel(true));
				}
			}
			
			CopyFilePlugin.this.progressDialog.dispose();			
		}
	};
	
	public CopyFilePlugin()
	{
		super();
		UIManager.put("ClassLoader", getClass().getClassLoader());
	}
	
	@Override
	public void executeAction(
		List<File> selectedFiles,
		String destinationPath,
		String sourcePath)
	{
		if (selectedFiles.size() == 0)
		{
			JOptionPane.showMessageDialog(
				this.mainWindow,
				I18N.getText("zipFileNoSelectedFiles"),
				I18N.getText("zipFileNoSelectedFilesTitle"),
				JOptionPane.ERROR_MESSAGE
			);
			return;
		}
		this.destinationPath = destinationPath;
		this.sourcePath = sourcePath;
		
		this.worker = new CopyTask(selectedFiles, destinationPath);
		this.worker.addPropertyChangeListener(this);
		this.worker.execute();
		
		
		this.jl.setPreferredSize(new Dimension(480, -1));
		this.pb.setStringPainted(true);
		this.fpb.setStringPainted(true);

		JPanel panel = new JPanel(new GridLayout(4, 1));
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panel.add(this.jl);
		panel.add(this.fpb);
		panel.add(this.pb);
		panel.add(new JButton(this.exitAction));

		this.progressDialog.add(panel);
		this.progressDialog.pack();
		this.progressDialog.setLocationRelativeTo(this.mainWindow);
		this.progressDialog.setResizable(false);
		this.progressDialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.progressDialog.addWindowListener(
			new WindowAdapter()
			{
				@Override
				 public void windowClosing(java.awt.event.WindowEvent arg0)
				{
					CopyFilePlugin.this.exitAction.actionPerformed(null);
				}
			}
		);
		
		this.progressDialog.setVisible(true);
	}
	
	@Override
	public String getName() {
		return "copy";
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt)
	{
		if (evt.getPropertyName() == "state" && evt.getNewValue() == StateValue.STARTED)
		{
			this.pb.setValue(0);
		}
		if (evt.getPropertyName() == "copyFile")
		{
			this.jl.setText(I18N.getText("fileCopying") + " " + evt.getNewValue().toString().substring(this.sourcePath.length() + 1));
			this.fpb.setValue(0);
		}
		if (evt.getPropertyName() == "progress")
		{
			this.pb.setValue((int) evt.getNewValue());
		}
		if (evt.getPropertyName() == "done")
		{
			this.sendEvent(destinationPath);
			this.exitAction.actionPerformed(null);
		}
		if (evt.getPropertyName() == "copyFileProgress")
		{
			this.fpb.setValue((int) evt.getNewValue());
		}
	}

	private class CopyTask extends SwingWorker<Void, Void>
	{
		private List<File> selectedFiles;
		private String destinationPath;
		protected int filesCount = 0, filesProcessed = 0;
		
		private CopyTask(
			List<File> selectedFiles,
			String destinationPath
		)
		{
			this.selectedFiles = selectedFiles;
			this.destinationPath = destinationPath;	
			this.filesCount = selectedFiles.size();
		}
		
		private int countFiles(List<File> selectedFiles2)
		{
			int ret = 0;
			
			for (File file : selectedFiles2)
			{
				if (file.isFile())
				{
					ret++;
				}
				else if (file.isDirectory())
				{
					try
					{
						ret += 1 + this.countFiles(file);
					}
					catch (NotDirectoryException ignore) {};
				}
			}
			
			return ret;
		}
		
		private int countFiles(File directory) throws NotDirectoryException
		{
			int ret = 0;
			
			if (directory.isDirectory())
			{
				File[] content = directory.listFiles();
				
				for (File item : content)
				{
					if (item.isFile())
					{
						ret++;
					}
					else if (item.isDirectory())
					{
						ret += 1 + this.countFiles(item);
					}
				}
			}
			else  throw new NotDirectoryException(directory.toString());
			
			return ret;
		}

		@Override
		protected Void doInBackground() throws Exception
		{
			this.filesCount = this.countFiles(this.selectedFiles);

			for (File file : selectedFiles)
			{
				final Path source = file.toPath();
				final Path target = new File(this.destinationPath + File.separator + file.getName()).toPath();
				
				Files.walkFileTree(
					source,
					EnumSet.of(FileVisitOption.FOLLOW_LINKS),
					Integer.MAX_VALUE,
					new SimpleFileVisitor<Path>()
					{
						private boolean overwriteAll = false;
						
						@Override
			            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
			                throws IOException
			            {
			                Path targetdir = target.resolve(source.relativize(dir));
			                try {
			                	Files.copy(dir, targetdir);
			                } catch (FileAlreadyExistsException e) {
			                     if (!Files.isDirectory(targetdir))
			                         throw e;
			                }
			                return FileVisitResult.CONTINUE;
			            }
						
			            @Override
			            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
			                throws IOException
			            {
			            	CopyTask.this.firePropertyChange("copyFile", null, file);
			            	if (target.resolve(source.relativize(file)).toFile().exists() && !this.overwriteAll)
			            	{
			    				int res = JOptionPane.showConfirmDialog(
			    						CopyFilePlugin.this.mainWindow,
			    						I18N.getText("overwriteFile", file),
			    						I18N.getText("overwriteFileTitle"),
			    						JOptionPane.YES_NO_CANCEL_OPTION
			    				);
			    				if (res == JOptionPane.NO_OPTION)
			    				{
			    					return FileVisitResult.CONTINUE;
			    				}
			    				else if (res == JOptionPane.CANCEL_OPTION)
			    				{
			    					return FileVisitResult.TERMINATE;
			    				}
			    				// TODO add overwrite always option
			            	}
			            	
			            	// custom copy used in order to be able to interrupt process by canceling the Worker thread
			            	copy(file);
			            	
			                CopyTask.this.setProgress((int) (((float) ++CopyTask.this.filesProcessed) / CopyTask.this.filesCount * 100));
			                return FileVisitResult.CONTINUE;
			            }

			            /**
			             * Copies file denoted by the Path argument to the directory stored in the current <code>target<code> property.
			             * @param file
			             * @throws IOException
			             */
						private void copy(Path file) throws IOException {
							File in = file.toFile();
			            	try
			            	(
			            		FileInputStream fin = new FileInputStream(in); 
				                FileChannel finCh = fin.getChannel();
			            		FileOutputStream fout = new FileOutputStream(target.resolve(source.relativize(file)).toFile());
				                FileChannel foutCh = fout.getChannel();
			            	)
			            	{
				                ByteBuffer buff = ByteBuffer.allocate(1048576);
				                int progress = 0;
				                float chunk = ((float) in.length() / 1000);
				                int descreteProgress = 0;
				                while (finCh.read(buff) != -1 || buff.position() > 0) {
				                    buff.flip();
				                    progress += foutCh.write(buff);
				                    if (progress - descreteProgress > chunk)
				                    {
				                    	descreteProgress = progress - (int)(progress % chunk);
				                    	CopyTask.this.firePropertyChange("copyFileProgress", (int) (((float) descreteProgress-chunk)/in.length() * 100), (int) (((float) descreteProgress)/in.length() * 100));
				                    }
				                    buff.compact();
				                    
				                    // flushing is required for progress indication
				                    foutCh.force(true);
				                }
			            	}
			            	catch (FileNotFoundException ex)
			            	{
			            		ex.printStackTrace();
			            	}
						}
					}
				);
			}
			
			this.firePropertyChange("done", false, true);
			return null;
		}
	}
}
