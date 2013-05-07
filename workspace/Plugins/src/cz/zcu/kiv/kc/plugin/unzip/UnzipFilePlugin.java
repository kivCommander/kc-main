package cz.zcu.kiv.kc.plugin.unzip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import cz.zcu.kiv.kc.plugin.AbstractPlugin;

public class UnzipFilePlugin extends AbstractPlugin {

	public UnzipFilePlugin()
	{
		super();
		UIManager.put("ClassLoader", getClass().getClassLoader());
	}
	
	@Override
	public void executeAction(List<File> selectedFiles, String destinationPath,
			String sourcePath) {
		for (File file : selectedFiles) {

			destinationPath = JOptionPane.showInputDialog(
				this.mainWindow,
				"Zadejte cílový adresáø pro dekompresi.",
				destinationPath
				+ (destinationPath.endsWith(File.separator) ? "" : File.separator)
				+ file.getName().substring(0, file.getName().lastIndexOf('.'))
			);
			if (destinationPath == null || destinationPath.trim().isEmpty())
			{
				JOptionPane.showMessageDialog(
					this.mainWindow,
					"Operace byla zrušen auživatelem."
				);
				return;
			}
			
			byte[] buffer = new byte[1024];
			try (ZipInputStream zis = new ZipInputStream(new FileInputStream(file)))
			{
				ZipEntry entry;
				while ((entry = zis.getNextEntry()) != null)
				{
					System.out.println(entry.getName() + " isDir: " + entry.isDirectory());
					File newFile = new File(destinationPath + File.separator + entry.getName());
					if (entry.isDirectory())
					{ // entry is directory, create and continue with other entry
						newFile.mkdirs();
					}
					else
					{ // entry is file, create parent directories and write content
						new File(newFile.getParent()).mkdirs();
						try (FileOutputStream fos = new FileOutputStream(newFile))
						{
							int len;
							while ((len = zis.read(buffer)) > 0)
							{
								fos.write(buffer, 0, len);
							}
						}
						catch (FileNotFoundException ex)
						{
							System.out.println("chyba zapisu: " + ex.getMessage());
						}
					}
					
					// close zip entry
					zis.closeEntry();
				}
			}
			catch (FileNotFoundException ex)
			{
				System.out.println("chyba cteni");
			}
			catch (IOException ex)
			{
				System.out.println("Neznama vstupne/vystupni chyba.");
			}
			sendEvent(destinationPath);
			
			/*try {
				// get the zip file content
				ZipInputStream zis = new ZipInputStream(new FileInputStream(
						file));
				// get the zipped file list entry
				ZipEntry ze = zis.getNextEntry();

				while (ze != null) {

					String fileName = ze.getName();
					File newFile = new File(destinationPath + File.separator
							+ fileName);
					// create all non exists folders
					// else you will hit FileNotFoundException for compressed
					// folder
					new File(newFile.getParent()).mkdirs();

					FileOutputStream fos = new FileOutputStream(newFile);

					int len;
					while ((len = zis.read(buffer)) > 0) {
						fos.write(buffer, 0, len);
					}

					fos.close();
					ze = zis.getNextEntry();
				}

				zis.closeEntry();
				zis.close();
				sendEvent(destinationPath);

			} catch (IOException ex) {
				ex.printStackTrace();
			}*/
		}
	}

	@Override
	public String getName() {
		return "Unzip";
	}

}
