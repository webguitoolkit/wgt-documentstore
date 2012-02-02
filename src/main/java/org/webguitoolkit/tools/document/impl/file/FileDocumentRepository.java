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
import java.util.Properties;

import org.webguitoolkit.tools.document.IAccessManager;
import org.webguitoolkit.tools.document.IConnectionManager;
import org.webguitoolkit.tools.document.IDocumentRepository;
import org.webguitoolkit.tools.document.IResource;
import org.webguitoolkit.tools.document.ISearchManager;
import org.webguitoolkit.tools.document.IStoreProxy;
import org.webguitoolkit.tools.document.ISubscriptionManager;
import org.webguitoolkit.tools.document.impl.AbstractDocumentRepository;
import org.webguitoolkit.tools.document.impl.DocumentRepositoryException;

public class FileDocumentRepository extends AbstractDocumentRepository implements IDocumentRepository {

	public FileDocumentRepository() throws DocumentRepositoryException {
		super();
	}

	public FileDocumentRepository(String root, String path, IAccessManager accessManager,
			Properties properties) throws DocumentRepositoryException {
		super(root, path, accessManager, properties);
	}

	public String getPathSeparator() {
		return File.separatorChar + "";
	}

	public IStoreProxy newProxy(String root, String path, IAccessManager accessManager)
			throws DocumentRepositoryException {
		return new FileProxy(root, path, accessManager);
	}

	public IStoreProxy newRootProxy() throws DocumentRepositoryException {
		return new FileProxy(getServerAddress(), getServerRoot(), getAccessManager());
	}

	public IConnectionManager getConnectionManager() {
		// No connection manager available for file repository implementation
		return null;
	}

	public ISearchManager getSearchManager() {
		// No search manager available for file repository implementation
		return null;
	}

	public void setConnectionManager(IConnectionManager connectionManager) {

	}

	public ISubscriptionManager getSubscriptionManager() {
		throw new RuntimeException("Not implemented");
	}

	public boolean copy(String destUri, IResource[] resources) throws DocumentRepositoryException {
		throw new RuntimeException("Not implemented");
	}

	public boolean delete(IResource[] resources) throws DocumentRepositoryException {
		throw new RuntimeException("Not implemented");
	}

	public boolean lock(IResource[] resources) throws DocumentRepositoryException {
		throw new RuntimeException("Not implemented");
	}

	public boolean move(String destUri, IResource[] resources) throws DocumentRepositoryException {
		throw new RuntimeException("Not implemented");
	}

	public boolean unlock(IResource[] resources) throws DocumentRepositoryException {
		throw new RuntimeException("Not implemented");
	}
}
