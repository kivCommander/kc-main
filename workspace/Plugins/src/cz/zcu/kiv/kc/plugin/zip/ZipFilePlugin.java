package cz.zcu.kiv.kc.plugin.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import cz.zcu.kiv.kc.plugin.AbstractPlugin;

public class ZipFilePlugin extends AbstractPlugin {

	@Override
	public void executeAction(List<File> selectedFiles, String destinationPath,
			String sourcePath) {
		try {
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
		}
	}

	@Override
	public String getName() {
		return "Zip";
	}

}
