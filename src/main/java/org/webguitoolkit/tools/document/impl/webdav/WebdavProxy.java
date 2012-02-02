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
package org.webguitoolkit.tools.document.impl.webdav;

import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpURL;
import org.apache.commons.httpclient.HttpsURL;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.webdav.lib.ResponseEntity;
import org.apache.webdav.lib.WebdavResource;
import org.webguitoolkit.tools.document.IAccessManager;
import org.webguitoolkit.tools.document.IProperty;
import org.webguitoolkit.tools.document.IResource;
import org.webguitoolkit.tools.document.IStoreProxy;
import org.webguitoolkit.tools.document.impl.DocumentRepositoryException;
import org.webguitoolkit.tools.document.impl.ResourceNotExistedException;

/**
 * The WebdavProxy provides easy access to an WebDAV server. The functionality
 * is limited to what is possible with "normal" file systems.
 * 
 * @author peter
 * 
 */
public class WebdavProxy implements IStoreProxy {

	private static Log log = LogFactory.getLog(WebdavProxy.class);
	private IAccessManager accessManager;
	private URL url;

	public WebdavProxy(String absoluteUri, IAccessManager accessManager)
			throws DocumentRepositoryException {
		try {
			String decodedUrl = URLDecoder.decode(absoluteUri, "UTF-8");
			url = new URL(decodedUrl);
			this.accessManager = accessManager;
		} catch (Exception e) {
			throw new DocumentRepositoryException(e);

		}
	}

	private WebdavResource getMyResource() throws Exception {
		return getWebDavResource(url, accessManager);
	}

