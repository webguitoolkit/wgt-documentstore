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
package org.webguitoolkit.tools.document.impl.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.webguitoolkit.tools.document.IAccessManager;
import org.webguitoolkit.tools.document.IDocumentRepository;
import org.webguitoolkit.tools.document.IProperty;
import org.webguitoolkit.tools.document.IResource;
import org.webguitoolkit.tools.document.IStoreProxy;
import org.webguitoolkit.tools.document.impl.DocumentRepositoryException;

public class FileProxy implements IStoreProxy {

	private File me;
	private static Log log = LogFactory.getLog(IDocumentRepository.class);
	private IAccessManager accessManager;
	private String root;
	private String path;

	public FileProxy(String root, String path, IAccessManager accessManager) throws DocumentRepositoryException {
		try {
			this.accessManager = accessManager;
			this.root = root;
			this.path = path;
			me = new File(root + path);
		} catch (Exception e) {
			throw new DocumentRepositoryException(e);
		}
	}

	private FileProxy(File file, IAccessManager accessManager) {
		me = file;
		this.accessManager = accessManager;
	}

	public boolean createDirectory(String name) throws Exception {
		File dir = new File(getAbsoluteUri() + File.separatorChar + name);
		dir.mkdir();
		if (!dir.exists())
			throw new RuntimeException("Could not create diretory " + name);
		return true;
	}

	public String getAccessURL() {
		return me.getAbsolutePath();
	}

	public String getAbsoluteUri() {
		return me.getAbsolutePath();
	}

	public IAccessManager getAccessManager() {
		return accessManager;
	}

	public List<IStoreProxy> getCollections() throws Exception {
		File[] list = me.listFiles();
		ArrayList<IStoreProxy> result = new ArrayList<IStoreProxy>();
		for (int i = 0; i < list.length; i++) {
			if (list[i].isDirectory() && accessManager.canRead(list[i].getPath()))
				result.add(new FileProxy(list[i], accessManager));
		}
		return result;
	}

	public List<IStoreProxy> getFiles() throws Exception {
		File[] list = me.listFiles();
		ArrayList<IStoreProxy> result = new ArrayList<IStoreProxy>();
		for (int i = 0; i < list.length; i++) {
			if (!list[i].isDirectory())
				result.add(new FileProxy(list[i], accessManager));
		}
		return result;
	}

	public InputStream getContent() throws Exception {
		if (!isCollection())
			return new FileInputStream(me);
		else
			throw new DocumentRepositoryException(new IllegalStateException("not a file"));
	}

	public InputStream getContent(String name) throws Exception {
		if (isCollection())
			return new FileInputStream(new File(me.getAbsolutePath() + File.separatorChar + name));
		else
			throw new DocumentRepositoryException(new IllegalStateException("not a directory"));
	}

	public String getName() throws Exception {
		return me.getName();
	}

	public String getRelativePath() {
		return path.substring(0, path.lastIndexOf(File.separatorChar));
	}

	public String getRelativeUri() {
		return path;
	}

	public List<IStoreProxy> getResources() throws Exception {
		File[] list = me.listFiles();
		ArrayList<IStoreProxy> result = new ArrayList<IStoreProxy>();
		for (int i = 0; i < list.length; i++) {
			if ((list[i].isDirectory() && accessManager.canRead(list[i].getPath()) || !list[i].isDirectory()))
				result.add(new FileProxy(list[i], accessManager));
		}
		return result;
	}

	public long getSize() throws Exception {
		if (me.isDirectory())
			return me.listFiles().length;
		return me.length();
	}

	public boolean isCollection() {
		return me.isDirectory();
	}

	public boolean remove(boolean recursive) throws Exception {
		return me.delete();
	}

	public boolean removeDirectory(String name, boolean recursive) throws Exception {
		if (recursive)
			new RuntimeException(name + " recursive delete not supported");
		File tobedeleted = new File(me.getAbsolutePath() + File.separatorChar + name);
		if (!tobedeleted.isDirectory())
			throw new RuntimeException(name + " not a folder");
		boolean result = tobedeleted.delete();
		return result;
	}

	public boolean rename(String name) throws Exception {

		String absolutePath = me.getAbsolutePath();
		String newAbsolutePath = absolutePath.substring(0, absolutePath.lastIndexOf(File.separatorChar));

		File newfile = new File(newAbsolutePath + File.separatorChar + name);
		boolean result = me.renameTo(newfile);
		me = newfile;
		return result;

	}

	public void write(String content) throws Exception {
		FileOutputStream toStream = new FileOutputStream(me);
		byte[] buffer = content.getBytes();

		try {
			toStream.write(buffer, 0, buffer.length);
		} finally {
			if (toStream != null)
				toStream.close();
		}

	}

	public void write(InputStream content) throws Exception {
		FileOutputStream toStream = new FileOutputStream(me);
		byte[] buffer = new byte[4096];
		int bytesRead;
		try {
			while ((bytesRead = content.read(buffer)) != -1)
				toStream.write(buffer, 0, bytesRead); // write
		} finally {
			if (toStream != null)
				toStream.close();
			if (content != null)
				content.close();
		}

	}

	public void createFile(String name, String content, boolean overwrite) throws Exception {
		FileOutputStream toStream = new FileOutputStream(new File(me.getAbsolutePath() + File.separatorChar + name));
		byte[] buffer = content.getBytes();

		try {
			toStream.write(buffer, 0, buffer.length);
		} finally {
			if (toStream != null)
				toStream.close();
		}
	}

