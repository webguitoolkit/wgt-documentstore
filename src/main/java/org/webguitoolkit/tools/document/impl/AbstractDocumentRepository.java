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
package org.webguitoolkit.tools.document.impl;

import java.net.MalformedURLException;
import java.util.Properties;

import org.webguitoolkit.messagebox.MessageBox;
import org.webguitoolkit.tools.document.IAccessManager;
import org.webguitoolkit.tools.document.IDirectory;
import org.webguitoolkit.tools.document.IDocumentRepository;
import org.webguitoolkit.tools.document.IResource;
import org.webguitoolkit.tools.document.ISearchManager;
import org.webguitoolkit.tools.document.IStoreProxy;

public abstract class AbstractDocumentRepository implements IDocumentRepository {

	private IAccessManager accessManager;
	private IDirectory root = null;

	private MessageBox messageBox = null;
	private String serverAddress;
	private String serverPath;
	private String owner;
	protected ISearchManager searchManager;
	private Properties properties;

	public AbstractDocumentRepository() throws DocumentRepositoryException {

	}

	public AbstractDocumentRepository(String base, String path,
			IAccessManager accessManager, Properties properties) throws DocumentRepositoryException {
		// remember parameters
		setAccessManager(accessManager);
		this.properties = properties;
		setServerAddress(base);
		setServerPath(path);
	}

	public IAccessManager getAccessManager() {
		return accessManager;
	}

	public IDirectory getRoot() throws DocumentRepositoryException {
		if (root == null)
			root = new VirtualDirectory("", this);
		return root;
	}

	public IResource getResource(String relativeUri)
			throws DocumentRepositoryException, MalformedURLException {

		IStoreProxy res = newProxy(getBaseAddress(), relativeUri, accessManager);
		try {
			if (res.exists()) {
				if (res.isCollection()) {
					return new VirtualDirectory(res, this);
				} else {
					return new VirtualFile(res, this);
				}
			}
		} catch (Exception e) {
			throw new DocumentRepositoryException(e);
		}
		return null;
	}

	public MessageBox getMessageBox() {
		if (messageBox == null) {
			messageBox = new MessageBox(properties);
		}
		return messageBox;
	}

	public String getBaseAddress() {
		return getServerAddress() + getServerRoot();
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public String getServerRoot() {
		return serverPath;
	}

	public void setServerPath(String serverPath) {
		this.serverPath = serverPath;
	}

	public void setAccessManager(IAccessManager accessManager) {
		this.accessManager = accessManager;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getOwner() {
		return owner;
	}
}
