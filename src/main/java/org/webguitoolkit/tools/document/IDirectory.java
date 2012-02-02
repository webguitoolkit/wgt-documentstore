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
import java.util.Set;

import org.webguitoolkit.tools.document.impl.DocumentRepositoryException;

/**
 * Generic description of a directory regardless of the physical storage
 * location. Intended to provide an common interface for file system, WebDAV,
 * SAP KM, data bases or other storage types. The only assumption is that the
 * files are organized in directories which the option of an hierarchy of
 * directories. <br>
 * An implementation shall check the accessibility of resources for a user by
 * means of the AccessManager with canRead() and canWrite(). <br>
 * The subscription mechanism supports notification if the content of a
 * directory was changed. The notification is done by means of the MessageBox
 * interfaces which is an abstraction for a communication towards application
 * users.
 * 
 * @author peter
 * 
 */
public interface IDirectory extends IResource {

	/**
	 * @return a list of sub-directories or a empty list.
	 * @throws DocumentRepositoryException
	 */
	public List<IDirectory> getDirectories() throws DocumentRepositoryException;

	/**
	 * @return a list of files or a empty list.
	 * @throws DocumentRepositoryException
	 */
	public List<IFile> getFiles() throws DocumentRepositoryException;

	/**
	 * Create a new directory underneath this directory. The name has to be
	 * unique.
	 * 
	 * @param name
	 *          the directories name
	 * @return the new directory
	 * @throws DocumentRepositoryException
	 * @throws IllegalArgumentException
	 *           if the directory already exist
	 */
	public IDirectory createDirectory(String name) throws DocumentRepositoryException;

	public IFile createFile(String name, String content, boolean overwrite) throws DocumentRepositoryException;

	public IFile createFile(String name, InputStream content, boolean overwrite) throws DocumentRepositoryException;

	/**
	 * Remove the directory.
	 * 
	 * @param recursive
	 *          if TRUE all sub-directories are deleted as well.
	 * @throws DocumentRepositoryException
	 * 
	 * @throws IllegalStateException
	 *           if the directory is not empty and recursive==false.
	 */
	public boolean remove(boolean recursive) throws DocumentRepositoryException;

	public boolean remove(IResource[] resources) throws DocumentRepositoryException;

	public boolean moveTo(IResource[] resources, IDirectory target) throws DocumentRepositoryException;

	/**
	 * Rename the directory to the new name
	 * 
	 * @param relativeUri
	 *          new repository relative URI
	 * @throws DocumentRepositoryException
	 * @throws IllegalArgumentException
	 *           if the name already exist.
	 */
	public boolean rename(String relativeUri) throws DocumentRepositoryException;

	public boolean copyTo(IResource[] resources, IDirectory dir, boolean deep) throws DocumentRepositoryException;

	public boolean lock(IResource[] resources) throws DocumentRepositoryException;

	public boolean unlock(IResource[] resources) throws DocumentRepositoryException;

	/**
	 * Subscribe to changes of this directory for notification. Whenever changes
	 * to this directory apply a notification will be sent to the MessageAddress.
	 * Nothing happens if the Address is already subscribed.
	 * 
	 * @param recipient
	 *          where the userid is taken to identify the recipient.
	 */
	public void subscribe(String recipient);

	/**
	 * Un-subscribe to changes of this directory for notification. Nothing happens
	 * if the Address is not subscribed.
	 * 
	 * @param recipient
	 *          where the userid is taken to identify the recipient.
	 */
	public void unsubscribe(String recipient);

	/**
	 * @return the list of subscribers
	 */
	public Set<String> subscriptions();

	/**
	 * Notify the subscribers of this directory - if any
	 * 
	 * @param message
	 *          the message
	 */
	public void notifySubscribers(String message);
	
	/**
	 * 
	 * @return Boolean.TRUE if resource is write-able.
	 */
	public Boolean getWriteable();

}
