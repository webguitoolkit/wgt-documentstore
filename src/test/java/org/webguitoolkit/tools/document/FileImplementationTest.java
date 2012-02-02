/*
Copyright 2008 Endress+Hauser Infoserve GmbH&Co KG 
Licensed under the Apache License, Version 2.0 (the "License"); 
you may not use this file except in compliance with the License. 
You may obtain a copy of the License at 

http://www.apache.org/licenses/LICENSE-2.0 

Unless required by applicable law or agreed to in writing, software 
distributed under the License is distributed on an "AS IS" BASIS, 
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
implied. See the License for the specific language governing permissions 
and limitations under the License.
 */
package org.webguitoolkit.tools.document;

import java.io.File;
import java.util.Properties;

import org.webguitoolkit.tools.document.impl.CredentialsAccessManager;
import org.webguitoolkit.tools.document.impl.file.FileDocumentRepository;

/**
 * Testing the functionality of a File based document store. The Tests a
 * implemented in the class DocumentRepositoryTest and here we just initialize
 * the file document store and a file based message box.
 * 
 * @author peter@17sprints.de
 * 
 */

public class FileImplementationTest extends DocumentRepositoryTest {

	private static final String ROOT = "/temp/documentstore";
	private static final String PATH = "/repository";
	private static final String SC = File.separatorChar + "";

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		Properties properties = new Properties();
		properties.put("file.myfile.location", "/temp/messages/test1");
		properties.put("file.myfile.owner", "Peter");
		properties.put("file.yourfile.location", "/temp/messages/test2");
		properties.put("file.yourfile.owner", "Paul");

		REPO = new FileDocumentRepository(ROOT, PATH,
				new CredentialsAccessManager("peter", "demo"), properties);

		new File(ROOT + PATH).mkdirs();
		new File(ROOT + PATH + "/Folder1").mkdir();
		new File(ROOT + PATH + "/Folder2").mkdir();
		new File(ROOT + PATH + "/Folder2/file1.txt").createNewFile();
		new File(ROOT + PATH + "/Folder2/file2.txt").createNewFile();
		new File(ROOT + PATH + "/tobemoved.txt").createNewFile();
		new File(ROOT + PATH + "/toberenamed.txt").createNewFile();
		new File(ROOT + PATH + "/newfile.txt").createNewFile();
	}

	@Override
	protected void tearDown() throws Exception {
		deleteAllFiles(new File(ROOT + PATH));
		super.tearDown();
	}

	private static boolean deleteAllFiles(File dir) {
		if (!dir.exists()) {
			return true;
		}
		boolean res = true;
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			for (int i = 0; i < files.length; i++) {
				res &= deleteAllFiles(files[i]);
			}
			res = dir.delete();// Delete dir itself
		} else {
			res = dir.delete();
		}
		return res;
	}

	@Override
	public String getSeperator() {
		return SC;
	}
}
