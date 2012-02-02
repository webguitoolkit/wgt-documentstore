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
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;
import org.webguitoolkit.messagebox.MessageBox;

public abstract class DocumentRepositoryTest extends TestCase {

	protected String AFILE = "afile.txt";

	protected IDocumentRepository REPO;

	public abstract String getSeperator();

	@Test
	public void testCreation() throws Exception {

		IDirectory dir = REPO.getRoot();
		long ts = System.currentTimeMillis();
		assertNotNull("dir must be not null", dir);
		assertTrue("name must be not null", dir.getName() != null);
		assertTrue("path must be not null", dir.getRelativeUri() != null);
	}

	@Test
	public void testMkdir() throws Exception {

		IDirectory root = REPO.getRoot();
		IDirectory newdir = root.createDirectory("dir1");

		assertEquals("dir1", newdir.getName());
		assertTrue("wrong uri = " + newdir.getRelativeUri(), newdir
				.getRelativeUri().endsWith("dir1"));
		String name = "file-" + System.currentTimeMillis() + ".txt";
		IFile newfile = newdir.createFile(name, "", false);
		assertTrue(newfile.getName().equals(name));
	}

	@Test
	public void testNotification() throws Exception {

		IDirectory root = REPO.getRoot();
		IDirectory newdir = root.createDirectory("dir2");
		assertTrue(newdir.getAbsoluteUri().endsWith("dir2"));
		String peter = "peter";
		newdir.subscribe(peter);
		assertTrue(newdir.subscriptions().size() == 1);
		MessageBox mb = REPO.getMessageBox();
		int size = 0;
		if (mb.getChannel("myfile").receive(false) != null)
			size = mb.getChannel("myfile").receive(false).size();
		IFile newfile = newdir.createFile("file-" + System.currentTimeMillis()
				+ ".txt", "", false);
		assertNotNull(newfile);
		newfile.writeContent("A tiny green rosetta makes a muffin betta");
		int newsize = mb.getChannel("myfile").receive(false).size();
		assertTrue(size == newsize);
		newdir.unsubscribe(peter);
		assertTrue(newdir.subscriptions().size() == 0);
	}

	@Test
	public void testWriteReadFiles() throws Exception {

		IDirectory root = REPO.getRoot();
		IDirectory newdir = root.createDirectory("dir2");
		IFile newfile = newdir.createFile("file-" + System.currentTimeMillis()
				+ ".txt", "", false);
		String TEXT = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		newfile.writeContent(TEXT);
		InputStream stream = newfile.getContent();
		byte[] b = new byte[TEXT.length()];
		stream.read(b, 0, TEXT.length());
		assertTrue(b[0] == 'A');
		assertTrue(b[TEXT.length() - 1] == 'Z');
		stream.close();
	}

	@Test
	public void testBrowse() throws Exception {

		IDirectory root = REPO.getRoot();

		assertNotNull("dir must not be null", root);
		IDirectory dir = root.createDirectory("xxx");
		List<IDirectory> directories = root.getDirectories();
		assertNotNull("subdirs must be not null", directories);
		assertTrue(directories.size() > 0);
		dir.createFile("yyy", "", false);
		List<IFile> files = dir.getFiles();
		assertNotNull("files must not be NULL", files);
		assertTrue("files must be not be empty", files.size() > 0);
		for (IFile file : files) {
			assertNotNull("file.name must not be NULL", file.getName());
			assertTrue("file.size >= 0", file.getSize() >= 0);
			assertNotNull("file.path must be not null", file.getRelativeUri());
		}
	}

	@Test
	public void testRemoveFile() throws Exception {

		IDirectory root = REPO.getRoot();
		IFile newfile = root.createFile(AFILE, "", false);
		assertNotNull(newfile);
		newfile.writeContent("1234567890");
		assertTrue(newfile.getSize() == 10);
		newfile.remove();
		newfile = root.createFile(AFILE, "", false);
		assertNotNull(newfile);
		assertTrue(newfile.getSize() == 0);
		newfile.remove();
	}

	@Test
	public void testRemoveDir() throws Exception {

		IDirectory root = REPO.getRoot();
		assertNotNull("dir must not be null", root);
		root.createDirectory("xxx");
		List<IDirectory> directories = root.getDirectories();
		assertNotNull("subdirs must be not null", directories);
		assertTrue(directories.size() > 0);
		IDirectory d = directories.get(0);
		assertNotNull("d must not null", d);
		int i = d.getDirectories().size();
		String dirname = "newdir" + System.currentTimeMillis();
		IDirectory newDir = d.createDirectory(dirname);

		// Unfortunately this sleep() does not really help - needs to be
		// investigated in detail
		//
		// // Thread.sleep(5000); // MH: Added this line because of timing
		// problems!
		//
		// directories = d.getDirectories();
		// for (IDirectory directory : directories) {
		// System.out.println(directory.getPath() + " - " + directory.getName()
		// );
		// }
		//
		// assertEquals("must be one more dir", i + 1,
		// d.getDirectories().size());

		// d.getDirectories().get(0).remove();
		// assertTrue( d.getDirectories().size() == i );
	}

	@Test
	public void testRemoveDirError() throws Exception {

		IDirectory root = REPO.getRoot();
		String name = "dir-" + System.currentTimeMillis();
		IDirectory dir = root.createDirectory(name);
		IFile fi = dir.createFile(AFILE, "", false);
		assertFalse(dir.remove(false));
		fi.remove();
		dir.remove(true);
	}