	private WebdavResource getWebDavResource(URL url,
			IAccessManager accessManager) throws Exception {
		WebdavResource result = null;
		try {
			if (url.getProtocol().equalsIgnoreCase("https")) {
				/*
				 * https in production environment always without port
				 */
				Protocol.registerProtocol("https", new Protocol("https",
						(ProtocolSocketFactory) new WebDavSSLSocketFactory(), 443));
				HttpsURL hrl;
				if (url.getPort() == 80)
					hrl = new HttpsURL(url.toExternalForm());
				else
					hrl = new HttpsURL(url.toExternalForm());

				log.debug("connecting to " + url.toExternalForm());
				if (accessManager != null) {
					log.debug(accessManager.toString());
					hrl.setUserinfo(accessManager.getUserid(), accessManager
							.getPassword());
					log.debug("userinfo " + hrl.getUserinfo());
				}

				// log.info("getResource() = " + hrl.toString());

				result = new WebdavResource(hrl);
			} else if (url.getHost().equalsIgnoreCase("http")) {
				HttpURL hrl = new HttpURL(url.toExternalForm());
				log.debug("connecting to " + url.toExternalForm());
				if (accessManager != null) {
					log.debug(accessManager.toString());
					hrl.setUserinfo(accessManager.getUserid(), accessManager
							.getPassword());
				}
				result = new WebdavResource(hrl);

			} else {
				HttpURL hrl = new HttpURL(url.toExternalForm());
				log.debug("connecting to " + url.toExternalForm());
				if (accessManager != null) {
					log.debug(accessManager.toString());
					hrl.setUserinfo(accessManager.getUserid(), accessManager
							.getPassword());
				}
				result = new WebdavResource(hrl);
			}
		} catch (HttpException he) {
			log.error(url + " : " + he.getMessage(), he);
			log.error("ReasonCode " + he.getReasonCode());
			throw he;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.webguitoolkit.tools.document.impl.webdav.IStoreProxy#getAbsoluteUri()
	 */
	public String getAbsoluteUri() {
		return url.toString(); // me.getHttpURL().toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.webguitoolkit.tools.document.impl.webdav.IStoreProxy#getUri()
	 */
	public String getRelativeUri() {
		try {
			return getMyResource().getPath();
		} catch (Exception e) {
			log.fatal(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.webguitoolkit.tools.document.impl.webdav.IStoreProxy#getPath()
	 */
	public String getRelativePath() {
		String result = getRelativeUri();
		return result.substring(0, result.lastIndexOf('/'));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.webguitoolkit.tools.document.impl.webdav.IStoreProxy#getName()
	 */
	public String getName() throws Exception {
		return getMyResource().getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.webguitoolkit.tools.document.impl.webdav.IStoreProxy#getModifiedAt()
	 */
	public long getModifiedAt() throws Exception {

		return getMyResource().getGetLastModified();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.webguitoolkit.tools.document.impl.webdav.IStoreProxy#getProperties()
	 */
	public Properties getProperties() {
		// TODO-PZ implement
		throw new IllegalStateException("Sorry - not jet implemented");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.webguitoolkit.tools.document.impl.webdav.IStoreProxy#getSize()
	 */
	public long getSize() throws Exception {
		if (!isCollection()) {
			return getMyResource().getGetContentLength();
		}
		return getResources().size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.webguitoolkit.tools.document.impl.webdav.IStoreProxy#isCollection()
	 */
	public boolean isCollection() {
		try {
			return getMyResource().isCollection();
		} catch (Exception e) {
			log.fatal(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.webguitoolkit.tools.document.impl.webdav.IStoreProxy#getFiles()
	 */
	public List<IStoreProxy> getFiles() throws Exception {
		List<IStoreProxy> result = getResources();
		for (Iterator<IStoreProxy> iterator = result.iterator(); iterator
				.hasNext();) {
			IStoreProxy resource = iterator.next();
			if (resource.isCollection())
				iterator.remove();
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.webguitoolkit.tools.document.impl.webdav.IStoreProxy#getCollections()
	 */
	public List<IStoreProxy> getCollections() throws Exception {
		List<IStoreProxy> result = getResources();
		for (Iterator<IStoreProxy> iterator = result.iterator(); iterator
				.hasNext();) {
			IStoreProxy resource = iterator.next();
			if (!resource.isCollection())
				iterator.remove();
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.webguitoolkit.tools.document.impl.webdav.IStoreProxy#getResources()
	 */
	public List<IStoreProxy> getResources() throws Exception {
		List<IStoreProxy> result = new ArrayList<IStoreProxy>();
		Vector props = new Vector();
		props.add("href");
		Enumeration propfindResult = getMyResource().propfindMethod(1, props);
		int uriStart = getRelativeUri().length();
		int i = 1;
		while (propfindResult.hasMoreElements()) {
			ResponseEntity entry = (ResponseEntity) propfindResult
					.nextElement();
			String name = entry.getHref();
			// log.info("name[" + (i++) + "]=" + name);
			// IIS WebDAV returns different values than Tomcat webdav
			if (name.startsWith("https://") || name.startsWith("http://")) {
				uriStart = getAbsoluteUri().length(); // needed for IIS
			}

			name = name.substring(uriStart);

			if (StringUtils.isNotEmpty(name) && !name.equals("/"))
				// PROPFIND returns also the resource itself - what
				// we do not want here
				result.add(new WebdavProxy(getAbsoluteUri() + name,
						accessManager));
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.webguitoolkit.tools.document.impl.webdav.IStoreProxy#addFolder(java
	 * .lang.String)
	 */
	public boolean addFolder(String name) throws Exception {
		if (!isCollection())
			throw new ResourceNotExistedException("Not a folder : "
					+ getAbsoluteUri());
		getMyResource().mkcolMethod(getAbsoluteUri() + "/" + name);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.webguitoolkit.tools.document.impl.webdav.IStoreProxy#removeFolder
	 * (java.lang.String)
	 */
	public boolean removeFolder(String name, boolean recursive)
			throws Exception {
		IStoreProxy folder = new WebdavProxy(getAbsoluteUri() + "/" + name,
				accessManager);
		return folder.remove(recursive);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.webguitoolkit.tools.document.impl.webdav.IStoreProxy#remove()
	 */
	public boolean remove(boolean recursive) throws Exception {
		if (isCollection()) {
			if (!recursive && getSize() > 0) {
				return false;
			}
			return getMyResource().deleteMethod(getAbsoluteUri());
		} else
			return getMyResource().deleteMethod(getAbsoluteUri());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.webguitoolkit.tools.document.impl.webdav.IStoreProxy#rename(java.
	 * lang.String)
	 */
	public boolean rename(String newuri) throws Exception {
		boolean OK = getMyResource().moveMethod(newuri);
		if (OK) {
			url = new URL(getServerBaseUrl() + newuri);
		}
		return OK;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.webguitoolkit.tools.document.impl.webdav.IStoreProxy#getInputStream()
	 */
	public InputStream getInputStream() throws Exception {
		if (isCollection())
			throw new ResourceNotExistedException("Not a file : "
					+ getAbsoluteUri());
		return getMyResource().getMethodData();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.webguitoolkit.tools.document.impl.webdav.IStoreProxy#getInputStream
	 * (java.lang.String)
	 */
	public InputStream getInputStream(String name) throws Exception {
		if (!isCollection())
			throw new ResourceNotExistedException("Not a collection : "
					+ getAbsoluteUri());
		WebdavResource file = new WebdavResource(getAbsoluteUri() + "/" + name);
		return file.getMethodData();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.webguitoolkit.tools.document.impl.webdav.IStoreProxy#writeFile(java
	 * .lang.String, java.lang.String)
	 */
	public void writeFile(String name, String content) throws Exception {
		if (!isCollection())
			throw new ResourceNotExistedException("not a collection : "
					+ getAbsoluteUri());
		getMyResource().putMethod(getAbsoluteUri() + "/" + name, content);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.webguitoolkit.tools.document.impl.webdav.IStoreProxy#writeFile(java
	 * .lang.String, java.io.InputStream)
	 */
	public void writeFile(String name, InputStream content) throws Exception {
		if (!isCollection())
			throw new ResourceNotExistedException("Not a collection : "
					+ getAbsoluteUri());
		getMyResource().putMethod(getAbsoluteUri() + "/" + name, content);

	}

	public void write(String content) throws Exception {
		if (isCollection())
			throw new ResourceNotExistedException("Not a file : "
					+ getAbsoluteUri());
		getMyResource().putMethod(getAbsoluteUri(), content);

	}

	public void write(InputStream content) throws Exception {
		if (isCollection())
			throw new ResourceNotExistedException("Not a file : "
					+ getAbsoluteUri());
		getMyResource().putMethod(getAbsoluteUri(), content);

	}

	public boolean exists() {
		try {
			return getMyResource().exists();
		} catch (Exception e) {
			return false;
		}
	}

	public IAccessManager getAccessManager() {
		return accessManager;
	}

	public String getServerBaseUrl() {
		String result = url.getProtocol() + "://" + url.getHost() + ":"
				+ url.getPort();
		return result;
		// String url = null;
		// try {
		// url = getMe().getHttpURL().toString();
		// } catch (Exception e1) {
		// log.fatal(e1);
		// }
		// String path = null;
		// try {
		// path = getMe().getHttpURL().getPath();
		// } catch (Exception e) {
		// throw new RuntimeException(e);
		// }
		//
		// return url.substring(0, url.length() - path.length());

	}

	public boolean isLocked() {
		try {
			return getMyResource().isLocked();
		} catch (Exception e) {
			log.fatal(e);
			return false;
		}
	}

	public boolean copyTo(String absoluteUri, boolean deep) {
		// TODO-PZ implement "copyTo"
		throw new RuntimeException("SORRY - NOT IMPLEMENTED");

	}

	public boolean moveTo(String absoluteUri) {
		// TODO-PZ implement "moveTo"
		throw new RuntimeException("SORRY - NOT IMPLEMENTED");
	}

	public String infoString() {
		try {
			return "AbsolutUri = " + getAbsoluteUri() + ", RelativeUri = "
					+ getRelativeUri() + ", Name = " + getName()
					+ ", RelativePath = " + getRelativePath();
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	public boolean addFeedback(String feedbackTxt, boolean notifyOwner)
			throws Exception {
		throw new RuntimeException();
	}

	public boolean canChangePermission() throws Exception {
		throw new RuntimeException("Not implemented");
	}

	public boolean canDelete() throws Exception {
		throw new RuntimeException("Not implemented");
	}

	public boolean canRead() throws Exception {
		throw new RuntimeException("Not implemented");
	}

	public boolean canWrite() throws Exception {
		throw new RuntimeException("Not implemented");
	}

	public boolean copyTo(IResource[] resources, String absoluteUri,
			boolean deep) throws Exception {
		throw new RuntimeException("Not implemented");
	}

	public boolean createDirectory(String name) throws Exception {
		return addFolder(name);
	}

	public void createFile(String name, String content, boolean overwrite)
			throws Exception {
		throw new RuntimeException("Not implemented");
	}

	public void createFile(String name, InputStream content, boolean overwrite)
			throws Exception {
		writeFile(name, content);
	}

	public boolean endEditContent(String content) throws Exception {
		throw new RuntimeException("Not implemented");
	}

	public boolean endEditContent(InputStream content) throws Exception {
		throw new RuntimeException("Not implemented");
	}

	public String getAccessURL() {
		throw new RuntimeException("Not implemented");
	}

	public InputStream getContent() throws Exception {
		return this.getInputStream();
	}

	public InputStream getContent(String name) throws Exception {
		throw new RuntimeException("Not implemented");
	}

	public String getContentSnippet() throws Exception {
		throw new RuntimeException("Not implemented");
	}

	public long getCreated() throws Exception {
		throw new RuntimeException("Not implemented");
	}

	public String getCreatedBy() throws Exception {
		throw new RuntimeException("Not implemented");
	}

	public String getDescription() throws Exception {
		throw new RuntimeException("Not implemented");
	}

	public String getEditedBy() throws Exception {
		throw new RuntimeException("Not implemented");
	}

	public List<String[]> getFeedback() throws Exception {
		throw new RuntimeException("Not implemented");
	}

	public int getFeedbackCount() throws Exception {
		throw new RuntimeException("Not implemented");
	}

	public long getLastModified() throws Exception {
		throw new RuntimeException("Not implemented");
	}

	public String getLastModifiedBy() throws Exception {
		throw new RuntimeException("Not implemented");
	}

	public String getLockedBy() throws Exception {
		throw new RuntimeException("Not implemented");
	}

	public List<IProperty> getProperties(String propertyGroupID)
			throws Exception {
		throw new RuntimeException("Not implemented");
	}

	public int getRatingAverage() throws Exception {
		throw new RuntimeException("Not implemented");
	}

	public int getRatingCount() throws Exception {
		throw new RuntimeException("Not implemented");
	}

	public int getRatingOfUser() throws Exception {
		throw new RuntimeException("Not implemented");
	}

	public boolean lock() throws Exception {
		throw new RuntimeException("Not implemented");
	}

	public boolean lock(IResource[] resources) throws Exception {
		throw new RuntimeException("Not implemented");
	}

	public boolean moveTo(IResource[] resources, String absoluteUri)
			throws Exception {
		throw new RuntimeException("Not implemented");
	}

	public boolean rate(int a) throws Exception {
		throw new RuntimeException("Not implemented");
	}

	public boolean reload() throws Exception {
		throw new RuntimeException("Not implemented");
	}

	public boolean remove(IResource[] resources) throws Exception {
		throw new RuntimeException("Not implemented");
	}

	public boolean removeDirectory(String name, boolean recursive)
			throws Exception {
		throw new RuntimeException("Not implemented");
	}

	public boolean setProperties(String description,
			List<IProperty> propertyList) throws Exception {
		throw new RuntimeException("Not implemented");
	}

	public boolean startEditContent() throws Exception {
		throw new RuntimeException("Not implemented");
	}

	public boolean unlock() throws Exception {
		throw new RuntimeException("Not implemented");
	}

	public boolean unlock(IResource[] resources) throws Exception {
		throw new RuntimeException("Not implemented");
	}

	public String getEditedByName() throws Exception {
		throw new RuntimeException("Not implemented");
	}

	public String getLockedByName() throws Exception {
		throw new RuntimeException("Not implemented");
	}

	public String getParentForumNameOfMessage() {
		throw new RuntimeException("Not implemented");
	}

	public boolean isForumsMessage() {
		throw new RuntimeException("Not implemented");
	}

	public String getConstantURL() {
		return null;
	}
}