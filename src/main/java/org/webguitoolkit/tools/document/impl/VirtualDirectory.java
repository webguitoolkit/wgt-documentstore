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

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.webguitoolkit.messagebox.IChannel;
import org.webguitoolkit.messagebox.IMessage;
import org.webguitoolkit.messagebox.MessageBox;
import org.webguitoolkit.tools.document.IAccessManager;
import org.webguitoolkit.tools.document.IDirectory;
import org.webguitoolkit.tools.document.IDocumentRepository;
import org.webguitoolkit.tools.document.IFile;
import org.webguitoolkit.tools.document.IProperty;
import org.webguitoolkit.tools.document.IResource;
import org.webguitoolkit.tools.document.IStoreProxy;
import org.webguitoolkit.tools.document.ISubscriptionRegistry;
/**
 * 
 * @author peter
 *
 */
public class VirtualDirectory extends AbstractResource implements IDirectory {

	private static final String SUBSCRIPTIONREGISTRY = ".subscriptionregistry";

	public VirtualDirectory(String uri, IDocumentRepository repos)
			throws DocumentRepositoryException {
		this.me = repos.newProxy(repos.getBaseAddress(), uri,
				repos.getAccessManager());
		this.repos = repos;
	}

	public VirtualDirectory(IStoreProxy proxy, IDocumentRepository repos)
			throws DocumentRepositoryException {
		this.me = proxy;
		this.repos = repos;
	}

