package cz.zcu.kiv.kc.plugin.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.JOptionPane;

import cz.zcu.kiv.kc.plugin.AbstractPlugin;

public class ZipFilePlugin extends AbstractPlugin {

	@Override
	public void executeAction(List<File> selectedFiles, String destinationPath,
			String sourcePath) {
		exec(selectedFiles, destinationPath, sourcePath);
		return;
		/*try {
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
					destinationPath + File.separator + askForName("Zip fiile name:")
							+ ".zip"));
			for (File file : selectedFiles) {
				FileInputStream in = new FileInputStream(file);
				// out put file
				// name the file inside the zip file
				out.putNextEntry(new ZipEntry(file.getName()));

				byte[] b = new byte[1024];

				int count;

				while ((count = in.read(b)) > 0) {
					out.write(b, 0, count);
				}
				in.close();
			}
			out.close();
			sendEvent(destinationPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

	public void exec(
		List<File> selectedFiles,
		String destinationPath,
		String sourcePath)
	{
		// create hint for destination filename
		String destinationFileNameHint = destinationPath
			+ (!new File(sourcePath).getName().trim().isEmpty() ? File.separatorChar : "")
			+ new File(sourcePath).getName()
			+ ".zip";
		// ask for actual destination filename
		String destinationFileName = JOptionPane.showInputDialog(
			this.mainWindow,
			"Zadej jméno výstupního souboru",
			destinationFileNameHint
		);
		if (destinationFileName == null)
		{ // operation canceled
			JOptionPane.showMessageDialog(
				this.mainWindow,
				"Operace byla zrušena uživatelem.",
				"Operace zrušena.",
				JOptionPane.INFORMATION_MESSAGE
			);
			return;
		}
		if (destinationFileName.isEmpty())
		{ // empty name given
			JOptionPane.showMessageDialog(
				this.mainWindow,
				"Nesprávné zadání. Operace byla zrušena.",
				"Operace zrušena.",
				JOptionPane.INFORMATION_MESSAGE
			);
			return;
		}
		File destinationFile = new File(destinationFileName);
		if (destinationFile.canRead())
		{
			int res = JOptionPane.showConfirmDialog(
				this.mainWindow,
				"Soubor již existuje. Pøepsat?",
				"Soubor již existuje.",
				JOptionPane.YES_NO_OPTION
			);
			if (res == JOptionPane.NO_OPTION)
			{
				return;
			}
		}
		try (FileOutputStream fout = new FileOutputStream(destinationFile))
		{
			try (ZipOutputStream zout = new ZipOutputStream(fout))
			{
				for (File file : selectedFiles)
				{
					if (file.isDirectory())
					{
		        		String newFolder = file.getName() + "/"; // '/' is defined in zip format
		        		zout.putNextEntry(new ZipEntry(newFolder));
						this.addDirectory(zout, file, newFolder);
					}
					else if (file.isFile())
					{
						this.addFile(zout, file, "");
					}
				}
			}
		}
		catch (FileNotFoundException e)
		{
			JOptionPane.showMessageDialog(
				this.mainWindow,
				"Nepodaøilo se vytvoøit výstupní soubor.",
				"Chyba výstupního souboru.",
				JOptionPane.ERROR_MESSAGE
			);
		} catch (IOException e)
		{
			JOptionPane.showMessageDialog(
				this.mainWindow,
				"Neznámá vstupnì/výstupní chyba.\n" + e.getMessage(),
				"Chyba vstupu/výstupu.",
				JOptionPane.ERROR_MESSAGE
			);
		}
	}
	
    private void addDirectory(ZipOutputStream zout, File directory, String parentFolder) throws IOException
    {
        //get sub-folder/files list
        File[] files = directory.listFiles();
        for (File file : files)
        {
        	if (file.isDirectory())
        	{ // the file is directory, call the function recursively
        		String newFolder = parentFolder + file.getName() + "/"; // '/' is defined in zip format
        		zout.putNextEntry(new ZipEntry(newFolder));
        		this.addDirectory(zout, file, newFolder);
        	}
        	else if (file.isFile())
        	{ // file is file (:-)), just add it
        		this.addFile(zout, file, parentFolder);
        	}
        }
    }
    
	private void addFile(ZipOutputStream zout, File file, String folder) {
		try (FileInputStream fin = new FileInputStream(file))
		{
		    //create byte buffer
		    byte[] buffer = new byte[1024];
		   
		    zout.putNextEntry(new ZipEntry(folder + file.getName()));
		
		    /*
		     * After creating entry in the zip file, actually
		     * write the file.
		     */
		    int length;
		    while((length = fin.read(buffer)) > 0)
		    {
		       zout.write(buffer, 0, length);
		    }
		
		    /*
		     * After writing the file to ZipOutputStream, use
		     *
		     * void closeEntry() method of ZipOutputStream class to
		     * close the current entry and position the stream to
		     * write the next entry.
		     */
		     zout.closeEntry();
		}
		catch(IOException ioe)
		{
		    JOptionPane.showMessageDialog(
		    	this.mainWindow,
		    	"Neznámá vstupnì/výstupní chyba.",
		    	"Error",
		    	JOptionPane.ERROR_MESSAGE
		    );           
		}
	}
	
	@Override
	public String getName() {
		return "Zip";
	}

}
