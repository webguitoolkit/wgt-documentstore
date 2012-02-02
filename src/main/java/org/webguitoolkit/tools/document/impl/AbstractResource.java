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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.webguitoolkit.tools.document.IAccessManager;
import org.webguitoolkit.tools.document.IDirectory;
import org.webguitoolkit.tools.document.IDocumentRepository;
import org.webguitoolkit.tools.document.IResource;
import org.webguitoolkit.tools.document.IStoreProxy;

/**
 * The basic properties and function of an resource in the document store.
 * 
 * @author peter
 * 
 */
public abstract class AbstractResource implements IResource {

	protected IStoreProxy me;
	protected IDocumentRepository repos;
	protected static Log log = LogFactory.getLog(IDocumentRepository.class);;

	public String getAbsoluteUri() {
		return me.getAbsoluteUri();
	}

	public String getAccessURL() {
		return me.getAccessURL();
	}

	public String getName() throws DocumentRepositoryException {
		try {
			return me.getName();
		} catch (ResourceNotExistedException e) {
			throw new ResourceNotExistedException(e);
		} catch (AccessDeniedException e) {
			throw new AccessDeniedException(e);
		} catch (LockExistedException e) {
			throw new LockExistedException(e);
		} catch (Exception e) {
			throw new DocumentRepositoryException(e);
		}
	}

	public boolean rename(String name) throws DocumentRepositoryException {
		try {
			return me.rename(name);
		} catch (ResourceNotExistedException e) {
			throw new ResourceNotExistedException(e);
		} catch (AccessDeniedException e) {
			throw new AccessDeniedException(e);
		} catch (LockExistedException e) {
			throw new LockExistedException(e);
		} catch (ResourceAlreadyExistException e) {
			throw new ResourceAlreadyExistException(e);
		} catch (Exception e) {
			throw new DocumentRepositoryException(e);
		}
	}

	public long getLastModified() throws DocumentRepositoryException {
		try {
			return me.getLastModified();
		} catch (ResourceNotExistedException e) {
			throw new ResourceNotExistedException(e);
		} catch (AccessDeniedException e) {
			throw new AccessDeniedException(e);
		} catch (LockExistedException e) {
			throw new LockExistedException(e);
		} catch (Exception e) {
			throw new DocumentRepositoryException(e);
		}
	}

	public long getSize() throws DocumentRepositoryException {
		try {
			return me.getSize();
		} catch (ResourceNotExistedException e) {
			throw new ResourceNotExistedException(e);
		} catch (AccessDeniedException e) {
			throw new AccessDeniedException(e);
		} catch (LockExistedException e) {
			throw new LockExistedException(e);
		} catch (Exception e) {
			throw new DocumentRepositoryException(e);
		}
	}

	public long getCreated() throws DocumentRepositoryException {
		try {
			return me.getCreated();
		} catch (ResourceNotExistedException e) {
			throw new ResourceNotExistedException(e);
		} catch (AccessDeniedException e) {
			throw new AccessDeniedException(e);
		} catch (LockExistedException e) {
			throw new LockExistedException(e);
		} catch (Exception e) {
			throw new DocumentRepositoryException(e);
		}
	}

	public String getCreatedBy() throws DocumentRepositoryException {
		try {
			return me.getCreatedBy();
		} catch (ResourceNotExistedException e) {
			throw new ResourceNotExistedException(e);
		} catch (AccessDeniedException e) {
			throw new AccessDeniedException(e);
		} catch (LockExistedException e) {
			throw new LockExistedException(e);
		} catch (Exception e) {
			throw new DocumentRepositoryException(e);
		}
	}

	public String getDescription() throws DocumentRepositoryException {
		try {
			return me.getDescription();
		} catch (ResourceNotExistedException e) {
			throw new ResourceNotExistedException(e);
		} catch (AccessDeniedException e) {
			throw new AccessDeniedException(e);
		} catch (LockExistedException e) {
			throw new LockExistedException(e);
		} catch (Exception e) {
			throw new DocumentRepositoryException(e);
		}
	}

	public String getRelativeUri() {
		return me.getAbsoluteUri().substring(repos.getBaseAddress().length());
	}

	public String getRelativePath() {
		return me.getRelativePath();
	}

	public IDirectory getParentDirectoty() throws DocumentRepositoryException {
		String sep = repos.getPathSeparator();
		String relativeUri = me.getRelativeUri();
		if (relativeUri != null && relativeUri.contains(sep)) {
			relativeUri = relativeUri.substring(0, relativeUri.lastIndexOf(sep));
			return new VirtualDirectory(relativeUri, repos);
		}
		return null;
	}

	public IAccessManager getAccessManager() {
		return repos.getAccessManager();
	}
	
	public String getConstantURL() {
		return me.getConstantURL();
	}
}