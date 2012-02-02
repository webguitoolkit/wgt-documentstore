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

import java.io.InputStream;
import java.util.List;

import org.webguitoolkit.tools.document.IDirectory;
import org.webguitoolkit.tools.document.IDocumentRepository;
import org.webguitoolkit.tools.document.IFile;
import org.webguitoolkit.tools.document.IProperty;
import org.webguitoolkit.tools.document.IStoreProxy;

public class VirtualFile extends AbstractResource implements IFile {

	public VirtualFile(IStoreProxy proxy, IDocumentRepository repos)
			throws DocumentRepositoryException {

		me = proxy;
		this.repos = repos;
	}

	public VirtualFile(String uri, IDocumentRepository repos)
			throws DocumentRepositoryException {

		me = repos.newProxy(repos.getRoot().getAbsoluteUri(), uri, repos
				.getAccessManager());

		this.repos = repos;
	}

	public String getExtension() throws DocumentRepositoryException {
		String name;
		try {
			name = me.getName();
		} catch (Exception e) {
			log.fatal("failed to get file extension", e);
			throw new DocumentRepositoryException(e);
		}
		if (name.lastIndexOf('.') == -1)
			return null;
		return name.substring(name.lastIndexOf('.'));
	}

	public InputStream getContent() throws Exception {
		return me.getContent();
	}

	public boolean moveTo(IDirectory target) throws DocumentRepositoryException {
		try {
			if (repos.getAccessManager().canWrite(me.getAbsoluteUri())
					&& repos.getAccessManager().canWrite(
							target.getRelativeUri())) {
				return me.moveTo(target.getAbsoluteUri());
			} else
				throw new IllegalAccessError("access denied");
		} catch (ResourceNotExistedException e) {
			throw new ResourceNotExistedException(e);
		} catch (ResourceAlreadyExistException e) {
			throw new ResourceAlreadyExistException(e);
		} catch (AccessDeniedException e) {
			throw new AccessDeniedException(e);
		} catch (LockExistedException e) {
			throw new LockExistedException(e);
		} catch (Exception e) {
			throw new DocumentRepositoryException(e);
		}
	}

