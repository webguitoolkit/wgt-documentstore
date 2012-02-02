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

/**
 * Covers search functionalities inside a document store<br>
 * <br>
 * 
 * @author Timo Dreier (timo.dreier@infoserve.endress.com)
 * 
 */
public interface ISearchManager {
	public IResource[] search(String userId, String[] directories, String searchDisplayName, String searchContentTerm,
			boolean includeDirectories, List<IProperty> properties, int maxResults) throws Exception;

	public IResource[] search(String userId, String[] directories, String searchAllTerm, boolean includeDirectories,
			List<IProperty> properties, int maxResults) throws Exception;

	public IResource[] searchLastUpdatedResources(String userId, String[] directories, boolean includeDirectories,
			List<IProperty> properties, int maxResults) throws Exception;

	public IResource[] searchLockedResources(String userId, String directory, boolean allUsers, boolean recursive,
			int maxResults) throws Exception;
}