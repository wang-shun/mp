package com.fiberhome.mos.core.openapi.rop.client;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.X509Certificate;

import org.springframework.http.client.SimpleClientHttpRequestFactory;

import com.google.common.base.Throwables;

public class CustomClientHttpRequestFactory extends SimpleClientHttpRequestFactory {
	@Override
	protected void prepareConnection(HttpURLConnection connection, String httpMethod) {
	    try {
	        if (!(connection instanceof HttpsURLConnection)) {
	            throw new RuntimeException("An instance of HttpsURLConnection is expected");
	        }

	        HttpsURLConnection httpsConnection = (HttpsURLConnection) connection;

	        TrustManager[] trustAllCerts = new TrustManager[]{
	                new X509TrustManager() {
	                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
	                        return null;
	                    }

	                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
	                    }

	                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
	                    }

						@Override
						public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
								throws CertificateException {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
								throws CertificateException {
							// TODO Auto-generated method stub
							
						}

	                }
	        };
	        SSLContext sslContext = SSLContext.getInstance("TLS");
	        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
	        httpsConnection.setSSLSocketFactory(new MyCustomSSLSocketFactory(sslContext.getSocketFactory()));

	        httpsConnection.setHostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});

	        super.prepareConnection(httpsConnection, httpMethod);
	    } catch (Exception e) {
	        throw Throwables.propagate(e);
	    }
	}

	/**
	 * We need to invoke sslSocket.setEnabledProtocols(new String[] {"SSLv3"});
	 * see http://www.oracle.com/technetwork/java/javase/documentation/cve-2014-3566-2342133.html (Java 8 section)
	 */
	private static class MyCustomSSLSocketFactory extends SSLSocketFactory {

	    private final SSLSocketFactory delegate;

	    public MyCustomSSLSocketFactory(SSLSocketFactory delegate) {
	        this.delegate = delegate;
	    }

	    @Override
	    public String[] getDefaultCipherSuites() {
	        return delegate.getDefaultCipherSuites();
	    }

	    @Override
	    public String[] getSupportedCipherSuites() {
	        return delegate.getSupportedCipherSuites();
	    }

	    @Override
	    public Socket createSocket(final Socket socket, final String host, final int port, final boolean autoClose) throws IOException {
	        final Socket underlyingSocket = delegate.createSocket(socket, host, port, autoClose);
	        return overrideProtocol(underlyingSocket);
	    }

	    @Override
	    public Socket createSocket(final String host, final int port) throws IOException {
	        final Socket underlyingSocket = delegate.createSocket(host, port);
	        return overrideProtocol(underlyingSocket);
	    }

	    @Override
	    public Socket createSocket(final String host, final int port, final InetAddress localAddress, final int localPort) throws IOException {
	        final Socket underlyingSocket = delegate.createSocket(host, port, localAddress, localPort);
	        return overrideProtocol(underlyingSocket);
	    }

	    @Override
	    public Socket createSocket(final InetAddress host, final int port) throws IOException {
	        final Socket underlyingSocket = delegate.createSocket(host, port);
	        return overrideProtocol(underlyingSocket);
	    }

	    @Override
	    public Socket createSocket(final InetAddress host, final int port, final InetAddress localAddress, final int localPort) throws IOException {
	        final Socket underlyingSocket = delegate.createSocket(host, port, localAddress, localPort);
	        return overrideProtocol(underlyingSocket);
	    }

	    private Socket overrideProtocol(final Socket socket) {
	        if (!(socket instanceof SSLSocket)) {
	            throw new RuntimeException("An instance of SSLSocket is expected");
	        }
	        ((SSLSocket) socket).setEnabledProtocols(new String[] {"SSLv3"});
	        return socket;
	    }
	}
}
