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

import java.util.List;

import org.webguitoolkit.tools.document.impl.DocumentRepositoryException;

/**
 * The basic properties and function of an resource in the document store.
 * 
 * @author peter
 * 
 */
public interface IResource {
	/**
	 * @return the name of the resource
	 * @throws DocumentRepositoryException
	 */
	public String getName() throws DocumentRepositoryException;

	/**
	 * The relative URI starts from the repository root.
	 * 
	 * @return the URI relative to the DocumentRepository.
	 */
	public String getRelativeUri() throws DocumentRepositoryException;;

	/**
	 * The absolute URI include the completed path to this resource including
	 * the path to the repository (the root). This depends on the storage type.
	 * For WebDav and file system storage this is defined. For database storage
	 * this might be a implementation depending identifier.
	 * 
	 * @return the absolute URI.
	 */
	public String getAbsoluteUri() throws DocumentRepositoryException;;

	/**
	 * @return the path to the resource. If the complete path is
	 *         "http://www.server.com:8080/webdav/folder/file.txt" it will
	 *         return "/folder"
	 */
	public String getRelativePath() throws DocumentRepositoryException;;

	public String getAccessURL() throws DocumentRepositoryException;;

	/**
	 * Rename a resource.
	 * 
	 * @param name
	 * @return
	 * @throws DocumentRepositoryException
	 *             when the name already exist or another problem occur.
	 */
	public boolean rename(String name) throws DocumentRepositoryException;

	/**
	 * check if the actual resource exists, maybe the resources was deleted by
	 * another person in the meantime.
	 * 
	 * @return true if resource exists, otherwise false
	 * @throws DocumentRepositoryException
	 */
	public boolean exists() throws DocumentRepositoryException;

	/**
	 * 
	 * @return
	 * @throws DocumentRepositoryException
	 */
	public boolean isLocked() throws DocumentRepositoryException;

	/**
	 * 
	 * @return
	 * @throws DocumentRepositoryException
	 */
	public String getLockedBy() throws DocumentRepositoryException;

	/**
	 * 
	 * @return
	 * @throws DocumentRepositoryException
	 */
	public String getLockedByName() throws DocumentRepositoryException;

	/**
	 * 
	 * @return
	 * @throws DocumentRepositoryException
	 */
	public boolean lock() throws DocumentRepositoryException;

	/**
	 * 
	 * @return
	 * @throws DocumentRepositoryException
	 */
	public boolean unlock() throws DocumentRepositoryException;

	/**
	 * Moves this director into another one. The target directory has to exist
	 * and shall not contain a directory named like the one that is moved.
	 * 
	 * @param target
	 *            Directory to move to.
	 * @throws IllegalArgumentException
	 *             if the target does not exist or contains a directory with the
	 *             same name
	 */
	public boolean moveTo(IDirectory target) throws DocumentRepositoryException;

	/**
	 * Copies this director into another one. The target directory has to exist
	 * and shall not contain a directory named like the one that shall be moved.
	 * 
	 * @param target
	 *            where to move to.
	 * @param deep
	 *            if TRUE all sub-directories with files are copied as well.
	 * @throws IllegalArgumentException
	 *             if the target does not exist or contains a directory with the
	 *             same name
	 */
	public boolean copyTo(IDirectory dir, boolean deep)
			throws DocumentRepositoryException;

	/**
	 * @return the size of the resource. For IFiles the file size for IDirectory
	 *         the elements
	 * @throws DocumentRepositoryException
	 * @Deprecated use ResourceProperties instead
	 */
	public long getSize() throws DocumentRepositoryException;

	/**
	 * Get the modification date as time stamp.
	 * 
	 * @return last modification date
	 * @throws DocumentRepositoryException
	 * @Deprecated use ResourceProperties instead
	 */
	public long getLastModified() throws DocumentRepositoryException;

	/**
	 * Get the last modifier of the resource
	 * 
	 * @return last modifier
	 * @throws DocumentRepositoryException
	 */
	public String getLastModifiedBy() throws DocumentRepositoryException;

	/**
	 * Get the creation date as time stamp
	 * 
	 * @return creation date
	 * @throws DocumentRepositoryException
	 */
	public String getCreatedBy() throws DocumentRepositoryException;

	/**
	 * Get the creator of the resource
	 * 
	 * @return creator
	 * @throws DocumentRepositoryException
	 */
	public long getCreated() throws DocumentRepositoryException;

	/**
	 * Get the description of the resource
	 * 
	 * @return description of the resource
	 * @throws DocumentRepositoryException
	 */
	public String getDescription() throws DocumentRepositoryException;

	public String getEditedBy() throws DocumentRepositoryException;

	public String getEditedByName() throws DocumentRepositoryException;

	public boolean canChangePermission() throws DocumentRepositoryException;

	public boolean canDelete() throws DocumentRepositoryException;

	public boolean canRead() throws DocumentRepositoryException;

	public boolean canWrite() throws DocumentRepositoryException;

	public boolean setProperties(String description,
			List<IProperty> propertyList) throws DocumentRepositoryException;

	public List<IProperty> getProperties(String propertyGroupID)
			throws DocumentRepositoryException;

	public boolean reload() throws DocumentRepositoryException;

	public IDirectory getParentDirectoty() throws DocumentRepositoryException;

	public IAccessManager getAccessManager() throws DocumentRepositoryException;
	
	/**
	 * Retrieves a url and/or path to this resource, that is constant, meaning that if the resource is moved or renamed, 
	 * such a constant url still point to the resource.
	 * @return The constant URL or null if there exists no constant URL
	 */
	public String getConstantURL();
}