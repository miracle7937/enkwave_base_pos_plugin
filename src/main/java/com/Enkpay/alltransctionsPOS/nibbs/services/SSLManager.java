package com.Enkpay.alltransctionsPOS.nibbs.services;

import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import javax.net.ssl.*;

public class SSLManager {



    public static SSLSocketFactory getTrustySSLSocketFactory() throws Exception {
        X509TrustManager trustManager = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) {}
            public void checkServerTrusted(X509Certificate[] chain, String authType) {}
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{trustManager}, new SecureRandom());
        return sslContext.getSocketFactory();
    }

    public static SSLSocketFactory getSSLSocketFactory(TrustManagerFactory trustManagerFactory, KeyManagerFactory keyManagerFactory, SecureRandom secureRandom) throws Exception {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory != null ? keyManagerFactory.getKeyManagers() : null, trustManagerFactory.getTrustManagers(), secureRandom);
        return sslContext.getSocketFactory();
    }

    public static SSLSocket createSocket(SSLSocketFactory sslSocketFactory, String ipAddress, int port) throws Exception {
        return (SSLSocket) sslSocketFactory.createSocket(ipAddress, port);
    }
}
