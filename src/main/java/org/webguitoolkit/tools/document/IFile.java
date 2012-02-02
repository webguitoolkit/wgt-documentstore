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

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.webguitoolkit.tools.document.impl.DocumentRepositoryException;

/**
 * Generic interface description of a file regardless of the physical storage
 * location. Intended to provide an common interface for file system, WebDAV,
 * SAP KM, data base or other storage media. The only assumption is that the
 * files are organized in directories which the option of an hierarchy of
 * directories.
 * 
 * @author peter
 * 
 */
public interface IFile extends IResource {

	/**
	 * @return the files extension or null. The extension is the string that
	 *         follows a "." at the end of the name.
	 * @throws DocumentRepositoryException
	 */
	public String getExtension() throws DocumentRepositoryException;

	/**
	 * Remove this file.
	 * 
	 * @throws DocumentRepositoryException
	 */
	public void remove() throws DocumentRepositoryException;

	/**
	 * Rename the file to the new name
	 * 
	 * @param name
	 *          new name
	 * @throws DocumentRepositoryException
	 * @throws IllegalArgumentException
	 *           if the name already exist.
	 */
	public boolean rename(String name) throws DocumentRepositoryException;

	/**
	 * Write a stream to the file
	 * 
	 * @param fromStream
	 * @throws Exception
	 */
	public void writeContent(InputStream fromStream) throws Exception;

	/**
	 * Get the content as stream. Note that the caller is responsible to close the
	 * stream.
	 * 
	 * @return the content as stream
	 * @throws FileNotFoundException
	 */
	public InputStream getContent() throws Exception;

	/**
	 * Write a string content to the file
	 * 
	 * @param content
	 * @throws Exception
	 */
	public void writeContent(String content) throws Exception;

	/**
	 * Rate this document
	 * 
	 * @param a
	 * @return
	 * @throws DocumentRepositoryException
	 */
	public boolean rate(int a) throws DocumentRepositoryException;

	/**
	 * Get the rating average of the document
	 * 
	 * @return average rating value
	 * @throws DocumentRepositoryException
	 */
	public int getRatingAverage() throws DocumentRepositoryException;

	/**
	 * get the actual rating of a specified user of this document
	 * 
	 * @return rating value
	 * @throws DocumentRepositoryException
	 */
	public int getRatingOfUser() throws DocumentRepositoryException;

	/**
	 * 
	 * @param feedbackTxt
	 * @param notifyOwner
	 * @return
	 * @throws DocumentRepositoryException
	 */
	public boolean addFeedback(String feedbackTxt, boolean notifyOwner) throws DocumentRepositoryException;

	/**
	 * Get all feedbacks of this document
	 * 
	 * @return Array of the single feedbacks as strings
	 * @throws DocumentRepositoryException
	 */
	public List<String[]> getFeedback() throws DocumentRepositoryException;

	/**
	 * Get the feedback count of the document
	 * 
	 * @return Number of feedbacks of this document
	 * @throws DocumentRepositoryException
	 */
	public int getFeedbackCount() throws DocumentRepositoryException;

	/**
	 * Get the rating count of the document
	 * 
	 * @return Number of ratings of this document
	 * @throws DocumentRepositoryException
	 */
	public int getRatingCount() throws DocumentRepositoryException;

	public boolean startEditContent() throws DocumentRepositoryException;

	public boolean endEditContent(String content) throws DocumentRepositoryException;

	public boolean endEditContent(InputStream content) throws DocumentRepositoryException;

	public String getContentSnippet() throws DocumentRepositoryException;
	
	public String getParentForumNameOfMessage();
	
	public boolean isForumsMessage();
}
