package org.sagebionetworks.template;

import static junit.framework.TestCase.assertNotNull;

import java.io.File;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

public class FileUtilsTest {
	FileUtil fileUtil;

	@Before
	public void setUp(){
		fileUtil = new FileUtilImpl();
	}

	@Test
	public void testGetClassPathResource_existingFile(){
		//this is working under the assumption that there exists a .ebextensions folder inside of /src/main/resources
		File dir = fileUtil.getClassPathResource(".ebextensions");
		assertNotNull(dir);
	}

	@Test (expected = IllegalArgumentException.class)
	public void testGetClassPathResource_NotExistingFile(){
		//generate a fake relative path that has is very unlikely to exist
		fileUtil.getClassPathResource(UUID.randomUUID().toString());
	}
}