	public List<IDirectory> getDirectories() throws DocumentRepositoryException {
		try {
			List<IStoreProxy> collections = me.getCollections();
			List<IDirectory> result = new ArrayList<IDirectory>();
			for (IStoreProxy proxy : collections) {
				if (repos.getAccessManager().canRead(proxy.getAbsoluteUri()))
					result.add(new VirtualDirectory(proxy, repos));
			}

			return result;
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

	public List<IFile> getFiles() throws DocumentRepositoryException {
		try {
			List<IStoreProxy> collections = me.getFiles();
			List<IFile> result = new ArrayList<IFile>();
			for (IStoreProxy proxy : collections) {
				result.add(new VirtualFile(proxy, repos));
			}
			return result;
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

	public IDirectory createDirectory(String name)
			throws DocumentRepositoryException {
		boolean result = false;
		try {
			if (repos.getAccessManager().canWrite(me.getAbsoluteUri())) {
				result = me.createDirectory(name);
				notifySubscribers(getRelativeUri()
						+ " changed (new directory) ");
			}
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
		return (result) ? (new VirtualDirectory(getRelativeUri()
				+ repos.getPathSeparator() + name, repos)) : (null);
	}

	public boolean remove(boolean recursive) throws DocumentRepositoryException {
		try {
			if (recursive) {
				if (!repos.getAccessManager().canWrite(
						me.getAbsoluteUri() + "*"))
					throw new IllegalAccessError("Access denied");
			} else {
				if (!repos.getAccessManager().canWrite(me.getAbsoluteUri()))
					throw new IllegalAccessError("Access denied");
			}

			if (me.remove(recursive)) {
				notifySubscribers(getRelativeUri() + " was removed");
				return true;
			}
			return false;
		} catch (ResourceNotExistedException e) {
			log.fatal("failed to remove", e);
			throw new ResourceNotExistedException(e);
		} catch (AccessDeniedException e) {
			log.fatal("failed to remove", e);
			throw new AccessDeniedException(e);
		} catch (LockExistedException e) {
			log.fatal("failed to remove", e);
			throw new LockExistedException(e);
		} catch (Exception e) {
			log.fatal("failed to remove", e);
			throw new DocumentRepositoryException(e);
		}
	}

	public boolean remove(IResource[] resources)
			throws DocumentRepositoryException {
		try {
			me.remove(resources);
			return true;
		} catch (ResourceNotExistedException e) {
			log.fatal("failed to remove", e);
			throw new ResourceNotExistedException(e);
		} catch (AccessDeniedException e) {
			log.fatal("failed to remove", e);
			throw new AccessDeniedException(e);
		} catch (LockExistedException e) {
			log.fatal("failed to remove", e);
			throw new LockExistedException(e);
		} catch (Exception e) {
			log.fatal("failed to remove", e);
			throw new DocumentRepositoryException(e);
		}
	}

	protected ISubscriptionRegistry loadRegistry(IDirectory dir) {
		ISubscriptionRegistry sr = null;
		try {
			sr = GenericSubscriptionRegistry.load(SUBSCRIPTIONREGISTRY);
		} catch (FileNotFoundException e) {
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return sr;
	}

	protected void saveRegistry(IDirectory dir, ISubscriptionRegistry sr) {
		try {
			GenericSubscriptionRegistry.save((GenericSubscriptionRegistry) sr,
					SUBSCRIPTIONREGISTRY);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected ISubscriptionRegistry newSubscriptionRegistry(IDirectory dir) {
		return new GenericSubscriptionRegistry();
	}

	public IAccessManager getAccessManager() {
		return repos.getAccessManager();
	}

	public MessageBox getMessageBox() {
		return repos.getMessageBox();
	}

	public IDocumentRepository getRepository() {
		return repos;
	}

	public boolean rename(String name) throws DocumentRepositoryException {

		try {
			if (!repos.getAccessManager().canWrite(me.getAbsoluteUri()))
				throw new IllegalAccessError("Access denied");
			if (me.rename(name)) {
				notifySubscribers(getRelativeUri() + " was renamed ");
				return true;
			}
			return false;
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

	public void notifySubscribers(String messageText) {
		Set<String> subscriptions = subscriptions();
		if (subscriptions == null || subscriptions.size() == 0)
			return;

		for (String channelId : getMessageBox().getAvailableChannels()) {
			IChannel channel = getMessageBox().getChannel(channelId);
			IMessage message = channel.newMessage(messageText, messageText);
			message.send();
		}
	}

	public void subscribe(String recipient) {
		ISubscriptionRegistry sr = null;
		synchronized (ISubscriptionRegistry.class) {
			sr = loadRegistry(this);
			if (sr == null)
				sr = newSubscriptionRegistry(this);
			sr.subscribe(this.getRelativeUri(), recipient);
			saveRegistry(this, sr);
		}
	}

	public void unsubscribe(String recipient) {
		ISubscriptionRegistry sr = null;
		synchronized (ISubscriptionRegistry.class) {
			sr = loadRegistry(this);
			if (sr == null)
				return;
			sr.unsubscribe(this.getRelativeUri(), recipient);
			saveRegistry(this, sr);
		}
	}

	public Set<String> subscriptions() {
		ISubscriptionRegistry sr = null;
		synchronized (ISubscriptionRegistry.class) {
			sr = loadRegistry(this);
			if (sr == null)
				return Collections.emptySet();
		}
		return sr.subscriptions(this.getRelativeUri());
	}

	public boolean copyTo(IDirectory target, boolean deep)
			throws DocumentRepositoryException {
		if (!repos.getAccessManager().canWrite(me.getAbsoluteUri()))
			throw new IllegalAccessError("Access denied");
		try {
			me.copyTo(target.getAbsoluteUri(), deep);
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
		notifySubscribers(target.getRelativeUri() + " changed");
		return true;
	}

	public boolean copyTo(IResource[] resources, IDirectory target, boolean deep)
			throws DocumentRepositoryException {
		try {
			return me.copyTo(resources, target.getAbsoluteUri(), deep);
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

	public Boolean getWriteable() {
		return repos.getAccessManager().canWrite(me.getRelativeUri());
	}

	public boolean moveTo(IDirectory target) throws DocumentRepositoryException {
		if (!repos.getAccessManager().canWrite(me.getAbsoluteUri()))
			throw new IllegalAccessError("Access denied");
		try {
			me.moveTo(target.getAbsoluteUri());
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
		notifySubscribers(me.getRelativeUri() + " was moved");
		return true;
	}

	public boolean moveTo(IResource[] resources, IDirectory target)
			throws DocumentRepositoryException {
		try {
			return me.moveTo(resources, target.getAbsoluteUri());
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

	public boolean lock(IResource[] resources)
			throws DocumentRepositoryException {
		try {
			return me.lock(resources);
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

	public boolean unlock(IResource[] resources)
			throws DocumentRepositoryException {
		try {
			return me.unlock(resources);
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

	public IFile createFile(String name, String content, boolean overwrite)
			throws DocumentRepositoryException {
		try {
			me.createFile(name, content, overwrite);
			VirtualFile file = new VirtualFile(getRelativeUri()
					+ repos.getPathSeparator() + name, repos);
			return file;
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

	public IFile createFile(String name, InputStream content, boolean overwrite)
			throws DocumentRepositoryException {
		try {
			me.createFile(name, content, overwrite);
			VirtualFile file = new VirtualFile(getRelativeUri()
					+ repos.getPathSeparator() + name, repos);
			return file;
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

	public boolean exists() throws DocumentRepositoryException {
		try {
			return me.exists();
		} catch (AccessDeniedException e) {
			throw new AccessDeniedException(e);
		} catch (Exception e) {
			return false;
		}
	}

}