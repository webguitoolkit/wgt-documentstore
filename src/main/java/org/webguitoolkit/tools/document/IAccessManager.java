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

/**
 * An access manager controls the accessibility of resources identified by URIs
 * for an user. An user is identified by an id. The user will be specified by
 * means of a concrete implementation of this interface and may be passed with
 * the constructor. Later on the access manager is used to control the
 * accessibility of resources in the implementation of the IFile and IDirectory
 * classes.<br>
 * The default implementation of File and Directory assumes that wild cards in
 * the URI are supported when deleting directories recursively.
 * 
 * @author peter
 * 
 */
public interface IAccessManager {

	/**
	 * @param uri
	 *            specify the absolute uri (e.g.
	 *            "http://www.ccc.com/abc/def/xyz.txt") that shall be checked
	 *            here. Wildcards may be applied here depending on the
	 *            implementation.
	 * 
	 * @return true if the configured user can read this uri
	 */
	public boolean canRead(String absoluteUri);

	/**
	 * @param uri
	 *            specify the absolute uri (e.g.
	 *            "http://www.ccc.com/abc/def/xyz.txt") that shall be checked
	 *            here. Wildcards may be applied here depending on the
	 *            implementation. The default implementation of File and
	 *            Directory assumes that wildcards are supported when deleting
	 *            directories recursively.
	 * 
	 * @return true if the configured user can write this uri
	 */
	public boolean canWrite(String absoluteUri);

	/**
	 * @return the user id of the configured user
	 */

	public String getUserid();

	/**
	 * 
	 * @return the password if provided
	 */
	public String getPassword();

	/**
	 * 
	 * @return the (SSO) ticket if provided
	 */
	public Object getTicket();

}
