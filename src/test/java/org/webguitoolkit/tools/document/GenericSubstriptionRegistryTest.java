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

import java.io.IOException;

import junit.framework.TestCase;

import org.webguitoolkit.tools.document.impl.GenericSubscriptionRegistry;

public class GenericSubstriptionRegistryTest extends TestCase {

	private static final String TESTREGISTRY = ".testregistry";
	ISubscriptionRegistry reg;

	@Override
	protected void setUp() throws Exception {
		reg = new GenericSubscriptionRegistry();
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		reg = null;
		super.tearDown();
	}

	public void testSubscribe() {
		reg.subscribe("/hello/world", "peter");
		assertTrue(reg.subscriptions("/hello/world") != null
				&& reg.subscriptions("/hello/world").size() == 1);
		reg.subscribe("/hello/world", "paul");
		assertTrue(reg.subscriptions("/hello/world").size() == 2);
		reg.subscribe("/hello/world", "peter");
		assertTrue(reg.subscriptions("/hello/world").size() == 2);
		assertTrue(reg.subscriptions("/hello/china").size() == 0);
		reg.subscribe("/hello/china", "peter");
		assertTrue(reg.subscriptions("/hello/china").size() == 1);
	}

	public void testUnsubscribe() {
		reg.subscribe("/hello/world", "peter");
		reg.subscribe("/hello/world", "paul");
		assertTrue(reg.subscriptions("/hello/world").size() == 2);
		reg.subscribe("/hello/world", "peter");
		assertTrue(reg.subscriptions("/hello/world").size() == 2);
		reg.unsubscribe("/hello/world", "peter");
		assertTrue(reg.subscriptions("/hello/world").size() == 1);
	}

	public void testSaveLoad() throws IOException, ClassNotFoundException {
		reg.subscribe("/hello/world", "peter");
		reg.subscribe("/hello/world", "paul");
		assertTrue(reg.subscriptions("/hello/world").size() == 2);
		GenericSubscriptionRegistry.save((GenericSubscriptionRegistry) reg,
				TESTREGISTRY);
		reg = null;
		reg = GenericSubscriptionRegistry.load(TESTREGISTRY);
		assertTrue(reg != null && reg.subscriptions("/hello/world").size() == 2);
		reg.subscribe("/hello/china", "mary");
		assertTrue(reg.subscriptions("/hello/china").size() == 1);
	}

}
