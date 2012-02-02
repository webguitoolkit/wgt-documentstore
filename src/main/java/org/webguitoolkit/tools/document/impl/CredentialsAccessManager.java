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

public class CredentialsAccessManager implements IAccessManager {
	private String uid, pw;

	public CredentialsAccessManager(String uid, String pw) {
		this.uid = uid;
		this.pw = pw;
	}

	public boolean canRead(String url) {
		return true;
	}

	public boolean canWrite(String url) {
		return true;
	}

	public String getPassword() {
		return pw;
	}

	public Object getTicket() {
		return null;
	}

	public String getUserid() {
		return uid;
	}

	@Override
	public String toString() {
		return "CredentialsAccessManager (" + uid + "," + pw + ")";
	}
}