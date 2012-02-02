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
package org.webguitoolkit.tools.document.impl.webdav;

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

/**
 * 
 * @author peter
 * 
 */
public class DavDocumentRepository extends AbstractDocumentRepository implements
		IDocumentRepository {

	public DavDocumentRepository() throws DocumentRepositoryException {
		super();
	}

	public DavDocumentRepository(String root, String path,
			IAccessManager accessManager, Properties properties)
			throws DocumentRepositoryException {
		super(root, path, accessManager, properties);

	}

	public String getPathSeparator() {
		return "/";
	}

	public IStoreProxy newProxy(String root, String path,
			IAccessManager accessManager) throws DocumentRepositoryException {
		return new WebdavProxy(root + path, accessManager);
	}

	public IStoreProxy newRootProxy() throws DocumentRepositoryException {
		return new WebdavProxy(getBaseAddress(), getAccessManager());
	}

	public boolean copy(String destUri, IResource[] resources)
			throws DocumentRepositoryException {
		throw new RuntimeException("SORRY - NOT IMPLEMENTED");
	}

	public boolean delete(IResource[] resources)
			throws DocumentRepositoryException {
		throw new RuntimeException("SORRY - NOT IMPLEMENTED");
	}

	public IConnectionManager getConnectionManager() {
		throw new RuntimeException("SORRY - NOT IMPLEMENTED");
	}

	public ISearchManager getSearchManager() {
		throw new RuntimeException("SORRY - NOT IMPLEMENTED");
	}

	public ISubscriptionManager getSubscriptionManager() {
		throw new RuntimeException("SORRY - NOT IMPLEMENTED");
	}

	public boolean lock(IResource[] resources)
			throws DocumentRepositoryException {
		throw new RuntimeException("SORRY - NOT IMPLEMENTED");
	}

	public boolean move(String destUri, IResource[] resources)
			throws DocumentRepositoryException {
		throw new RuntimeException("SORRY - NOT IMPLEMENTED");
	}

	public void setConnectionManager(IConnectionManager connectionManager) {
		throw new RuntimeException("SORRY - NOT IMPLEMENTED");
	}

	public boolean unlock(IResource[] resources)
			throws DocumentRepositoryException {
		throw new RuntimeException("SORRY - NOT IMPLEMENTED");
	}
}
