package org.sagebionetworks.template;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

public interface FileUtil {
	
	/**
	 * Create a temp file.
	 * @param prefix
	 * @param suffix
	 * @return
	 * @throws IOException 
	 */
	File createTempFile(String prefix, String suffix) throws IOException;

	/**
	 * Create a new file with the given parent.
	 * @param parent
	 * @param fileName
	 * @return
	 */
	File createNewFile(File parent, String fileName);
	
	/**
	 * Create a 'UTF-8' file writer for the passed file.
	 * @param parent
	 * @param fileName
	 * @return
	 */
	Writer createFileWriter(File file);


	/**
	 * Gets a file or directory in the src/main/resources directory
	 * @param relativePath the relative path of the file or directory from src/main/resources
	 * @return absolute File path of the file or directory
	 */
	File getClassPathResource(String relativePath);

	/**
	 * Copies a whole directory to a new location preserving the file dates.
	 * @see org.apache.commons.io.FileUtils#copyDirectory(File, File)
	 * @param srcDir source directory of files to be copied
	 * @param destDir destination directory to which the files will be copied
	 */
	void copyDirectory(File srcDir, File destDir);
}
