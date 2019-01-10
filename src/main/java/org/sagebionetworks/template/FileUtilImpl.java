package org.sagebionetworks.template;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URL;

import org.apache.commons.io.FileUtils;

/**
 * Basic implementation of FileUtil.
 *
 */
public class FileUtilImpl implements FileUtil {

	@Override
	public File createTempFile(String prefix, String suffix) throws IOException {
		return File.createTempFile(prefix, suffix);
	}

	@Override
	public File createNewFile(File parent, String childName) {
		return new File(parent, childName);
	}

	@Override
	public Writer createFileWriter(File file) {
		try {
			return new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public File getClassPathResource(String relativePath){
		URL fileUrl = ClassLoader.getSystemClassLoader().getResource(relativePath);
		if (fileUrl == null){
			throw new IllegalArgumentException("The path " + relativePath +  " does not exist on the classpath");
		}
		return new File(fileUrl.getFile());
	}

	@Override
	public void copyDirectory(File srcDir, File destDir){
		try {
			FileUtils.copyDirectory(srcDir, destDir);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
