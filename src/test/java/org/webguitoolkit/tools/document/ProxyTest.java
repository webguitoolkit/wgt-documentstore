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

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;
import org.webguitoolkit.tools.document.impl.DocumentRepositoryException;

/**
 * Testing the Proxy implementation.
 * 
 * @author peter
 * 
 */
public abstract class ProxyTest extends TestCase {

	protected String ROOT = "http://localhost:8080";
	protected String BASEFOLDER = "/webdav";
	protected String ROOTFOLDER = ROOT + BASEFOLDER;
	protected String BASENAME = "webdav";
	protected IAccessManager accessManager;

	public abstract IStoreProxy newProxy(String url, String path,
			IAccessManager am) throws DocumentRepositoryException;

	public abstract String getSeperator();

	@Test
	public void testCreateResource() throws Exception {
		IStoreProxy root = newProxy(ROOT, BASEFOLDER, accessManager);
		assertNotNull(root);
		assertTrue(root.isCollection());
		assertEquals(ROOTFOLDER, root.getAbsoluteUri());
		assertEquals(BASEFOLDER, root.getRelativeUri());
		assertEquals("", root.getRelativePath());
		assertEquals(BASENAME, root.getName());

		IStoreProxy folder = newProxy(ROOT, BASEFOLDER + getSeperator()
				+ "Folder2", accessManager);
		assertEquals(ROOTFOLDER + getSeperator() + "Folder2",
				folder.getAbsoluteUri());
		assertEquals(BASEFOLDER + getSeperator() + "Folder2",
				folder.getRelativeUri());
		assertEquals(BASEFOLDER, folder.getRelativePath());
		assertEquals("Folder2", folder.getName());

		IStoreProxy file = newProxy(ROOT, BASEFOLDER + getSeperator()
				+ "Folder2" + getSeperator() + "file1.txt", accessManager);
		assertEquals(ROOTFOLDER + getSeperator() + "Folder2" + getSeperator()
				+ "file1.txt", file.getAbsoluteUri());
		assertFalse(file.isCollection());
		assertEquals(BASEFOLDER + getSeperator() + "Folder2" + getSeperator()
				+ "file1.txt", file.getRelativeUri());
		assertEquals(BASEFOLDER + getSeperator() + "Folder2",
				file.getRelativePath());
		// assertTrue("is " + file.getModifiedAt(), file.getModifiedAt() > 0);
	}

	@Test
	public void testListCollection() throws Exception {
		IStoreProxy root = newProxy(ROOT, BASEFOLDER, accessManager);
		assertTrue(root.isCollection());
		List<IStoreProxy> resources = root.getCollections();
		assertNotNull(resources);
		assertFalse(resources.isEmpty());
		for (Iterator<IStoreProxy> iterator = resources.iterator(); iterator
				.hasNext();) {
			IStoreProxy resource = iterator.next();
			assertNotNull(resource.getName());
			assertTrue(resource.getAbsoluteUri().startsWith(ROOTFOLDER));
			assertTrue(resource.isCollection());
		}
	}

	@Test
	public void testListFiles() throws Exception {
		IStoreProxy root = newProxy(ROOT, BASEFOLDER, accessManager);
		assertTrue(root.isCollection());
		List<IStoreProxy> resources = root.getFiles();
		assertNotNull(resources);
		assertFalse(resources.isEmpty());
		for (Iterator<IStoreProxy> iterator = resources.iterator(); iterator
				.hasNext();) {
			IStoreProxy resource = iterator.next();
			assertNotNull(resource.getName());
			assertTrue(resource.getAbsoluteUri().startsWith(ROOTFOLDER));
			assertFalse(resource.isCollection());
			assertTrue("size = " + resource.getSize(), resource.getSize() >= 0);
		}
	}

	@Test
	public void testCreateAndDeleteCollection() throws Exception {
		IStoreProxy root = newProxy(ROOT, BASEFOLDER, accessManager);
		String randomName = "Folder" + System.currentTimeMillis();
		assertTrue(root.createDirectory(randomName));
		IStoreProxy newdir = newProxy(ROOT, BASEFOLDER + getSeperator()
				+ randomName, accessManager);
		assertEquals(ROOTFOLDER + getSeperator() + randomName,
				newdir.getAbsoluteUri());
		assertTrue(newdir.isCollection());
		assertTrue(newdir.remove(true));
	}

	@Test
	public void testCreateAndDeleteNonEmptyCollection() throws Exception {
		IStoreProxy root = newProxy(ROOT, BASEFOLDER, accessManager);
		String randomName = "Folder" + System.currentTimeMillis();
		assertTrue(root.createDirectory(randomName));
		IStoreProxy newdir = newProxy(ROOT, BASEFOLDER + getSeperator()
				+ randomName, accessManager);
		assertEquals(ROOTFOLDER + getSeperator() + randomName,
				newdir.getAbsoluteUri());
		assertTrue(newdir.isCollection());
		// let's put something in the folder
		String TEXT = "this is a file that will be deleted again";
		newdir.createFile("tobedeleted.txt", TEXT, false);
		IStoreProxy file = newProxy(ROOT, BASEFOLDER + getSeperator()
				+ randomName + getSeperator() + "tobedeleted.txt",
				accessManager);
		assertEquals(TEXT.length(), file.getSize());
		// now try to remove the folder
		assertFalse(newdir.remove(false));
	}

