package cz.zcu.kiv.kc.plugin.show;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileTypeDetector
{

	private enum FileTypes
	{
		IMAGE,
		TEXT
	}
	
	public static final String[][] extensions = new String[FileTypes.values().length][];
	static {
		extensions[FileTypes.TEXT.ordinal()] = new String[] {
			".ini",
			".inf",
			".html",
			".htm",
			".hta",
			".java",
			".c",
			".log",
			".bat",
			".cmd",
			".sql",
			".rdp"
		};
	}
	
	/**
	 * Checks whether the file can be showed. So far only checks extension.
	 * @param pathToFile
	 * @return
	 * @throws IOException
	 */
	public static String probeFile(Path pathToFile) throws IOException
	{
		String ret = null;
		
		ret = Files.probeContentType(pathToFile);
		if (ret != null) return ret;
		
		String fileName = pathToFile.getFileName().toString().toLowerCase();
		
		for (FileTypes type : FileTypes.values())
		{
			if (extensions[type.ordinal()] == null) continue;
			
			for (String extension : extensions[type.ordinal()])
			{
				if (fileName.endsWith(extension))
				{
					ret = type.name().toLowerCase() + "/" + extension.substring(1);
					return ret;
				}
			}
		}
		
		return ret;
	}
}
