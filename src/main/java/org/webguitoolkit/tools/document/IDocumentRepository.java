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

import java.net.MalformedURLException;

import org.webguitoolkit.messagebox.MessageBox;
import org.webguitoolkit.tools.document.impl.DocumentRepositoryException;

/**
 * The document repository provides a high level interface to an simplified
 * document storage system. A document repository has the following functions: <br>
 * Store and retrieve documents. <br>
 * Organize documents hierarchically in directories.<br>
 * Provide basic access control (canRead, canWrite) by means of a AccessManager
 * interface on directory level.<br>
 * Provide a notification feature where somebody (person or application) can
 * register for modification of an directory. Whenever a directory is changed a
 * subscribed subject will be notified depending on the used implementation of
 * the MessageBox feature.<br>
 * The Implementation for the file system is only intended to act as reference
 * and for development purposes. Other implementation like WebDAV or SAP KM are
 * used for production. <br>
 * To implement a different store it is recommended to implement the IStoreProxy
 * interface. That enables you to used the abstract implementations
 * VirtualDirectory and VirtualFile.
 * 
 * @author peter
 * 
 */
public interface IDocumentRepository {

	/**
	 * @return the root of the document repository. Specified via the constructor
	 *         of the implementation
	 */
	public IDirectory getRoot() throws DocumentRepositoryException;

	/**
	 * @return the configured connection manager for remote connection to<br>
	 *         the persistence layer (e.g. SapKm, DB, ...)
	 */
	public IConnectionManager getConnectionManager();

	/**
	 * Define a connection manager for the document repository, in case<br>
	 * of a remote connection e.g. webservice to the persistence layer. <br>
	 * 
	 * @param connectionManager
	 *          connection manager for document repository
	 */
	public void setConnectionManager(IConnectionManager connectionManager);

	/**
	 * 
	 * @return the configured search manager for the document repository
	 */
	public ISearchManager getSearchManager();

	/**
	 * 
	 * @return the configured subscription manager for the document repository
	 */
	public ISubscriptionManager getSubscriptionManager();

	/**
	 * @return the configured access manager
	 */
	public IAccessManager getAccessManager();

	
	/**
	 * Get the Resource identified by the URI.
	 * 
	 * @param uri
	 *          specifies the resource relatively to the root of the repository.
	 *          The format of the URI may depend on the implementation
	 * @return the resource or null if it does not exist
	 * @throws DocumentRepositoryException
	 * @throws MalformedURLException
	 */
	public IResource getResource(String uri) throws DocumentRepositoryException, MalformedURLException;

	/**
	 * Delete all resources relative to the uri. The reason ist mass deletion of
	 * resources with different parents.
	 * 
	 * @param uris
	 * @return
	 * @throws DocumentRepositoryException
	 */
	public boolean delete(IResource[] resources) throws DocumentRepositoryException;

	/**
	 * Copy all resources relative to the uri. The reason ist mass deletion of
	 * resources with different parents.
	 * 
	 * @param uris
	 * @return
	 * @throws DocumentRepositoryException
	 */
	public boolean copy(String destUri, IResource[] resources) throws DocumentRepositoryException;

	public boolean move(String destUri, IResource[] resources) throws DocumentRepositoryException;

	public boolean lock(IResource[] resources) throws DocumentRepositoryException;

	public boolean unlock(IResource[] resources) throws DocumentRepositoryException;

	/**
	 * @return a MessageBox implementation from the configured factory.
	 */
	public MessageBox getMessageBox();

	/**
	 * @return the address which was passed when calling the constructor.
	 */
	public String getBaseAddress();

	/**
	 * @return the servers address when the repository is backed by a (e.g. WebDAV
	 *         server. [http://www.host.com]/webdav/root/xxx
	 */
	public String getServerAddress();

	/**
	 * 
	 * @return the servers root path when the repository is backed by a (e.g.
	 *         WebDAV server. http://www.host.com[/webdav/root]/xxx
	 */
	public String getServerRoot();

	/**
	 * @return the path separator char.
	 */
	public String getPathSeparator();

	/**
	 * 
	 * @param root
	 * @param path
	 * @param accessManager
	 * @param connectionManager
	 * @return
	 * @throws DocumentRepositoryException
	 */
	public IStoreProxy newProxy(String root, String path, IAccessManager accessManager)
			throws DocumentRepositoryException;

	/**
	 * returns a new IStoreProxy instance, which represents the root node of the<br>
	 * document repository<br>
	 * 
	 * @return Root IStoreProxy instance of document repository
	 * @throws DocumentRepositoryException
	 */
	public IStoreProxy newRootProxy() throws DocumentRepositoryException;
}
