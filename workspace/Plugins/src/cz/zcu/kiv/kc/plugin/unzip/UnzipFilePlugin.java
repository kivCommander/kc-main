package cz.zcu.kiv.kc.plugin.unzip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import cz.zcu.kiv.kc.plugin.AbstractPlugin;

public class UnzipFilePlugin extends AbstractPlugin {

	@Override
	public void executeAction(List<File> selectedFiles, String destinationPath,
			String sourcePath) {
		for (File file : selectedFiles) {

			byte[] buffer = new byte[1024];
			try {
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
			}
		}
	}

	@Override
	public String getName() {
		return "Unzip";
	}

}