	public void remove() throws DocumentRepositoryException {
		try {
			if (repos.getAccessManager().canWrite(me.getAbsoluteUri())) {
				me.remove(false);
			} else
				throw new IllegalAccessError("access denied");
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

	public boolean copyTo(IDirectory target, boolean deep)
			throws DocumentRepositoryException {
		try {
			if (repos.getAccessManager().canWrite(target.getAbsoluteUri())) {
				me.copyTo(target.getAbsoluteUri(), deep);
			} else
				throw new IllegalAccessError("access denied");
		} catch (ResourceNotExistedException e) {
			throw new ResourceNotExistedException(e);
		} catch (ResourceAlreadyExistException e) {
			throw new ResourceAlreadyExistException(e);
		} catch (AccessDeniedException e) {
			throw new AccessDeniedException(e);
		} catch (LockExistedException e) {
			throw new LockExistedException(e);
		} catch (Exception e) {
			throw new DocumentRepositoryException(e);
		}

		// IFile targetfile;
		// try {
		// if (repos.getAccessManager().canWrite(target.getAbsoluteUri())) {
		// me.copyTo(target.getAbsoluteUri(), deep);
		// targetfile = target.createFile(getName(), "");
		// } else
		// throw new IllegalAccessError("access denied");
		// } catch (ResourceNotExistedException e) {
		// throw new ResourceNotExistedException(e);
		// } catch (AccessDeniedException e) {
		// throw new AccessDeniedException(e);
		// } catch (LockExistedException e) {
		// throw new LockExistedException(e);
		// } catch (Exception e) {
		// throw new DocumentRepositoryException(e);
		// }
		// if (targetfile == null)
		// throw new IllegalArgumentException("target already exist");
		//
		// InputStream fromStream = null;
		//
		// try {
		// fromStream = me.getInputStream();
		// targetfile.writeContent(fromStream);
		// } catch (ResourceNotExistedException e) {
		// throw new ResourceNotExistedException(e);
		// } catch (ResourceAlreadyExistException e) {
		// throw new ResourceAlreadyExistException(e);
		// } catch (AccessDeniedException e) {
		// throw new AccessDeniedException(e);
		// } catch (LockExistedException e) {
		// throw new LockExistedException(e);
		// } catch (Exception e) {
		// throw new DocumentRepositoryException(e);
		// }
		//
		// target.notifySubscribers(target.getRelativeUri() + " changed");

		return true;
	}

	public void writeContent(String content) throws Exception {
		me.write(content);
	}

	public void writeContent(InputStream fromStream) throws Exception {
		me.write(fromStream);
	}

	public boolean isLocked() throws DocumentRepositoryException {
		try {
			return me.isLocked();
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

	public boolean lock() throws DocumentRepositoryException {
		try {
			return me.lock();
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

	public boolean unlock() throws DocumentRepositoryException {
		try {
			return me.unlock();
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

	public String getLastModifiedBy() throws DocumentRepositoryException {
		try {
			return me.getLastModifiedBy();
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

	public List<String[]> getFeedback() throws DocumentRepositoryException {
		try {
			return me.getFeedback();
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

	public boolean addFeedback(String feedbackTxt, boolean notifyOwner)
			throws DocumentRepositoryException {
		try {
			return me.addFeedback(feedbackTxt, notifyOwner);
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

	// rate resource
	public boolean rate(int a) throws DocumentRepositoryException {
		try {
			return me.rate(a);
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

	// get rating average of resource
	public int getRatingAverage() throws DocumentRepositoryException {
		try {
			return me.getRatingAverage();
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

	// get rating of user
	public int getRatingOfUser() throws DocumentRepositoryException {
		try {
			return me.getRatingOfUser();
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

	public int getFeedbackCount() throws DocumentRepositoryException {
		try {
			return me.getFeedbackCount();
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

	public int getRatingCount() throws DocumentRepositoryException {
		try {
			return me.getRatingCount();
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

	public String getEditedBy() throws DocumentRepositoryException {
		try {
			return me.getEditedBy();
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

	public String getEditedByName() throws DocumentRepositoryException {
		try {
			return me.getEditedByName();
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

	public String getLockedBy() throws DocumentRepositoryException {
		try {
			return me.getLockedBy();
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

	public String getLockedByName() throws DocumentRepositoryException {
		try {
			return me.getLockedByName();
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

	public boolean canChangePermission() throws DocumentRepositoryException {
		try {
			return me.canChangePermission();
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

	public boolean canDelete() throws DocumentRepositoryException {
		try {
			return me.canDelete();
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

	public boolean canRead() throws DocumentRepositoryException {
		try {
			return me.canRead();
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

	public boolean canWrite() throws DocumentRepositoryException {
		try {
			return me.canWrite();
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

	public List<IProperty> getProperties(String propertyGroupID)
			throws DocumentRepositoryException {
		try {
			return me.getProperties(propertyGroupID);
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

	public boolean reload() throws DocumentRepositoryException {
		try {
			return me.reload();
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

	public boolean setProperties(String description,
			List<IProperty> propertyList) throws DocumentRepositoryException {
		try {
			return me.setProperties(description, propertyList);
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

	public boolean exists() throws DocumentRepositoryException {
		try {
			return me.exists();
		} catch (AccessDeniedException e) {
			throw new AccessDeniedException(e);
		} catch (Exception e) {
			return false;
		}
	}

	public boolean endEditContent(String content)
			throws DocumentRepositoryException {
		try {
			me.endEditContent(content);
			return true;
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

	public boolean endEditContent(InputStream content)
			throws DocumentRepositoryException {
		try {
			me.endEditContent(content);
			return true;
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

	public boolean startEditContent() throws DocumentRepositoryException {
		try {
			return me.startEditContent();
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

	public String getContentSnippet() throws DocumentRepositoryException {
		try {
			return me.getContentSnippet();
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
	
	public String getParentForumNameOfMessage() {
		return me.getParentForumNameOfMessage();
	}
	
	public boolean isForumsMessage() {
		return me.isForumsMessage();
	}
}