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

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory;

/**
 * Using this Factory will accept all certificates.
 * @author thorsten
 * 
 * Usage: <blockquote> 1) register a common protocol handler
 * Protocol.registerProtocol("https", new Protocol("https", new
 * SelfSignedSSLProtocolSocketFactory(), 443));
 * 
 * 2) register per client instance Protocol httpsProtocol = new Protocol(
 * "https", new SelfSignedSSLProtocolSocketFactory(), 443); HttpClient client =
 * new HttpClient(); client.getHostConfiguration().setHost("localhost", 443,
 * httpsProtocol); </blockquote>
 */
public class WebDavSSLSocketFactory implements SecureProtocolSocketFactory,
		ProtocolSocketFactory {

	private static class GenerousTrustManager implements X509TrustManager {
		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[0];
		}

		public void checkClientTrusted(X509Certificate[] arg0, String arg1)
				throws CertificateException {
		}

		public void checkServerTrusted(X509Certificate[] arg0, String arg1)
				throws CertificateException {
		}
	}

	private static SSLSocketFactory getSocketFactory() {
		try {
			SSLContext context = SSLContext.getInstance("SSL");
			context.init(null, new TrustManager[] { new GenerousTrustManager() }, null);
			return context.getSocketFactory();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Socket createSocket(String host, int port, InetAddress clientHost,
			int clientPort) throws IOException, UnknownHostException {
		return getSocketFactory().createSocket(host, port, clientHost,
				clientPort);
	}

	public Socket createSocket(String host, int port) throws IOException,
			UnknownHostException {
		return getSocketFactory().createSocket(host, port);
	}

	public Socket createSocket(Socket socket, String host, int port,
			boolean autoClose) throws IOException, UnknownHostException {
		return getSocketFactory().createSocket(socket, host, port, autoClose);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.commons.httpclient.protocol.ProtocolSocketFactory#createSocket(java.lang.String,
	 *      int, java.net.InetAddress, int,
	 *      org.apache.commons.httpclient.params.HttpConnectionParams)
	 */
	public Socket createSocket(String arg0, int arg1, InetAddress arg2,
			int arg3, HttpConnectionParams arg4) throws IOException,
			UnknownHostException, ConnectTimeoutException {
		return getSocketFactory().createSocket(arg0, arg1, arg2, arg3);
	}
}