	@Test
	public void testFileproperties() throws Exception {

		IDirectory root = REPO.getRoot();
		String name = "file-" + System.currentTimeMillis() + ".txt";
		IFile f = root.createFile(name, "", false);
		assertNotNull(f);
		assertTrue(f.getLastModified() != 0);
		assertEquals(".txt", f.getExtension());
		assertEquals(name, f.getName());
	}

	@Test
	public void testFileMove() throws Exception {

		IDirectory root = REPO.getRoot();
		String name = "mvfile-" + System.currentTimeMillis() + ".txt";
		IFile f = root.createFile(name, "", false);
		assertNotNull(f);
		f.writeContent("The further we stretch the higher the sky");
		IDirectory subdir = root.createDirectory("sub");
		f.moveTo(subdir);
	}

	@Test
	public void testFileCopy() throws Exception {

		IDirectory root = REPO.getRoot();
		String name = "cpfile-" + System.currentTimeMillis() + ".txt";
		IFile f = root.createFile(name, "", false);
		assertNotNull(f);
		f.writeContent("The further we stretch the higher the sky");
		IDirectory subdir = root.createDirectory("sub");
		f.copyTo(subdir, false);
	}

	@Test
	public void testFileRename() throws Exception {

		IDirectory root = REPO.getRoot();
		String name1 = "rnfile-" + System.currentTimeMillis();
		IFile txt_file = root.createFile(name1 + ".txt", "", false);
		txt_file.writeContent(name1 + ".txt");
		IFile xxl_file = root.createFile(name1 + ".xxl", "", false);
		assertNotNull(xxl_file);

		// assertFalse("false expected ", txt_file.rename(name1 + ".xxl"));

		assertTrue("false expected ", txt_file.rename(name1 + ".doc"));

		assertTrue("wrong name ", txt_file.getName().equals(name1 + ".doc"));

		assertTrue("wrong extension", txt_file.getExtension().equals(".doc"));
	}

	@Test
	public void testDirectoryRename() throws Exception {

		IDirectory root = REPO.getRoot();
		String name1 = "dir1-" + System.currentTimeMillis();
		IDirectory dir1 = root.createDirectory(name1);

		String name2 = "dir2-" + System.currentTimeMillis();
		IDirectory dir2 = root.createDirectory(name2);

		// assertFalse("false expected", dir1.rename(name2));

		String name3 = "dir3-" + System.currentTimeMillis();

		assertTrue(dir1.rename(name3));
		assertTrue("", dir1.getName().equals(name3));

	}

	@Test
	public void testExists() throws Exception {
		// assertNotNull(dir1);
		// assertEquals(0, dir1.getSize());
		// IFile touch = dir1.touch(AFILE);
		// assertEquals(1, dir1.getSize());
		// assertEquals(0, touch.getSize());
		// touch.writeContent("0123456789");
		// assertEquals(10, touch.getSize());
	}

	@Test
	public void testSize() throws Exception {

		IDirectory root = REPO.getRoot();
		String name1 = "emptydir-" + System.currentTimeMillis();
		IDirectory dir1 = root.createDirectory(name1);
		assertNotNull(dir1);
		assertEquals(0, dir1.getSize());
		dir1.createDirectory("bla");

		IFile touch = dir1.createFile(AFILE, "", false);

		// TODO: Timo in KM muss nach einem Touch die size
		// hochgezählt werden bzw. die Resource neu geladen werden
		// eventuell refresh funktion.....
		// assertEquals(1, dir1.getSize());
		assertEquals(0, touch.getSize());
		touch.writeContent("0123456789");
		assertEquals(10, touch.getSize());
	}

	// public void testProperties() throws Exception {

	// PZ: no properties in the first version

	// IDirectory root = REPO.getRoot();
	// String name1 = "resdir-" + System.currentTimeMillis();
	// IDirectory dir1 = root.mkDirectory(name1);
	// Properties properties = dir1.getProperties();
	// assertNotNull(properties);
	// System.out.println(properties);
	// IFile touch = dir1.touch(AFILE);
	// assertEquals(1, dir1.getSize());
	// assertEquals(0, touch.getSize());
	// touch.writeContent("0123456789");
	// properties = touch.getProperties();
	// assertNotNull(properties);
	// System.out.println(properties);

	// }

	@Test
	public void testDirectAccess() throws Exception {

		IDirectory root = REPO.getRoot();
		String name1 = "directdir-" + System.currentTimeMillis();
		IDirectory dir1 = root.createDirectory(name1);
		String name2 = "sub";
		IDirectory dir2 = root.createDirectory(name2);
		IDirectory resource = (IDirectory) REPO.getResource(getSeperator()
				+ name1);
		assertNotNull(resource);

		assertEquals("Path : ", dir1.getRelativeUri(),
				resource.getRelativeUri());

		IFile touch = dir2.createFile(AFILE, "", false);
		assertNotNull(touch);
		touch.writeContent("0123456789");

		IFile file = (IFile) REPO.getResource(touch.getRelativeUri());
		assertNotNull(file);

	}

	@Test
	public void testNames() throws Exception {

		IDirectory root = REPO.getRoot();

		IDirectory dir = root.createDirectory("thedir");

		assertEquals("thedir", dir.getName());

		IFile file = dir.createFile("thefile1.txt", "", false);

		assertEquals("thefile1.txt", file.getName());
		String NAME = getSeperator() + "thedir" + getSeperator()
				+ "thefile1.txt";
		assertEquals(root.getAbsoluteUri() + NAME, file.getAbsoluteUri());
		assertEquals(NAME, file.getRelativeUri());
		assertEquals(".txt", file.getExtension());
		assertEquals(getSeperator() + "thedir", dir.getRelativeUri());

		file.remove();
		dir.remove(true);
	}
}
