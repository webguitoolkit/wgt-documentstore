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

import org.webguitoolkit.tools.document.IAccessManager;

/**
 * A default implementation for the access manager that allows everything
 * 
 * @author i102374
 */
public class DefaultAccessManager implements IAccessManager {

	private String userid, password;

	public DefaultAccessManager(String userid) {
		this.userid = userid;
	}
	public DefaultAccessManager(String userid, String password) {
		this.userid = userid;
		this.password = password;
	}

	public boolean canRead(String url) {
		return true;
	}

	public boolean canWrite(String url) {
		return true;
	}

	public String getUserid() {
		return userid;
	}

	public String getPassword() {
		return password;
	}

	public Object getTicket() {
		return null;
	}

}
