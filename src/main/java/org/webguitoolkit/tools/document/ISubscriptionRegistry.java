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

import java.util.Set;

/**
 * Generic interface for a notification subscription registry where a user can
 * subscribe for changes at resources to be notified if a change happens.
 * 
 * @author peter
 * 
 */
public interface ISubscriptionRegistry {
	/**
	 * Subscribe for the passed resource. If the resource does not exist it will
	 * be created in the registry. If the massage address is already registered
	 * it will be ignored. <br>
	 * NOTE : to perform this check the equals() method of the MessageAddres has
	 * to be implemented correctly.
	 * 
	 * @param uri
	 *            the resource
	 * @param recipient
	 *            the message recipient
	 */
	public void subscribe(String uri, String recipient);

	/**
	 * Un-subscribe for the passed resource. If the resource does not exist
	 * nothing happens. If the MessageAddress is not registered nothing happens.
	 * <br>
	 * NOTE : to perform this check the equals() method of the MessageAddres has
	 * to be implemented correctly.
	 * 
	 * @param uri
	 *            the resource
	 * @param recipient
	 *            the message recipient
	 */
	public void unsubscribe(String uri, String recipient);

	/**
	 * @param uri
	 *            the resource
	 * @return Set of registered MessageAddresses or an empty set
	 */
	public Set<String> subscriptions(String uri);

}
