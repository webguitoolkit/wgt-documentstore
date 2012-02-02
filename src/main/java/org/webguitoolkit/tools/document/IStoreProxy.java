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

/**
 * This interface is intended to provide some abstract implementations of the
 * IFile and IDirectory interfaces. <br>
 * An implementation for e.g. file system or WebDAV can be handled here. The aim
 * is to keep the implementation for a concreted storage as simple as possible.
 * Usually it should be sufficient to implement the IStoreProxy and some very
 * small classes (mainly a constructor) for Directory and File to adapt to a new
 * storage type like SAP KM.
 * 
 * @author peter
 * 
 */
public interface IStoreProxy {

	/**
	 * @return the full path like
	 *         "http://www.server.com:8080/webdav/folder/file.txt"
	 */
	public String getAbsoluteUri();

	public String getAccessURL();

	/**
	 * 
	 * @return the path to the resource. If the complete path is
	 *         "http://www.server.com:8080/webdav/folder/file.txt" it will return
	 *         "/folder/file.txt"
	 */
	public String getRelativeUri();

	/**
	 * @return the path to the resource. If the complete path is
	 *         "http://www.server.com:8080/webdav/folder/file.txt" it will return
	 *         "/folder"
	 */
	public String getRelativePath();

	/**
	 * @return the resource name
	 * @throws Exception
	 */
	public String getName() throws Exception;

	/**
	 * @return true if resource is a folder
	 */
	public boolean isCollection();

	/**
	 * @return just the files in a folder
	 * @throws Exception
	 */
	public List<IStoreProxy> getFiles() throws Exception;

	/**
	 * @return just the sub-folders in a folder
	 * @throws Exception
	 */
	public List<IStoreProxy> getCollections() throws Exception;

	/**
	 * @return files and folder
	 * @throws Exception
	 */
	public List<IStoreProxy> getResources() throws Exception;

	/**
	 * Create a new folder
	 * 
	 * @param name
	 *          folder name
	 * @return true if successful
	 * @throws Exception
	 */
	public boolean createDirectory(String name) throws Exception;

	/**
	 * Remove a folder and all its content
	 * 
	 * @param name
	 *          folders name
	 * @return true if a success
	 * @throws Exception
	 */
	public boolean removeDirectory(String name, boolean recursive) throws Exception;

	/**
	 * Remove this resource
	 * 
	 * @return true if a success
	 * @throws Exception
	 */
	public boolean remove(boolean recursive) throws Exception;

	/**
	 * Remove all subresources
	 * 
	 * @param resources
	 *          to be removed
	 * @return true in case of success otherwise false
	 * @throws Exception
	 */
	public boolean remove(IResource[] resources) throws Exception;

	/**
	 * Rename a resource
	 * 
	 * @param name
	 *          new name
	 * @return true if a success
	 * @throws Exception
	 */
	public boolean rename(String name) throws Exception;

	/**
	 * Fetch the content of the resource
	 * 
	 * @return InputStream of content
	 * @throws Exception
	 */
	public InputStream getContent() throws Exception;

	/**
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public InputStream getContent(String name) throws Exception;

	/**
	 * 
	 * @param name
	 * @param content
	 * @throws Exception
	 */
	public void createFile(String name, String content, boolean overwrite) throws Exception;

	/**
	 * 
	 * @param name
	 * @param content
	 * @throws Exception
	 */
	public void createFile(String name, InputStream content, boolean overwrite) throws Exception;

	/**
	 * 
	 * @param content
	 * @throws Exception
	 */
	public void write(String content) throws Exception;

	/**
	 * 
	 * @param content
	 * @throws Exception
	 */
	public void write(InputStream content) throws Exception;

	/**
	 * 
	 * @return
	 */
	public IAccessManager getAccessManager();

	public String getServerBaseUrl();

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean exists() throws Exception;

	/**
	 * 
	 * @param absoluteUri
	 * @param deep
	 * @return
	 * @throws Exception
	 */
	public boolean copyTo(String absoluteUri, boolean deep) throws Exception;

	/**
	 * 
	 * @param resources
	 * @param absoluteUri
	 * @param deep
	 * @return
	 * @throws Exception
	 */
	public boolean copyTo(IResource[] resources, String absoluteUri, boolean deep) throws Exception;

	/**
	 * 
	 * @param absoluteUri
	 * @return
	 * @throws Exception
	 */
	public boolean moveTo(String absoluteUri) throws Exception;

	/**
	 * 
	 * @param resources
	 * @param absoluteUri
	 * @return
	 * @throws Exception
	 */
	public boolean moveTo(IResource[] resources, String absoluteUri) throws Exception;

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean isLocked() throws Exception;

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean lock() throws Exception;

	public boolean lock(IResource[] resources) throws Exception;

	public boolean unlock() throws Exception;

	public boolean unlock(IResource[] resources) throws Exception;

	/**
	 * @return the file size if the resource is a file or the number of elements
	 *         in a folder
	 * @throws Exception
	 */
	public long getSize() throws Exception;

	/**
	 * @return the time stamp when this resource was modified
	 * @throws Exception
	 * @Deprecated use ResourceProperties instead
	 */
	public long getLastModified() throws Exception;

	/**
	 * 
	 * @return
	 * @throws Exception
	 * @Deprecated use ResourceProperties instead
	 */
	public String getLastModifiedBy() throws Exception;

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getCreatedBy() throws Exception;

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public long getCreated() throws Exception;

	/**
	 * 
	 * @return
	 * @throws Exception
	 * @Deprecated use ResourceProperties instead
	 */
	public String getDescription() throws Exception;

	/**
	 * 
	 * @param feedbackTxt
	 * @param notifyOwner
	 * @return
	 * @throws Exception
	 */
	public boolean addFeedback(String feedbackTxt, boolean notifyOwner) throws Exception;

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<String[]> getFeedback() throws Exception;

	/**
	 * 
	 * @param a
	 * @return
	 * @throws Exception
	 */
	public boolean rate(int a) throws Exception;

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public int getRatingAverage() throws Exception;

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public int getRatingOfUser() throws Exception;

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public int getFeedbackCount() throws Exception;

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public int getRatingCount() throws Exception;

	public String getLockedBy() throws Exception;
	
	public String getLockedByName() throws Exception;

	public String getEditedBy() throws Exception;
	
	public String getEditedByName() throws Exception;

	public boolean canChangePermission() throws Exception;

	public boolean canDelete() throws Exception;

	public boolean canRead() throws Exception;

	public boolean canWrite() throws Exception;

	public boolean setProperties(String description, List<IProperty> propertyList) throws Exception;

	public List<IProperty> getProperties(String propertyGroupID) throws Exception;

	public boolean reload() throws Exception;

	public boolean startEditContent() throws Exception;

	public boolean endEditContent(String content) throws Exception;

	public boolean endEditContent(InputStream content) throws Exception;

	public String getContentSnippet() throws Exception;
	
	public String getParentForumNameOfMessage();
	
	public boolean isForumsMessage();
	
	public String getConstantURL();
}