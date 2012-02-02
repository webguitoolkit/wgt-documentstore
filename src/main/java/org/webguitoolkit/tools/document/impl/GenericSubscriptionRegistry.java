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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;

import org.webguitoolkit.tools.document.ISubscriptionRegistry;

/**
 * Very basic implementation of the registry that holds all subscriptions within
 * one instance. Intended to be serialized.
 * 
 * @author peter
 * 
 */
public class GenericSubscriptionRegistry implements ISubscriptionRegistry,
		Serializable {

	private static final String NEWLINE = "\n";
	private static final String DELIMITERS = "#;";
	private static final String DELIMITER2 = ";";
	private static final String DELIMITER1 = "#";
	private static final long serialVersionUID = 1L;

	private Map<String, Set<String>> subscriptions;

	public void subscribe(String uri, String recipient) {
		if (subscriptions == null)
			subscriptions = new HashMap<String, Set<String>>();
		Set<String> subscribers = subscriptions.get(uri);
		if (subscribers == null) {
			subscribers = new HashSet<String>();
			subscriptions.put(uri, subscribers);
		}
		subscribers.add(recipient);
	}

	public Set<String> subscriptions(String uri) {
		if (subscriptions == null)
			return Collections.emptySet();
		Set<String> subscribers = subscriptions.get(uri);
		if (subscribers == null)
			return Collections.emptySet();
		else
			return subscribers;
	}

	public void unsubscribe(String uri, String recipient) {
		if (subscriptions == null)
			return;
		Set<String> subscribers = subscriptions.get(uri);
		if (subscribers != null)
			subscribers.remove(recipient);
	}

	/**
	 * @param sr1
	 *            Object - The object that is saved.
	 * @param filename
	 *            String - The filename of the file it is saved to.
	 */
	@SuppressWarnings("unchecked")
	public static void save(GenericSubscriptionRegistry reg, String filename)
			throws IOException {
		PrintWriter fs = new PrintWriter(new File(filename));
		if (reg.getSubscriptions() == null)
			return;
		for (Iterator it = reg.getSubscriptions().entrySet().iterator(); it
				.hasNext();) {
			Map.Entry entry = (Entry) it.next();
			String key = (String) entry.getKey();
			Set<String> addresses = (Set<String>) entry.getValue();
			if (addresses != null && !addresses.isEmpty()) {
				fs.write(key);
				fs.write(DELIMITER1);
				for (Iterator it2 = addresses.iterator(); it2.hasNext();) {
					String adr = (String) it2.next();
					fs.print(adr);
					fs.print(DELIMITER2);
				}
				fs.write(NEWLINE);
			}
		}
		fs.flush();
		fs.close();

	}

	/**
	 * @param filename
	 *            String - The filename for the file to be loaded
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException
	 */
	public static ISubscriptionRegistry load(String filename)
			throws FileNotFoundException, IOException, ClassNotFoundException {
		GenericSubscriptionRegistry reg = new GenericSubscriptionRegistry();
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(new File(filename))));
		String line = br.readLine();
		while (line != null && line.length() > 0) {
			StringTokenizer st = new StringTokenizer(line, DELIMITERS);
			String uri = st.nextToken();
			while (st.hasMoreElements()) {
				String uid = st.nextToken();
				reg.subscribe(uri, uid);
			}
			line = br.readLine();
		}
		return reg;
	}

	private Map<String, Set<String>> getSubscriptions() {
		return subscriptions;
	}

}