	public void createFile(String name, InputStream content, boolean overwrite) throws Exception {
		FileOutputStream toStream = new FileOutputStream(new File(me.getAbsolutePath() + File.separatorChar + name));
		byte[] buffer = new byte[4096];
		int bytesRead;
		try {
			while ((bytesRead = content.read(buffer)) != -1)
				toStream.write(buffer, 0, bytesRead); // write
		} finally {
			if (toStream != null)
				toStream.close();
			if (content != null)
				content.close();
		}
	}

	public boolean exists() {
		return me.exists();
	}

	public String getServerBaseUrl() {
		return root;
	}

	public boolean isLocked() {
		return me.canWrite();
	}

	public boolean copyTo(String absoluteUri, boolean deep) throws Exception {
		FileReader in = null;
		FileWriter out = null;
		try {
			File outputFile = new File(absoluteUri);

			in = new FileReader(me);
			out = new FileWriter(outputFile);
			int c;

			while ((c = in.read()) != -1)
				out.write(c);
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			if (in != null)
				in.close();
			if (out != null)
				out.close();
		}
	}

	public boolean copyTo(IResource[] resources, String absoluteUri, boolean deep) throws Exception {
		throw new RuntimeException("NOT IMPLEMENTED");
	}

	public boolean moveTo(String relativeUri) {
		File newloc = new File(getServerBaseUrl() + relativeUri);
		me.renameTo(newloc);
		me = newloc;
		path = relativeUri;

		return true;
	}

	public long getCreated() throws Exception {
		// NOT IMPLEMENTED
		return 0;
	}

	public String getCreatedBy() throws Exception {
		// NOT IMPLEMENTED
		return null;
	}

	public String getDescription() throws Exception {
		// NOT IMPLEMENTED
		return null;
	}

	public long getLastModified() throws Exception {
		return me.lastModified();
	}

	public String getLastModifiedBy() throws Exception {
		// NOT IMPLEMENTED
		return null;
	}

	public boolean lock() throws Exception {
		// NOT IMPLEMENTED
		return false;
	}

	public boolean lock(IResource[] resources) throws Exception {
		// NOT IMPLEMENTED
		return false;
	}

	public boolean moveTo(IResource[] resources, String absoluteUri) {
		// NOT IMPLEMENTED
		return false;
	}

	public boolean remove(IResource[] resources) throws Exception {
		// NOT IMPLEMENTED
		return false;
	}

	public boolean unlock() throws Exception {
		// NOT IMPLEMENTED
		return false;
	}

	public boolean unlock(IResource[] resources) throws Exception {
		// NOT IMPLEMENTED
		return false;
	}

	// rate resource
	public boolean rate(int a) throws DocumentRepositoryException {
		throw new RuntimeException("Not implemented");
	}

	// get rating average of resource
	public int getRatingAverage() throws DocumentRepositoryException {
		throw new RuntimeException("Not implemented");
	}

	// get rating of user
	public int getRatingOfUser() throws DocumentRepositoryException {
		throw new RuntimeException("Not implemented");
	}

	public boolean addFeedback(String feedbackTxt, boolean notifyOwner) throws Exception {
		throw new RuntimeException("Not implemented");
	}

	public List<String[]> getFeedback() throws Exception {
		throw new RuntimeException("Not implemented");
	}

	public int getFeedbackCount() throws Exception {
		throw new RuntimeException("Not implemented");
	}

	public int getRatingCount() throws Exception {
		throw new RuntimeException("Not implemented");
	}

	public String getEditedBy() throws Exception {
		throw new RuntimeException("Not implemented");
	}

	public String getLockedBy() throws Exception {
		throw new RuntimeException("Not implemented");
	}

	public boolean canChangePermission() throws Exception {
		throw new RuntimeException("Not implemented");
	}

	public boolean canDelete() throws Exception {
		throw new RuntimeException("Not implemented");
	}

	public boolean canRead() throws Exception {
		throw new RuntimeException("Not implemented");
	}

	public boolean canWrite() throws Exception {
		throw new RuntimeException("Not implemented");
	}

	public List<IProperty> getProperties(String propertyGroupId) throws Exception {
		throw new RuntimeException("Not implemented");
	}

	public boolean reload() throws Exception {
		throw new RuntimeException("Not implemented");
	}

	public boolean setProperties(String description, List<IProperty> propertyList) throws Exception {
		throw new RuntimeException("Not implemented");
	}

	public boolean endEditContent(String content) throws Exception {
		throw new RuntimeException("Not implemented");
	}

	public boolean endEditContent(InputStream content) throws Exception {
		throw new RuntimeException("Not implemented");
	}

	public boolean startEditContent() throws Exception {
		throw new RuntimeException("Not implemented");
	}

	public String getContentSnippet() throws Exception {
		throw new RuntimeException("Not implemented");
	}

	public String getEditedByName() throws Exception {
		throw new RuntimeException("Not implemented");
	}

	public String getLockedByName() throws Exception {
		throw new RuntimeException("Not implemented");
	}

	public String getParentForumNameOfMessage() {
		throw new RuntimeException("Not implemented");
	}

	public boolean isForumsMessage() {
		throw new RuntimeException("Not implemented");
	}
	
	public String getConstantURL() {
		return null;
	}
}