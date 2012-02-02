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

import java.io.File;

import junit.framework.TestCase;

public class CleanUp extends TestCase {
	public void testCleanUp() {
		deleteAllFiles(new File(
				"D:\\Apps\\JavaDev\\Tomcat\\jakarta-tomcat-5.5.9\\webapps\\webdav\\aFolder1222779723204"));
	}

	private static boolean deleteAllFiles(File dir) {
		if (!dir.exists()) {
			return true;
		}
		boolean res = true;
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			for (int i = 0; i < files.length; i++) {
				res &= deleteAllFiles(files[i]);
			}
			res = dir.delete();// Delete dir itself
		} else {
			res = dir.delete();
		}
		return res;
	}

}