	@Test
	public void testCreateAndDeleteFile() throws Exception {
		IStoreProxy folder = newProxy(ROOT, BASEFOLDER + getSeperator()
				+ "Folder1", accessManager);
		String TEXT = "this is a file that will be deleted again";
		folder.createFile("tobedeleted.txt", TEXT, false);
		IStoreProxy file = newProxy(ROOT, BASEFOLDER + getSeperator()
				+ "Folder1" + getSeperator() + "tobedeleted.txt", accessManager);
		assertEquals(TEXT.length(), file.getSize());
		assertTrue(file.remove(false));
	}

	@Test
	public void testRename() throws Exception {
		IStoreProxy renameable = newProxy(ROOT, BASEFOLDER + getSeperator()
				+ "toberenamed.txt", accessManager);

		assertTrue(renameable.rename("renamed.txt"));
		assertEquals("renamed.txt", renameable.getName());
		assertTrue(renameable.rename("toberenamed.txt"));

		renameable = newProxy(ROOT, BASEFOLDER + getSeperator() + "Folder1",
				accessManager);
		assertTrue(renameable.rename("Folder1-renamed"));
		assertEquals("Folder1-renamed", renameable.getName());
		assertTrue(renameable.rename("Folder1"));

		IStoreProxy moveable = newProxy(ROOT, BASEFOLDER + getSeperator()
				+ "tobemoved.txt", accessManager);
		IStoreProxy target = newProxy(ROOT, BASEFOLDER + getSeperator()
				+ "Folder1", accessManager);
		// moveable.rename(BASEFOLDER + getSeperator() + "Folder1" +
		// getSeperator() + "tobemoved.txt");
		// moveable.rename(BASEFOLDER + getSeperator() + "tobemoved.txt");
	}

	@Test
	public void testReadFile() throws Exception {
		IStoreProxy file = newProxy(ROOT, BASEFOLDER + getSeperator()
				+ "file1.txt", accessManager);
		assertEquals("file1.txt", file.getName());
		assertEquals(10, file.getSize());
		InputStream inputStream = file.getContent();
		int size = (int) file.getSize();
		byte[] b = new byte[size];
		inputStream.read(b, 0, size);
		inputStream.close();
		String result = new String(b);
		assertEquals("1234567890", result);

		IStoreProxy folder = newProxy(ROOT, BASEFOLDER, accessManager);
		inputStream = folder.getContent("file1.txt");
		inputStream.read(b, 0, size);
		inputStream.close();
		result = new String(b);
		assertEquals("1234567890", result);

		try {
			folder = newProxy(ROOT, BASEFOLDER, accessManager);
			inputStream = folder.getContent("fileXY.txt");
			assertTrue("Must throw an exception", false);
		} catch (Exception e) {

		}

		try {
			folder = newProxy(ROOT, BASEFOLDER, accessManager);
			inputStream = folder.getContent();

			assertTrue("Must throw an exception", false);
		} catch (Exception e) {

		}

		try {
			file = newProxy(ROOT, BASEFOLDER + getSeperator() + "file1.txt",
					accessManager);
			inputStream = file.getContent("file1.txt");
			assertTrue("Must throw an exception", false);
		} catch (Exception e) {

		}
	}

	@Test
	public void testWriteFile() throws Exception {
		IStoreProxy folder = newProxy(ROOT, BASEFOLDER, accessManager);
		String TEXT = "this is the end";
		folder.createFile("newfile3.txt", TEXT, false);
		IStoreProxy file = newProxy(ROOT, BASEFOLDER + getSeperator()
				+ "newfile3.txt", accessManager);
		assertEquals(TEXT.length(), file.getSize());

		// TEXT = "this is the end my friend";
		// folder.createFile("newfile.txt", new
		// ByteArrayInputStream(TEXT.getBytes()), false);
		// file = newProxy(ROOT, BASEFOLDER + getSeperator() + "newfile.txt",
		// accessManager);
		// assertEquals(TEXT.length(), file.getSize());
	}

	@Test
	public void testWrite() throws Exception {

		IStoreProxy file = newProxy(ROOT, BASEFOLDER + getSeperator()
				+ "newfile.txt", accessManager);
		String TEXT = "here comes the big one";
		file.write(TEXT);
		file = newProxy(ROOT, BASEFOLDER + getSeperator() + "newfile.txt",
				accessManager);
		assertEquals(TEXT.length(), file.getSize());
	}

	@Test
	public void testCreateFolder() throws Exception {
		IStoreProxy folder = newProxy(ROOT, BASEFOLDER, accessManager);
		boolean ok = folder.createDirectory("newfolder");
		IStoreProxy newfolder = newProxy(ROOT, BASEFOLDER + getSeperator()
				+ "newfolder", accessManager);
		newfolder.remove(false);
	}
}
